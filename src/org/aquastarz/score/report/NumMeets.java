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

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Season;
import org.aquastarz.score.domain.Swimmer;

public class NumMeets {

    private Swimmer swimmer;
    private Long count;

    public NumMeets(Swimmer swimmer, Long count) {
        this.swimmer = swimmer;
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Swimmer getSwimmer() {
        return swimmer;
    }

    public void setSwimmer(Swimmer swimmer) {
        this.swimmer = swimmer;
    }

    public static List<NumMeets> generateList(List<Swimmer> swimmers, Season season) {
        EntityManager entityManager = ScoreApp.getEntityManager();
        Query numMeetsQuery = entityManager.createNamedQuery("FiguresParticipant.countBySeasonAndSwimmerAndHasScore");
        ArrayList<NumMeets> numMeetsList = new ArrayList<NumMeets>();
        for(Swimmer swimmer:swimmers) {
            numMeetsQuery.setParameter("swimmer", swimmer);
            numMeetsQuery.setParameter("season", season);
            Long count = (Long) numMeetsQuery.getSingleResult();
            numMeetsList.add(new NumMeets(swimmer,count));
        }
        return numMeetsList;
    }
}
