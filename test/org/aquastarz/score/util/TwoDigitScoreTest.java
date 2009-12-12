/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.aquastarz.score.util;

import java.math.BigDecimal;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author shayne
 */
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
        System.out.println("convertScore");
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