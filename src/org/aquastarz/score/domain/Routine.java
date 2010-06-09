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
@Table(name = "routine")
@NamedQueries({
    @NamedQuery(name = "Routine.findByMeetOrderByRoutineOrder", query = "SELECT r FROM Routine r WHERE r.meet = :meet ORDER BY routineOrder")})
public class Routine implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private Long routineId;
    @ManyToOne
    @JoinColumn(name = "meet")
    private Meet meet;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 60)
    private String name;
    @ManyToOne(optional = false)
    @JoinColumn(name = "level", referencedColumnName = "routineLevelId", nullable = false)
    private RoutineLevel level;
    @ManyToOne(optional = false)
    @JoinColumn(name = "team", referencedColumnName = "teamId", nullable = false)
    private Team team;
    @Basic(optional = false)
    @Column(name = "routineType", nullable = false, length = 4)
    private String routineType;
    @Basic(optional = true)
    @Column(nullable = true, length = 60)
    private String swimmers1;
    @Basic(optional = true)
    @Column(nullable = true, length = 60)
    private String swimmers2;
    @Basic(optional = true)
    @Column(name = "routineOrder", nullable = true)
    private Integer routineOrder;
    @Basic(optional = true)
    @Column(name = "tscore1", nullable = true, precision = 2, scale = 1)
    private BigDecimal tScore1;
    @Basic(optional = true)
    @Column(name = "tscore2", nullable = true, precision = 2, scale = 1)
    private BigDecimal tScore2;
    @Basic(optional = true)
    @Column(name = "tscore3", nullable = true, precision = 2, scale = 1)
    private BigDecimal tScore3;
    @Basic(optional = true)
    @Column(name = "tscore4", nullable = true, precision = 2, scale = 1)
    private BigDecimal tScore4;
    @Basic(optional = true)
    @Column(name = "tscore5", nullable = true, precision = 2, scale = 1)
    private BigDecimal tScore5;
    @Basic(optional = true)
    @Column(name = "tscore6", nullable = true, precision = 2, scale = 1)
    private BigDecimal tScore6;
    @Basic(optional = true)
    @Column(name = "tscore7", nullable = true, precision = 2, scale = 1)
    private BigDecimal tScore7;
    @Basic(optional = true)
    @Column(name = "ascore1", nullable = true, precision = 2, scale = 1)
    private BigDecimal aScore1;
    @Basic(optional = true)
    @Column(name = "ascore2", nullable = true, precision = 2, scale = 1)
    private BigDecimal aScore2;
    @Basic(optional = true)
    @Column(name = "ascore3", nullable = true, precision = 2, scale = 1)
    private BigDecimal aScore3;
    @Basic(optional = true)
    @Column(name = "ascore4", nullable = true, precision = 2, scale = 1)
    private BigDecimal aScore4;
    @Basic(optional = true)
    @Column(name = "ascore5", nullable = true, precision = 2, scale = 1)
    private BigDecimal aScore5;
    @Basic(optional = true)
    @Column(name = "ascore6", nullable = true, precision = 2, scale = 1)
    private BigDecimal aScore6;
    @Basic(optional = true)
    @Column(name = "ascore7", nullable = true, precision = 2, scale = 1)
    private BigDecimal aScore7;
    @Basic(optional = true)
    @Column(name = "penalty", nullable = true, precision = 2, scale = 1)
    private BigDecimal penalty;
    @Basic(optional = true)
    @Column(name = "totalScore", nullable = true, precision = 2, scale = 1)
    private BigDecimal totalScore;
    @Basic(optional = true)
    @Column(name = "place", nullable = true)
    private Integer place;
    @Basic(optional = true)
    @Column(name = "points", nullable = true, precision = 2, scale = 1)
    private BigDecimal points;

    public Routine() {
    }

    public Routine(Integer routineOrder) {
        this.routineOrder = routineOrder;
    }

    public Long getRoutineId() {
        return routineId;
    }

    public void setRoutineId(Long routineId) {
        this.routineId = routineId;
    }

    public RoutineLevel getLevel() {
        return level;
    }

    public void setLevel(RoutineLevel level) {
        this.level = level;
    }

    public Meet getMeet() {
        return meet;
    }

    public void setMeet(Meet meet) {
        this.meet = meet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRoutineOrder() {
        return routineOrder;
    }

    public void setRoutineOrder(Integer routineOrder) {
        this.routineOrder = routineOrder;
    }

    public String getRoutineType() {
        return routineType;
    }

    public void setRoutineType(String routineType) {
        this.routineType = routineType;
    }

    public String getSwimmers1() {
        return swimmers1;
    }

    public void setSwimmers1(String swimmers1) {
        this.swimmers1 = swimmers1;
    }

    public String getSwimmers2() {
        return swimmers2;
    }

    public void setSwimmers2(String swimmers2) {
        this.swimmers2 = swimmers2;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public BigDecimal getAScore1() {
        return aScore1;
    }

    public void setAScore1(BigDecimal aScore1) {
        this.aScore1 = aScore1;
    }

    public BigDecimal getAScore2() {
        return aScore2;
    }

    public void setAScore2(BigDecimal aScore2) {
        this.aScore2 = aScore2;
    }

    public BigDecimal getAScore3() {
        return aScore3;
    }

    public void setAScore3(BigDecimal aScore3) {
        this.aScore3 = aScore3;
    }

    public BigDecimal getAScore4() {
        return aScore4;
    }

    public void setAScore4(BigDecimal aScore4) {
        this.aScore4 = aScore4;
    }

    public BigDecimal getAScore5() {
        return aScore5;
    }

    public void setAScore5(BigDecimal aScore5) {
        this.aScore5 = aScore5;
    }

    public BigDecimal getAScore6() {
        return aScore6;
    }

    public void setAScore6(BigDecimal aScore6) {
        this.aScore6 = aScore6;
    }

    public BigDecimal getAScore7() {
        return aScore7;
    }

    public void setAScore7(BigDecimal aScore7) {
        this.aScore7 = aScore7;
    }

    public BigDecimal getTScore1() {
        return tScore1;
    }

    public void setTScore1(BigDecimal tScore1) {
        this.tScore1 = tScore1;
    }

    public BigDecimal getTScore2() {
        return tScore2;
    }

    public void setTScore2(BigDecimal tScore2) {
        this.tScore2 = tScore2;
    }

    public BigDecimal getTScore3() {
        return tScore3;
    }

    public void setTScore3(BigDecimal tScore3) {
        this.tScore3 = tScore3;
    }

    public BigDecimal getTScore4() {
        return tScore4;
    }

    public void setTScore4(BigDecimal tScore4) {
        this.tScore4 = tScore4;
    }

    public BigDecimal getTScore5() {
        return tScore5;
    }

    public void setTScore5(BigDecimal tScore5) {
        this.tScore5 = tScore5;
    }

    public BigDecimal getTScore6() {
        return tScore6;
    }

    public void setTScore6(BigDecimal tScore6) {
        this.tScore6 = tScore6;
    }

    public BigDecimal getTScore7() {
        return tScore7;
    }

    public void setTScore7(BigDecimal tScore7) {
        this.tScore7 = tScore7;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    public BigDecimal getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(BigDecimal totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getPlace() {
        return place;
    }

    public void setPlace(Integer place) {
        this.place = place;
    }

    public BigDecimal getPoints() {
        return points;
    }

    public void setPoints(BigDecimal points) {
        this.points = points;
    }

    @Override
    public String toString() {
        if (routineOrder == null) {
            return name;
        } else {
            return routineOrder + ": " + name;
        }
    }
}
