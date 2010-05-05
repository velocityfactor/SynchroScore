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

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Figure;
import org.aquastarz.score.domain.FigureScore;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Level;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Season;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.gui.MeetSelectionDialog;
import org.aquastarz.score.gui.SynchroFrame;

/**
 *
 * @author Shayne Hughes <velocityfactor@gmail.com>
 */
public class ScoreController {

    private static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(ScoreController.class.getName());
    EntityManager entityManager = ScoreApp.getEntityManager();
    private SynchroFrame mainFrame;

    public ScoreController() {
        init();
    }

    private void init() {
        Meet meet = selectMeetFromList();
        if (meet == null) {
            EntityTransaction transaction = entityManager.getTransaction();
            transaction.begin();
            try {
                meet = new Meet();
                meet.setName("Mock Meet");
                DateFormat df = SimpleDateFormat.getDateInstance(SimpleDateFormat.LONG);
                meet.setMeetDate(df.format(new Date()));
                meet.setSeason(ScoreApp.getCurrentSeason());
                transaction.commit();
            } catch (Exception e) {
                logger.error("Error creating new meet.", e);
                transaction.rollback();
                //TODO feedback
                System.exit(-1);
            }
        }
        mainFrame = new SynchroFrame(this, meet);
        mainFrame.setVisible(true);
    }

    private Meet selectMeetFromList() {
        try {
            return MeetSelectionDialog.selectMeet();
        } catch (MeetSelectionDialog.MeetSelectionCanceledException e) {
            System.exit(0);
        }

        return null;
    }

    public List<Team> getTeams() {
        javax.persistence.Query query = entityManager.createQuery("SELECT t FROM Team t order by t.name");
        return query.getResultList();
    }

    public List<Figure> getFigures() {
        javax.persistence.Query query = entityManager.createQuery("SELECT f FROM Figure f order by f.figureId");
        return query.getResultList();
    }

    public static List<Level> getLevels() {
        javax.persistence.Query query = ScoreApp.getEntityManager().createQuery("SELECT l FROM Level l order by l.levelId");
        return query.getResultList();
    }

    public static List<Meet> getMeets(Season season) {
        javax.persistence.Query query = ScoreApp.getEntityManager().createNamedQuery("Meet.findAllBySeasonOrderByDateDesc");
        query.setParameter("season", season);
        return query.getResultList();
    }

    public static Meet findMeet(Season season, String name) {
        javax.persistence.Query query = ScoreApp.getEntityManager().createNamedQuery("Meet.findBySeasonAndName");
        query.setParameter("season", season);
        query.setParameter("name", name);
        try {
            return (Meet) query.getSingleResult();
        }
        catch(Exception e) {
            return null;
        }
    }

    public static List<Season> getSeasons() {
        javax.persistence.Query query = ScoreApp.getEntityManager().createNamedQuery("Season.findAllOrderByName");
        return query.getResultList();
    }

    public static Season getSeason(String name) {
        javax.persistence.Query query = ScoreApp.getEntityManager().createNamedQuery("Season.findByName");
        query.setParameter("name", name);
        try {
            return (Season) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void saveMeet(Meet meet) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            if(entityManager.contains(meet)) {
                meet = entityManager.merge(meet);
            }
            else {
                entityManager.persist(meet);
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Error saving meet.", e);
        }
    }

    public static List<FiguresParticipant> findAllFiguresParticipantByMeetAndDivision(Meet meet, boolean isNovice) {
        if(!ScoreController.meetResultsValid(meet)) {
            return null;
        }
        Query figureOrderQuery = ScoreApp.getEntityManager().createNamedQuery("FiguresParticipant.findByMeetAndLevelOrderByTotalScore");
        figureOrderQuery.setParameter("meet", meet);
        figureOrderQuery.setParameter("level", isNovice?"N%":"I%");
        return figureOrderQuery.getResultList();
    }


    public FiguresParticipant findFiguresParticipantByFigureOrder(Meet meet, String figureOrder) {
        Query figureOrderQuery = entityManager.createNamedQuery("FiguresParticipant.findByMeetAndFigureOrder");
        figureOrderQuery.setParameter("meet", meet);
        figureOrderQuery.setParameter("figureOrder", figureOrder);
        try {
            FiguresParticipant figuresParticipant = (FiguresParticipant) figureOrderQuery.getSingleResult();

            return figuresParticipant;
        } catch (Exception e) {
            //not found
            return null;
        }
    }

    /* Accepts a new meet with no swimmers or an existing meet with swimmers.
     * Creates new FiguresParticipants as needed and gives them an appropriate
     * figureOrder if figureOrders have been calculated.  Deletes FigureParticipants
     * that are no longer needed.
     */
    public void updateFiguresSwimmers(Meet meet, List<Swimmer> swimmers) {
        List<FiguresParticipant> newList = new ArrayList<FiguresParticipant>();

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
            HashMap<Swimmer, FiguresParticipant> map = new HashMap<Swimmer, FiguresParticipant>();
            for (FiguresParticipant fp : meet.getFiguresParticipants()) {
                map.put(fp.getSwimmer(), fp);
            }

            for (Swimmer s : swimmers) {
                FiguresParticipant fp = map.remove(s);
                if (fp == null) { //Adding a new swimmer
                    FiguresParticipant newFp = new FiguresParticipant(meet, s);
                    newList.add(newFp);
                    if (meet.getFiguresOrderGenerated()) {
                        String maxOrder = "";
                        Integer newLevelOrder = newFp.getSwimmer().getLevel().getSortOrder();
                        for (FiguresParticipant levelFp : meet.getFiguresParticipants()) {
                            Integer levelOrder = levelFp.getSwimmer().getLevel().getSortOrder();
                            if (levelFp.getFigureOrder().compareTo(maxOrder) > 0 && newLevelOrder >= levelOrder) {
                                maxOrder = levelFp.getFigureOrder();
                            }
                        }
                        String nextOrder = null;
                        char seq = maxOrder.charAt(maxOrder.length() - 1);
                        if (seq >= 'A') {
                            seq++;
                            nextOrder = maxOrder.substring(0, maxOrder.length() - 1) + seq;
                        } else {
                            nextOrder = maxOrder + "A";
                        }
                        newFp.setFigureOrder(nextOrder);
                    }
                    entityManager.persist(newFp);
                } else {  //Keep existing swimmer
                    newList.add(fp);
                }
            }

            //Remaining FiguresParticipants in map not needed, delete them
            for (FiguresParticipant fp : map.values()) {
                entityManager.remove(fp);
            }
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error updating figures swimmers.", e);
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        meet.setFiguresParticipants(newList);
    }

    public boolean generateRandomFiguresOrder(Meet meet) {
        Map<Double, FiguresParticipant> map = new TreeMap<Double, FiguresParticipant>();
        for (FiguresParticipant fp : meet.getFiguresParticipants()) {
            map.put(Math.random() + fp.getSwimmer().getLevel().getSortOrder(), fp);
        }

        //Calculate length to zero pad numbers (could be trickier, but this is good enough)
        String format = "%02d";
        if (map.size() > 99) {
            format = "%03d";
        }
        if (map.size() > 999) {
            format = "%04d";
        }

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            int i = 1;
            for (FiguresParticipant fp : map.values()) {
                fp.setFigureOrder(String.format(format, i));
                i++;
            }
            meet.setFiguresOrderGenerated(true);
            transaction.commit();
        } catch (Exception e) {
            logger.error("Error saving randomized figures order.", e);
            transaction.rollback();
            return false;
        }

        return true;
    }

    public boolean saveFigureScores(FiguresParticipant figuresParticipant, Collection<FigureScore> figureScores) {
        //Mark meet as needing recalc
        figuresParticipant.getMeet().clearPoints();

        //Calculate totals before saving
        for (FigureScore figureScore : figureScores) {
            if (logger.isDebugEnabled()) {
                logger.debug("Getting total for " + figureScore.getFigure().getName());
            }
            figureScore.setTotalScore(totalScore(figureScore));
            if (figureScore.getTotalScore() == null) {
                logger.error("Saving figure scores aborted because of error getting totalScore.");
                return false;
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("Total = " + figureScore.getTotalScore().toPlainString());
                }
            }
        }

        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            BigDecimal totalScore = BigDecimal.ZERO;
            for (FigureScore figureScore : figureScores) {
                totalScore = totalScore.add(figureScore.getTotalScore());
                figureScore = entityManager.merge(figureScore);
                if (!figuresParticipant.getFiguresScores().contains(figureScore)) {
                    figuresParticipant.getFiguresScores().add(figureScore);
                }
            }
            figuresParticipant.setTotalScore(totalScore);
            figuresParticipant.setPlace(null);
            figuresParticipant.setPoints(null);
            entityManager.merge(figuresParticipant);

            transaction.commit();

            mainFrame.updateFiguresStatus();

            return true;
        } catch (Exception e) {
            logger.error("Error saving figures scores.", e);
            transaction.rollback();
            return false;
        }
    }

    public static boolean isValid(FigureScore figureScore) {
        return figureScore.getScore1() != null && figureScore.getScore2() != null && figureScore.getScore3() != null
                && figureScore.getScore4() != null && figureScore.getScore5() != null && figureScore.getPenalty() != null
                && figureScore.getFiguresParticipant() != null && figureScore.getFigure() != null;
    }

    public static boolean isNovice(FiguresParticipant fp) {
        return fp.getSwimmer().getLevel().getLevelId().startsWith("N");
    }

    public static int numberOfFigures(FiguresParticipant fp) {
        if ("N8".equals(fp.getSwimmer().getLevel().getLevelId())) {
            return 2;  // Novice 8 & Under have two figures
        } else {
            return 4;  // All others have four figures
        }
    }

    public static int percentCompleteFigures(Meet meet, boolean countNovice) {
        int count = 0;
        int possible = 0;
        for (FiguresParticipant fp : meet.getFiguresParticipants()) {
            if (isNovice(fp) == countNovice) {
                possible += numberOfFigures(fp);
                count += fp.getFiguresScores().size();
            }
        }
        int percent = possible > 0 ? count * 100 / possible : 0;

        if (logger.isDebugEnabled()) {
            if (countNovice) {
                logger.debug("Novice figures are " + percent + "% complete.");
            } else {
                logger.debug("Intermediate figures are " + percent + "% complete.");
            }
        }
        return percent;
    }

    public static BigDecimal totalScore(FigureScore fs) {
        // Must be valid (values for all five scores, penalty, etc)
        if (!isValid(fs)) {
            return null;
        }

        // Drop the lowest and highest scores
        List<BigDecimal> scores = new ArrayList<BigDecimal>(5);
        scores.add(fs.getScore1());
        scores.add(fs.getScore2());
        scores.add(fs.getScore3());
        scores.add(fs.getScore4());
        scores.add(fs.getScore5());

        BigDecimal max = BigDecimal.ZERO; // No score smaller than 0
        BigDecimal min = BigDecimal.TEN;  // No score larger than 10
        for (BigDecimal score : scores) {
            if (score.compareTo(max) > 0) {
                max = score;
            }
            if (score.compareTo(min) < 0) {
                min = score;
            }
        }
        boolean minRemoved = false;
        boolean maxRemoved = false;
        List<BigDecimal> adjScores = new ArrayList<BigDecimal>(5);
        for (BigDecimal score : scores) {
            if (!minRemoved && score.compareTo(min) == 0) {
                minRemoved = true;
            } else if (!maxRemoved && score.compareTo(max) == 0) {
                maxRemoved = true;
            } else {
                adjScores.add(score);
            }
        }

        //We must now have three scores
        if (adjScores.size() != 3) {
            logger.error("Dropped min and max but didn't end up with three scores.");
            logger.error("Scores = " + scores);
            logger.error("AdjScores = " + adjScores);
            return null;
        }

        //Sum the scores
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal score : adjScores) {
            sum = sum.add(score);
        }

        //Multiply by DD
        BigDecimal ddSum = sum.multiply(fs.getFigure().getDegreeOfDifficulty());

        //Subtract penalty
        BigDecimal total = ddSum.subtract(fs.getPenalty());

        //Score can't be less than zero
        if (BigDecimal.ZERO.compareTo(total) >= 0) {
            total = BigDecimal.ZERO;
        }

        return total;
    }

    public static boolean meetResultsValid(Meet meet) {
        if (meet.needsPointsCalc()) {
            calculateMeetResults(meet);
        }
        return !meet.hasCalcErrors();
    }

    public boolean figuresParticipantHasAllScores(FiguresParticipant fp) {
        //Regular meets N8U have 2 scores, otherwise 4
        if (fp.getMeet().getType() == 'R' && "N8U".equals(fp.getSwimmer().getLevel().getLevelId())) {
            return fp.getFiguresScores().size() == 2;
        } else {
            return fp.getFiguresScores().size() == 4;
        }
    }

    public static void calculateMeetResults(Meet meet) {
        List<String> calcErrors = new ArrayList<String>();

        //Calculate total score for figures participants
        for (FiguresParticipant fp : meet.getFiguresParticipants()) {
            if (fp.getFiguresScores().size() < 4) {
                calcErrors.add("Swimmer #" + fp.getFigureOrder() + " does not have 4 figure scores.");
            }
            for (FigureScore fs : fp.getFiguresScores()) {
                if (BigDecimal.ZERO.compareTo(fs.getTotalScore()) >= 0) {
                    calcErrors.add("Swimmer #" + fp.getFigureOrder() + " score for " + fs.getFigure().getName() + " is zero.");
                }
            }
            fp.setTotalScore(calculateTotalScore(fp));
        }

        //Assign places and points to figures participants by level
        for (Level level : getLevels()) {
            List<FiguresParticipant> pointList = new ArrayList<FiguresParticipant>();
            for (FiguresParticipant fp : meet.getFiguresParticipants()) {
                if (fp.getSwimmer().getLevel().equals(level)) {
                    pointList.add(fp);
                }
            }

            Collections.sort(pointList, new Comparator<FiguresParticipant>() {

                public int compare(FiguresParticipant fp1, FiguresParticipant fp2) {
                    return fp2.getTotalScore().compareTo(fp1.getTotalScore());
                }
            });

            BigDecimal lastTotal = BigDecimal.ZERO;
            Map<Integer, Integer> tieMap = new HashMap<Integer, Integer>();
            int place = 0;
            for (FiguresParticipant fp : pointList) {
                if (fp.getTotalScore().equals(lastTotal) && place > 0) {
                    //tie
                    fp.setPlace(place);
                } else {
                    int placeInc = 1;
                    if (tieMap.containsKey(place)) {
                        placeInc = tieMap.get(place);
                    }
                    place += placeInc;
                    fp.setPlace(place);
                }
                int tieCount = 0;
                if (tieMap.containsKey(place)) {
                    tieCount = tieMap.get(place);
                }
                tieCount++;
                tieMap.put(place, tieCount);
                lastTotal = fp.getTotalScore();
            }
            for (FiguresParticipant fp : pointList) {
                int tieCount = 1;
                if (tieMap.containsKey(fp.getPlace())) {
                    tieCount = tieMap.get(fp.getPlace());
                }
                fp.setPoints(calculateFigurePlacePoints(fp.getPlace(), tieCount, meet.getType()));
            }
        }

        //Sum points for teams
        Map<Team, BigDecimal> meetPointsMap = new HashMap<Team, BigDecimal>();
        meetPointsMap.put(meet.getHomeTeam(), BigDecimal.ZERO);
        for (Team team : meet.getOpponents()) {
            meetPointsMap.put(team, BigDecimal.ZERO);
        }
        for (FiguresParticipant fp : meet.getFiguresParticipants()) {
            Team team = fp.getSwimmer().getTeam();
            BigDecimal points = meetPointsMap.get(team);
            points = points.add(fp.getPoints());
            meetPointsMap.put(team, points);
        }
        meet.setPointsMap(meetPointsMap);

        //Assign places for teams
        Map<Team, Integer> meetPlaceMap = new HashMap<Team, Integer>();
        List<Entry<Team, BigDecimal>> list = new LinkedList<Entry<Team, BigDecimal>>(meetPointsMap.entrySet());
        Collections.sort(list, new Comparator<Entry<Team, BigDecimal>>() {

            public int compare(Entry<Team, BigDecimal> o1, Entry<Team, BigDecimal> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int place = 0;
        BigDecimal lastPoints = BigDecimal.ZERO;
        for (Entry<Team, BigDecimal> entry : list) {
            BigDecimal points = entry.getValue();
            if (!points.equals(lastPoints) || place == 0) {
                place++;
            }
            meetPlaceMap.put(entry.getKey(), place);
            lastPoints = points;
        }
        meet.setPlaceMap(meetPlaceMap);

        //Save
        EntityManager entityManager = ScoreApp.getEntityManager();
        entityManager.getTransaction().begin();
        for(FiguresParticipant fp : meet.getFiguresParticipants()) {
            entityManager.merge(fp);
        }
        entityManager.getTransaction().commit();
    }

    private static BigDecimal calculateFigurePlacePoints(int place, int tieCount, char meetType) {
        BigDecimal points = BigDecimal.ZERO;
        for (int i = 0; i < tieCount; i++) {
            points = points.add(getFigurePlacePoints(place + i, meetType));
        }
        points = points.divide(BigDecimal.valueOf(tieCount),2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros();
        return points;
    }

    private static BigDecimal getFigurePlacePoints(int place, char meetType) {
        if (place < 1) {
            throw new IllegalArgumentException("place<1 not allowed.");
        }
        if (meetType == 'R') {
            switch (place) {
                case 1:
                    return BigDecimal.valueOf(7);
                case 2:
                    return BigDecimal.valueOf(5);
                case 3:
                    return BigDecimal.valueOf(3);
                case 4:
                    return BigDecimal.valueOf(2);
                case 5:
                    return BigDecimal.valueOf(1);
                default:
                    return BigDecimal.ZERO;
            }
        } else if (meetType == 'C') {
            switch (place) {
                case 1:
                    return BigDecimal.valueOf(10);
                case 2:
                    return BigDecimal.valueOf(8);
                case 3:
                    return BigDecimal.valueOf(6);
                case 4:
                    return BigDecimal.valueOf(5);
                case 5:
                    return BigDecimal.valueOf(4);
                case 6:
                    return BigDecimal.valueOf(3);
                case 7:
                    return BigDecimal.valueOf(2);
                case 8:
                    return BigDecimal.valueOf(1);
                default:
                    return BigDecimal.ZERO;
            }
        } else {
            throw new IllegalArgumentException("meetType was \"" + meetType + "\" but only R and C are allowed.");
        }
    }

    public static BigDecimal calculateTotalScore(FiguresParticipant fp) {
        BigDecimal total = BigDecimal.ZERO;
        for (FigureScore fs : fp.getFiguresScores()) {
            total = total.add(fs.getTotalScore());
        }
        return total;
    }

    public static Map<Team,BigDecimal> calculateTeamPoints(Meet meet) {
        if(!ScoreController.meetResultsValid(meet)) {
            return null;
        }

        Map<Team,BigDecimal> meetPoints = new HashMap<Team,BigDecimal>();
        for(FiguresParticipant fp:meet.getFiguresParticipants()) {
            if(fp.getPoints()==null) return null; //Points not calculated!
            BigDecimal points = meetPoints.get(fp.getSwimmer().getTeam());
            if(points==null) points=BigDecimal.ZERO;
            points=points.add(fp.getPoints());
            meetPoints.put(fp.getSwimmer().getTeam(), points);
        }
        return meetPoints;
    }
}
