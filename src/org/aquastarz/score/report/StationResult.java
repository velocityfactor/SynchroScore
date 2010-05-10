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
package org.aquastarz.score.report;

import java.math.BigDecimal;

public class StationResult implements Comparable {
    String figureId;
    String figureName;
    String figureDd;
    String level;
    Integer levelOrder;
    BigDecimal score;
    String lastName;
    String firstName;
    String figureOrder;
    String team;

    public Integer getLevelOrder() {
        return levelOrder;
    }

    public void setLevelOrder(Integer levelOrder) {
        this.levelOrder = levelOrder;
    }

    public String getFigureDd() {
        return figureDd;
    }

    public void setFigureDd(String figureDd) {
        this.figureDd = figureDd;
    }

    public String getFigureId() {
        return figureId;
    }

    public void setFigureId(String figureId) {
        this.figureId = figureId;
    }

    public String getFigureName() {
        return figureName;
    }

    public void setFigureName(String figureName) {
        this.figureName = figureName;
    }

    public String getFigureOrder() {
        return figureOrder;
    }

    public void setFigureOrder(String figureOrder) {
        this.figureOrder = figureOrder;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int compareTo(Object o) {
        StationResult sr = (StationResult) o;
        int comp = figureId.compareTo(sr.figureId);
        if(comp != 0) return comp;
        comp = levelOrder.compareTo(sr.levelOrder);
        if(comp != 0) return comp;
        return -score.compareTo(sr.score);
    }
}
