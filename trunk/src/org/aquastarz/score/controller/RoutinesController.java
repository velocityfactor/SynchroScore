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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Routine;
import org.aquastarz.score.domain.RoutineLevel;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.gui.RoutinesPanel;
import org.aquastarz.score.gui.SynchroFrame;
import org.aquastarz.score.manager.RoutineLevelManager;
import org.aquastarz.score.manager.RoutineManager;

public class RoutinesController {

    private static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(RoutinesController.class.getName());
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
        RoutineManager.calculate(routine);
        RoutineManager.save(routine);
        routine.getMeet().setRoutinesChanged(true);
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
            int ret = JOptionPane.showOptionDialog(panel, "Routines order already randomized.  Randomize again?", "Confirm Randomize",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                    new String[]{"Yes", "No"}, "No");
            if (ret != JOptionPane.YES_OPTION) {
                confirm = false;
            }
        }
        if (confirm) {
            RoutineManager.randomize(meet);
        }
    }

    public void print() {
        showRoutinesOrderReport(meet);
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

    private void showRoutinesOrderReport(Meet meet) {
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("/org/aquastarz/score/report/RoutinesOrder.jasper"));
            JRDataSource data = new JRBeanCollectionDataSource(RoutineManager.findAll(meet));
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("MeetDate", meet.getMeetDate());
            params.put("MeetName", meet.getName());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, data);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception ex) {
            logger.error("Could not create the report.\n" + ex.getLocalizedMessage());
        }
    }

    public static void showRoutinesReport(Meet meet, boolean showNovice, boolean showIntermediate) {
        ScoreController.calculateMeetResultsIfNeeded(meet);
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(RoutinesController.class.getResourceAsStream("/org/aquastarz/score/report/Routines.jasper"));
            JRDataSource data = new JRBeanCollectionDataSource(ScoreController.generateRoutinesResults(meet, showNovice, showIntermediate));
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("MeetDate", meet.getMeetDate());
            params.put("MeetName", meet.getName());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, data);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception ex) {
            logger.error("Could not create the report.\n" + ex.getLocalizedMessage());
        }
    }

    public static void showRoutinesLabelsReport(Meet meet, int skipLabels, boolean showNovice, boolean showIntermediate) {
        ScoreController.calculateMeetResultsIfNeeded(meet);
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(RoutinesController.class.getResourceAsStream("/org/aquastarz/score/report/RoutineLabels.jasper"));
            List<Routine> routines = new LinkedList<Routine>();
            for (int i = 0; i < skipLabels; i++) {
                routines.add(null);
            }
            routines.addAll(ScoreController.generateRoutineLabels(meet, showNovice, showIntermediate));
            JRDataSource data = new JRBeanCollectionDataSource(routines);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("MeetDate", meet.getMeetDate());
            params.put("MeetName", meet.getName());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, data);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception ex) {
            logger.error("Could not create the report.\n" + ex.getLocalizedMessage());
        }
    }

    public static int percentCompleteRoutines(Meet meet) {
        if (meet.getRoutines().isEmpty()) {
            return 0;
        } else {
            int count = 0;
            for (Routine routine : meet.getRoutines()) {
                if (routine.getTotalScore() != null) {
                    count++;
                }
            }
            return (count * 100) / meet.getRoutines().size();
        }
    }
}
