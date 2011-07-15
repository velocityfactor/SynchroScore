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
package org.aquastarz.score.manager;

import au.com.bytecode.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.controller.ScoreController;
import org.aquastarz.score.domain.FigureScore;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Routine;
import org.aquastarz.score.domain.Team;

public class MeetManager {

    private static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(SwimmerManager.class.getName());

    public static void exportMeet(Meet meet, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write("Meet,\"" + meet.getName() + "\",");
        writer.write("\"" + meet.getMeetDate() + "\",");
        writer.write("\"" + meet.getType() + "\",");
        writer.write("\"" + meet.getSeason().getName() + "\",");
        writer.write("\"" + meet.getFiguresOrderGenerated() + "\",");
        writer.write("\"" + meet.isRoutinesOrderGenerated() + "\",");
        writer.write("\"" + meet.getHomeTeam().getTeamId() + "\"");
        for (Team team : meet.getOpponents()) {
            writer.write(",\"" + team.getTeamId() + "\"");
        }
        writer.write("\n");
        writer.write("NovFigure1,\"" + meet.getNov1Figure().getFigureId() + "\",\"" + meet.getNov1Figure().getName() + "\"\n");
        writer.write("NovFigure2,\"" + meet.getNov2Figure().getFigureId() + "\",\"" + meet.getNov2Figure().getName() + "\"\n");
        writer.write("NovFigure3,\"" + meet.getNov3Figure().getFigureId() + "\",\"" + meet.getNov3Figure().getName() + "\"\n");
        writer.write("NovFigure4,\"" + meet.getNov4Figure().getFigureId() + "\",\"" + meet.getNov4Figure().getName() + "\"\n");
        writer.write("IntFigure1,\"" + meet.getInt1Figure().getFigureId() + "\",\"" + meet.getInt1Figure().getName() + "\"\n");
        writer.write("IntFigure2,\"" + meet.getInt2Figure().getFigureId() + "\",\"" + meet.getInt2Figure().getName() + "\"\n");
        writer.write("IntFigure3,\"" + meet.getInt3Figure().getFigureId() + "\",\"" + meet.getInt3Figure().getName() + "\"\n");
        writer.write("IntFigure4,\"" + meet.getInt4Figure().getFigureId() + "\",\"" + meet.getInt4Figure().getName() + "\"\n");
        writer.write("EUFigs,\"" + meet.isEu1() + "\",\"" + meet.isEu2() + "\",\"" + meet.isEu3() + "\",\"" + meet.isEu4() + "\"\n");
        for (FiguresParticipant fp : meet.getFiguresParticipants()) {
            writer.write(getFiguresParticipantExport(fp));
        }
        for (Routine r : meet.getRoutines()) {
            writer.write("Routine," + r.getRoutineOrder() + "," + r.getTeam().getTeamId() + "," + r.getLevel().getLevelId()
                    + "," + r.getRoutineType() + "," + r.getNumSwimmers() + ",\"" + r.getName() + "\",\"" + r.getSwimmers1() + "\",\""
                    + r.getSwimmers2() + "\"," + r.getAScore1() + "," + r.getAScore2() + "," + r.getAScore3() + "," + r.getAScore4()
                    + "," + r.getAScore5() + "," + r.getAScore6() + "," + r.getAScore7() + "," + r.getTScore1() + "," + r.getTScore2()
                    + "," + r.getTScore3() + "," + r.getTScore4() + "," + r.getTScore5() + "," + r.getTScore6() + "," + r.getTScore7()
                    + "," + r.getPenalty() + "\n");
        }
        writer.close();
    }

    public static String getFiguresParticipantExport(FiguresParticipant fp) {
        StringBuilder sb = new StringBuilder();
        sb.append("FiguresParticipant,\"").append(fp.getFigureOrder()).append("\",").append(fp.getSwimmer().getLeagueNum()).append(",\"").append(fp.getSwimmer().getFirstName()).append("\",\"").append(fp.getSwimmer().getLastName()).append("\",\"").append(fp.getSwimmer().getLevel().getLevelId()).append("\",\"").append(fp.getSwimmer().getTeam().getTeamId()).append("\"\n");
        for (FigureScore fs : fp.getFiguresScores()) {
            sb.append("FigureScore,\"").append(fp.getFigureOrder()).append("\",").append(fs.getStation()).append(",").append(fs.getScore1()).append(",").append(fs.getScore2()).append(",").append(fs.getScore3()).append(",").append(fs.getScore4()).append(",").append(fs.getScore5()).append(",").append(fs.getPenalty()).append("\n");
        }
        return sb.toString();
    }

    public static Meet readMeet(File file) {
        Meet meet = null;
        try {
            CSVReader csv = new CSVReader(new FileReader(file));
            List<String[]> rows = csv.readAll();
            csv.close();
            HashMap<String, FiguresParticipant> map = new HashMap<String, FiguresParticipant>();
            for (String[] sa : rows) {
                if ("Meet".equals(sa[0])) {
                    meet = new Meet();
                    meet.setName(sa[1]);
                    meet.setMeetDate(sa[2]);
                    meet.setType(sa[3].charAt(0));
                    meet.setSeason(SeasonManager.find(sa[4]));
                    meet.setFiguresOrderGenerated(Boolean.parseBoolean(sa[5]));
                    meet.setRoutinesOrderGenerated(Boolean.parseBoolean(sa[6]));
                    meet.setHomeTeam(TeamManager.findById(sa[7]));
                    ArrayList<Team> opponents = new ArrayList<Team>();
                    for (int i = 8; i < sa.length; i++) {
                        opponents.add(TeamManager.findById(sa[i]));
                    }
                    meet.setOpponents(opponents);
                }
                if ("NovFigure1".equals(sa[0])) {
                    meet.setNov1Figure(FigureManager.findById(sa[1]));
                }
                if ("NovFigure2".equals(sa[0])) {
                    meet.setNov2Figure(FigureManager.findById(sa[1]));
                }
                if ("NovFigure3".equals(sa[0])) {
                    meet.setNov3Figure(FigureManager.findById(sa[1]));
                }
                if ("NovFigure4".equals(sa[0])) {
                    meet.setNov4Figure(FigureManager.findById(sa[1]));
                }
                if ("IntFigure1".equals(sa[0])) {
                    meet.setInt1Figure(FigureManager.findById(sa[1]));
                }
                if ("IntFigure2".equals(sa[0])) {
                    meet.setInt2Figure(FigureManager.findById(sa[1]));
                }
                if ("IntFigure3".equals(sa[0])) {
                    meet.setInt3Figure(FigureManager.findById(sa[1]));
                }
                if ("IntFigure4".equals(sa[0])) {
                    meet.setInt4Figure(FigureManager.findById(sa[1]));
                }
                if ("EUFigs".equals(sa[0])) {
                    meet.setEu1(Boolean.parseBoolean(sa[1]));
                    meet.setEu2(Boolean.parseBoolean(sa[2]));
                    meet.setEu3(Boolean.parseBoolean(sa[3]));
                    meet.setEu4(Boolean.parseBoolean(sa[4]));
                }
                if ("FiguresParticipant".equals(sa[0])) {
                    FiguresParticipant fp = new FiguresParticipant();
                    fp.setMeet(meet);
                    fp.setFigureOrder(sa[1]);
                    fp.setSwimmer(SwimmerManager.findByLeagueNum(Integer.parseInt(sa[2]), meet.getSeason()));
                    meet.getFiguresParticipants().add(fp);
                    map.put(fp.getFigureOrder(), fp);
                }
                if ("FigureScore".equals(sa[0])) {
                    FigureScore fs = new FigureScore();
                    FiguresParticipant fp = map.get(sa[1]);
                    fs.setFiguresParticipant(fp);
                    fs.setStation(Integer.parseInt(sa[2]));
                    fs.setScore1("null".equals(sa[3]) ? null : new BigDecimal(sa[3]));
                    fs.setScore2("null".equals(sa[4]) ? null : new BigDecimal(sa[4]));
                    fs.setScore3("null".equals(sa[5]) ? null : new BigDecimal(sa[5]));
                    fs.setScore4("null".equals(sa[6]) ? null : new BigDecimal(sa[6]));
                    fs.setScore5("null".equals(sa[7]) ? null : new BigDecimal(sa[7]));
                    fs.setPenalty("null".equals(sa[8]) ? null : new BigDecimal(sa[8]));
                    fs.setTotalScore(ScoreController.totalScore(fs));
                    fp.getFiguresScores().add(fs);
                }
                if ("Routine".equals(sa[0])) {
                    Routine r = new Routine();
                    r.setMeet(meet);
                    r.setRoutineOrder(Integer.parseInt(sa[1]));
                    r.setTeam(TeamManager.findById(sa[2]));
                    r.setLevel(RoutineLevelManager.find(sa[3]));
                    r.setRoutineType(sa[4]);
                    r.setNumSwimmers(Integer.parseInt(sa[5]));
                    r.setName(sa[6]);
                    r.setSwimmers1(sa[7]);
                    r.setSwimmers2(sa[8]);
                    r.setAScore1("null".equals(sa[9]) ? null : new BigDecimal(sa[9]));
                    r.setAScore2("null".equals(sa[10]) ? null : new BigDecimal(sa[10]));
                    r.setAScore3("null".equals(sa[11]) ? null : new BigDecimal(sa[11]));
                    r.setAScore4("null".equals(sa[12]) ? null : new BigDecimal(sa[12]));
                    r.setAScore5("null".equals(sa[13]) ? null : new BigDecimal(sa[13]));
                    r.setAScore6("null".equals(sa[14]) ? null : new BigDecimal(sa[14]));
                    r.setAScore7("null".equals(sa[15]) ? null : new BigDecimal(sa[15]));
                    r.setTScore1("null".equals(sa[16]) ? null : new BigDecimal(sa[16]));
                    r.setTScore2("null".equals(sa[17]) ? null : new BigDecimal(sa[17]));
                    r.setTScore3("null".equals(sa[18]) ? null : new BigDecimal(sa[18]));
                    r.setTScore4("null".equals(sa[19]) ? null : new BigDecimal(sa[19]));
                    r.setTScore5("null".equals(sa[20]) ? null : new BigDecimal(sa[20]));
                    r.setTScore6("null".equals(sa[21]) ? null : new BigDecimal(sa[21]));
                    r.setTScore7("null".equals(sa[22]) ? null : new BigDecimal(sa[22]));
                    r.setPenalty("null".equals(sa[23]) ? null : new BigDecimal(sa[23]));
                    meet.getRoutines().add(r);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
            meet = null;
        }
        return meet;
    }

    public static List<String> compareMeets(Meet meet1, Meet meet2) {
        ArrayList<String> errors = new ArrayList<String>();

        //Compare figures
        HashMap<String, FigureScore> fsMap = new HashMap<String, FigureScore>();
        for (FiguresParticipant fp1 : meet1.getFiguresParticipants()) {
            for (FigureScore fs1 : fp1.getFiguresScores()) {
                fsMap.put(fp1.getFigureOrder() + "-" + fs1.getStation(), fs1);
            }
        }
        for (FiguresParticipant fp2 : meet2.getFiguresParticipants()) {
            for (FigureScore fs2 : fp2.getFiguresScores()) {
                FigureScore fs1 = fsMap.get(fp2.getFigureOrder() + "-" + fs2.getStation());
                if (fs1 == null) {
                    errors.add("Missing figure score " + fs2.getStation() + " for swimmer #" + fp2.getFigureOrder());
                } else {
                    if (!fs1.equals(fs2)) {
                        errors.add("Mismatched figure score " + fs2.getStation() + " for swimmer #" + fp2.getFigureOrder());
                    }
                    fsMap.remove(fp2.getFigureOrder() + "-" + fs2.getStation());
                }
            }
        }
        for (FigureScore fs1 : fsMap.values()) {
            errors.add("Extra figure score " + fs1.getStation() + " for swimmer #" + fs1.getFiguresParticipant().getFigureOrder());
        }

        //Compare routines
        HashMap<String, Routine> routineMap = new HashMap<String, Routine>();
        for (Routine routine1 : meet1.getRoutines()) {
            routineMap.put(routine1.getName(), routine1);
        }
        for (Routine routine2 : meet2.getRoutines()) {
            Routine routine1 = routineMap.get(routine2.getName());
            if (routine1 == null) {
                errors.add("Missing routine \"" + routine2.getName() + "\"");
            } else {
                if (!routine1.equals(routine2)) {
                    errors.add("Mismatched routine score for \"" + routine1.getName() + "\"");
                }
                routineMap.remove(routine2.getName());
            }
        }
        for (Routine routine1 : routineMap.values()) {
            errors.add("Extra routine score for \"" + routine1.getName() + "\"");
        }
        return errors;
    }

    public static void importMeet(File file) throws IOException {
        try {
            Meet meet = readMeet(file);
            try {
                ScoreApp.getEntityManager().getTransaction().begin();
                ScoreApp.getEntityManager().persist(meet);
                for (FiguresParticipant fp : meet.getFiguresParticipants()) {
                    ScoreApp.getEntityManager().persist(fp);
                    for (FigureScore fs : fp.getFiguresScores()) {
                        ScoreApp.getEntityManager().persist(fs);
                    }
                }
                for (Routine r : meet.getRoutines()) {
                    ScoreApp.getEntityManager().persist(r);
                }
                ScoreApp.getEntityManager().getTransaction().commit();
            } catch (Exception e) {
                ScoreApp.getEntityManager().getTransaction().rollback();
                throw new IOException("Error commmitting meet to database.  " + e.getMessage());
            }
        } catch (Exception e) {
            throw new IOException("Error reading file.  " + e.getMessage());
        }
    }
}
