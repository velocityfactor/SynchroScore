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

/**
 *
 * @author Shayne Hughes
 */
public class TwoDigitScore {

    public static BigDecimal convert(String s) {
        BigDecimal score = null;
        s = s.replaceAll("[^0-9]", "");
        if (s.length() > 2) {
            s = s.substring(0, 2);
        }
        if (!"".equals(s) && s.length() == 2) {
            score = new BigDecimal(s);
            score = score.movePointLeft(1);
        }
        return score;
    }
}
