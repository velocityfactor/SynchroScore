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
import org.aquastarz.score.domain.Season;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.domain.Team;

public class SwimmerDB {
    private static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(SwimmerDB.class.getName());

    public static Swimmer findByLeagueNum(Integer leagueNum, Season season) {
        Query swimmerQuery = ScoreApp.getEntityManager().createNamedQuery("Swimmer.findByLeagueNumAndSeason");
        swimmerQuery.setParameter("leagueNum", leagueNum);
        swimmerQuery.setParameter("season", season);
        Swimmer swimmer = null;
        try {
            swimmer = (Swimmer)swimmerQuery.getSingleResult();
        }
        catch(Exception e) {
            logger.error("Swimmer not found.  Season="+ScoreApp.getCurrentSeason().getSeasonId()+" leagueNum="+leagueNum,e);
        }
        return swimmer;
    }

    public static List<Swimmer> getSwimmers(Team team) {
        Query swimmerQuery = ScoreApp.getEntityManager().createNamedQuery("Swimmer.findByTeamIdAndSeasonOrderByName");
        swimmerQuery.setParameter("teamId", team.getTeamId());
        swimmerQuery.setParameter("season", ScoreApp.getCurrentSeason());
        List<Swimmer> swimmers = swimmerQuery.getResultList();
        return swimmers;
    }


}
