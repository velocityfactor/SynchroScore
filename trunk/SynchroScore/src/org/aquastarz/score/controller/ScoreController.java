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

import java.awt.Color;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.gui.SwimmerSelectionPanel;
import org.aquastarz.score.gui.SynchroFrame;

/**
 *
 * @author Shayne Hughes <velocityfactor@gmail.com>
 */
public class ScoreController {

    EntityManager entityManager = ScoreApp.getEntityManager();
    private static ScoreController instance = null;
    private SynchroFrame mainFrame;

    protected ScoreController() {
        //Controller created, create the gui
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                mainFrame=new SynchroFrame();
                initMainFrame();
                mainFrame.setVisible(true);
            }
        });
    }

    public static synchronized ScoreController getInstance() {
        if (instance == null) {
            instance = new ScoreController();

        }
        return instance;
    }

    private void initMainFrame() {
        mainFrame.disableAllTabs();
        mainFrame.setTabEnabled(SynchroFrame.Tab.MEET_SETUP, true);
    }

    public void meetSetupSaved(Meet meet) {
        ScoreApp.getEntityManager().persist(meet);
        System.out.println("meetId="+meet.getMeetId());
        mainFrame.setSetupStatus(Color.GREEN, 50);
        mainFrame.setTabEnabled(SynchroFrame.Tab.SWIMMERS, true);
    }

    public void tabChanged(SynchroFrame.Tab tab) {
        if(tab==SynchroFrame.Tab.SWIMMERS) {
            initSwimmersTab();
        }
    }

    private void initSwimmersTab() {
        Query teamQuery = entityManager.createQuery("SELECT t FROM Team t");
        List<Team> teams = teamQuery.getResultList();
        for(Team team:teams) {
            Query swimmerQuery = entityManager.createQuery("SELECT s from Swimmer s WHERE s.team='"+team.getTeamId()+"' ORDER BY s.lastName,s.firstName");
            List<Swimmer> swimmers = swimmerQuery.getResultList();
            SwimmerSelectionPanel ssp=new SwimmerSelectionPanel(team,swimmers);
            mainFrame.addSwimmerTab(ssp);
        }
    }

    public void swimmersSaved() {
        mainFrame.setSetupStatus(Color.GREEN, 100);
        mainFrame.setTabEnabled(SynchroFrame.Tab.FIGURES, true);
        mainFrame.setTabEnabled(SynchroFrame.Tab.ROUTINES, true);
        mainFrame.setTabEnabled(SynchroFrame.Tab.REPORTS, true);
    }
}
