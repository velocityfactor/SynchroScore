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
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.log4j.Level;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Figure;
import org.aquastarz.score.domain.FigureScore;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Meet;
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
            return MeetSelectionDialog.selectMeet(getMeets());
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

    public List<Meet> getMeets() {
        javax.persistence.Query query = entityManager.createNamedQuery("Meet.findAllMeetsDescendingDate");
        return query.getResultList();
    }

    public void saveMeet(Meet meet) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            meet = entityManager.merge(meet);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            logger.error("Error saving meet.", e);
        }
    }

    public List<Swimmer> getSwimmers(Team team) {
        Query swimmerQuery = entityManager.createQuery("SELECT s from Swimmer s WHERE s.team='" + team.getTeamId() + "' ORDER BY s.lastName,s.firstName");
        List<Swimmer> swimmers = swimmerQuery.getResultList();
        return swimmers;
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
                        if (seq > 'A') {
                            seq++;
                            nextOrder = maxOrder.substring(0, maxOrder.length() - 1) + seq;
                        } else {
                            nextOrder = maxOrder + "B";
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
        saveMeet(meet);
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
        //Calculate totals before saving
        logger.setLevel(Level.DEBUG);
        for (FigureScore figureScore : figureScores) {
            if(logger.isDebugEnabled()) logger.debug("Getting total for "+figureScore.getFigure().getName());
            figureScore.setTotalScore(totalScore(figureScore));
            if(figureScore.getTotalScore() == null) {
                logger.error("Saving figure scores aborted because of error getting totalScore.");
                return false;
            }
            else {
                if(logger.isDebugEnabled()) logger.debug("Total = "+figureScore.getTotalScore().toPlainString());
            }
        }
        logger.setLevel(Level.WARN);
        
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            for (FigureScore figureScore : figureScores) {
                figureScore = entityManager.merge(figureScore);
                if (!figuresParticipant.getFiguresScores().contains(figureScore)) {
                    figuresParticipant.getFiguresScores().add(figureScore);
                }
            }
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
        int percent = count * 100 / possible;

        if (countNovice) {
            logger.warn("Novice figures are " + percent + "% complete.");
        } else {
            logger.warn("Intermediate figures are " + percent + "% complete.");
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
            }
            else if(!maxRemoved && score.compareTo(max) == 0) {
                maxRemoved = true;
            }
            else {
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
        for(BigDecimal score : adjScores) {
            sum = sum.add(score);
        }

        //Multiply by DD
        BigDecimal ddSum = sum.multiply(fs.getFigure().getDegreeOfDifficulty());

        //Subtract penalty
        BigDecimal total = ddSum.subtract(fs.getPenalty());

        return total;
    }
}
