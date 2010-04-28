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
package org.aquastarz.score.db;

import java.util.List;
import javax.persistence.Query;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.domain.Team;

public class SwimmerDB {

    public static Swimmer findByLeagueNum(Integer leagueNum) {
        Query swimmerQuery = ScoreApp.getEntityManager().createNamedQuery("Swimmer.findByLeagueNumAndSeason");
        swimmerQuery.setParameter("leagueNum", leagueNum);
        swimmerQuery.setParameter("seasonId", ScoreApp.getCurrentSeason().getSeasonId());
        return (Swimmer)swimmerQuery.getSingleResult();
    }

    public static List<Swimmer> getSwimmers(Team team) {
        Query swimmerQuery = ScoreApp.getEntityManager().createNamedQuery("Swimmer.findByTeamAndSeasonOrderByName");
        swimmerQuery.setParameter("teamId", team.getTeamId());
        swimmerQuery.setParameter("seasonId", ScoreApp.getCurrentSeason().getSeasonId());
        List<Swimmer> swimmers = swimmerQuery.getResultList();
        return swimmers;
    }


}
