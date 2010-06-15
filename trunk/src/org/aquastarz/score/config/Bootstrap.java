// <editor-fold defaultstate="collapsed" desc="GNU General Public License">
//
//   SynchroScore
//   Copyright (C) 2009 Shayne Hughes
//
//   This program is free software: you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as published by
//   the Free Software Foundation, either version 3 of the License, or
//   (at your option) any later version.
//
//   This program is distributed in the hope that it will be useful,
//   but WITHOUT ANY WARRANTY; without even the implied warranty of
//   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//   GNU General Public License for more details.
//
//   You should have received a copy of the GNU General Public License
//   along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
// </editor-fold>
package org.aquastarz.score.config;

import au.com.bytecode.opencsv.CSVReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.aquastarz.score.domain.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.persistence.EntityManager;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.controller.ScoreController;
import org.aquastarz.score.manager.SeasonManager;
import org.aquastarz.score.manager.SwimmerManager;
import org.aquastarz.score.domain.Season;

public class Bootstrap {

    private static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(Bootstrap.class.getName());

    public static void clearDB(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        entityManager.createQuery("delete from FigureScore").executeUpdate();
        entityManager.createQuery("delete from FiguresParticipant").executeUpdate();
        entityManager.createQuery("delete from Swimmer").executeUpdate();
        entityManager.createQuery("delete from Meet").executeUpdate();
        entityManager.createQuery("delete from Level").executeUpdate();
        entityManager.createQuery("delete from Team").executeUpdate();
        entityManager.createQuery("delete from Figure").executeUpdate();
        entityManager.createQuery("delete from Season").executeUpdate();

        entityManager.getTransaction().commit();

    }

    public static void loadLeagueData() {
        EntityManager entityManager = ScoreApp.getEntityManager();
        entityManager.getTransaction().begin();

        checkAndPersistLevel(entityManager, "N8", "Novice 8 and Under", 1);
        checkAndPersistLevel(entityManager, "N9-10", "Novice 9-10", 2);
        checkAndPersistLevel(entityManager, "N11-12", "Novice 11-12", 3);
        checkAndPersistLevel(entityManager, "N13-14", "Novice 13-14", 4);
        checkAndPersistLevel(entityManager, "N15-18", "Novice 15-18", 5);
        checkAndPersistLevel(entityManager, "I11-12", "Intermediate 11-12", 6);
        checkAndPersistLevel(entityManager, "I13-14", "Intermediate 13-14", 7);
        checkAndPersistLevel(entityManager, "I15-16", "Intermediate 15-16", 8);
        checkAndPersistLevel(entityManager, "I17-18", "Intermediate 17-18", 9);

        checkAndPersistRoutineLevel(entityManager, "N12U", "Novice 12 and Under", 1);
        checkAndPersistRoutineLevel(entityManager, "N13O", "Novice 13 and Over", 2);
        checkAndPersistRoutineLevel(entityManager, "I11", "Intermediate 11-14", 3);
        checkAndPersistRoutineLevel(entityManager, "I15", "Intermediate 15-18", 4);
        checkAndPersistRoutineLevel(entityManager, "I11T", "Int. 11 and Over Team", 5);

        checkAndPersistTeam(entityManager, "AUB", "Auburn Mermaids");
        checkAndPersistTeam(entityManager, "COR", "Cordova Cordettes");
        checkAndPersistTeam(entityManager, "FEC", "Fulton El Camino Stingrays");
        checkAndPersistTeam(entityManager, "DAV", "Davis AquaStarz");
        checkAndPersistTeam(entityManager, "ROS", "Roseville Aquabunnies");
        checkAndPersistTeam(entityManager, "SUN", "Sunrise Swans");

        checkAndPersistFigure(entityManager, "101", new BigDecimal("1.6"), "Ballet Leg, R/L");
        checkAndPersistFigure(entityManager, "311", new BigDecimal("1.8"), "Kip");
        checkAndPersistFigure(entityManager, "360", new BigDecimal("2.1"), "Walkover, Front");
        checkAndPersistFigure(entityManager, "349", new BigDecimal("1.8"), "Tower");
        checkAndPersistFigure(entityManager, "342", new BigDecimal("2.1"), "Heron");
        checkAndPersistFigure(entityManager, "316", new BigDecimal("2.4"), "Kip, Split, Walkout");
        checkAndPersistFigure(entityManager, "140d", new BigDecimal("2.5"), "Flamingo, Bent Knee, Spin 180");
        checkAndPersistFigure(entityManager, "345", new BigDecimal("2.2"), "Ariana");
        checkAndPersistFigure(entityManager, "350", new BigDecimal("2.2"), "Minerva");
        checkAndPersistFigure(entityManager, "240", new BigDecimal("2.2"), "Albatross");
        checkAndPersistFigure(entityManager, "301", new BigDecimal("2.0"), "Barracuda");
        checkAndPersistFigure(entityManager, "150", new BigDecimal("3.1"), "Knight");

        entityManager.getTransaction().commit();
    }

    public static void checkAndPersistLevel(EntityManager entityManager, String key, String name, int sortOrder) {
        if (entityManager.find(Level.class, key) == null) {
            entityManager.persist(new Level(key, name, sortOrder));
        }
    }

    public static void checkAndPersistRoutineLevel(EntityManager entityManager, String key, String name, int sortOrder) {
        if (entityManager.find(RoutineLevel.class, key) == null) {
            entityManager.persist(new RoutineLevel(key, name, sortOrder));
        }
    }

    public static void checkAndPersistTeam(EntityManager entityManager, String key, String name) {
        if (entityManager.find(Team.class, key) == null) {
            entityManager.persist(new Team(key, name));
        }
    }

    public static void checkAndPersistFigure(EntityManager entityManager, String key, BigDecimal dd, String name) {
        if (entityManager.find(Figure.class, key) == null) {
            entityManager.persist(new Figure(key, dd, name));
        }
    }

    public static List<List<Object>> loadUpdateData(File zipFile) {
        //This must read through the zip entries in order, so it handles
        //any ordering
        List<List<Object>> results = new ArrayList<List<Object>>();
        try {
            InputStream theFile = new FileInputStream(zipFile);
            ZipInputStream stream = new ZipInputStream(theFile);

            //Save the meets and results for later and put them together for
            //processing
            Map<String, Map<String, LegacyResult>> lrMap = new HashMap<String, Map<String, LegacyResult>>();
            Map<String, LegacyMeet> lmMap = new HashMap<String, LegacyMeet>();

            try {
                ZipEntry entry;
                while ((entry = stream.getNextEntry()) != null) {
                    String fname = entry.getName();
                    String folder = null;
                    int i = fname.lastIndexOf("/");
                    if (i > 0) {
                        folder = fname.substring(0, i);
                    }
                    if (fname.toUpperCase().endsWith("ROSTER.CSV") && folder != null) {
                        Season season = SeasonManager.findOrCreate(folder);
                        loadRoster(season, stream);
                    } else if (fname.toUpperCase().endsWith("RESULTS.CSV")) {
                        lrMap.put(folder, readLegacyResults(stream));
                    } else if (fname.toUpperCase().endsWith("FIGSTAT.CSV")) {
                        lmMap.put(folder, readLegacyMeet(stream));
                    }
                }
            } finally {
                // we must always close the zip file.
                stream.close();
            }

            //Now put all the meet data togther and load it
            for (String folder : lmMap.keySet()) {
                int i = folder.indexOf("/");
                if (i > 0) {
                    String seasonName = folder.substring(0, i);
                    LegacyMeet lm = lmMap.get(folder);
                    lm.setResults(lrMap.get(folder));
                    Season season = SeasonManager.findOrCreate(seasonName);

                    //Skip if we have a meet with same name
                    if (ScoreController.findMeet(season, lm.meetTitle) == null) {
                        if (lm.getResults() != null && season != null) {
                            results.add(loadLegacyMeet(season, lm));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    public static void loadRoster(Season season, InputStream is) {
        EntityManager entityManager = ScoreApp.getEntityManager();

        //Map legacy level names to current
        Map<String, Level> levels = new HashMap<String, Level>();
        levels.put("NOV8 & UNDER", entityManager.find(Level.class, "N8"));
        levels.put("NOV9-10", entityManager.find(Level.class, "N9-10"));
        levels.put("NOV11-12", entityManager.find(Level.class, "N11-12"));
        levels.put("NOV13-14", entityManager.find(Level.class, "N13-14"));
        levels.put("NOV15-18 (N)", entityManager.find(Level.class, "N15-18"));
        levels.put("NOV15-18", entityManager.find(Level.class, "N15-18"));
        levels.put("INT11-12", entityManager.find(Level.class, "I11-12"));
        levels.put("INT13-14", entityManager.find(Level.class, "I13-14"));
        levels.put("INT15-16 (I)", entityManager.find(Level.class, "I15-16"));
        levels.put("INT17-18 (I)", entityManager.find(Level.class, "I17-18"));
        levels.put("INT15-16", entityManager.find(Level.class, "I15-16"));
        levels.put("INT17-18", entityManager.find(Level.class, "I17-18"));

        InputStreamReader isr = new InputStreamReader(is);
        CSVReader csv = new CSVReader(isr);
        String[] nextLine;
        try {
            csv.readNext(); //skip header
            entityManager.getTransaction().begin();
            while ((nextLine = csv.readNext()) != null) {
                LegacySwimmer ls = new LegacySwimmer(nextLine);
                Team team = (Team) entityManager.find(Team.class, ls.team.toUpperCase());
                Level level = levels.get((ls.novInt + ls.ageGrp).toUpperCase());
                if (level == null) {
                    logger.error("Level not found [" + ls.novInt + ls.ageGrp + "]");
                }
                Swimmer swimmer = SwimmerManager.findByLeagueNum(ls.code, season);
                if (swimmer == null) {
                    swimmer = new Swimmer(ls.code, season, team, level, ls.gName, ls.sName);
                }
                else {
                    swimmer.setFirstName(ls.gName);
                    swimmer.setLastName(ls.sName);
                    swimmer.setLevel(level);
                    swimmer.setTeam(team);
                }
                entityManager.persist(swimmer);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, LegacyResult> readLegacyResults(InputStream is) {
        Map<String, LegacyResult> legacyResults = new HashMap<String, LegacyResult>();

        CSVReader csv = new CSVReader(new InputStreamReader(is));
        String[] nextLine;
        try {
            csv.readNext(); //skip header
            while ((nextLine = csv.readNext()) != null) {
                LegacyResult lr = new LegacyResult(nextLine);
                legacyResults.put(lr.swmrNo, lr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return legacyResults;
    }

    private static LegacyMeet readLegacyMeet(InputStream is) {
        CSVReader csv = new CSVReader(new InputStreamReader(is));
        String[] nextLine;
        LegacyMeet legacyMeet = null;

        try {
            csv.readNext(); //skip header
            nextLine = csv.readNext();
            legacyMeet = new LegacyMeet(nextLine);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return legacyMeet;
    }

    public static List<Object> loadLegacyMeet(Season season, LegacyMeet legacyMeet) {
        List<FigureScoreTracker> figureScoreTrackers = new ArrayList<FigureScoreTracker>();
        List<FiguresParticipantTracker> figuresParticipantTrackers = new ArrayList<FiguresParticipantTracker>();
        ArrayList<Object> retVals = new ArrayList<Object>();
        retVals.add(figureScoreTrackers);
        retVals.add(figuresParticipantTrackers);
        Meet meet = null;
        EntityManager entityManager = ScoreApp.getEntityManager();

        Map<String, LegacyResult> legacyResults = legacyMeet.getResults();

        meet = new Meet();
        meet.setName(legacyMeet.meetTitle);
        meet.setSeason(season);
        meet.setMeetDate(legacyMeet.meetDate);
        if ("Dual".equals(legacyMeet.meetType)) {
            meet.setType('R');
        } else {
            meet.setType('C');
        }

        meet.setHomeTeam(entityManager.find(Team.class, legacyMeet.homeTm));
        List<Team> oppList = new ArrayList<Team>();
        oppList.add(entityManager.find(Team.class, legacyMeet.oppnTm));
        meet.setOpponents(oppList);
        meet.setNov1Figure(entityManager.find(Figure.class, legacyMeet.nRestSta1));
        meet.setNov2Figure(entityManager.find(Figure.class, legacyMeet.nRestSta2));
        meet.setNov3Figure(entityManager.find(Figure.class, legacyMeet.nRestSta3));
        meet.setNov4Figure(entityManager.find(Figure.class, legacyMeet.nRestSta4));
        meet.setInt1Figure(entityManager.find(Figure.class, legacyMeet.iSta1));
        meet.setInt2Figure(entityManager.find(Figure.class, legacyMeet.iSta2));
        meet.setInt3Figure(entityManager.find(Figure.class, legacyMeet.iSta3));
        meet.setInt4Figure(entityManager.find(Figure.class, legacyMeet.iSta4));
        meet.setEu1(legacyMeet.n8USta1);
        meet.setEu2(legacyMeet.n8USta2);
        meet.setEu3(legacyMeet.n8USta3);
        meet.setEu4(legacyMeet.n8USta4);
        meet.setFiguresOrderGenerated(true);
        entityManager.getTransaction().begin();
        entityManager.persist(meet);
        entityManager.getTransaction().commit();

        for (LegacyResult lr : legacyResults.values()) {
            FiguresParticipant fp = new FiguresParticipant();
            fp.setFigureOrder(lr.swmrNo);

            fp.setSwimmer(SwimmerManager.findByLeagueNum(lr.leagueNo, season));
            fp.setMeet(meet);

            entityManager.getTransaction().begin();
            entityManager.persist(fp);
            entityManager.getTransaction().commit();


            FiguresParticipantTracker fpt = new FiguresParticipantTracker();
            fpt.figuresParticipant = fp;
            fpt.place = lr.place;
            fpt.points = lr.points;
            fpt.total = lr.finTot;
            figuresParticipantTrackers.add(fpt);

            List<FigureScore> scores = new ArrayList<FigureScore>();
            int figNum = 1;
            for (int i = 0; i < 4; i++) {
                if ("8 & UNDER".equals(lr.ageGrp.toUpperCase())
                        && ((i == 0 && !meet.isEu1())
                        || (i == 1 && !meet.isEu2())
                        || (i == 2 && !meet.isEu3())
                        || (i == 3 && !meet.isEu4()))) {
                    continue;
                }
                FigureScore fs = new FigureScore();
                fs.setFiguresParticipant(fp);
                fs.setStation(i + 1);
                try {
                    fs.setScore1((BigDecimal) lr.getClass().getDeclaredField("s" + (i + 1) + "j1").get(lr));
                    fs.setScore2((BigDecimal) lr.getClass().getDeclaredField("s" + (i + 1) + "j2").get(lr));
                    fs.setScore3((BigDecimal) lr.getClass().getDeclaredField("s" + (i + 1) + "j3").get(lr));
                    fs.setScore4((BigDecimal) lr.getClass().getDeclaredField("s" + (i + 1) + "j4").get(lr));
                    fs.setScore5((BigDecimal) lr.getClass().getDeclaredField("s" + (i + 1) + "j5").get(lr));
                    fs.setPenalty((BigDecimal) lr.getClass().getDeclaredField("s" + (i + 1) + "Pen").get(lr));
                    fs.setTotalScore(ScoreController.totalScore(fs));
                    FigureScoreTracker fst = new FigureScoreTracker();
                    fst.figureScore = fs;
                    fst.total = (BigDecimal) lr.getClass().getDeclaredField("s" + (i + 1) + "Tot").get(lr);
                    figureScoreTrackers.add(fst);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                entityManager.getTransaction().begin();
                entityManager.persist(fs);
                entityManager.getTransaction().commit();

                scores.add(fs);
                figNum++;
            }
            fp.setFiguresScores(scores);
            meet.getFiguresParticipants().add(fp);
        }
        ScoreController.calculateMeetResults(meet);
        entityManager.getTransaction().begin();
        entityManager.persist(meet);
        entityManager.getTransaction().commit();

        return retVals;
    }

    public static class LegacySwimmer {

        int code;
        String sName;
        String gName;
        String team;
        String novInt;
        String ageGrp;

        LegacySwimmer(String[] line) {
            code = Integer.parseInt(line[0]);
            sName = line[1];
            gName = line[2];
            team = line[3];
            novInt = line[4];
            ageGrp = line[5];
        }
    }

    public static class LegacyResult {

        int niCat;
        int ageCat;
        int dFinTot;
        String swmrNo;
        BigDecimal finTot;
        String novInt;
        String ageGrp;
        String fName;
        String gName;
        String team;
        int leagueNo;
        BigDecimal s1j1;
        BigDecimal s1j2;
        BigDecimal s1j3;
        BigDecimal s1j4;
        BigDecimal s1j5;
        BigDecimal s1Tot;
        BigDecimal s1Pen;
        BigDecimal s2j1;
        BigDecimal s2j2;
        BigDecimal s2j3;
        BigDecimal s2j4;
        BigDecimal s2j5;
        BigDecimal s2Tot;
        BigDecimal s2Pen;
        BigDecimal s3j1;
        BigDecimal s3j2;
        BigDecimal s3j3;
        BigDecimal s3j4;
        BigDecimal s3j5;
        BigDecimal s3Tot;
        BigDecimal s3Pen;
        BigDecimal s4j1;
        BigDecimal s4j2;
        BigDecimal s4j3;
        BigDecimal s4j4;
        BigDecimal s4j5;
        BigDecimal s4Tot;
        BigDecimal s4Pen;
        BigDecimal totPen;
        int place;
        BigDecimal points;

        LegacyResult(String[] line) {
            niCat = Integer.valueOf(line[0]);
            ageCat = Integer.valueOf(line[1]);
            dFinTot = Integer.valueOf(line[2]);
            swmrNo = line[3].toUpperCase();
            finTot = new BigDecimal(line[4]).setScale(2);
            novInt = line[5];
            ageGrp = line[6];
            fName = line[7];
            gName = line[8];
            team = line[9];
            leagueNo = Integer.valueOf(line[10]);
            s1j1 = new BigDecimal(line[11]).setScale(1);
            s1j2 = new BigDecimal(line[12]).setScale(1);
            s1j3 = new BigDecimal(line[13]).setScale(1);
            s1j4 = new BigDecimal(line[14]).setScale(1);
            s1j5 = new BigDecimal(line[15]).setScale(1);
            s1Tot = new BigDecimal(line[16]).setScale(2);
            s1Pen = new BigDecimal(line[17]).setScale(1);
            s2j1 = new BigDecimal(line[18]).setScale(1);
            s2j2 = new BigDecimal(line[19]).setScale(1);
            s2j3 = new BigDecimal(line[20]).setScale(1);
            s2j4 = new BigDecimal(line[21]).setScale(1);
            s2j5 = new BigDecimal(line[22]).setScale(1);
            s2Tot = new BigDecimal(line[23]).setScale(2);
            s2Pen = new BigDecimal(line[24]).setScale(1);
            s3j1 = new BigDecimal(line[25]).setScale(1);
            s3j2 = new BigDecimal(line[26]).setScale(1);
            s3j3 = new BigDecimal(line[27]).setScale(1);
            s3j4 = new BigDecimal(line[28]).setScale(1);
            s3j5 = new BigDecimal(line[29]).setScale(1);
            s3Tot = new BigDecimal(line[30]).setScale(2);
            s3Pen = new BigDecimal(line[31]).setScale(1);
            s4j1 = new BigDecimal(line[32]).setScale(1);
            s4j2 = new BigDecimal(line[33]).setScale(1);
            s4j3 = new BigDecimal(line[34]).setScale(1);
            s4j4 = new BigDecimal(line[35]).setScale(1);
            s4j5 = new BigDecimal(line[36]).setScale(1);
            s4Tot = new BigDecimal(line[37]).setScale(2);
            s4Pen = new BigDecimal(line[38]).setScale(1);
            totPen = new BigDecimal(line[39]).setScale(2);
            place = Integer.valueOf(line[40]);
            points = new BigDecimal(line[41]).setScale(2);
        }
    }

    public static class LegacyMeet {

        String meetType;
        String homeTm;
        String oppnTm;
        String meetDate;
        String meetTitle;
        boolean n8USta1;
        boolean n8USta2;
        boolean n8USta3;
        boolean n8USta4;
        String nRestSta1;
        BigDecimal nRestS1DD;
        String nRestS1Des;
        String nRestSta2;
        BigDecimal nRestS2DD;
        String nRestS2Des;
        String nRestSta3;
        BigDecimal nRestS3DD;
        String nRestS3Des;
        String nRestSta4;
        BigDecimal nRestS4DD;
        String nRestS4Des;
        String iSta1;
        BigDecimal iS1DD;
        String iS1Des;
        String iSta2;
        BigDecimal iS2DD;
        String iS2Des;
        String iSta3;
        BigDecimal iS3DD;
        String iS3Des;
        String iSta4;
        BigDecimal iS4DD;
        String iS4Des;
        Map<String, LegacyResult> results;

        public Map<String, LegacyResult> getResults() {
            return results;
        }

        public void setResults(Map<String, LegacyResult> results) {
            this.results = results;
        }

        LegacyMeet(String line[]) {
            meetType = line[0];
            homeTm = line[1];
            oppnTm = line[2];
            meetDate = line[3];
            meetTitle = line[4];
            n8USta1 = "1".equals(line[5]);
            n8USta2 = "1".equals(line[6]);
            n8USta3 = "1".equals(line[7]);
            n8USta4 = "1".equals(line[8]);
            nRestSta1 = line[9];
            nRestS1DD = new BigDecimal(line[10]).setScale(1);
            nRestS1Des = line[11];
            nRestSta2 = line[12];
            nRestS2DD = new BigDecimal(line[13]).setScale(1);
            nRestS2Des = line[14];
            nRestSta3 = line[15];
            nRestS3DD = new BigDecimal(line[16]).setScale(1);
            nRestS3Des = line[17];
            nRestSta4 = line[18];
            nRestS4DD = new BigDecimal(line[19]).setScale(1);
            nRestS4Des = line[20];
            iSta1 = line[21];
            iS1DD = new BigDecimal(line[22]).setScale(1);
            iS1Des = line[23];
            iSta2 = line[24];
            iS2DD = new BigDecimal(line[25]).setScale(1);
            iS2Des = line[26];
            iSta3 = line[27];
            iS3DD = new BigDecimal(line[28]).setScale(1);
            iS3Des = line[29];
            iSta4 = line[30];
            iS4DD = new BigDecimal(line[31]).setScale(1);
            iS4Des = line[32];
        }
    }

    public static class FigureScoreTracker {

        public FigureScore figureScore;
        public BigDecimal total;
    }

    public static class FiguresParticipantTracker {

        public FiguresParticipant figuresParticipant;
        public BigDecimal total;
        public BigDecimal points;
        public int place;
    }
}
