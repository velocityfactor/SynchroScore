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
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Vector;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
@NamedQueries({@NamedQuery(name = "FiguresParticipant.findAll", query = "SELECT f FROM FiguresParticipant f"),
    @NamedQuery(name = "FiguresParticipant.findByMeetAndFigureOrder", query = "SELECT f FROM FiguresParticipant f WHERE f.figureOrder = :figureOrder and f.meet = :meet"),
    @NamedQuery(name = "FiguresParticipant.findByMeetAndSwimmer", query = "SELECT f FROM FiguresParticipant f WHERE f.meet = :meet and f.swimmer = :swimmer"),
    @NamedQuery(name = "FiguresParticipant.findByMeetAndLevelOrderByTotalScore", query = "SELECT f FROM FiguresParticipant f WHERE f.swimmer.level.id like :level and f.meet = :meet order by f.swimmer.level.sortOrder asc, f.totalScore desc")})
public class FiguresParticipant implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer figuresParticipantId;

    @Basic(optional = true)
    private String figureOrder;

    @ManyToOne
    @JoinColumn(name = "swimmerId")
    private Swimmer swimmer;

    @ManyToOne
    @JoinColumn(name = "meetId")
    private Meet meet;

    @Basic(optional = true)
    private BigDecimal totalScore;

    @Basic(optional = true)
    private Integer place;

    @Basic(optional = true)
    private BigDecimal points;

    @OneToMany(mappedBy = "figuresParticipant")
    private Collection<FigureScore> figureScores = new Vector<FigureScore>();

    public Collection<FigureScore> getFiguresScores() {
        return figureScores;
    }

    public void setFiguresScores(Collection<FigureScore> figureScores) {
        this.figureScores = figureScores;
    }

    public FiguresParticipant() {
    }

    public FiguresParticipant(Meet meet, Swimmer swimmer) {
        this.meet = meet;
        this.swimmer = swimmer;
    }

    public Integer getFiguresParticipantId() {
        return figuresParticipantId;
    }

    public String getFigureOrder() {
        return figureOrder;
    }

    public void setFigureOrder(String figureOrder) {
        String oldFigureOrder = this.figureOrder;
        this.figureOrder = figureOrder;
        changeSupport.firePropertyChange("figureOrder", oldFigureOrder, figureOrder);
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        BigDecimal oldTotalScore = this.totalScore;
        this.totalScore = totalScore;
        changeSupport.firePropertyChange("totalScore", oldTotalScore, totalScore);
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        Integer oldPlace = this.place;
        this.place = place;
        changeSupport.firePropertyChange("place", oldPlace, place);
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        BigDecimal oldPoints = this.points;
        this.points = points;
        changeSupport.firePropertyChange("points", oldPoints, points);
    }

    public Swimmer getSwimmer() {
        return swimmer;
    }

    public void setSwimmer(Swimmer swimmer) {
        Swimmer oldSwimmer = this.swimmer;
        this.swimmer = swimmer;
        changeSupport.firePropertyChange("swimmer", oldSwimmer, swimmer);
    }

    public Meet getMeet() {
        return meet;
    }

    public void setMeet(Meet meet) {
        Meet oldMeet = this.meet;
        this.meet = meet;
        changeSupport.firePropertyChange("meet", oldMeet, meet);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (figuresParticipantId != null ? figuresParticipantId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FiguresParticipant)) {
            return false;
        }
        FiguresParticipant other = (FiguresParticipant) object;
        if ((this.figuresParticipantId == null && other.figuresParticipantId != null) || (this.figuresParticipantId != null && !this.figuresParticipantId.equals(other.figuresParticipantId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "#" + figureOrder + (swimmer!=null?(" "+swimmer.getLastName()+", "+swimmer.getFirstName()):"");
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
}
