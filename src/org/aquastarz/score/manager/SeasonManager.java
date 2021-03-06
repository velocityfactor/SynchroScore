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

import javax.persistence.EntityManager;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.controller.ScoreController;
import org.aquastarz.score.domain.Season;

public class SeasonManager {

    public static Season findOrCreate(String name) {
        EntityManager entityManager = ScoreApp.getEntityManager();
        Season season = ScoreController.getSeason(name);
        if (season == null) {
            season = new Season(name);
            entityManager.getTransaction().begin();
            entityManager.persist(season);
            entityManager.getTransaction().commit();
        }
        return season;
    }

    public static Season find(String name) {
        return ScoreController.getSeason(name);
    }
}
