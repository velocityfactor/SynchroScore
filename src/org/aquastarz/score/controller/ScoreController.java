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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Figure;
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
                e.printStackTrace();
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
            e.printStackTrace();
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
    public void updateFiguresSwimmers(Meet meetx, List<Swimmer> swimmers) {
        List<FiguresParticipant> newList = new ArrayList<FiguresParticipant>();

        Meet meet = meetx; //entityManager.find(Meet.class, meetx.getMeetId());

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
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
        }
        meet.setFiguresParticipants(newList);
        saveMeet(meet);
    }

    //TODO this method needs to randomize swimmers within levels and order the levels instead
    //  of the current complete randomization
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
            e.printStackTrace();
            transaction.rollback();
            return false;
        }

        return true;
    }
}
