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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
//@NamedQueries({@NamedQuery(name = "Meet.findAll", query = "SELECT m FROM Meet m"), @NamedQuery(name = "Meet.findByMeetId", query = "SELECT m FROM Meet m WHERE m.meetId = :meetId"), @NamedQuery(name = "Meet.findByMeetDate", query = "SELECT m FROM Meet m WHERE m.meetDate = :meetDate"), @NamedQuery(name = "Meet.findByName", query = "SELECT m FROM Meet m WHERE m.name = :name"), @NamedQuery(name = "Meet.findByType", query = "SELECT m FROM Meet m WHERE m.type = :type")})
@NamedQueries({@NamedQuery(name = "Meet.findAllMeetsDescendingDate", query = "SELECT m FROM Meet m order by m.meetDate desc")})
public class Meet implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer meetId;
    private Date meetDate;
    private String name;
    private List<Team> opponents = new Vector();
    private List<FiguresParticipant> figuresParticipants = new Vector<FiguresParticipant>();
    private char type;
    private boolean figuresOrderGenerated = false;
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

    public Meet(Integer meetId, Date meetDate, String name, char type) {
        this.meetId = meetId;
        this.meetDate = meetDate;
        this.name = name;
        this.type = type;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getMeetId() {
        return meetId;
    }

    public void setMeetId(Integer meetId) {
        this.meetId = meetId;
    }

    @Transient
    public List<Swimmer> getSwimmers() {
        ArrayList<Swimmer> swimmers = new ArrayList<Swimmer>();
        for(FiguresParticipant fp:figuresParticipants) {
            swimmers.add(fp.getSwimmer());
        }
        return swimmers;
    }

    @ManyToMany
    public List<Team> getOpponents() {
        return opponents;
    }
    public void setOpponents(List<Team> opponents) {
        this.opponents = opponents;
    }

    @Temporal(value = TemporalType.TIMESTAMP)
    public Date getMeetDate() {
        return meetDate;
    }

    public void setMeetDate(Date meetDate) {
        this.meetDate = meetDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "meet")
    public List<FiguresParticipant> getFiguresParticipants() {
        return figuresParticipants;
    }

    public void setFiguresParticipants(List<FiguresParticipant> figuresParticipants) {
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

    @ManyToOne
    @JoinColumn(name="nov3Figure")
    public Figure getNov3Figure() {
        return nov3Figure;
    }

    public void setNov3Figure(Figure nov3Figure) {
        this.nov3Figure = nov3Figure;
    }

    @ManyToOne
    @JoinColumn(name="nov4Figure")
    public Figure getNov4Figure() {
        return nov4Figure;
    }

    public void setNov4Figure(Figure nov4Figure) {
        this.nov4Figure = nov4Figure;
    }

    @ManyToOne
    @JoinColumn(name="eu1Figure")
    public Figure getEu1Figure() {
        return eu1Figure;
    }

    public void setEu1Figure(Figure eu1Figure) {
        this.eu1Figure = eu1Figure;
    }

    @ManyToOne
    @JoinColumn(name="eu2Figure")
    public Figure getEu2Figure() {
        return eu2Figure;
    }

    public void setEu2Figure(Figure eu2Figure) {
        this.eu2Figure = eu2Figure;
    }

    @ManyToOne
    @JoinColumn(name="homeTeam")
    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    @ManyToOne
    @JoinColumn(name="int1Figure")
    public Figure getInt1Figure() {
        return int1Figure;
    }

    public void setInt1Figure(Figure int1Figure) {
        this.int1Figure = int1Figure;
    }

    @ManyToOne
    @JoinColumn(name="int2Figure")
    public Figure getInt2Figure() {
        return int2Figure;
    }

    public void setInt2Figure(Figure int2Figure) {
        this.int2Figure = int2Figure;
    }

    @ManyToOne
    @JoinColumn(name="int3Figure")
    public Figure getInt3Figure() {
        return int3Figure;
    }

    public void setInt3Figure(Figure int3Figure) {
        this.int3Figure = int3Figure;
    }

    @ManyToOne
    @JoinColumn(name="int4Figure")
    public Figure getInt4Figure() {
        return int4Figure;
    }

    public void setInt4Figure(Figure int4Figure) {
        this.int4Figure = int4Figure;
    }

    @ManyToOne
    @JoinColumn(name="nov1Figure")
    public Figure getNov1Figure() {
        return nov1Figure;
    }

    public void setNov1Figure(Figure nov1Figure) {
        this.nov1Figure = nov1Figure;
    }

    @ManyToOne
    @JoinColumn(name="nov2Figure")
    public Figure getNov2Figure() {
        return nov2Figure;
    }

    public void setNov2Figure(Figure nov2Figure) {
        this.nov2Figure = nov2Figure;
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
        return name+" ["+DateFormat.getDateInstance(DateFormat.SHORT).format(meetDate)+"]";
    }

}
