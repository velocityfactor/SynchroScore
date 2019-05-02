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

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Routine;
import org.aquastarz.score.domain.RoutineLevel;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.gui.RoutinesPanel;
import org.aquastarz.score.gui.SynchroFrame;
import org.aquastarz.score.manager.RoutineLevelManager;
import org.aquastarz.score.manager.RoutineManager;
import org.aquastarz.score.manager.TeamManager;
import org.aquastarz.score.report.RoutineScoreSheet;

import au.com.bytecode.opencsv.CSVReader;

public class RoutinesController {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(RoutinesController.class.getName());
	private Meet meet = null;
	private final RoutinesPanel panel;

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
		meet.getRoutines().remove(routine);
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
			JOptionPane
					.showMessageDialog(
							panel,
							"Routine must have a title and fully complete or fully blank score.",
							"Entry Error", JOptionPane.OK_OPTION);
		}
	}

	public void randomize() {
		boolean confirm = true;
		if (meet.isRoutinesOrderGenerated()) {
			int ret = JOptionPane.showOptionDialog(panel,
					"Routines order already randomized.  Randomize again?",
					"Confirm Randomize", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, new String[] { "Yes",
							"No" }, "No");
			if (ret != JOptionPane.YES_OPTION) {
				confirm = false;
			}
		}
		if (confirm) {
			RoutineManager.randomize(meet);
		}
	}
	
	public boolean isChamps() {
		return meet.getType()=='C';
	}

	public void importRoutines(File file) {
		try {
			CSVReader csv = new CSVReader(new FileReader(file));
			List<String[]> rows = csv.readAll();
			csv.close();
			HashMap<String, FiguresParticipant> map = new HashMap<String, FiguresParticipant>();
			for (String[] sa : rows) {
				Routine r = new Routine();
				r.setMeet(meet);
				r.setRoutineOrder(0);
				r.setTeam(TeamManager.findById(sa[0]));
				r.setLevel(RoutineLevelManager.find(sa[1]));
				r.setNumSwimmers(Integer.parseInt(sa[2]));
				r.setRoutineType(sa[3]);
				r.setName(sa[4]);
				r.setSwimmers1(sa[5]);
				if (sa.length > 6 && sa[6] != null)
					r.setSwimmers2(sa[6]);
				meet.getRoutines().add(r);
				RoutineManager.save(r);
			}
		} catch (Exception e) {
			logger.error("", e);
			meet = null;
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
			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(getClass()
							.getResourceAsStream(
									"/org/aquastarz/score/report/RoutinesOrder2.jasper"));
			JRDataSource data = new JRBeanCollectionDataSource(
					RoutineManager.findAll(meet));
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("MeetDate", meet.getMeetDate());
			params.put("MeetName", meet.getName());
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, params, data);
			JasperViewer.viewReport(jasperPrint, false);
		} catch (Exception ex) {
			logger.error("Could not create the report.", ex);
		}
	}

	public static void showRoutinesReport(Meet meet, boolean showNovice,
			boolean showIntermediate) {
		ScoreController.calculateMeetResultsIfNeeded(meet);
		try {
			JasperReport jasperReport = (JasperReport) JRLoader
					.loadObject(RoutinesController.class
							.getResourceAsStream("/org/aquastarz/score/report/Routines.jasper"));
			JRDataSource data = new JRBeanCollectionDataSource(
					ScoreController.generateRoutinesResults(meet, showNovice,
							showIntermediate));
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("MeetDate", meet.getMeetDate());
			params.put("MeetName", meet.getName());
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					jasperReport, params, data);
			JasperViewer.viewReport(jasperPrint, false);
		} catch (Exception ex) {
			logger.error("Could not create the report.\n"
					+ ex.getLocalizedMessage());
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

	public Collection<RoutineScoreSheet> generateRoutineScoreSheets() {
		TreeMap<String, RoutineScoreSheet> rss = new TreeMap<String, RoutineScoreSheet>();
		for (Routine routine : meet.getRoutines()) {
			RoutineScoreSheet sheet = new RoutineScoreSheet();

			sheet.setAgeGroup(routine.getLevel().getName());
			sheet.setEvent(routine.getRoutineType());
			if (routine.getRoutineOrder() > 0) {
				sheet.setRoutineNum(routine.getRoutineOrder());
			}
			sheet.setTheme(routine.getName());
			sheet.setMeetName(routine.getMeet().getName());
			sheet.setMeetDate(routine.getMeet().getMeetDate());
			String participants = routine.getSwimmers1().trim();
			String participants2 = routine.getSwimmers2().trim();
			if (!participants2.isEmpty()) {
				if (participants.endsWith(",")) {
					participants = participants + " " + participants2;

				} else {
					participants = participants + ", " + participants2;
				}
			}
			sheet.setParticipants(participants);
			sheet.setJudgeNum("0");
			sheet.setTeam(routine.getTeam().getName());

			String key = "";
			if ("Solo".equals(routine.getRoutineType())) {
				key = String.format("1%03d%03d", routine.getRoutineOrder(),
						rss.size());
				sheet.setExecution(50);
				sheet.setSynchronization(10);
				sheet.setDifficulty(40);
				sheet.setChoreography(50);
				sheet.setMusic(20);
				sheet.setPresentation(30);
			} else if ("Duet".equals(routine.getRoutineType())) {
				key = String.format("2%03d%03d", routine.getRoutineOrder(),
						rss.size());
				sheet.setExecution(40);
				sheet.setSynchronization(30);
				sheet.setDifficulty(30);
				sheet.setChoreography(50);
				sheet.setMusic(30);
				sheet.setPresentation(20);
			} else if ("Trio".equals(routine.getRoutineType())) {
				key = String.format("3%03d%03d", routine.getRoutineOrder(),
						rss.size());
				sheet.setExecution(40);
				sheet.setSynchronization(30);
				sheet.setDifficulty(30);
				sheet.setChoreography(50);
				sheet.setMusic(30);
				sheet.setPresentation(20);
			} else if ("Team".equals(routine.getRoutineType())) {
				key = String.format("4%03d%03d", routine.getRoutineOrder(),
						rss.size());
				sheet.setExecution(40);
				sheet.setSynchronization(30);
				sheet.setDifficulty(30);
				sheet.setChoreography(50);
				sheet.setMusic(30);
				sheet.setPresentation(20);
				if(routine.getLevel().getLevelId().endsWith("CT")) sheet.setEvent("Combo");
			}

			rss.put(key + String.format("%02d", 0), sheet);
			int numJudges = (meet.isChamps() && "Team".equals(routine
					.getRoutineType())) ? 10 : 7;
			for (int i = 1; i <= numJudges; i++) {
				String js = null;
				if (numJudges == 10) {
					if (i < 6) {
						js = Integer.toString(i) + "A";
					} else {
						js = Integer.toString(i - 5) + "T";
					}

				} else {
					js = Integer.toString(i);
				}
				rss.put(key + String.format("%02d", i), new RoutineScoreSheet(
						sheet, js));
			}
		}
		return rss.values();
	}
}
