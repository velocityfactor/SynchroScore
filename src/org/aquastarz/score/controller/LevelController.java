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

import java.util.List;
import javax.persistence.Query;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Level;

public class LevelController {

    public static Level findById(String levelid) {
        return ScoreApp.getEntityManager().find(Level.class, levelid);
    }

    public static int getNextSortOrder() {
        List<Level> levels = findAllOrderBySortOrder();
        if (levels != null && levels.size() > 0) {
            return levels.get(levels.size() - 1).getSortOrder() + 1;
        } else {
            return 1;
        }
    }

    public static List<Level> findAllOrderBySortOrder() {
        Query query = ScoreApp.getEntityManager().createNamedQuery("Level.findAllInOrder");
        return query.getResultList();
    }
}
