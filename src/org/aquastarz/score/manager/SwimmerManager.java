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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Level;
import org.aquastarz.score.domain.Season;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.domain.Team;

import au.com.bytecode.opencsv.CSVReader;

public class SwimmerManager {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(SwimmerManager.class.getName());

	public static Swimmer findByLeagueNum(Integer leagueNum, Season season) {
		Query swimmerQuery = ScoreApp.getEntityManager().createNamedQuery(
				"Swimmer.findByLeagueNumAndSeason");
		swimmerQuery.setParameter("leagueNum", leagueNum);
		swimmerQuery.setParameter("season", season);
		Swimmer swimmer = null;
		try {
			swimmer = (Swimmer) swimmerQuery.getSingleResult();
		} catch (Exception e) {
			// Ignore not found error
		}
		return swimmer;
	}

	public static String findNameLikeName(String firstName, String lastName,
			Season season) {
		Query swimmerQuery = ScoreApp
				.getEntityManager()
				.createQuery(
						"SELECT s FROM Swimmer s where upper(s.firstName) like :firstName and upper(s.lastName) like :lastName and s.season = :season");
		swimmerQuery.setParameter("firstName", firstName.toUpperCase() + "%");
		swimmerQuery.setParameter("lastName", lastName.toUpperCase() + "%");
		swimmerQuery.setParameter("season", season);
		Swimmer swimmer = null;
		try {
			List<Swimmer> swimmers = swimmerQuery.getResultList();
			if (swimmers.isEmpty() || swimmers.size() > 1) {
				return null;
			} else {
				swimmer = swimmers.get(0);
			}
		} catch (Exception e) {
			logger.error("Query error", e);
		}
		if (swimmer == null) {
			return null;
		} else {
			return swimmer.getFirstName() + " " + swimmer.getLastName();
		}
	}

	public static List<Swimmer> getSwimmers(Team team) {
		Query swimmerQuery = ScoreApp.getEntityManager().createNamedQuery(
				"Swimmer.findByTeamIdAndSeasonOrderByName");
		swimmerQuery.setParameter("teamId", team.getTeamId());
		swimmerQuery.setParameter("season", ScoreApp.getCurrentSeason());
		List<Swimmer> swimmers = swimmerQuery.getResultList();
		return swimmers;
	}

	public static List<Swimmer> getSwimmers(Season season) {
		Query swimmerQuery = ScoreApp.getEntityManager().createNamedQuery(
				"Swimmer.findBySeasonOrderByTeamAndName");
		swimmerQuery.setParameter("season", season);
		List<Swimmer> swimmers = swimmerQuery.getResultList();
		return swimmers;
	}

	public static List<Swimmer> getSwimmersOrderByLeagueNum(Season season) {
		Query swimmerQuery = ScoreApp.getEntityManager().createNamedQuery(
				"Swimmer.findBySeasonOrderByLeagueNum");
		swimmerQuery.setParameter("season", season);
		List<Swimmer> swimmers = swimmerQuery.getResultList();
		return swimmers;
	}

	public static Integer getNextLeagueNumber() {
		Query swimmerQuery = ScoreApp.getEntityManager().createNamedQuery(
				"Swimmer.findMaxLeagueIdBySeason");
		swimmerQuery.setParameter("season", ScoreApp.getCurrentSeason());
		return (Integer) swimmerQuery.getSingleResult() + 1;
	}

	public static void exportSwimmers(File file, Season season)
			throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write("League#,Last name,First name,Level,Team\n");
		for (Swimmer swimmer : getSwimmers(season)) {
			String s = swimmer.getLeagueNum() + ",\"" + swimmer.getLastName()
					+ "\",\"" + swimmer.getFirstName() + "\",\""
					+ swimmer.getLevel().getLevelId() + "\",\""
					+ swimmer.getTeam().getTeamId() + "\"\n";
			writer.write(s);
		}
		writer.close();
	}

	public static void importSwimmers(File file, Season season)
			throws IOException {
		CSVReader csv = new CSVReader(new FileReader(file));
		String[] header = csv.readNext();
		List<String[]> rows = csv.readAll();
		csv.close();
		if (header.length < 5 || !"League#".equals(header[0])
				|| !"Last name".equals(header[1])
				|| !"First name".equals(header[2])
				|| !"Level".equals(header[3]) || !"Team".equals(header[4])) {
			throw new IOException("Unexpected file format.");
		}
		int ln = 1;
		ArrayList<Swimmer> swimmers = new ArrayList<Swimmer>();
		StringBuilder sb = new StringBuilder();
		for (String[] row : rows) {
			ln++;
			Integer leagueNum = null;
			try {
				leagueNum = Integer.parseInt(row[0]);
			} catch (Exception e) {
				sb.append("Invalid league number on line ").append(ln)
						.append("\n");
			}
			Swimmer swimmer = SwimmerManager.findByLeagueNum(leagueNum, season);
			if (swimmer == null) {
				swimmer = new Swimmer();
			}
			swimmer.setLeagueNum(leagueNum);
			swimmer.setLastName(row[1]);
			swimmer.setFirstName(row[2]);
			Level level = LevelManager.findById(row[3]);
			if (level == null) {
				sb.append("Invalid level on line ").append(ln).append("\n");
			}
			swimmer.setLevel(level);
			Team team = TeamManager.findById(row[4]);
			if (team == null) {
				sb.append("Invalid team on line ").append(ln).append("\n");
			}
			swimmer.setTeam(team);
			swimmer.setSeason(season);
			swimmers.add(swimmer);
		}
		if (sb.length() == 0) {
			EntityManager entityManager = ScoreApp.getEntityManager();
			EntityTransaction transaction = entityManager.getTransaction();
			transaction.begin();
			try {
				for (Swimmer swimmer : swimmers) {
					entityManager.persist(swimmer);
				}
				transaction.commit();
			} catch (Exception e) {
				transaction.rollback();
				sb.append("There was an error loading the data.  Restart and try again.  If this continues, you may need to clear the database and try again.\n");
			}
		} else {
			throw new IOException(sb.toString());
		}
	}
}
