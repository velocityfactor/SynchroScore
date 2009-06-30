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
import javax.persistence.Table;

@Entity
@Table(name = "figurescore")
@NamedQueries({@NamedQuery(name = "FigureScore.findAll", query = "SELECT s FROM FigureScore s"), @NamedQuery(name = "FigureScore.findByFigureScoreId", query = "SELECT s FROM FigureScore s WHERE s.figureScoreId = :figureScoreId")})
public class FigureScore implements Serializable {
    private static final long serialVersionUID = 1L;

   public FigureScore() {
    }

    public FigureScore(Integer figureScoreId) {
        this.figureScoreId = figureScoreId;
    }

    private Integer figureScoreId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "figureScoreId", nullable = false)
    public Integer getFigureScoreId() {
        return figureScoreId;
    }
    public void setFigureScoreId(Integer figureScoreId) {
        this.figureScoreId = figureScoreId;
    }

    private BigDecimal score;
    @Basic(optional = false)
    @Column(name = "score", nullable = false, precision=2, scale=1)
    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    private BigDecimal penalty;
    @Basic(optional = false)
    @Column(name = "penalty", nullable = false, precision=2, scale=1)
    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    private int judge;
    @Basic(optional = false)
    @Column(name = "judge", nullable = false)
    public int getJudge() {
        return judge;
    }

    public void setJudge(int judge) {
        this.judge = judge;
    }

    private Meet meet;
    @JoinColumn(name = "meetId", referencedColumnName = "meetId", nullable = false)
    @ManyToOne(optional = false)
    public Meet getMeet() {
        return meet;
    }

    public void setMeet(Meet meet) {
        this.meet = meet;
    }

    private Figure figure;
    @JoinColumn(name = "figureId", referencedColumnName = "figureId", nullable = false)
    @ManyToOne(optional = false)
    public Figure getFigure() {
        return figure;
    }

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    private Swimmer swimmer;
    @JoinColumn(name = "swimmerId", referencedColumnName = "swimmerId", nullable = false)
    @ManyToOne(optional = false)
    public Swimmer getSwimmer() {
        return swimmer;
    }

    public void setSwimmer(Swimmer swimmer) {
        this.swimmer = swimmer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (figureScoreId != null ? figureScoreId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FigureScore)) {
            return false;
        }
        FigureScore other = (FigureScore) object;
        if ((this.figureScoreId == null && other.figureScoreId != null) || (this.figureScoreId != null && !this.figureScoreId.equals(other.figureScoreId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FigureScore #"+figureScoreId;
    }

}
