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
package org.aquastarz.score.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.aquastarz.score.report.TeamDetailPoints;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "season", "name" }) })
@NamedQueries({
		@NamedQuery(name = "Meet.findAllBySeasonOrderByIdDesc", query = "SELECT m FROM Meet m where m.season = :season order by m.meetId desc"),
		@NamedQuery(name = "Meet.findBySeasonAndName", query = "SELECT m FROM Meet m where m.season = :season and m.name like :name") })
public class Meet implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer meetId;
	@Basic(optional = false)
	@Column(name = "meetDate", nullable = false, length = 100)
	private String meetDate;
	@Basic(optional = false)
	@Column(name = "name", nullable = false, length = 255)
	private String name;
	@ManyToMany
	private List<Team> opponents = new Vector();
	@OneToMany(mappedBy = "meet")
	private List<FiguresParticipant> figuresParticipants = new Vector<FiguresParticipant>();
	@OneToMany(mappedBy = "meet")
	private List<Routine> routines = new Vector<Routine>();
	private char type;
	private boolean figuresOrderGenerated = false;
	private Boolean routinesOrderGenerated;
	@ManyToOne
	@JoinColumn(name = "homeTeam")
	private Team homeTeam;
	private boolean eu1; /* Obsolete as of rules revision 2 - N8U swim all four */

	private boolean eu2;
	private boolean eu3;
	private boolean eu4;
	@ManyToOne
	@JoinColumn(name = "nov1Figure")
	private Figure nov1Figure;
	@ManyToOne
	@JoinColumn(name = "nov2Figure")
	private Figure nov2Figure;
	@ManyToOne
	@JoinColumn(name = "nov3Figure")
	private Figure nov3Figure;
	@ManyToOne
	@JoinColumn(name = "nov4Figure")
	private Figure nov4Figure;
	@ManyToOne
	@JoinColumn(name = "int1Figure")
	private Figure int1Figure;
	@ManyToOne
	@JoinColumn(name = "int2Figure")
	private Figure int2Figure;
	@ManyToOne
	@JoinColumn(name = "int3Figure")
	private Figure int3Figure;
	@ManyToOne
	@JoinColumn(name = "int4Figure")
	private Figure int4Figure;
	@ManyToOne
	@JoinColumn(name = "season")
	private Season season;
	@Transient
	private Map<Team, BigDecimal> pointsMap = null;
	@Transient
	private Map<Team, Integer> placeMap = null;
	@Transient
	private List<String> calcErrors = null;
	@Transient
	private boolean routinesChanged = false;

	public Meet() {
	}

	public Meet(Integer meetId) {
		this.meetId = meetId;
	}

	public Meet(Integer meetId, String meetDate, String name, char type) {
		this.meetId = meetId;
		this.meetDate = meetDate;
		this.name = name;
		this.type = type;
	}

	public List<String> getCalcErrors() {
		return calcErrors;
	}

	public void setCalcErrors(List<String> calcErrors) {
		this.calcErrors = calcErrors;
	}

	public Integer getMeetId() {
		return meetId;
	}

	public void setMeetId(Integer meetId) {
		this.meetId = meetId;
	}

	public List<Swimmer> getSwimmers() {
		ArrayList<Swimmer> swimmers = new ArrayList<Swimmer>();
		for (FiguresParticipant fp : figuresParticipants) {
			swimmers.add(fp.getSwimmer());
		}
		return swimmers;
	}

	public List<Team> getOpponents() {
		return opponents;
	}

	public void setOpponents(List<Team> opponents) {
		this.opponents = opponents;
	}

	public String getMeetDate() {
		return meetDate;
	}

	public void setMeetDate(String meetDate) {
		this.meetDate = meetDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FiguresParticipant> getFiguresParticipants() {
		return figuresParticipants;
	}

	public void setFiguresParticipants(
			List<FiguresParticipant> figuresParticipants) {
		this.figuresParticipants = figuresParticipants;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public boolean getFiguresOrderGenerated() {
		return figuresOrderGenerated;
	}

	public void setFiguresOrderGenerated(boolean figuresOrderGenerated) {
		this.figuresOrderGenerated = figuresOrderGenerated;
	}

	public Figure getNov3Figure() {
		return nov3Figure;
	}

	public void setNov3Figure(Figure nov3Figure) {
		this.nov3Figure = nov3Figure;
	}

	public Figure getNov4Figure() {
		return nov4Figure;
	}

	public void setNov4Figure(Figure nov4Figure) {
		this.nov4Figure = nov4Figure;
	}

	public boolean isEu1() {
		return eu1;
	}

	public void setEu1(boolean eu1) {
		this.eu1 = eu1;
	}

	public boolean isEu2() {
		return eu2;
	}

	public void setEu2(boolean eu2) {
		this.eu2 = eu2;
	}

	public boolean isEu3() {
		return eu3;
	}

	public void setEu3(boolean eu3) {
		this.eu3 = eu3;
	}

	public boolean isEu4() {
		return eu4;
	}

	public void setEu4(boolean eu4) {
		this.eu4 = eu4;
	}

	public Team getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}

	public Figure getInt1Figure() {
		return int1Figure;
	}

	public void setInt1Figure(Figure int1Figure) {
		this.int1Figure = int1Figure;
	}

	public Figure getInt2Figure() {
		return int2Figure;
	}

	public void setInt2Figure(Figure int2Figure) {
		this.int2Figure = int2Figure;
	}

	public Figure getInt3Figure() {
		return int3Figure;
	}

	public void setInt3Figure(Figure int3Figure) {
		this.int3Figure = int3Figure;
	}

	public Figure getInt4Figure() {
		return int4Figure;
	}

	public void setInt4Figure(Figure int4Figure) {
		this.int4Figure = int4Figure;
	}

	public Figure getNov1Figure() {
		return nov1Figure;
	}

	public void setNov1Figure(Figure nov1Figure) {
		this.nov1Figure = nov1Figure;
	}

	public Figure getNov2Figure() {
		return nov2Figure;
	}

	public void setNov2Figure(Figure nov2Figure) {
		this.nov2Figure = nov2Figure;
	}

	public Season getSeason() {
		return season;
	}

	public void setSeason(Season season) {
		this.season = season;
	}

	public List<Routine> getRoutines() {
		return routines;
	}

	public void setRoutines(List<Routine> routines) {
		this.routines = routines;
	}

	public Boolean getRoutinesOrderGenerated() {
		return routinesOrderGenerated;
	}

	public void setRoutinesOrderGenerated(Boolean routinesOrderGenerated) {
		this.routinesOrderGenerated = routinesOrderGenerated;
	}

	public boolean needsPointsCalc() {
		return placeMap == null || pointsMap == null || routinesChanged;
	}

	public void clearPoints() {
		placeMap = null;
		pointsMap = null;
	}

	public Map<Team, Integer> getPlaceMap() {
		return placeMap;
	}

	public void setPlaceMap(Map<Team, Integer> placeMap) {
		this.placeMap = placeMap;
	}

	public Map<Team, BigDecimal> getPointsMap() {
		return pointsMap;
	}

	public void setPointsMap(Map<Team, BigDecimal> pointsMap) {
		this.pointsMap = pointsMap;
	}

	public Collection<TeamDetailPoints> getDetailPointsList() {
		TreeMap<String, TeamDetailPoints> detailMap = new TreeMap<String, TeamDetailPoints>();
		for (FiguresParticipant fp : getFiguresParticipants()) {
			String key = fp.getSwimmer().getTeam().getTeamId() + "F"
					+ fp.getSwimmer().getLevel().getSortOrder().toString();
			TeamDetailPoints tdp = detailMap.get(key);
			if (tdp == null) {
				tdp = new TeamDetailPoints(fp.getSwimmer().getTeam()
						.getTeamId(), fp.getSwimmer().getLevel().getLevelId(),
						BigDecimal.ZERO);
				detailMap.put(key, tdp);
			}
			tdp.setPoints(tdp.getPoints().add(fp.getPoints()));
		}

		for (Routine routine : getRoutines()) {
			String key = routine.getTeam().getTeamId();
			if ("Solo".equals(routine.getRoutineType()))
				key = key + "R1";
			else if ("Duet".equals(routine.getRoutineType()))
				key = key + "R2";
			else if ("Trio".equals(routine.getRoutineType()))
				key = key + "R3";
			else if ("Team".equals(routine.getRoutineType()))
				key = key + "R4";
			TeamDetailPoints tdp = detailMap.get(key);
			if (tdp == null)
				tdp = new TeamDetailPoints(routine.getTeam().getTeamId(),
						routine.getRoutineType(), BigDecimal.ZERO);
			tdp.setPoints(tdp.getPoints().add(routine.getPoints()));
		}

		return detailMap.values();
	}

	public boolean isRoutinesOrderGenerated() {
		if (routinesOrderGenerated != null) {
			return routinesOrderGenerated.booleanValue();
		} else {
			return false;
		}
	}

	public void setRoutinesOrderGenerated(boolean routinesOrderGenerated) {
		this.routinesOrderGenerated = routinesOrderGenerated;
	}

	public boolean hasCalcErrors() {
		return calcErrors != null && calcErrors.size() > 0;
	}

	public boolean hasFiguresParticipants(Meet meet) {
		return figuresParticipants != null && figuresParticipants.size() > 0;
	}

	public boolean isValid() {
		boolean valid = true;
		if (name == null || name.length() < 1) {
			valid = false;
		}
		if (meetDate == null || meetDate.length() < 1) {
			valid = false;
		}
		if (type != 'R' && type != 'C') {
			valid = false;
		}
		if (homeTeam == null) {
			valid = false;
		}
		if (opponents == null || opponents.size() < 1
				|| opponents.contains(homeTeam)) {
			valid = false;
		}
		if (nov1Figure == null) {
			valid = false;
		}
		if (nov2Figure == null) {
			valid = false;
		}
		if (nov3Figure == null) {
			valid = false;
		}
		if (nov4Figure == null) {
			valid = false;
		}
		if (int1Figure == null) {
			valid = false;
		}
		if (int2Figure == null) {
			valid = false;
		}
		if (int3Figure == null) {
			valid = false;
		}
		if (int4Figure == null) {
			valid = false;
		}
		int euCount = 0;
		if (eu1) {
			euCount++;
		}
		if (eu2) {
			euCount++;
		}
		if (eu3) {
			euCount++;
		}
		if (eu4) {
			euCount++;
		}
		if (season.getRulesRevision() < 2) {
			if (type == 'R' && euCount != 2) {
				valid = false;
			}
			if (type == 'C' && euCount != 4) {
				valid = false;
			}
		}
		return valid;
	}

	public boolean isChamps() {
		return type == 'C';
	}

	public List<Figure> getFigureList(Swimmer swimmer) {
		List<Figure> figures = new ArrayList<Figure>();
		String level = swimmer.getLevel().getLevelId();
		if (level.startsWith("N")) {
			if (getSeason().getRulesRevision() < 2 && "N8".equals(level)) {
				if (eu1) {
					figures.add(nov1Figure);
				} else {
					figures.add(null);
				}
				if (eu2) {
					figures.add(nov2Figure);
				} else {
					figures.add(null);
				}
				if (eu3) {
					figures.add(nov3Figure);
				} else {
					figures.add(null);
				}
				if (eu4) {
					figures.add(nov4Figure);
				} else {
					figures.add(null);
				}
			} else {
				figures.add(nov1Figure);
				figures.add(nov2Figure);
				figures.add(nov3Figure);
				figures.add(nov4Figure);
			}
		} else {
			figures.add(int1Figure);
			figures.add(int2Figure);
			figures.add(int3Figure);
			figures.add(int4Figure);
		}
		return figures;
	}

	public void setRoutinesChanged(boolean routinesChanged) {
		this.routinesChanged = routinesChanged;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (meetId != null ? meetId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Meet)) {
			return false;
		}
		Meet other = (Meet) object;
		if ((this.meetId == null && other.meetId != null)
				|| (this.meetId != null && !this.meetId.equals(other.meetId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return name + " [" + meetDate + "]";
	}
}
