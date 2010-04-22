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
package org.aquastarz.score.util;

import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TwoDigitScoreTest {

    public TwoDigitScoreTest() {
    }

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

    /**
     * Test of convertScore method, of class TwoDigitScore.
     */
    @Test
    public void testConvert() {
        String s = "00";
        BigDecimal expResult = new BigDecimal("0.0");
        BigDecimal result = TwoDigitScore.convert(s);
        assertEquals(expResult, result);

        s = "15";
        expResult = new BigDecimal("1.5");
        result = TwoDigitScore.convert(s);
        assertEquals(expResult, result);

        s = "1.5";
        expResult = new BigDecimal("1.5");
        result = TwoDigitScore.convert(s);
        assertEquals(expResult, result);

        s = "1.54";
        expResult = new BigDecimal("1.5");
        result = TwoDigitScore.convert(s);
        assertEquals(expResult, result);

        s = "159X9";
        expResult = new BigDecimal("1.5");
        result = TwoDigitScore.convert(s);
        assertEquals(expResult, result);

        s = "xx";
        expResult = null;
        result = TwoDigitScore.convert(s);
        assertEquals(expResult, result);

        s = "xx99yy";
        expResult = new BigDecimal("9.9");
        result = TwoDigitScore.convert(s);
        assertEquals(expResult, result);
    }

}