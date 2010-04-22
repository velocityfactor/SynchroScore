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
package org.aquastarz.score.controller;

import au.com.bytecode.opencsv.CSVReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.FigureScore;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Level;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.domain.Team;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScoreControllerTest {

    Map<String, LegacyResult> legacyResults = new HashMap<String, LegacyResult>();

    public ScoreControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        CSVReader csv = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("results.csv")));
        String[] nextLine;
        try {
            //skip header
            csv.readNext();
            while ((nextLine = csv.readNext()) != null) {
                LegacyResult lr = new LegacyResult(nextLine);
                legacyResults.put(lr.swmrNo, lr);
            }
            csv.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        csv = new CSVReader(new InputStreamReader(getClass().getResourceAsStream("figstat.csv")));
        LegacyMeet legacyMeet=null;
        try {
            //skip header
            csv.readNext();
            legacyMeet = new LegacyMeet(nextLine);
            csv.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Meet meet = new Meet();
        meet.setName(legacyMeet.meetTitle);
        meet.setMeetDate(legacyMeet.meetDate);
        if("Dual".equals(legacyMeet.meetType)) {
            meet.setType('R');
        }
        else {
            meet.setType('C');
        }

        //TODO fill these from legacyMeet
        meet.setHomeTeam(null);
        meet.setEu1Figure(null);
        meet.setEu2Figure(null);
        meet.setNov1Figure(null);
        meet.setNov2Figure(null);
        meet.setNov3Figure(null);
        meet.setNov4Figure(null);
        meet.setInt1Figure(null);
        meet.setInt2Figure(null);
        meet.setInt3Figure(null);
        meet.setInt4Figure(null);
        meet.setFiguresOrderGenerated(true);
        meet.setHomeTeam(null);
        meet.setOpponents(null);
        EntityManager entityManager = ScoreApp.getEntityManager();
        Map<String,Level> levels = new HashMap<String,Level>();
        levels.put("NOV8 & Under",entityManager.find(Level.class,"N8"));
        levels.put("NOV9-10",entityManager.find(Level.class,"N9-10"));
        levels.put("NOV11-12",entityManager.find(Level.class,"N11-12"));
        levels.put("NOV13-14",entityManager.find(Level.class,"N13-14"));
        levels.put("NOV15-18",entityManager.find(Level.class,"N15-18"));
        levels.put("INT11-12",entityManager.find(Level.class,"I11-12"));
        levels.put("INT13-14",entityManager.find(Level.class,"I13-14"));
        levels.put("INT15-16 (I)",entityManager.find(Level.class,"I15-16"));
        levels.put("INT17-18 (I)",entityManager.find(Level.class,"I17-18"));

        int swimmerId=1;
        for(LegacyResult lr : legacyResults.values()) {
            Team team=entityManager.find(Team.class, lr.team);
            if(team==null) {
                System.out.println("Team not found ["+lr.team+"]");
            }
            Level level=levels.get(lr.novInt+lr.ageGrp);
            if(level==null) {
                System.out.println("Level not found ["+lr.novInt+lr.ageGrp+"]");
            }
            Swimmer swimmer = new Swimmer();
            swimmer.setFirstName(lr.gName);
            swimmer.setLastName(lr.fName);
            swimmer.setLevel(level);
            swimmer.setSwimmerId(swimmerId++);
            swimmer.setTeam(team);
            entityManager.persist(swimmer);
        }
        for(LegacyResult lr : legacyResults.values()) {
            FiguresParticipant fp = new FiguresParticipant();
            fp.setFigureOrder(lr.swmrNo);
            List<FigureScore> scores = new ArrayList<FigureScore>();
            for(int i=0;i<5) {
                FigureScore fs = new FigureScore();

            }
            fp.setFiguresScores(null);
            fp.setMeet(meet);

            fp.setSwimmer(null);

            entityManager.persist(fp);
            meet.getFiguresParticipants().add(fp);
        }
        entityManager.persist(meet);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCalculateTotalScore() {
    }



    private class LegacyResult {

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
            swmrNo = line[3];
            finTot = new BigDecimal(line[5]);
            novInt = line[5];
            ageGrp = line[6];
            fName = line[7];
            gName = line[8];
            team = line[9];
            leagueNo = Integer.valueOf(line[10]);
            s1j1 = new BigDecimal(line[11]);
            s1j2 = new BigDecimal(line[12]);
            s1j3 = new BigDecimal(line[13]);
            s1j4 = new BigDecimal(line[14]);
            s1j5 = new BigDecimal(line[15]);
            s1Tot = new BigDecimal(line[16]);
            s1Pen = new BigDecimal(line[17]);
            s2j1 = new BigDecimal(line[18]);
            s2j2 = new BigDecimal(line[19]);
            s2j3 = new BigDecimal(line[20]);
            s2j4 = new BigDecimal(line[21]);
            s2j5 = new BigDecimal(line[22]);
            s2Tot = new BigDecimal(line[23]);
            s2Pen = new BigDecimal(line[24]);
            s3j1 = new BigDecimal(line[25]);
            s3j2 = new BigDecimal(line[26]);
            s3j3 = new BigDecimal(line[27]);
            s3j4 = new BigDecimal(line[28]);
            s3j5 = new BigDecimal(line[29]);
            s3Tot = new BigDecimal(line[30]);
            s3Pen = new BigDecimal(line[31]);
            s4j1 = new BigDecimal(line[32]);
            s4j2 = new BigDecimal(line[33]);
            s4j3 = new BigDecimal(line[34]);
            s4j4 = new BigDecimal(line[35]);
            s4j5 = new BigDecimal(line[36]);
            s4Tot = new BigDecimal(line[37]);
            s4Pen = new BigDecimal(line[38]);
            totPen = new BigDecimal(line[39]);
            place = Integer.valueOf(line[40]);
            points = new BigDecimal(line[41]);
        }
    }

    private class LegacyMeet {
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
            nRestS1DD = new BigDecimal(line[10]);
            nRestS1Des = line[11];
            nRestSta2 = line[12];
            nRestS2DD = new BigDecimal(line[13]);
            nRestS2Des = line[14];
            nRestSta3 = line[15];
            nRestS3DD = new BigDecimal(line[16]);
            nRestS3Des = line[17];
            nRestSta4 = line[18];
            nRestS4DD = new BigDecimal(line[19]);
            nRestS4Des = line[20];
            iSta1 = line[21];
            iS1DD = new BigDecimal(line[22]);
            iS1Des = line[23];
            iSta2 = line[24];
            iS2DD = new BigDecimal(line[25]);
            iS2Des = line[26];
            iSta3 = line[27];
            iS3DD = new BigDecimal(line[28]);
            iS3Des = line[29];
            iSta4 = line[30];
            iS4DD = new BigDecimal(line[31]);
            iS4Des = line[32];
        }
    }
}
