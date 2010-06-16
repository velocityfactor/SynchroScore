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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Routine;

public class RoutineManager {

    private static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(RoutineManager.class.getName());

    public static List<Routine> findAll(Meet meet) {
        if (ScoreApp.getEntityManager().contains(meet)) {
            Query query = ScoreApp.getEntityManager().createNamedQuery("Routine.findByMeetOrderByRoutineOrder");
            query.setParameter("meet", meet);
            return query.getResultList();
        } else {
            return new ArrayList<Routine>();
        }
    }

    public static Routine getNewRoutine(Meet meet) {
        Routine routine = new Routine();

        routine.setName("NEW ROUTINE");
        routine.setRoutineOrder(0);
        routine.setMeet(meet);
        routine.setLevel(RoutineLevelManager.findAll().get(0));
        routine.setRoutineType("Solo");
        routine.setTeam(meet.getHomeTeam());
        save(routine);

        return routine;
    }

    public static void save(Routine routine) {
        EntityManager em = ScoreApp.getEntityManager();
        em.getTransaction().begin();
        em.persist(routine);
        em.getTransaction().commit();
        routine.getMeet().clearPoints();
    }

    public static void randomize(Meet meet) {
        List<Routine> routines = findAll(meet);
        TreeMap<Double, Routine> map = new TreeMap<Double, Routine>();
        for (Routine routine : routines) {
            Double weight = Math.random() + routine.getLevel().getSortOrder();
            map.put(weight, routine);
        }
        int order = 1;
        EntityManager em = ScoreApp.getEntityManager();
        em.getTransaction().begin();
        for (Routine routine : map.values()) {
            routine.setRoutineOrder(order);
            em.persist(routine);
            order++;
        }
        meet.setRoutinesOrderGenerated(true);
        em.persist(meet);
        em.getTransaction().commit();
    }

    public static boolean isValid(Routine routine) {
        if (routine.getName() == null || routine.getName().length() == 0) {
            return false;
        }
        boolean isNull = (routine.getTScore1() == null)
                && (routine.getTScore2() == null)
                && (routine.getTScore3() == null)
                && (routine.getTScore4() == null)
                && (routine.getTScore5() == null)
                && (routine.getTScore6() == null)
                && (routine.getTScore7() == null)
                && (routine.getAScore1() == null)
                && (routine.getAScore2() == null)
                && (routine.getAScore3() == null)
                && (routine.getAScore4() == null)
                && (routine.getAScore5() == null)
                && (routine.getAScore6() == null)
                && (routine.getAScore7() == null)
                && (routine.getPenalty() == null);
        if (!(isNull || isScored(routine))) {
            return false;
        }
        return true;
    }

    public static boolean isScored(Routine routine) {
        return (routine.getTScore1() != null)
                && (routine.getTScore2() != null)
                && (routine.getTScore3() != null)
                && (routine.getTScore4() != null)
                && (routine.getTScore5() != null)
                && (routine.getTScore6() != null)
                && (routine.getTScore7() != null)
                && (routine.getAScore1() != null)
                && (routine.getAScore2() != null)
                && (routine.getAScore3() != null)
                && (routine.getAScore4() != null)
                && (routine.getAScore5() != null)
                && (routine.getAScore6() != null)
                && (routine.getAScore7() != null)
                && (routine.getPenalty() != null);
    }

    public static void calculate(Routine routine) {
        // Must be valid (values for all five scores, penalty, etc)
        if (!isScored(routine)) {
            return;
        }

        char meetType = routine.getMeet().getType();

        // Drop the lowest and highest technical scores
        List<BigDecimal> scores = new ArrayList<BigDecimal>(7);
        scores.add(routine.getTScore1());
        scores.add(routine.getTScore2());
        scores.add(routine.getTScore3());
        scores.add(routine.getTScore4());
        scores.add(routine.getTScore5());
        if (!(meetType == 'C' && "TEAM".equals(routine.getRoutineType()))) {
            scores.add(routine.getTScore6());
            scores.add(routine.getTScore7());
        }

        BigDecimal max = BigDecimal.ZERO; // No score smaller than 0
        BigDecimal min = BigDecimal.TEN;  // No score larger than 10
        BigDecimal max2 = BigDecimal.ZERO; // No score smaller than 0
        BigDecimal min2 = BigDecimal.TEN;  // No score larger than 10
        for (BigDecimal score : scores) {
            if (score.compareTo(max) > 0) {
                if (score.compareTo(max2) > 0) {
                    max = max2;
                    max2 = score;
                } else {
                    max = score;
                }
            }
            if (score.compareTo(min) < 0) {
                if (score.compareTo(min2) < 0) {
                    min = min2;
                    min2 = score;
                } else {
                    min = score;
                }
            }
        }
        boolean minRemoved = false;
        boolean maxRemoved = false;
        boolean min2Removed = false;
        boolean max2Removed = false;
        boolean isDropTwo = (scores.size() == 7);
        List<BigDecimal> adjScores = new ArrayList<BigDecimal>(3);
        for (BigDecimal score : scores) {
            if (isDropTwo && !minRemoved && score.compareTo(min) == 0) {
                minRemoved = true;
            } else if (isDropTwo && !maxRemoved && score.compareTo(max) == 0) {
                maxRemoved = true;
            } else if (!min2Removed && score.compareTo(min2) == 0) {
                min2Removed = true;
            } else if (!max2Removed && score.compareTo(max2) == 0) {
                max2Removed = true;
            } else {
                adjScores.add(score);
            }
        }

        //We must now have the correct number of scores
        if (adjScores.size() != 3) {
            logger.error("Dropped min and max but didn't end up with three scores.");
            logger.error("Technical Scores = " + scores);
            logger.error("AdjScores = " + adjScores);
            return;
        }

        //Sum the scores
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal score : adjScores) {
            sum = sum.add(score);
        }

        //Multiply by DD (scale becomes 2)
        sum = sum.multiply(new BigDecimal(2));

        routine.setTechScore(sum);

        // Drop the lowest and highest artistic scores
        scores = new ArrayList<BigDecimal>(7);
        scores.add(routine.getAScore1());
        scores.add(routine.getAScore2());
        scores.add(routine.getAScore3());
        scores.add(routine.getAScore4());
        scores.add(routine.getAScore5());
        if (!(meetType == 'C' && "TEAM".equals(routine.getRoutineType()))) {
            scores.add(routine.getAScore6());
            scores.add(routine.getAScore7());
        }

        max = BigDecimal.ZERO; // No score smaller than 0
        min = BigDecimal.TEN;  // No score larger than 10
        max2 = BigDecimal.ZERO; // No score smaller than 0
        min2 = BigDecimal.TEN;  // No score larger than 10
        for (BigDecimal score : scores) {
            if (score.compareTo(max) > 0) {
                if (score.compareTo(max2) > 0) {
                    max = max2;
                    max2 = score;
                } else {
                    max = score;
                }
            }
            if (score.compareTo(min) < 0) {
                if (score.compareTo(min2) < 0) {
                    min = min2;
                    min2 = score;
                } else {
                    min = score;
                }
            }
        }
        minRemoved = false;
        maxRemoved = false;
        min2Removed = false;
        max2Removed = false;
        adjScores = new ArrayList<BigDecimal>(3);
        for (BigDecimal score : scores) {
            if (isDropTwo && !minRemoved && score.compareTo(min) == 0) {
                minRemoved = true;
            } else if (isDropTwo && !maxRemoved && score.compareTo(max) == 0) {
                maxRemoved = true;
            } else if (!min2Removed && score.compareTo(min2) == 0) {
                min2Removed = true;
            } else if (!max2Removed && score.compareTo(max2) == 0) {
                max2Removed = true;
            } else {
                adjScores.add(score);
            }
        }

        //We must now have the correct number of scores
        if (adjScores.size() != 3) {
            logger.error("Dropped min and max but didn't end up with three scores.");
            logger.error("Artistic Scores = " + scores);
            logger.error("AdjScores = " + adjScores);
            return;
        }

        //Sum the scores
        sum = BigDecimal.ZERO;
        for (BigDecimal score : adjScores) {
            sum = sum.add(score);
        }

        routine.setArtScore(sum.setScale(2));

        //Total and subtract penalty
        BigDecimal total = routine.getTechScore().add(routine.getArtScore()).subtract(routine.getPenalty());

        //Score can't be less than zero
        if (BigDecimal.ZERO.compareTo(total) >= 0) {
            total = BigDecimal.ZERO;
        }

        routine.setTotalScore(total);
    }

    public static void delete(Routine routine) {
        EntityManager em = ScoreApp.getEntityManager();
        em.getTransaction().begin();
        em.remove(routine);
        em.getTransaction().commit();
    }

    public static BigDecimal calcChampsPlacePoints(int place, String routineType) {
        switch (place) {
            case 1:
                if ("SOLO".equals(routineType)) {
                    return new BigDecimal(10);
                } else if ("DUET".equals(routineType)) {
                    return new BigDecimal(14);
                } else if ("TRIO".equals(routineType)) {
                    return new BigDecimal(16);
                } else if ("TEAM".equals(routineType)) {
                    return new BigDecimal(18);
                }
            case 2:
                if ("SOLO".equals(routineType)) {
                    return new BigDecimal(8);
                } else if ("DUET".equals(routineType)) {
                    return new BigDecimal(11);
                } else if ("TRIO".equals(routineType)) {
                    return new BigDecimal(13);
                } else if ("TEAM".equals(routineType)) {
                    return new BigDecimal(15);
                }
            case 3:
                if ("SOLO".equals(routineType)) {
                    return new BigDecimal(6);
                } else if ("DUET".equals(routineType)) {
                    return new BigDecimal(8);
                } else if ("TRIO".equals(routineType)) {
                    return new BigDecimal(10);
                } else if ("TEAM".equals(routineType)) {
                    return new BigDecimal(12);
                }
            case 4:
                if ("SOLO".equals(routineType)) {
                    return new BigDecimal(5);
                } else if ("DUET".equals(routineType)) {
                    return new BigDecimal(6);
                } else if ("TRIO".equals(routineType)) {
                    return new BigDecimal(8);
                } else if ("TEAM".equals(routineType)) {
                    return new BigDecimal(10);
                }
            case 5:
                if ("SOLO".equals(routineType)) {
                    return new BigDecimal(4);
                } else if ("DUET".equals(routineType)) {
                    return new BigDecimal(5);
                } else if ("TRIO".equals(routineType)) {
                    return new BigDecimal(7);
                } else if ("TEAM".equals(routineType)) {
                    return new BigDecimal(9);
                }
            case 6:
                if ("SOLO".equals(routineType)) {
                    return new BigDecimal(3);
                } else if ("DUET".equals(routineType)) {
                    return new BigDecimal(4);
                } else if ("TRIO".equals(routineType)) {
                    return new BigDecimal(6);
                } else if ("TEAM".equals(routineType)) {
                    return new BigDecimal(7);
                }
            case 7:
                if ("SOLO".equals(routineType)) {
                    return new BigDecimal(2);
                } else if ("DUET".equals(routineType)) {
                    return new BigDecimal(3);
                } else if ("TRIO".equals(routineType)) {
                    return new BigDecimal(4);
                } else if ("TEAM".equals(routineType)) {
                    return new BigDecimal(5);
                }
            case 8:
                if ("SOLO".equals(routineType)) {
                    return new BigDecimal(1);
                } else if ("DUET".equals(routineType)) {
                    return new BigDecimal(2);
                } else if ("TRIO".equals(routineType)) {
                    return new BigDecimal(3);
                } else if ("TEAM".equals(routineType)) {
                    return new BigDecimal(4);
                }
        }
        return BigDecimal.ZERO;
    }
}
