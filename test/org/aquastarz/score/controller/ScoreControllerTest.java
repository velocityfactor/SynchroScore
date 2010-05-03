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
package org.aquastarz.score.controller;

import java.util.ArrayList;
import java.util.List;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.config.Bootstrap;
import org.aquastarz.score.config.Bootstrap.FigureScoreTracker;
import org.aquastarz.score.config.Bootstrap.FiguresParticipantTracker;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScoreControllerTest {

    static List<FigureScoreTracker> figureScoreTrackers;
    static List<FiguresParticipantTracker> figuresParticipantTrackers;

    public ScoreControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        Bootstrap.loadLeagueData();
        Bootstrap.loadRoster(ScoreApp.getCurrentSeason());
        List<Object> trackers = Bootstrap.loadLegacyMeet(ScoreApp.getCurrentSeason(), "DAVSUN");
        figureScoreTrackers = (List<FigureScoreTracker>) trackers.get(0);
        figuresParticipantTrackers = (List<FiguresParticipantTracker>) trackers.get(1);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTotalScore() {
        assertTrue(figureScoreTrackers.size() > 0);
        for (FigureScoreTracker fst : figureScoreTrackers) {
            assertTrue(fst.total.compareTo(fst.figureScore.getTotalScore()) == 0);
        }
    }

    @Test
    public void testCalculateTotalScore() {
        assertTrue(figuresParticipantTrackers.size() > 0);
        for (FiguresParticipantTracker fpt : figuresParticipantTrackers) {
            assertTrue(fpt.total.compareTo(fpt.figuresParticipant.getTotalScore()) == 0);
            assertEquals(fpt.place, fpt.figuresParticipant.getPlace().intValue());
            assertTrue(fpt.points.compareTo(fpt.figuresParticipant.getPoints()) == 0);
        }
    }
}
