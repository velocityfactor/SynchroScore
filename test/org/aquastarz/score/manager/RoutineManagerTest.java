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
package org.aquastarz.score.manager;

import java.math.BigDecimal;
import org.aquastarz.score.domain.Routine;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class RoutineManagerTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
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
    public void testCalculateRoutineBonus() {
        Routine routine = new Routine();
        routine.setRoutineType("Team");
        routine.setNumSwimmers(3);
        assertTrue(BigDecimal.valueOf(0.0).compareTo(RoutineManager.calculateRoutineBonus(routine))==0);
        routine.setNumSwimmers(4);
        assertTrue(BigDecimal.valueOf(0.0).compareTo(RoutineManager.calculateRoutineBonus(routine))==0);
        routine.setNumSwimmers(5);
        assertTrue(BigDecimal.valueOf(0.5).compareTo(RoutineManager.calculateRoutineBonus(routine))==0);
        routine.setNumSwimmers(6);
        assertTrue(BigDecimal.valueOf(1.0).compareTo(RoutineManager.calculateRoutineBonus(routine))==0);
        routine.setNumSwimmers(7);
        assertTrue(BigDecimal.valueOf(1.5).compareTo(RoutineManager.calculateRoutineBonus(routine))==0);
        routine.setNumSwimmers(8);
        assertTrue(BigDecimal.valueOf(2.0).compareTo(RoutineManager.calculateRoutineBonus(routine))==0);
        routine.setNumSwimmers(9);
        assertTrue(BigDecimal.valueOf(2.0).compareTo(RoutineManager.calculateRoutineBonus(routine))==0);
        routine.setNumSwimmers(10);
        assertTrue(BigDecimal.valueOf(2.0).compareTo(RoutineManager.calculateRoutineBonus(routine))==0);
    }
}
