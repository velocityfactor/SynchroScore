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
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "meet")
@NamedQueries({@NamedQuery(name = "Meet.findAll", query = "SELECT m FROM Meet m"), @NamedQuery(name = "Meet.findByMeetId", query = "SELECT m FROM Meet m WHERE m.meetId = :meetId"), @NamedQuery(name = "Meet.findByDate", query = "SELECT m FROM Meet m WHERE m.date = :date"), @NamedQuery(name = "Meet.findByName", query = "SELECT m FROM Meet m WHERE m.name = :name"), @NamedQuery(name = "Meet.findByType", query = "SELECT m FROM Meet m WHERE m.type = :type")})
public class Meet implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer meetId;
    private Date date;
    private String name;

    List<Team> opponents = new Vector();
    @ManyToMany
    public List<Team> getOpponents() {
        return opponents;
    }
    public void setOpponents(List<Team> opponents) {
        this.opponents = opponents;
    }

    List<Swimmer> swimmers = new Vector<Swimmer>();
    @ManyToMany
    public List<Swimmer> getSwimmers() {
        return swimmers;
    }
    public void setSwimmers(List<Swimmer> swimmers) {
        this.swimmers = swimmers;
    }

    private char type;
    private Team homeTeam;
    private Figure eu1Figure;
    private Figure eu2Figure;
    private Figure nov1Figure;
    private Figure nov2Figure;
    private Figure nov3Figure;
    private Figure nov4Figure;
    private Figure int1Figure;
    private Figure int2Figure;
    private Figure int3Figure;
    private Figure int4Figure;

    public Meet() {
    }

    public Meet(Integer meetId) {
        this.meetId = meetId;
    }

    public Meet(Integer meetId, Date date, String name, char type) {
        this.meetId = meetId;
        this.date = date;
        this.name = name;
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "meetId", nullable = false)
    //@Id
    public Integer getMeetId() {
        return meetId;
    }

    public void setMeetId(Integer meetId) {
        this.meetId = meetId;
    }

    @Basic(optional = false)
    @Column(name = "date", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic(optional = false)
    @Column(name = "type", nullable = false)
    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    @JoinColumn(name = "nov3Figure", referencedColumnName = "figureId", nullable = false)
    @ManyToOne(optional = false)
    public Figure getNov3Figure() {
        return nov3Figure;
    }

    public void setNov3Figure(Figure nov3Figure) {
        this.nov3Figure = nov3Figure;
    }

    @JoinColumn(name = "nov4Figure", referencedColumnName = "figureId", nullable = false)
    @ManyToOne(optional = false)
    public Figure getNov4Figure() {
        return nov4Figure;
    }

    public void setNov4Figure(Figure nov4Figure) {
        this.nov4Figure = nov4Figure;
    }

    @JoinColumn(name = "eu1Figure", referencedColumnName = "figureId", nullable = false)
    @ManyToOne(optional = false)
    public Figure getEu1Figure() {
        return eu1Figure;
    }

    public void setEu1Figure(Figure eu1Figure) {
        this.eu1Figure = eu1Figure;
    }

    @JoinColumn(name = "eu2Figure", referencedColumnName = "figureId", nullable = false)
    @ManyToOne(optional = false)
    public Figure getEu2Figure() {
        return eu2Figure;
    }

    public void setEu2Figure(Figure eu2Figure) {
        this.eu2Figure = eu2Figure;
    }

    @JoinColumn(name = "homeTeam", referencedColumnName = "teamId", nullable = false)
    @ManyToOne(optional = false)
    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    @JoinColumn(name = "int1Figure", referencedColumnName = "figureId", nullable = false)
    @ManyToOne(optional = false)
    public Figure getInt1Figure() {
        return int1Figure;
    }

    public void setInt1Figure(Figure int1Figure) {
        this.int1Figure = int1Figure;
    }

    @JoinColumn(name = "int2Figure", referencedColumnName = "figureId", nullable = false)
    @ManyToOne(optional = false)
    public Figure getInt2Figure() {
        return int2Figure;
    }

    public void setInt2Figure(Figure int2Figure) {
        this.int2Figure = int2Figure;
    }

    @JoinColumn(name = "int3Figure", referencedColumnName = "figureId", nullable = false)
    @ManyToOne(optional = false)
    public Figure getInt3Figure() {
        return int3Figure;
    }

    public void setInt3Figure(Figure int3Figure) {
        this.int3Figure = int3Figure;
    }

    @JoinColumn(name = "int4Figure", referencedColumnName = "figureId", nullable = false)
    @ManyToOne(optional = false)
    public Figure getInt4Figure() {
        return int4Figure;
    }

    public void setInt4Figure(Figure int4Figure) {
        this.int4Figure = int4Figure;
    }

    @JoinColumn(name = "nov1Figure", referencedColumnName = "figureId", nullable = false)
    @ManyToOne(optional = false)
    public Figure getNov1Figure() {
        return nov1Figure;
    }

    public void setNov1Figure(Figure nov1Figure) {
        this.nov1Figure = nov1Figure;
    }

    @JoinColumn(name = "nov2Figure", referencedColumnName = "figureId", nullable = false)
    @ManyToOne(optional = false)
    public Figure getNov2Figure() {
        return nov2Figure;
    }

    public void setNov2Figure(Figure nov2Figure) {
        this.nov2Figure = nov2Figure;
    }

    private Collection<FigureScore> figureScores = new Vector<FigureScore>();
    @OneToMany(mappedBy = "meet", targetEntity = FigureScore.class)
    public Collection<FigureScore> getFigureScores() {
        return figureScores;
    }

    public void setFigureScores(Collection<FigureScore> figureScores) {
        this.figureScores = figureScores;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (meetId != null ? meetId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Meet)) {
            return false;
        }
        Meet other = (Meet) object;
        if ((this.meetId == null && other.meetId != null) || (this.meetId != null && !this.meetId.equals(other.meetId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name+" ["+DateFormat.getDateInstance(DateFormat.SHORT).format(date)+"]";
    }

}
