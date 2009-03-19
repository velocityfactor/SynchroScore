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
package org.aquastarz.score.gui;

import java.math.BigDecimal;

public class FigureScoreBean {
    public static final BigDecimal PENALTY2=new BigDecimal(2);
    public static final BigDecimal PENALTY1=new BigDecimal(1);
    public static final BigDecimal PENALTYHALF=new BigDecimal(0.5);
    public static final BigDecimal PENALTY0=new BigDecimal(0);

    private BigDecimal scoreS1J1;
    private BigDecimal scoreS1J2;
    private BigDecimal scoreS1J3;
    private BigDecimal scoreS1J4;
    private BigDecimal scoreS1J5;
    private BigDecimal penaltyS1=PENALTY0;
    private BigDecimal dd1,raw1,total1,penalty1;

    public BigDecimal getDd1() {
        return dd1;
    }

    public void setDd1(BigDecimal dd1) {
        this.dd1 = dd1;
    }

    public int getFigure1() {
        return figure1;
    }

    public void setFigure1(int figure1) {
        this.figure1 = figure1;
    }

    public BigDecimal getPenalty1() {
        return penalty1;
    }

    public void setPenalty1(BigDecimal penalty1) {
        this.penalty1 = penalty1;
    }

    public BigDecimal getRaw1() {
        return raw1;
    }

    public void setRaw1(BigDecimal raw1) {
        this.raw1 = raw1;
    }

    public BigDecimal getTotal1() {
        return total1;
    }

    public void setTotal1(BigDecimal total1) {
        this.total1 = total1;
    }
    private int figure1;

    public BigDecimal getScoreS1J1() {
        return scoreS1J1;
    }

    public void setScoreS1J1(BigDecimal score) {
        scoreS1J1=score;
    }

    public BigDecimal getScoreS1J2() {
        return scoreS1J2;
    }

    public void setScoreS1J2(BigDecimal score) {
        scoreS1J2=score;
    }

    public BigDecimal getScoreS1J3() {
        return scoreS1J3;
    }

    public void setScoreS1J3(BigDecimal score) {
        scoreS1J3=score;
    }

    public BigDecimal getScoreS1J4() {
        return scoreS1J4;
    }

    public void setScoreS1J4(BigDecimal score) {
        scoreS1J4=score;
    }

    public BigDecimal getScoreS1J5() {
        return scoreS1J5;
    }

    public void setScoreS1J5(BigDecimal score) {
        scoreS1J5=score;
    }

    public BigDecimal getPenaltyS1() {
        return penaltyS1;
    }

    public void setPenaltyS1(BigDecimal penalty) {
        penaltyS1=penalty;
    }

    public void setPenalty2S1(boolean b) {
        if(b) setPenaltyS1(PENALTY2);
        else if(penaltyS1==PENALTY2) setPenaltyS1(PENALTY0);
    }

    public boolean getPenalty2S1() {
        return (penaltyS1==PENALTY2);
    }

    public void setPenalty1S1(boolean b) {
        if(b) setPenaltyS1(PENALTY1);
        else if(penaltyS1==PENALTY1) setPenaltyS1(PENALTY0);
    }

    public boolean getPenalty1S1() {
        return (penaltyS1==PENALTY1);
    }

    public void setPenaltyHS1(boolean b) {
        if(b) setPenaltyS1(PENALTYHALF);
        else if(penaltyS1==PENALTYHALF) setPenaltyS1(PENALTY0);
    }

    public boolean getPenaltyHS1() {
        return (penaltyS1==PENALTYHALF);
    }

    private BigDecimal scoreS2J1;
    private BigDecimal scoreS2J2;
    private BigDecimal scoreS2J3;
    private BigDecimal scoreS2J4;
    private BigDecimal scoreS2J5;
    private BigDecimal penaltyS2=PENALTY0;
    private BigDecimal dd2,raw2,total2,penalty2;
    private int figure2;

    public BigDecimal getDd2() {
        return dd2;
    }

    public void setDd2(BigDecimal dd2) {
        this.dd2 = dd2;
    }

    public int getFigure2() {
        return figure2;
    }

    public void setFigure2(int figure2) {
        this.figure2 = figure2;
    }

    public BigDecimal getPenalty2() {
        return penalty2;
    }

    public void setPenalty2(BigDecimal penalty2) {
        this.penalty2 = penalty2;
    }

    public BigDecimal getRaw2() {
        return raw2;
    }

    public void setRaw2(BigDecimal raw2) {
        this.raw2 = raw2;
    }

    public BigDecimal getTotal2() {
        return total2;
    }

    public void setTotal2(BigDecimal total2) {
        this.total2 = total2;
    }

    public BigDecimal getScoreS2J1() {
        return scoreS2J1;
    }

    public void setScoreS2J1(BigDecimal score) {
        scoreS2J1=score;
    }

    public BigDecimal getScoreS2J2() {
        return scoreS2J2;
    }

    public void setScoreS2J2(BigDecimal score) {
        scoreS2J2=score;
    }

    public BigDecimal getScoreS2J3() {
        return scoreS2J3;
    }

    public void setScoreS2J3(BigDecimal score) {
        scoreS2J3=score;
    }

    public BigDecimal getScoreS2J4() {
        return scoreS2J4;
    }

    public void setScoreS2J4(BigDecimal score) {
        scoreS2J4=score;
    }

    public BigDecimal getScoreS2J5() {
        return scoreS2J5;
    }

    public void setScoreS2J5(BigDecimal score) {
        scoreS2J5=score;
    }

    public BigDecimal getPenaltyS2() {
        return penaltyS2;
    }

    public void setPenaltyS2(BigDecimal penalty) {
        penaltyS2=penalty;
    }

    public void setPenalty2S2(boolean b) {
        if(b) setPenaltyS2(PENALTY2);
        else if(penaltyS2==PENALTY2) setPenaltyS2(PENALTY0);
    }

    public boolean getPenalty2S2() {
        return (penaltyS2==PENALTY2);
    }

    public void setPenalty1S2(boolean b) {
        if(b) setPenaltyS2(PENALTY1);
        else if(penaltyS2==PENALTY1) setPenaltyS2(PENALTY0);
    }

    public boolean getPenalty1S2() {
        return (penaltyS2==PENALTY1);
    }

    public void setPenaltyHS2(boolean b) {
        if(b) setPenaltyS2(PENALTYHALF);
        else if(penaltyS2==PENALTYHALF) setPenaltyS2(PENALTY0);
    }

    public boolean getPenaltyHS2() {
        return (penaltyS2==PENALTYHALF);
    }

    private BigDecimal scoreS3J1;
    private BigDecimal scoreS3J2;
    private BigDecimal scoreS3J3;
    private BigDecimal scoreS3J4;
    private BigDecimal scoreS3J5;
    private BigDecimal penaltyS3=PENALTY0;
    private BigDecimal dd3,raw3,total3,penalty3;
    private int figure3;

    public BigDecimal getDd3() {
        return dd3;
    }

    public void setDd3(BigDecimal dd3) {
        this.dd3 = dd3;
    }

    public int getFigure3() {
        return figure3;
    }

    public void setFigure3(int figure3) {
        this.figure3 = figure3;
    }

    public BigDecimal getPenalty3() {
        return penalty3;
    }

    public void setPenalty3(BigDecimal penalty3) {
        this.penalty3 = penalty3;
    }

    public BigDecimal getRaw3() {
        return raw3;
    }

    public void setRaw3(BigDecimal raw3) {
        this.raw3 = raw3;
    }

    public BigDecimal getTotal3() {
        return total3;
    }

    public void setTotal3(BigDecimal total3) {
        this.total3 = total3;
    }

    public BigDecimal getScoreS3J1() {
        return scoreS3J1;
    }

    public void setScoreS3J1(BigDecimal score) {
        scoreS3J1=score;
    }

    public BigDecimal getScoreS3J2() {
        return scoreS3J2;
    }

    public void setScoreS3J2(BigDecimal score) {
        scoreS3J2=score;
    }

    public BigDecimal getScoreS3J3() {
        return scoreS3J3;
    }

    public void setScoreS3J3(BigDecimal score) {
        scoreS3J3=score;
    }

    public BigDecimal getScoreS3J4() {
        return scoreS3J4;
    }

    public void setScoreS3J4(BigDecimal score) {
        scoreS3J4=score;
    }

    public BigDecimal getScoreS3J5() {
        return scoreS3J5;
    }

    public void setScoreS3J5(BigDecimal score) {
        scoreS3J5=score;
    }

    public BigDecimal getPenaltyS3() {
        return penaltyS3;
    }

    public void setPenaltyS3(BigDecimal penalty) {
        penaltyS3=penalty;
    }

    public void setPenalty2S3(boolean b) {
        if(b) setPenaltyS3(PENALTY2);
        else if(penaltyS3==PENALTY2) setPenaltyS3(PENALTY0);
    }

    public boolean getPenalty2S3() {
        return (penaltyS3==PENALTY2);
    }

    public void setPenalty1S3(boolean b) {
        if(b) setPenaltyS3(PENALTY1);
        else if(penaltyS3==PENALTY1) setPenaltyS3(PENALTY0);
    }

    public boolean getPenalty1S3() {
        return (penaltyS3==PENALTY1);
    }

    public void setPenaltyHS3(boolean b) {
        if(b) setPenaltyS3(PENALTYHALF);
        else if(penaltyS3==PENALTYHALF) setPenaltyS3(PENALTY0);
    }

    public boolean getPenaltyHS3() {
        return (penaltyS3==PENALTYHALF);
    }

    private BigDecimal scoreS4J1;
    private BigDecimal scoreS4J2;
    private BigDecimal scoreS4J3;
    private BigDecimal scoreS4J4;
    private BigDecimal scoreS4J5;
    private BigDecimal penaltyS4=PENALTY0;
    private BigDecimal dd4,raw4,total4,penalty4;
    private int figure4;

    public BigDecimal getDd4() {
        return dd4;
    }

    public void setDd4(BigDecimal dd4) {
        this.dd4 = dd4;
    }

    public int getFigure4() {
        return figure4;
    }

    public void setFigure4(int figure4) {
        this.figure4 = figure4;
    }

    public BigDecimal getPenalty4() {
        return penalty4;
    }

    public void setPenalty4(BigDecimal penalty4) {
        this.penalty4 = penalty4;
    }

    public BigDecimal getRaw4() {
        return raw4;
    }

    public void setRaw4(BigDecimal raw4) {
        this.raw4 = raw4;
    }

    public BigDecimal getTotal4() {
        return total4;
    }

    public void setTotal4(BigDecimal total4) {
        this.total4 = total4;
    }

    public BigDecimal getScoreS4J1() {
        return scoreS4J1;
    }

    public void setScoreS4J1(BigDecimal score) {
        scoreS4J1=score;
    }

    public BigDecimal getScoreS4J2() {
        return scoreS4J2;
    }

    public void setScoreS4J2(BigDecimal score) {
        scoreS4J2=score;
    }

    public BigDecimal getScoreS4J3() {
        return scoreS4J3;
    }

    public void setScoreS4J3(BigDecimal score) {
        scoreS4J3=score;
    }

    public BigDecimal getScoreS4J4() {
        return scoreS4J4;
    }

    public void setScoreS4J4(BigDecimal score) {
        scoreS4J4=score;
    }

    public BigDecimal getScoreS4J5() {
        return scoreS4J5;
    }

    public void setScoreS4J5(BigDecimal score) {
        scoreS4J5=score;
    }

    public BigDecimal getPenaltyS4() {
        return penaltyS4;
    }

    public void setPenaltyS4(BigDecimal penalty) {
        penaltyS4=penalty;
    }

    public void setPenalty2S4(boolean b) {
        if(b) setPenaltyS4(PENALTY2);
        else if(penaltyS4==PENALTY2) setPenaltyS4(PENALTY0);
    }

    public boolean getPenalty2S4() {
        return (penaltyS4==PENALTY2);
    }

    public void setPenalty1S4(boolean b) {
        if(b) setPenaltyS4(PENALTY1);
        else if(penaltyS4==PENALTY1) setPenaltyS4(PENALTY0);
    }

    public boolean getPenalty1S4() {
        return (penaltyS4==PENALTY1);
    }

    public void setPenaltyHS4(boolean b) {
        if(b) setPenaltyS4(PENALTYHALF);
        else if(penaltyS4==PENALTYHALF) setPenaltyS4(PENALTY0);
    }

    public boolean getPenaltyHS4() {
        return (penaltyS4==PENALTYHALF);
    }
}
