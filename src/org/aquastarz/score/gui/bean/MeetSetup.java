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
package org.aquastarz.score.gui.bean;

import java.util.Date;
import java.util.List;
import java.util.Set;
import org.aquastarz.score.domain.Figure;
import org.aquastarz.score.domain.Team;

public class MeetSetup {

    protected String meetName;
    protected Date meetDate;
    protected Team homeTeam;
    protected Set<Team> opponents;
    protected List<Figure> noviceFigures;
    protected List<Figure> intFigures;
    protected List<Figure> eightAndUnderFigures;
    public enum MeetType { Regular, Championship };
    protected MeetType meetType;

    public String getMeetName() {
        return meetName;
    }

    public void setMeetName(String meetName) {
        this.meetName = meetName;
    }

    public Date getMeetDate() {
        return meetDate;
    }

    public void setMeetDate(Date meetDate) {
        this.meetDate = meetDate;
    }

    public List<Figure> getEightAndUnderFigures() {
        return eightAndUnderFigures;
    }

    public void setEightAndUnderFigures(List<Figure> eightAndUnderFigures) {
        this.eightAndUnderFigures = eightAndUnderFigures;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public List<Figure> getIntFigures() {
        return intFigures;
    }

    public void setIntFigures(List<Figure> intFigures) {
        this.intFigures = intFigures;
    }

    public List<Figure> getNoviceFigures() {
        return noviceFigures;
    }

    public void setNoviceFigures(List<Figure> noviceFigures) {
        this.noviceFigures = noviceFigures;
    }

    public Set<Team> getOpponents() {
        return opponents;
    }

    public void setOpponents(Set<Team> opponents) {
        this.opponents = opponents;
    }

    public MeetType getMeetType() {
        return meetType;
    }

    public void setMeetType(MeetType meetType) {
        this.meetType = meetType;
    }

}
