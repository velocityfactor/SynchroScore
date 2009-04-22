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

import java.util.List;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Figure;
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
        if (meet != null) {
            mainFrame = new SynchroFrame(this, meet);
            mainFrame.setVisible(true);
        } else {
            System.exit(0);
        }
    }

    private Meet selectMeetFromList() {
        return MeetSelectionDialog.selectMeet(getMeets());
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
        javax.persistence.Query query = entityManager.createQuery("SELECT m FROM Meet m order by m.date desc");
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

    public boolean isMeetSetupValid(Meet meet) {
        boolean valid=true;
        if(meet.getName()==null || meet.getName().length()<1) valid=false;
        if(meet.getDate()==null) valid=false;
        if(meet.getType()!='R' && meet.getType()!='C') valid=false;
        if(meet.getHomeTeam()==null) valid=false;
        if(meet.getOpponents()==null || meet.getOpponents().size()<1) valid=false;
        if(meet.getNov1Figure()==null) valid=false;
        if(meet.getNov2Figure()==null) valid=false;
        if(meet.getNov3Figure()==null) valid=false;
        if(meet.getNov4Figure()==null) valid=false;
        if(meet.getInt1Figure()==null) valid=false;
        if(meet.getInt2Figure()==null) valid=false;
        if(meet.getInt3Figure()==null) valid=false;
        if(meet.getInt4Figure()==null) valid=false;
        if(meet.getEu1Figure()==null) valid=false;
        if(meet.getEu2Figure()==null) valid=false;
        return valid;
    }

    public boolean hasSwimmers(Meet meet) {
        return meet.getSwimmers()!=null && meet.getSwimmers().size()>0;
    }
}
