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
import org.aquastarz.score.controller.ScoreController;
import org.aquastarz.score.domain.Figure;
import org.aquastarz.score.domain.FigureScore;
import org.aquastarz.score.domain.FiguresParticipant;

public class FiguresMeetSheet implements Comparable {
    String firstName;
    String lastName;
    String levelName;
    Integer place;
    String team;
    BigDecimal score;
    String figuresOrder;
    Integer leagueNum;
    String figure1;
    BigDecimal dd1;
    BigDecimal j11;
    BigDecimal j21;
    BigDecimal j31;
    BigDecimal j41;
    BigDecimal j51;
    BigDecimal score1;
    BigDecimal penalty1;
    String figure2;
    BigDecimal dd2;
    BigDecimal j12=null;
    BigDecimal j22=null;
    BigDecimal j42;
    BigDecimal j52;
    BigDecimal j32;
    BigDecimal penalty2;
    BigDecimal score2;
    String figure3;
    BigDecimal dd3;
    BigDecimal j13;
    BigDecimal j23;
    BigDecimal j33;
    BigDecimal j43;
    BigDecimal j53;
    BigDecimal penalty3;
    BigDecimal score3;
    String figure4;
    BigDecimal dd4;
    BigDecimal j14;
    BigDecimal j24;
    BigDecimal j34;
    BigDecimal j44;
    BigDecimal j54;
    BigDecimal penalty4;
    BigDecimal score4;
    BigDecimal points;
    String sortKey;

    public FiguresMeetSheet(FiguresParticipant fp) {
        firstName = fp.getSwimmer().getFirstName();
        lastName = fp.getSwimmer().getLastName();
        levelName = fp.getSwimmer().getLevel().getName();
        place = fp.getPlace();
        team = fp.getSwimmer().getTeam().getTeamId();
        score = fp.getTotalScore();
        figuresOrder = fp.getFigureOrder();
        leagueNum = fp.getSwimmer().getLeagueNum();
        points = fp.getPoints();
        for(FigureScore fs:fp.getFiguresScores()) {
            Figure figure = ScoreController.getFigure(fs);
            switch(fs.getStation()) {
                case 1:
                    figure1 = figure.getName();
                    dd1 = figure.getDegreeOfDifficulty();
                    j11 = fs.getScore1();
                    j21 = fs.getScore2();
                    j31 = fs.getScore3();
                    j41 = fs.getScore4();
                    j51 = fs.getScore5();
                    penalty1 = fs.getPenalty();
                    score1 = fs.getTotalScore();
                    break;
                case 2:
                    figure2 = figure.getName();
                    dd2 = figure.getDegreeOfDifficulty();
                    j12 = fs.getScore1();
                    j22 = fs.getScore2();
                    j32 = fs.getScore3();
                    j42 = fs.getScore4();
                    j52 = fs.getScore5();
                    penalty2 = fs.getPenalty();
                    score2 = fs.getTotalScore();
                    break;
                case 3:
                    figure3 = figure.getName();
                    dd3 = figure.getDegreeOfDifficulty();
                    j13 = fs.getScore1();
                    j23 = fs.getScore2();
                    j33 = fs.getScore3();
                    j43 = fs.getScore4();
                    j53 = fs.getScore5();
                    penalty3 = fs.getPenalty();
                    score3 = fs.getTotalScore();
                    break;
                case 4:
                    figure4 = figure.getName();
                    dd4 = figure.getDegreeOfDifficulty();
                    j14 = fs.getScore1();
                    j24 = fs.getScore2();
                    j34 = fs.getScore3();
                    j44 = fs.getScore4();
                    j54 = fs.getScore5();
                    penalty4 = fs.getPenalty();
                    score4 = fs.getTotalScore();
                    break;
            }
        }
        sortKey = team + String.format("%03d%03d", fp.getSwimmer().getLevel().getSortOrder(),fp.getPlace());
    }

    public BigDecimal getDd1() {
        return dd1;
    }

    public void setDd1(BigDecimal dd1) {
        this.dd1 = dd1;
    }

    public BigDecimal getDd2() {
        return dd2;
    }

    public void setDd2(BigDecimal dd2) {
        this.dd2 = dd2;
    }

    public BigDecimal getDd3() {
        return dd3;
    }

    public void setDd3(BigDecimal dd3) {
        this.dd3 = dd3;
    }

    public BigDecimal getDd4() {
        return dd4;
    }

    public void setDd4(BigDecimal dd4) {
        this.dd4 = dd4;
    }

    public String getFigure1() {
        return figure1;
    }

    public void setFigure1(String figure1) {
        this.figure1 = figure1;
    }

    public String getFigure2() {
        return figure2;
    }

    public void setFigure2(String figure2) {
        this.figure2 = figure2;
    }

    public String getFigure3() {
        return figure3;
    }

    public void setFigure3(String figure3) {
        this.figure3 = figure3;
    }

    public String getFigure4() {
        return figure4;
    }

    public void setFigure4(String figure4) {
        this.figure4 = figure4;
    }

    public String getFiguresOrder() {
        return figuresOrder;
    }

    public void setFiguresOrder(String figuresOrder) {
        this.figuresOrder = figuresOrder;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public BigDecimal getJ11() {
        return j11;
    }

    public void setJ11(BigDecimal j11) {
        this.j11 = j11;
    }

    public BigDecimal getJ12() {
        return j12;
    }

    public void setJ12(BigDecimal j12) {
        this.j12 = j12;
    }

    public BigDecimal getJ13() {
        return j13;
    }

    public void setJ13(BigDecimal j13) {
        this.j13 = j13;
    }

    public BigDecimal getJ14() {
        return j14;
    }

    public void setJ14(BigDecimal j14) {
        this.j14 = j14;
    }

    public BigDecimal getJ21() {
        return j21;
    }

    public void setJ21(BigDecimal j21) {
        this.j21 = j21;
    }

    public BigDecimal getJ22() {
        return j22;
    }

    public void setJ22(BigDecimal j22) {
        this.j22 = j22;
    }

    public BigDecimal getJ23() {
        return j23;
    }

    public void setJ23(BigDecimal j23) {
        this.j23 = j23;
    }

    public BigDecimal getJ24() {
        return j24;
    }

    public void setJ24(BigDecimal j24) {
        this.j24 = j24;
    }

    public BigDecimal getJ31() {
        return j31;
    }

    public void setJ31(BigDecimal j31) {
        this.j31 = j31;
    }

    public BigDecimal getJ32() {
        return j32;
    }

    public void setJ32(BigDecimal j32) {
        this.j32 = j32;
    }

    public BigDecimal getJ33() {
        return j33;
    }

    public void setJ33(BigDecimal j33) {
        this.j33 = j33;
    }

    public BigDecimal getJ34() {
        return j34;
    }

    public void setJ34(BigDecimal j34) {
        this.j34 = j34;
    }

    public BigDecimal getJ41() {
        return j41;
    }

    public void setJ41(BigDecimal j41) {
        this.j41 = j41;
    }

    public BigDecimal getJ42() {
        return j42;
    }

    public void setJ42(BigDecimal j42) {
        this.j42 = j42;
    }

    public BigDecimal getJ43() {
        return j43;
    }

    public void setJ43(BigDecimal j43) {
        this.j43 = j43;
    }

    public BigDecimal getJ44() {
        return j44;
    }

    public void setJ44(BigDecimal j44) {
        this.j44 = j44;
    }

    public BigDecimal getJ51() {
        return j51;
    }

    public void setJ51(BigDecimal j51) {
        this.j51 = j51;
    }

    public BigDecimal getJ52() {
        return j52;
    }

    public void setJ52(BigDecimal j52) {
        this.j52 = j52;
    }

    public BigDecimal getJ53() {
        return j53;
    }

    public void setJ53(BigDecimal j53) {
        this.j53 = j53;
    }

    public BigDecimal getJ54() {
        return j54;
    }

    public void setJ54(BigDecimal j54) {
        this.j54 = j54;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getLeagueNum() {
        return leagueNum;
    }

    public void setLeagueNum(Integer leagueNum) {
        this.leagueNum = leagueNum;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public BigDecimal getPenalty1() {
        return penalty1;
    }

    public void setPenalty1(BigDecimal penalty1) {
        this.penalty1 = penalty1;
    }

    public BigDecimal getPenalty2() {
        return penalty2;
    }

    public void setPenalty2(BigDecimal penalty2) {
        this.penalty2 = penalty2;
    }

    public BigDecimal getPenalty3() {
        return penalty3;
    }

    public void setPenalty3(BigDecimal penalty3) {
        this.penalty3 = penalty3;
    }

    public BigDecimal getPenalty4() {
        return penalty4;
    }

    public void setPenalty4(BigDecimal penalty4) {
        this.penalty4 = penalty4;
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

    public BigDecimal getScore1() {
        return score1;
    }

    public void setScore1(BigDecimal score1) {
        this.score1 = score1;
    }

    public BigDecimal getScore2() {
        return score2;
    }

    public void setScore2(BigDecimal score2) {
        this.score2 = score2;
    }

    public BigDecimal getScore3() {
        return score3;
    }

    public void setScore3(BigDecimal score3) {
        this.score3 = score3;
    }

    public BigDecimal getScore4() {
        return score4;
    }

    public void setScore4(BigDecimal score4) {
        this.score4 = score4;
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
        if(o==null) throw new NullPointerException("Cannot compareTo null");
        if(!(o instanceof FiguresMeetSheet)) throw new ClassCastException("Cannot compareTo");
        FiguresMeetSheet fms = (FiguresMeetSheet)o;
        return sortKey.compareTo(fms.sortKey);
    }

}
