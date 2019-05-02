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

public class Label implements Comparable {

    String levelName;
    int levelOrder;
    String name;
    int place;
    String team;
    BigDecimal score;

    public Label(String levelName, int levelOrder, String name, int place, String team, BigDecimal score) {
        this.levelName = levelName;
        this.levelOrder = levelOrder;
        this.name = name;
        this.place = place;
        this.team = team;
        this.score = score;
    }

    public int getLevelOrder() {
        return levelOrder;
    }

    public void setLevelOrder(int levelOrder) {
        this.levelOrder = levelOrder;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
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

    public String toString() {
        return levelOrder + "-" + place;
    }

    public int compareTo(Object o) {
        Label fl = (Label) o;
        int comp = levelOrder - fl.levelOrder;
        if (comp != 0) {
            return comp;
        }
        return place - fl.place;
    }
}
