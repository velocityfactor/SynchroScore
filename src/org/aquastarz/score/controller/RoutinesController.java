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

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Routine;
import org.aquastarz.score.domain.RoutineLevel;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.gui.RoutinesPanel;
import org.aquastarz.score.gui.SynchroFrame;
import org.aquastarz.score.manager.RoutineLevelManager;
import org.aquastarz.score.manager.RoutineManager;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class RoutinesController {

    private Meet meet = null;
    private RoutinesPanel panel;

    public RoutinesController(RoutinesPanel panel) {
        this.panel = panel;
        this.meet = SynchroFrame.getMeet();
    }

    public List<Routine> getRoutinesList() {
        return RoutineManager.findAll(meet);
    }

    public Routine add() {
        return RoutineManager.getNewRoutine(meet);
    }

    public void delete(Routine routine) {
        RoutineManager.delete(routine);
    }

    public void save(Routine routine) {
        RoutineManager.save(routine);
    }

    public void calculate(Routine routine) {
        if (RoutineManager.isValid(routine)) {
            RoutineManager.calculate(routine);
            save(routine);
        } else {
            JOptionPane.showMessageDialog(panel, "Routine must have a title and fully complete or fully blank score.", "Entry Error", JOptionPane.OK_OPTION);
        }
    }

    public void randomize() {
        boolean confirm = true;
        if (meet.isRoutinesOrderGenerated()) {
            int ret = JOptionPane.showConfirmDialog(panel, "Routines order already randomized.  Randomize again?", "Confirm Randomize", JOptionPane.YES_NO_OPTION);
            if (ret != JOptionPane.YES_OPTION) {
                confirm = false;
            }
        }
        if (confirm) {
            RoutineManager.randomize(meet);
        }
    }

    public void print() {
        throw new NotImplementedException();
    }

    public List<RoutineLevel> getRoutineLevels() {
        return RoutineLevelManager.findAll();
    }

    public List<Team> getTeams() {
        ArrayList<Team> teams = new ArrayList<Team>();
        teams.add(meet.getHomeTeam());
        teams.addAll(meet.getOpponents());
        return teams;
    }
}
