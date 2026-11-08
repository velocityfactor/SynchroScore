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
import java.util.function.BooleanSupplier;

import javax.persistence.EntityManager;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Figure;
import org.aquastarz.score.domain.FigureScore;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Level;
import org.aquastarz.score.domain.RoutineLevel;
import org.aquastarz.score.domain.Season;
import org.aquastarz.score.domain.Team;

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
		if (jfc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) return;
		File file = jfc.getSelectedFile();
		if (file == null) return;
		try {
			boolean loaded = loadLeagueData(file, () -> JOptionPane.showOptionDialog(null,
					"Loading \"" + file.getName() + "\" will permanently replace ALL existing data.\n"
					+ "All meets, scores, swimmers, teams, figures, levels, and previous seasons will be removed.\n"
					+ "Only the data in this season file will remain.\n\n"
					+ "Export any meet data you want to keep before continuing.\n"
					+ "Do you want to replace all data and load this file?",
					"Replace All Data", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
					null, new Object[] {"Cancel", "Replace All Data"}, "Cancel") == 1);
			if (loaded) JOptionPane.showMessageDialog(null, "Season file loaded. Previous data has been replaced.");
		} catch (Exception e) {
			logger.error("Loading season file " + file.getAbsolutePath(), e);
			JOptionPane.showMessageDialog(null,
					"File not loaded. Existing data has been kept.\n" + e.getMessage(),
					"Season File Not Loaded", JOptionPane.ERROR_MESSAGE);
		}
	}

	/** Reads and closes the file before asking for confirmation or changing stored data. */
	public static boolean loadLeagueData(File file, BooleanSupplier confirmReplacement) throws IOException {
		SeasonFileImporter importer;
		try (Workbook workbook = WorkbookFactory.create(file)) {
			importer = SeasonFileImporter.read(workbook);
		}
		if (!confirmReplacement.getAsBoolean()) return false;
		Season season = importer.replaceAllData(ScoreApp.getEntityManager(), ScoreApp.getCurrentSeason().getName());
		ScoreApp.setCurrentSeason(season);
		return true;
	}

	public static void checkAndPersistLevel(EntityManager entityManager, String key, String name, int sortOrder) {
		Level level = entityManager.find(Level.class, key);
		if (level == null) {
			entityManager.persist(new Level(key, name, sortOrder));
		} else {
			level.setName(name);
			level.setSortOrder(sortOrder);
			entityManager.persist(level);
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
