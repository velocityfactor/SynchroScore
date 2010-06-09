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

@Entity
@Table(name = "figurescore")
@NamedQueries({
    @NamedQuery(name = "FigureScore.findAll", query = "SELECT s FROM FigureScore s"),
    @NamedQuery(name = "FigureScore.findByFigureScoreId", query = "SELECT s FROM FigureScore s WHERE s.figureScoreId = :figureScoreId")})
public class FigureScore implements Serializable {

    private static final long serialVersionUID = 1L;

    public FigureScore() {
    }

    public FigureScore(FiguresParticipant figuresParticipant, int station) {
        this.figuresParticipant = figuresParticipant;
        this.station = station;
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
    private BigDecimal score1;

    @Basic(optional = false)
    @Column(name = "score1", nullable = false, precision = 2, scale = 1)
    public BigDecimal getScore1() {
        return score1;
    }

    public void setScore1(BigDecimal score1) {
        this.score1 = score1;
    }
    private BigDecimal score2;

    @Basic(optional = false)
    @Column(name = "score2", nullable = false, precision = 2, scale = 1)
    public BigDecimal getScore2() {
        return score2;
    }

    public void setScore2(BigDecimal score2) {
        this.score2 = score2;
    }
    private BigDecimal score3;

    @Basic(optional = false)
    @Column(name = "score3", nullable = false, precision = 2, scale = 1)
    public BigDecimal getScore3() {
        return score3;
    }

    public void setScore3(BigDecimal score3) {
        this.score3 = score3;
    }
    private BigDecimal score4;

    @Basic(optional = false)
    @Column(name = "score4", nullable = false, precision = 2, scale = 1)
    public BigDecimal getScore4() {
        return score4;
    }

    public void setScore4(BigDecimal score4) {
        this.score4 = score4;
    }
    private BigDecimal score5;

    @Basic(optional = false)
    @Column(name = "score5", nullable = false, precision = 2, scale = 1)
    public BigDecimal getScore5() {
        return score5;
    }

    public void setScore5(BigDecimal score5) {
        this.score5 = score5;
    }
    private BigDecimal totalScore;

    @Basic(optional = true)
    @Column(name = "totalScore", nullable = true, precision = 2, scale = 1)
    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }
    private BigDecimal penalty;

    @Basic(optional = false)
    @Column(name = "penalty", nullable = false, precision = 2, scale = 1)
    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }
    private int station;

    public int getStation() {
        return station;
    }

    public void setStation(int station) {
        this.station = station;
    }
    private FiguresParticipant figuresParticipant;

    @ManyToOne(optional = false)
    @JoinColumn(name = "figuresParticipantId")
    public FiguresParticipant getFiguresParticipant() {
        return figuresParticipant;
    }

    public void setFiguresParticipant(FiguresParticipant figuresParticipant) {
        this.figuresParticipant = figuresParticipant;
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
        return "FigureScore #" + figureScoreId + " s1=" + score1 + " s2=" + score2 + " s3=" + score3 + " s4=" + score4 + " s5=" + score5 + " pen=" + penalty + " station=" + station;
    }
}
