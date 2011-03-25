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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.RoutineLevel;

public class RoutineLevelManager {

    public static List<RoutineLevel> findAll() {
        Query query = ScoreApp.getEntityManager().createNamedQuery("RoutineLevel.findAllInOrder");
        List<RoutineLevel> allRoutineLevels = query.getResultList();
        ArrayList<RoutineLevel> routineLevels = new ArrayList<RoutineLevel>();
        for (RoutineLevel rl : allRoutineLevels) {
            if (ScoreApp.getCurrentSeason().getRulesRevision() < 2) {
                if ("N12U".equals(rl.getLevelId()) || "N13O".equals(rl.getLevelId()) || "I11".equals(rl.getLevelId()) || "I15".equals(rl.getLevelId()) || "I11T".equals(rl.getLevelId())) {
                    routineLevels.add(rl);
                }
            } else {
                if ("N12U".equals(rl.getLevelId()) || "N13O".equals(rl.getLevelId()) || "I11".equals(rl.getLevelId()) || "I15".equals(rl.getLevelId()) || "I11-14T".equals(rl.getLevelId()) || "I15-18T".equals(rl.getLevelId()) || "I11-18CT".equals(rl.getLevelId())) {
                    routineLevels.add(rl);
                }
            }
        }
        return routineLevels;
    }

    public static RoutineLevel find(String id) {
        return (RoutineLevel) ScoreApp.getEntityManager().find(RoutineLevel.class, id);
    }
}
