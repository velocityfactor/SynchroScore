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
package org.aquastarz.score.config;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Figure;
import org.aquastarz.score.domain.FigureScore;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Level;
import org.aquastarz.score.domain.RoutineLevel;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.manager.LevelManager;
import org.aquastarz.score.manager.SwimmerManager;
import org.aquastarz.score.manager.TeamManager;

public class Bootstrap {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Bootstrap.class.getName());

	public static void loadLeagueData() {

		JFileChooser jfc = new JFileChooser();
		jfc.addChoosableFileFilter(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".xls") || pathname.getName().endsWith(".xlsx");
			}

			@Override
			public String getDescription() {
				return "Excel Files";
			}

		});
		jfc.showOpenDialog(null);
		File file = jfc.getSelectedFile();
		if (file == null)
			return;

		Workbook workbook;
		try {
			workbook = WorkbookFactory.create(file);
		} catch (Exception e) {
			logger.error("Opening file " + file.getAbsolutePath(), e);
			JOptionPane.showMessageDialog(null, "Error opening file.");
			return;
		}

		EntityManager entityManager = ScoreApp.getEntityManager();
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		StringBuffer sb = new StringBuffer();

		Sheet sheet = workbook.getSheet("Teams");
		if (sheet == null) {
			logger.error("Missing Teams sheet");
			sb.append("Error reading file, missing Teams sheet.\n");
		} else {
			Iterator<Row> it = sheet.rowIterator();
			while (it.hasNext()) {
				Row row = it.next();
				if(row.getRowNum()==0) continue;
				checkAndPersistTeam(entityManager, getStringFromCell(row.getCell(0)),
						getStringFromCell(row.getCell(1)));
			}
		}
		sheet = workbook.getSheet("Figures");
		if (sheet == null) {
			logger.error("Missing Figures sheet");
			sb.append("Error reading file, missing Figures sheet.\n");
		} else {
			Iterator<Row> it = sheet.rowIterator();
			while (it.hasNext()) {
				Row row = it.next();
				if(row.getRowNum()==0) continue;
				checkAndPersistFigure(entityManager, getStringFromCell(row.getCell(0)),
						new BigDecimal(getStringFromCell(row.getCell(1))), getStringFromCell(row.getCell(2)));
			}
		}

		sheet = workbook.getSheet("Figures Levels");
		if (sheet == null) {
			logger.error("Missing Figures Levels sheet");
			JOptionPane.showMessageDialog(null, "Error reading file, missing Figures Levels sheet.\n");
		} else {
			int sortOrder = 1;
			Iterator<Row> it = sheet.rowIterator();
			while (it.hasNext()) {
				Row row = it.next();
				if(row.getRowNum()==0) continue;
				checkAndPersistLevel(entityManager, getStringFromCell(row.getCell(0)),
						getStringFromCell(row.getCell(1)), sortOrder++);
			}
		}

		sheet = workbook.getSheet("Routines Levels");
		if (sheet == null) {
			logger.error("Missing Routines Levels sheet");
			JOptionPane.showMessageDialog(null, "Error reading file, missing Routines Levels sheet.\n");
		} else {
			int sortOrder = 1;
			Iterator<Row> it = sheet.rowIterator();
			while (it.hasNext()) {
				Row row = it.next();
				if(row.getRowNum()==0) continue;
				checkAndPersistRoutineLevel(entityManager, getStringFromCell(row.getCell(0)),
						getStringFromCell(row.getCell(1)), sortOrder++);
			}
		}

		sheet = workbook.getSheet("Swimmers");
		if (sheet == null) {
			logger.error("Missing Swimmers sheet");
			sb.append( "Error reading file, missing Swimmers sheet.\n");
		}

		ArrayList<Swimmer> swimmers = new ArrayList<Swimmer>();
		Iterator<Row> it = sheet.rowIterator();
		while (it.hasNext()) {
			Row row = it.next();
			if(row.getRowNum()==0) continue;
			Integer leagueNum = null;
			try {
				String s=getStringFromCell(row.getCell(0));
				leagueNum = Integer.parseInt(getStringFromCell(row.getCell(0)));
			} catch (Exception e) {
				sb.append("Invalid league number on line ").append(row.getRowNum()).append("\n");
			}
			Swimmer swimmer = SwimmerManager.findByLeagueNum(leagueNum, ScoreApp.getCurrentSeason());
			if (swimmer == null) {
				swimmer = new Swimmer();
			}
			swimmer.setLeagueNum(leagueNum);
			swimmer.setFirstName(getStringFromCell(row.getCell(1)));
			swimmer.setLastName(getStringFromCell(row.getCell(2)));
			Level level = LevelManager.findById(getStringFromCell(row.getCell(3)));
			if (level == null) {
				sb.append("Invalid level on line ").append(row.getRowNum()).append("\n");
			}
			swimmer.setLevel(level);
			Team team = TeamManager.findById(getStringFromCell(row.getCell(4)));
			if (team == null) {
				sb.append("Invalid team on line ").append(row.getRowNum()).append("\n");
			}
			swimmer.setTeam(team);
			swimmer.setSeason(ScoreApp.getCurrentSeason());
			swimmers.add(swimmer);
		}
		
		try {
			workbook.close();
		} catch (IOException e) {
			logger.error("Closing excel file",e);
		}
		
		if (sb.length() == 0) {
			try {
				for (Swimmer swimmer : swimmers) {
					entityManager.persist(swimmer);
				}
				transaction.commit();
			} catch (Exception e) {
				logger.error("",e);
				sb.append(
						"There was an error loading the file. Restart and try again. If this continues, you may need to clear the database and try again.\n");
			}
		} 
		
		if(sb.length()==0) {
			JOptionPane.showMessageDialog(null, "File loaded successfully.");
		}
		else {
			JOptionPane.showMessageDialog(null, "File not loaded: "+sb.toString());
			transaction.rollback();
		}
	}

	private static String getStringFromCell(Cell cell) {
		if (cell == null)
			return "";
		DataFormatter formatter = new DataFormatter();
		return formatter.formatCellValue(cell);	}

	public static void checkAndPersistLevel(EntityManager entityManager, String key, String name, int sortOrder) {
		if (entityManager.find(Level.class, key) == null) {
			entityManager.persist(new Level(key, name, sortOrder));
		}
	}

	public static void checkAndPersistRoutineLevel(EntityManager entityManager, String key, String name,
			int sortOrder) {
		RoutineLevel rl = (RoutineLevel) entityManager.find(RoutineLevel.class, key);
		if (rl == null) {
			entityManager.persist(new RoutineLevel(key, name, sortOrder));
		} else {
			rl.setName(name);
			rl.setSortOrder(sortOrder);
			entityManager.persist(rl);
		}
	}

	public static void checkAndPersistTeam(EntityManager entityManager, String key, String name) {
		if (entityManager.find(Team.class, key) == null) {
			entityManager.persist(new Team(key, name));
		}
	}

	public static void checkAndPersistFigure(EntityManager entityManager, String key, BigDecimal dd, String name) {
		Figure figure=entityManager.find(Figure.class, key);
		if (figure== null) {
			entityManager.persist(new Figure(key, dd, name));
		}
		else {
			figure.setDegreeOfDifficulty(dd);
			figure.setName(name);
			entityManager.persist(figure);
		}
	}

	public static class FigureScoreTracker {

		public FigureScore figureScore;
		public BigDecimal total;
	}

	public static class FiguresParticipantTracker {

		public FiguresParticipant figuresParticipant;
		public BigDecimal total;
		public BigDecimal points;
		public int place;
	}
}
