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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames={"season","leagueNum"})})
@NamedQueries({@NamedQuery(name = "Swimmer.findByTeamIdAndSeasonOrderByLeagueNum", query = "SELECT s FROM Swimmer s where s.team.teamId like :teamId and s.season like :season order by leagueNum"),
    @NamedQuery(name = "Swimmer.findByLeagueNumAndSeason", query = "SELECT s FROM Swimmer s where s.leagueNum like :leagueNum and s.season = :season"),
    @NamedQuery(name = "Swimmer.findByTeamIdAndSeasonOrderByName", query = "SELECT s FROM Swimmer s where s.team.teamId like :teamId and s.season like :season order by lastName,firstName"),
    @NamedQuery(name = "Swimmer.findByTeamIdAndSeasonOrderByTeamAndName", query = "SELECT s FROM Swimmer s where s.team.teamId like :teamId and s.season like :season order by team,lastName,firstName"),
    @NamedQuery(name = "Swimmer.findByTeamIdAndSeasonOrderByLevelAndName", query = "SELECT s FROM Swimmer s where s.team.teamId like :teamId and s.season like :season order by level.sortOrder,lastName,firstName")})
public class Swimmer implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer swimmerId;

    @Basic(optional = false)
    @Column(name = "leagueNum", nullable = false)
    private Integer leagueNum;

    @Basic(optional = false)
    @Column(name = "firstName", nullable = false, length = 45)
    private String firstName;

    @Basic(optional = false)
    @Column(name = "lastName", nullable = false, length = 45)
    private String lastName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "level", referencedColumnName = "levelId", nullable = false)
    private Level level;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team", referencedColumnName = "teamId", nullable = false)
    private Team team;

    @ManyToOne(optional = false)
    @JoinColumn(name = "season")
    private Season season;

    public Swimmer() {
    }

    public Swimmer(Integer leagueNum, Season season) {
        this.leagueNum = leagueNum;
        this.season = season;
    }

    public Swimmer(Integer leagueNum, Season season, Team team, Level level, String firstName, String lastName) {
        this.leagueNum = leagueNum;
        this.season = season;
        this.firstName = firstName;
        this.lastName = lastName;
        this.level = level;
        this.team = team;
    }

    public Integer getSwimmerId() {
        return swimmerId;
    }

    public Integer getLeagueNum() {
        return leagueNum;
    }

    public void setLeagueNum(Integer leagueNum) {
        Integer oldLeagueNum = this.leagueNum;
        this.leagueNum = leagueNum;
        changeSupport.firePropertyChange("leagueNum", oldLeagueNum, leagueNum);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        String oldFirstName = this.firstName;
        this.firstName = firstName;
        changeSupport.firePropertyChange("firstName", oldFirstName, firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        String oldLastName = this.lastName;
        this.lastName = lastName;
        changeSupport.firePropertyChange("lastName", oldLastName, lastName);
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        Level oldLevel = this.level;
        this.level = level;
        changeSupport.firePropertyChange("level", oldLevel, level);
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        Team oldTeam = this.team;
        this.team = team;
        changeSupport.firePropertyChange("team", oldTeam, team);
    }

    public Season getSeason() {
        return season;
    }

    public void setSeason(Season season) {
        this.season = season;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (swimmerId != null ? swimmerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Swimmer)) {
            return false;
        }
        Swimmer other = (Swimmer) object;
        if ((this.swimmerId == null && other.swimmerId != null) || (this.swimmerId != null && !this.swimmerId.equals(other.swimmerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "#" + swimmerId + " " + lastName + ", " + firstName + " (" + level.getLevelId() + ")";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
