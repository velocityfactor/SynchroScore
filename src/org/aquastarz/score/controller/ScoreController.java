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

import java.awt.Cursor;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Figure;
import org.aquastarz.score.domain.FigureScore;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Level;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Routine;
import org.aquastarz.score.domain.RoutineLevel;
import org.aquastarz.score.domain.Season;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.gui.SynchroFrame;
import org.aquastarz.score.manager.FigureManager;
import org.aquastarz.score.manager.MeetManager;
import org.aquastarz.score.manager.RoutineManager;
import org.aquastarz.score.report.FiguresLabel;
import org.aquastarz.score.report.FiguresMeetSheet;
import org.aquastarz.score.report.StationResult;

/**
 * 
 * @author Shayne Hughes <velocityfactor@gmail.com>
 */
public class ScoreController {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(ScoreController.class.getName());
	EntityManager entityManager = ScoreApp.getEntityManager();
	private final SynchroFrame mainFrame;

	public ScoreController(Meet meet) {
		if (meet == null) {
			logger.info("ScoreController initialize new Meet.");
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();
			try {
				meet = new Meet();
				meet.setName("Mock Meet");
				DateFormat df = SimpleDateFormat
						.getDateInstance(SimpleDateFormat.LONG);
				meet.setMeetDate(df.format(new Date()));
				meet.setSeason(ScoreApp.getCurrentSeason());
				meet.setEu1(true);
				meet.setEu2(false);
				meet.setEu3(true);
				meet.setEu4(false);
				meet.setNov1Figure(FigureManager.findById("101"));
				meet.setNov2Figure(FigureManager.findById("360"));
				meet.setNov3Figure(null);
				meet.setNov4Figure(null);
				meet.setInt1Figure(FigureManager.findById("342"));
				meet.setInt2Figure(FigureManager.findById("355d"));
				meet.setInt3Figure(null);
				meet.setInt4Figure(null);
				transaction.commit();
			} catch (Exception e) {
				logger.error("Error creating new meet.", e);
				transaction.rollback();
				// TODO feedback
				System.exit(-1);
			}
		}
		logger.info("ScoreController starting for meetId=" + meet.getMeetId()
				+ " \"" + meet.getName() + "\"");
		mainFrame = new SynchroFrame(this, meet);
		mainFrame.setVisible(true);
	}

	public List<Team> getTeams() {
		javax.persistence.Query query = entityManager
				.createQuery("SELECT t FROM Team t order by t.name");
		return query.getResultList();
	}

	public List<Figure> getFigures() {
		javax.persistence.Query query = entityManager
				.createQuery("SELECT f FROM Figure f order by f.figureId");
		return query.getResultList();
	}

	public static List<Level> getLevels() {
		javax.persistence.Query query = ScoreApp.getEntityManager()
				.createQuery("SELECT l FROM Level l order by l.levelId");
		return query.getResultList();
	}

	public static List<Meet> getMeets(Season season) {
		javax.persistence.Query query = ScoreApp.getEntityManager()
				.createNamedQuery("Meet.findAllBySeasonOrderByIdDesc");
		query.setParameter("season", season);
		return query.getResultList();
	}

	public static Meet findMeet(Season season, String name) {
		javax.persistence.Query query = ScoreApp.getEntityManager()
				.createNamedQuery("Meet.findBySeasonAndName");
		query.setParameter("season", season);
		query.setParameter("name", name);
		try {
			return (Meet) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public static List<Season> getSeasons() {
		javax.persistence.Query query = ScoreApp.getEntityManager()
				.createNamedQuery("Season.findAllOrderByName");
		return query.getResultList();
	}

	public static Season getSeason(String name) {
		javax.persistence.Query query = ScoreApp.getEntityManager()
				.createNamedQuery("Season.findByName");
		query.setParameter("name", name);
		try {
			return (Season) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public void saveMeet(Meet meet) {
		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			entityManager.persist(meet);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			logger.error("Error saving meet.", e);
		}
	}

	public static FiguresParticipant findFiguresParticipant(Meet meet,
			Swimmer swimmer) {
		Query figureOrderQuery = ScoreApp.getEntityManager().createNamedQuery(
				"FiguresParticipant.findByMeetAndSwimmer");
		figureOrderQuery.setParameter("meet", meet);
		figureOrderQuery.setParameter("swimmer", swimmer);
		return (FiguresParticipant) figureOrderQuery.getSingleResult();
	}

	public static List<FiguresParticipant> findAllFiguresParticipantByMeetAndDivision(
			Meet meet, boolean isNovice) {
		Query figureOrderQuery = ScoreApp.getEntityManager().createNamedQuery(
				"FiguresParticipant.findByMeetAndLevelOrderByTotalScore");
		figureOrderQuery.setParameter("meet", meet);
		figureOrderQuery.setParameter("level", isNovice ? "N%" : "I%");
		List<FiguresParticipant> figuresParticipants = figureOrderQuery
				.getResultList();
		for (Iterator<FiguresParticipant> it = figuresParticipants.iterator(); it
				.hasNext();) {
			FiguresParticipant fp = it.next();
			if (!figuresParticipantHasAllScores(fp)) {
				it.remove();
			}
		}
		return figuresParticipants;
	}

	public FiguresParticipant findFiguresParticipantByFigureOrder(Meet meet,
			String figureOrder) {
		Query figureOrderQuery = entityManager
				.createNamedQuery("FiguresParticipant.findByMeetAndFigureOrder");
		figureOrderQuery.setParameter("meet", meet);
		figureOrderQuery.setParameter("figureOrder", figureOrder);
		try {
			FiguresParticipant figuresParticipant = (FiguresParticipant) figureOrderQuery
					.getSingleResult();

			return figuresParticipant;
		} catch (Exception e) {
			// not found
			return null;
		}
	}

	/*
	 * Accepts a new meet with no swimmers or an existing meet with swimmers.
	 * Creates new FiguresParticipants as needed and gives them an appropriate
	 * figureOrder if figureOrders have been calculated. Deletes
	 * FigureParticipants that are no longer needed.
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
				if (fp == null) { // Adding a new swimmer
					fp = new FiguresParticipant(meet, s);
					if (meet.getFiguresOrderGenerated()) {
						String maxOrder = "";
						Integer newLevelOrder = fp.getSwimmer().getLevel()
								.getSortOrder();
						for (FiguresParticipant levelFp : meet
								.getFiguresParticipants()) {
							Integer levelOrder = levelFp.getSwimmer()
									.getLevel().getSortOrder();
							if (levelFp.getFigureOrder().compareTo(maxOrder) > 0
									&& newLevelOrder >= levelOrder) {
								maxOrder = levelFp.getFigureOrder();
							}
						}
						for (FiguresParticipant levelFp : newList) {
							Integer levelOrder = levelFp.getSwimmer()
									.getLevel().getSortOrder();
							if (levelFp.getFigureOrder().compareTo(maxOrder) > 0
									&& newLevelOrder >= levelOrder) {
								maxOrder = levelFp.getFigureOrder();
							}
						}
						String nextOrder = null;
						char seq = maxOrder.charAt(maxOrder.length() - 1);
						if (seq >= 'A') {
							seq++;
							nextOrder = maxOrder.substring(0,
									maxOrder.length() - 1) + seq;
						} else {
							nextOrder = maxOrder + "A";
						}
						fp.setFigureOrder(nextOrder);
					}
					entityManager.persist(fp);
				}
				newList.add(fp);
			}

			// Remaining FiguresParticipants in map not needed, delete them
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
		mainFrame.updateFiguresStatus();
	}

	public boolean generateRandomFiguresOrder(Meet meet) {
		Map<Double, FiguresParticipant> map = new TreeMap<Double, FiguresParticipant>();
		for (FiguresParticipant fp : meet.getFiguresParticipants()) {
			map.put(Math.random() + fp.getSwimmer().getLevel().getSortOrder(),
					fp);
		}

		// Calculate length to zero pad numbers (could be trickier, but this is
		// good enough)
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

	public static Figure getFigure(FigureScore fs) {
		Meet meet = fs.getFiguresParticipant().getMeet();
		Level level = fs.getFiguresParticipant().getSwimmer().getLevel();
		if (level.isNovice()) {
			switch (fs.getStation()) {
			case 1:
				return meet.getNov1Figure();
			case 2:
				return meet.getNov2Figure();
			case 3:
				return meet.getNov3Figure();
			case 4:
				return meet.getNov4Figure();
			}
		} else {
			switch (fs.getStation()) {
			case 1:
				return meet.getInt1Figure();
			case 2:
				return meet.getInt2Figure();
			case 3:
				return meet.getInt3Figure();
			case 4:
				return meet.getInt4Figure();
			}
		}
		return null;
	}

	/*
	 * figureScores=null clears all scores for figuresParticipant
	 */
	public boolean saveFigureScores(FiguresParticipant figuresParticipant,
			Collection<FigureScore> figureScores) {
		logger.info("Saving figures score for figureOrder="
				+ figuresParticipant.getFigureOrder());
		// Mark meet as needing recalc
		figuresParticipant.getMeet().clearPoints();

		// Calculate totals before saving
		if (figureScores != null) {
			for (FigureScore figureScore : figureScores) {
				if (logger.isDebugEnabled()) {
					logger.debug("Getting total for station="
							+ figureScore.getStation());
				}
				figureScore.setTotalScore(totalScore(figureScore));
				if (figureScore.getTotalScore() == null) {
					logger.error("Saving figure scores aborted because of error getting totalScore.");
					return false;
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("Total = "
								+ figureScore.getTotalScore().toPlainString());
					}
				}
			}
		}

		EntityTransaction transaction = entityManager.getTransaction();
		transaction.begin();
		try {
			if (figureScores != null) {
				for (FigureScore figureScore : figureScores) {
					entityManager.persist(figureScore);
					if (!figuresParticipant.getFiguresScores().contains(
							figureScore)) {
						figuresParticipant.getFiguresScores().add(figureScore);
					}
				}
			} else {
				for (FigureScore figureScore : figuresParticipant
						.getFiguresScores()) {
					entityManager.remove(figureScore);
				}
				figuresParticipant
						.setFiguresScores(new ArrayList<FigureScore>());
			}
			figuresParticipant
					.setTotalScore(calculateTotalScore(figuresParticipant));
			figuresParticipant.setPlace(null);
			figuresParticipant.setPoints(null);
			entityManager.persist(figuresParticipant);

			transaction.commit();

			mainFrame.updateFiguresStatus();
			logger.info("Saving figures score complete.");
			return true;
		} catch (Exception e) {
			transaction.rollback();
			logger.error("Error saving figures scores.", e);
			return false;
		}
	}

	public static List<StationResult> generateStationResults(Meet meet,
			boolean isNovice) {
		List<StationResult> results = new ArrayList<StationResult>();

		for (FiguresParticipant fp : meet.getFiguresParticipants()) {
			if (isNovice(fp) == isNovice) {
				for (FigureScore fs : fp.getFiguresScores()) {
					StationResult sr = new StationResult();
					Figure figure = getFigure(fs);
					sr.setFigureId(figure.getFigureId());
					sr.setFigureName(figure.getName());
					sr.setFigureDd(figure.getDegreeOfDifficulty().toString());
					sr.setLastName(fp.getSwimmer().getLastName());
					sr.setFirstName(fp.getSwimmer().getFirstName());
					sr.setFigureOrder(fp.getFigureOrder());
					sr.setScore(fs.getTotalScore());
					sr.setLevel(fp.getSwimmer().getLevel().getLevelId());
					sr.setLevelOrder(fp.getSwimmer().getLevel().getSortOrder()
							.intValue());
					sr.setTeam(fp.getSwimmer().getTeam().getTeamId());
					results.add(sr);
				}
			}
		}
		Collections.sort(results);
		return results;
	}

	public static List<Routine> generateRoutinesResults(Meet meet,
			boolean showNovice, boolean showIntermediate) {
		EntityManager entityManager = ScoreApp.getEntityManager();
		Query query;
		if (showNovice && !showIntermediate) {
			query = entityManager
					.createNamedQuery("Routine.findByMeetAndLevelIsNoviceOrderByLevelAndRoutineTypeAndPlace");
		} else if (!showNovice && showIntermediate) {
			query = entityManager
					.createNamedQuery("Routine.findByMeetAndLevelIsIntermediateOrderByLevelAndRoutineTypeAndPlace");
		} else if (showNovice && showIntermediate) {
			query = entityManager
					.createNamedQuery("Routine.findByMeetOrderByLevelAndRoutineTypeAndPlace");
		} else {
			return new ArrayList<Routine>();

		}
		query.setParameter("meet", meet);
		return query.getResultList();
	}

	public static List<Routine> generateRoutineLabels(Meet meet,
			boolean showNovice, boolean showIntermediate) {
		List<Routine> routines = generateRoutinesResults(meet, showNovice,
				showIntermediate);
		LinkedList<Routine> labels = new LinkedList<Routine>();
		for (Routine routine : routines) {
			String routineType = routine.getRoutineType();
			if ("Duet".equals(routineType)) {
				labels.add(routine);
				labels.add(routine);
			} else if ("Trio".equals(routineType)) {
				labels.add(routine);
				labels.add(routine);
				labels.add(routine);
			} else if ("Team".equals(routineType)) {
				for (int i = 0; i < routine.getNumSwimmers(); i++) {
					labels.add(routine);
				}
			} else { // SOLO
				labels.add(routine);
			}
		}
		return labels;
	}

	public static List<FiguresLabel> generateFiguresLabels(Meet meet,
			boolean isNovice) {
		List<FiguresLabel> results = new ArrayList<FiguresLabel>();

		for (FiguresParticipant fp : meet.getFiguresParticipants()) {
			if (isNovice(fp) == isNovice && figuresParticipantHasAllScores(fp)) {
				FiguresLabel fl = new FiguresLabel(fp.getSwimmer().getLevel()
						.getName(), fp.getSwimmer().getLevel().getSortOrder(),
						fp.getSwimmer().getFirstName() + " "
								+ fp.getSwimmer().getLastName(), fp.getPlace(),
						fp.getSwimmer().getTeam().getTeamId(),
						fp.getTotalScore());
				results.add(fl);
			}
		}
		Collections.sort(results);
		return results;
	}

	public static List<FiguresMeetSheet> generateFiguresMeetSheets(Meet meet,
			boolean isNovice) {
		List<FiguresMeetSheet> results = new ArrayList<FiguresMeetSheet>();

		for (FiguresParticipant fp : meet.getFiguresParticipants()) {
			if (isNovice(fp) == isNovice && figuresParticipantHasAllScores(fp)) {
				results.add(new FiguresMeetSheet(fp));
			}
		}
		Collections.sort(results);
		return results;
	}

	public static boolean isValid(FigureScore figureScore) {
		return figureScore.getScore1() != null
				&& figureScore.getScore2() != null
				&& figureScore.getScore3() != null
				&& figureScore.getScore4() != null
				&& figureScore.getScore5() != null
				&& figureScore.getPenalty() != null
				&& figureScore.getFiguresParticipant() != null
				&& figureScore.getStation() >= 1
				&& figureScore.getStation() <= 4;
	}

	public static boolean isNovice(FiguresParticipant fp) {
		return fp.getSwimmer().getLevel().getLevelId().startsWith("N");
	}

	public static int numberOfFigures(FiguresParticipant fp) {
		if (fp.getMeet().getSeason().getRulesRevision() < 2) {
			// Before 2011
			if (fp.getMeet().getType() != 'C'
					&& "N8".equals(fp.getSwimmer().getLevel().getLevelId())) {
				return 2; // Novice 8 & Under have two figures
			} else {
				return 4; // All others have four figures

			}
		} else {
			return 4;
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
		int percent = possible > 0 ? (count * 100) / possible : 0;

		if (percent > 100) {
			logger.error("percent = " + percent);
		}

		if (logger.isDebugEnabled()) {
			if (countNovice) {
				logger.debug("Novice figures are " + percent + "% complete.");
			} else {
				logger.debug("Intermediate figures are " + percent
						+ "% complete.");
			}
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
		BigDecimal min = BigDecimal.TEN; // No score larger than 10
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
			} else if (!maxRemoved && score.compareTo(max) == 0) {
				maxRemoved = true;
			} else {
				adjScores.add(score);
			}
		}

		// We must now have three scores
		if (adjScores.size() != 3) {
			logger.error("Dropped min and max but didn't end up with three scores.");
			logger.error("Scores = " + scores);
			logger.error("AdjScores = " + adjScores);
			return null;
		}

		// Sum the scores
		BigDecimal sum = BigDecimal.ZERO;
		for (BigDecimal score : adjScores) {
			sum = sum.add(score);
		}

		// Multiply by DD
		BigDecimal ddSum = sum.multiply(getFigure(fs).getDegreeOfDifficulty());

		// Subtract penalty
		BigDecimal total = ddSum.subtract(fs.getPenalty());

		// Score can't be less than zero
		if (BigDecimal.ZERO.compareTo(total) >= 0) {
			total = BigDecimal.ZERO;
		}

		return total;
	}

	public static void calculateMeetResultsIfNeeded(Meet meet) {
		if (meet.needsPointsCalc()) {
			calculateMeetResults(meet);
		}
	}

	public static boolean figuresParticipantHasAllScores(FiguresParticipant fp) {
		boolean result = true;
		if (fp.getMeet().getSeason().getRulesRevision() < 2) {
			// Regular meets N8U have 2 scores, otherwise 4
			if (fp.getMeet().getType() == 'R'
					&& "N8U".equals(fp.getSwimmer().getLevel().getLevelId())) {
				result = fp.getFiguresScores().size() == 2;
			} else {
				result = fp.getFiguresScores().size() == 4;

			}
		} else {
			result = fp.getFiguresScores().size() == 4;
		}
		BigDecimal ts = fp.getTotalScore();
		if (ts == null || BigDecimal.ZERO.compareTo(ts) >= 0) {
			result = false;
		}
		return result;
	}

	public static void calculateMeetResults(Meet meet) {
		List<String> calcErrors = new ArrayList<String>();
		meet.setCalcErrors(calcErrors);

		// Calculate total score for figures participants
		for (FiguresParticipant fp : meet.getFiguresParticipants()) {
			if (fp.getFiguresScores().size() < 4) {
				calcErrors.add("ERROR: "
						+ ((fp.getSwimmer().getLevel().isNovice()) ? "NOVICE:"
								: "INTERMEDIATE:") + " Swimmer #"
						+ fp.getFigureOrder()
						+ " does not have 4 figure scores.");
			}
			for (FigureScore fs : fp.getFiguresScores()) {
				fs.setTotalScore(totalScore(fs));
				if (fs.getTotalScore() != null) {
					if (BigDecimal.ZERO.compareTo(fs.getTotalScore()) >= 0) {
						calcErrors
								.add("WARNING: "
										+ ((fp.getSwimmer().getLevel()
												.isNovice()) ? "NOVICE:"
												: "INTERMEDIATE:")
										+ " Swimmer #" + fp.getFigureOrder()
										+ " score for "
										+ getFigure(fs).getName() + " is zero.");
					}
				} else {
					calcErrors
							.add("ERROR: "
									+ ((fp.getSwimmer().getLevel().isNovice()) ? "NOVICE:"
											: "INTERMEDIATE:") + " Swimmer #"
									+ fp.getFigureOrder() + " score for "
									+ getFigure(fs).getName()
									+ " is blank or invalid.");
				}
			}
			fp.setTotalScore(calculateTotalScore(fp));
		}

		// Assign places and points to figures participants by level
		for (Level level : getLevels()) {
			List<FiguresParticipant> pointList = new ArrayList<FiguresParticipant>();
			for (FiguresParticipant fp : meet.getFiguresParticipants()) {
				if (fp.getSwimmer().getLevel().equals(level)) {
					pointList.add(fp);
				}
			}

			Collections.sort(pointList, new Comparator<FiguresParticipant>() {

				@Override
				public int compare(FiguresParticipant fp1,
						FiguresParticipant fp2) {
					BigDecimal ts1 = fp1.getTotalScore();
					if (ts1 == null) {
						ts1 = BigDecimal.ZERO;
					}
					BigDecimal ts2 = fp2.getTotalScore();
					if (ts2 == null) {
						ts2 = BigDecimal.ZERO;
					}
					return ts2.compareTo(ts1); // Desc sort
				}
			});

			BigDecimal lastTotal = BigDecimal.ZERO;
			Map<Integer, Integer> tieMap = new HashMap<Integer, Integer>();
			int place = 0;
			for (FiguresParticipant fp : pointList) {
				BigDecimal totalScore = fp.getTotalScore();
				if (totalScore == null) {
					totalScore = BigDecimal.ZERO;
				}
				if (totalScore.equals(lastTotal) && place > 0) {
					// tie
					fp.setPlace(place);
				} else {
					int placeInc = 1;
					if (tieMap.containsKey(place)) {
						placeInc = tieMap.get(place);
					}
					place += placeInc;
					fp.setPlace(place);
				}
				int tieCount = 0;
				if (tieMap.containsKey(place)) {
					tieCount = tieMap.get(place);
				}
				tieCount++;
				tieMap.put(place, tieCount);
				lastTotal = totalScore;
			}
			for (FiguresParticipant fp : pointList) {
				int tieCount = 1;
				if (tieMap.containsKey(fp.getPlace())) {
					tieCount = tieMap.get(fp.getPlace());
				}
				if (fp.getTotalScore() != null
						&& BigDecimal.ZERO.compareTo(fp.getTotalScore()) < 0) {
					fp.setPoints(calculateFigurePlacePoints(fp.getPlace(),
							tieCount, meet.getType()));
				} else {
					fp.setPoints(BigDecimal.ZERO);
				}
			}
		}

		assignRoutinePlaces(meet);
		assignRoutinePoints(meet);
		meet.setRoutinesChanged(false);

		// Sum points for teams
		Map<Team, BigDecimal> meetPointsMap = new HashMap<Team, BigDecimal>();
		meetPointsMap.put(meet.getHomeTeam(), BigDecimal.ZERO);
		for (Team team : meet.getOpponents()) {
			meetPointsMap.put(team, BigDecimal.ZERO);
		}

		// Figures Points
		for (FiguresParticipant fp : meet.getFiguresParticipants()) {
			Team team = fp.getSwimmer().getTeam();
			BigDecimal points = meetPointsMap.get(team);
			try {
				points = points.add(fp.getPoints());
			} catch (Exception e) {
				logger.error("", e);
			}
			meetPointsMap.put(team, points);
		}

		// Routine Points
		for (Team team : meetPointsMap.keySet()) {
			BigDecimal points = meetPointsMap.get(team);
			points = points.add(getRoutinePointsTotal(meet, team));
			meetPointsMap.put(team, points);
		}

		meet.setPointsMap(meetPointsMap);

		// Assign places for teams
		Map<Team, Integer> meetPlaceMap = new HashMap<Team, Integer>();
		List<Entry<Team, BigDecimal>> list = new LinkedList<Entry<Team, BigDecimal>>(
				meetPointsMap.entrySet());
		Collections.sort(list, new Comparator<Entry<Team, BigDecimal>>() {

			@Override
			public int compare(Entry<Team, BigDecimal> o1,
					Entry<Team, BigDecimal> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		int place = 0;
		BigDecimal lastPoints = BigDecimal.ZERO;
		for (Entry<Team, BigDecimal> entry : list) {
			BigDecimal points = entry.getValue();
			if (!points.equals(lastPoints) || place == 0) {
				place++;
			}
			meetPlaceMap.put(entry.getKey(), place);
			lastPoints = points;
		}
		meet.setPlaceMap(meetPlaceMap);

		// Save
		EntityManager entityManager = ScoreApp.getEntityManager();
		entityManager.getTransaction().begin();
		for (FiguresParticipant fp : meet.getFiguresParticipants()) {
			entityManager.persist(fp);
		}
		entityManager.getTransaction().commit();
	}

	private static BigDecimal getRoutinePointsTotal(Meet meet, Team team) {
		BigDecimal sum = BigDecimal.ZERO;
		if (meet.getType() == 'C') {
			for (Routine routine : meet.getRoutines()) {
				if (routine.getTeam().equals(team)) {
					sum = sum.add(routine.getPoints());
				}
			}
		} else {
			int c = countRoutines(meet, team);
			if (c == 1) {
				sum = new BigDecimal(5).setScale(2);
			} else if (c > 1) {
				sum = new BigDecimal(10).setScale(2);
			}
		}
		return sum;
	}

	private static int countRoutines(Meet meet, Team team) {
		int count = 0;
		for (Routine routine : meet.getRoutines()) {
			if (routine.getTeam().equals(team)) {
				count++;
			}
		}
		return count;
	}

	private static void assignRoutinePlaces(Meet meet) {
		List<Routine> routines = new LinkedList<Routine>();
		routines.addAll(meet.getRoutines());
		for (Routine routine : routines) {
			if (routine.getTotalScore() == null) {
				RoutineManager.calculate(routine);
				RoutineManager.save(routine);
				if (routine.getTotalScore() == null) {
					meet.getCalcErrors().add(
							"ERROR: Routine \"" + routine.getName()
									+ "\" not scored.");
				}
			}
		}
		Collections.sort(routines, new Comparator<Routine>() {

			@Override
			public int compare(Routine r1, Routine r2) {
				int c = r1.getRoutineType().compareTo(r2.getRoutineType());
				if (c != 0) {
					return c;
				}
				c = r1.getLevel().getSortOrder()
						.compareTo(r2.getLevel().getSortOrder());
				if (c != 0) {
					return c;
				}
				BigDecimal ts1 = r1.getTotalScore();
				if (ts1 == null) {
					ts1 = BigDecimal.ZERO;
				}
				BigDecimal ts2 = r2.getTotalScore();
				if (ts2 == null) {
					ts2 = BigDecimal.ZERO;
				}
				return ts2.compareTo(ts1);
			}
		});
		EntityManager em = ScoreApp.getEntityManager();
		em.getTransaction().begin();
		int place = 0;
		BigDecimal lastScore = BigDecimal.ZERO;
		String lastRoutineType = null;
		RoutineLevel lastRoutineLevel = null;
		for (Routine routine : routines) {
			if (lastRoutineType != null
					&& (!lastRoutineLevel.equals(routine.getLevel()) || !lastRoutineType
							.equals(routine.getRoutineType()))) {
				place = 0;
				lastScore = BigDecimal.ZERO;
			}
			BigDecimal score = routine.getTotalScore();
			if (score == null) {
				score = BigDecimal.ZERO;
			}
			if (!score.equals(lastScore) || place == 0) {
				place++;
			}
			routine.setPlace(place);
			em.persist(routine);
			lastScore = score;
			lastRoutineType = routine.getRoutineType();
			lastRoutineLevel = routine.getLevel();
		}
		em.getTransaction().commit();
	}

	private static void assignRoutinePoints(Meet meet) {
		EntityManager em = ScoreApp.getEntityManager();
		em.getTransaction().begin();
		if (meet.getType() == 'C') {
			HashMap<String, Integer> tieCountMap = new HashMap<String, Integer>();
			for (Routine routine : meet.getRoutines()) {
				String key = routine.getRoutineType() + "-"
						+ routine.getLevel().getLevelId() + "-"
						+ routine.getPlace().toString();
				Integer tieCount = tieCountMap.get(key);
				if (tieCount == null) {
					tieCount = 1;
				} else {
					tieCount++;
				}
				tieCountMap.put(key, tieCount);
			}
			for (Routine routine : meet.getRoutines()) {
				String key = routine.getRoutineType() + "-"
						+ routine.getLevel().getLevelId() + "-"
						+ routine.getPlace().toString();
				routine.setPoints(RoutineManager.calcChampsPlacePoints(
						routine.getPlace(), routine.getRoutineType(),
						tieCountMap.get(key)));
				em.persist(routine);
			}
		} else {
			for (Routine routine : meet.getRoutines()) {
				routine.setPoints(BigDecimal.ZERO);
				em.persist(routine);
			}
		}
		em.getTransaction().commit();
	}

	private static BigDecimal calculateFigurePlacePoints(int place,
			int tieCount, char meetType) {
		BigDecimal points = BigDecimal.ZERO;
		for (int i = 0; i < tieCount; i++) {
			points = points.add(getFigurePlacePoints(place + i, meetType));
		}
		points = points.divide(BigDecimal.valueOf(tieCount), 2,
				BigDecimal.ROUND_HALF_UP);
		return points;
	}

	private static BigDecimal getFigurePlacePoints(int place, char meetType) {
		if (place < 1) {
			throw new IllegalArgumentException("place<1 not allowed.");
		}
		if (meetType == 'R') {
			switch (place) {
			case 1:
				return BigDecimal.valueOf(7);
			case 2:
				return BigDecimal.valueOf(5);
			case 3:
				return BigDecimal.valueOf(3);
			case 4:
				return BigDecimal.valueOf(2);
			case 5:
				return BigDecimal.valueOf(1);
			default:
				return BigDecimal.ZERO;
			}
		} else if (meetType == 'C') {
			switch (place) {
			case 1:
				return BigDecimal.valueOf(10);
			case 2:
				return BigDecimal.valueOf(8);
			case 3:
				return BigDecimal.valueOf(6);
			case 4:
				return BigDecimal.valueOf(5);
			case 5:
				return BigDecimal.valueOf(4);
			case 6:
				return BigDecimal.valueOf(3);
			case 7:
				return BigDecimal.valueOf(2);
			case 8:
				return BigDecimal.valueOf(1);
			default:
				return BigDecimal.ZERO;
			}
		} else {
			throw new IllegalArgumentException("meetType was \"" + meetType
					+ "\" but only R and C are allowed.");
		}
	}

	public static BigDecimal calculateTotalScore(FiguresParticipant fp) {
		if (fp.getFiguresScores().isEmpty()) {
			return null;
		}
		BigDecimal total = BigDecimal.ZERO;
		for (FigureScore fs : fp.getFiguresScores()) {
			total = total.add(totalScore(fs));
		}
		return total;
	}

	public static void exportMeetData(Meet meet, java.awt.Component component) {
		if (meet != null) {
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Save Meet data file");
			jfc.setFileFilter(new FileNameExtensionFilter("csv file", "csv"));
			jfc.setSelectedFile(new File(meet.getName() + ".csv"));
			int ret = jfc.showSaveDialog(component);
			if (ret == JFileChooser.APPROVE_OPTION) {
				component.setCursor(Cursor
						.getPredefinedCursor(Cursor.WAIT_CURSOR));
				try {
					File f = jfc.getSelectedFile();
					if (!f.getName().endsWith(".csv")) {
						f = new File(f.getAbsolutePath() + ".csv");
					}
					MeetManager.exportMeet(meet, f);
					JOptionPane.showMessageDialog(component, "Done.");
				} catch (IOException e) {
					JOptionPane.showMessageDialog(component,
							"Error exporting\n" + e.getMessage());
				}
				component.setCursor(Cursor.getDefaultCursor());
			}
		}
	}
}
