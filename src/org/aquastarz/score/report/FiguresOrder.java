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
import java.util.Collection;
import java.util.List;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Swimmer;

public class FiguresOrder {

    String figuresOrder;
    String leagueNum;
    String lastName;
    String firstName;
    String team;
    String level;
    boolean hasLineBelow;

    FiguresOrder(String figuresOrder, String leagueNum, String lastName, String firstName, String team, String level) {
        this.figuresOrder = figuresOrder;
        this.leagueNum = leagueNum;
        this.lastName = lastName;
        this.firstName = firstName;
        this.team = team;
        this.level = level;
        hasLineBelow=false;
    }

    public static List<FiguresOrder> getList(Collection<FiguresParticipant> figuresParticipants) {
        List<FiguresOrder> items = new ArrayList<FiguresOrder>();
        for (FiguresParticipant fp : figuresParticipants) {
            Swimmer s = fp.getSwimmer();
            FiguresOrder item = new FiguresOrder(fp.getFigureOrder(), s.getLeagueNum().toString(), s.getLastName(), s.getFirstName(), s.getTeam().getTeamId(), s.getLevel().toString());
            items.add(item);
        }
        return items;
    }
}
