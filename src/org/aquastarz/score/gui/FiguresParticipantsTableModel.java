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
package org.aquastarz.score.gui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Swimmer;

public class FiguresParticipantsTableModel implements TableModel {
    public final static int FIGURES_ORDER_COL=0;
    public final static int LEVEL_COL=5;

    class TableItem {
        String figuresOrder;
        String leagueNum;
        String lastName;
        String firstName;
        String team;
        String level;

        TableItem(String figuresOrder, String leagueNum, String lastName, String firstName, String team, String level) {
            this.figuresOrder = figuresOrder;
            this.leagueNum = leagueNum;
            this.lastName = lastName;
            this.firstName = firstName;
            this.team = team;
            this.level = level;
        }
    }
    List<TableItem> items = new ArrayList<TableItem>();

    public FiguresParticipantsTableModel(Collection<FiguresParticipant> figuresParticipants) {
        for (FiguresParticipant fp : figuresParticipants) {
            Swimmer s = fp.getSwimmer();
            TableItem item = new TableItem(fp.getFigureOrder(), s.getLeagueNum().toString(), s.getLastName(), s.getFirstName(), s.getTeam().getTeamId(), s.getLevel().toString());
            items.add(item);
        }
    }

    public int getRowCount() {
        return items.size();
    }

    public int getColumnCount() {
        return 6;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Figures Order";
            case 1:
                return "League #";
            case 2:
                return "Last Name";
            case 3:
                return "First Name";
            case 4:
                return "Team";
            case 5:
                return "Level";
        }
        return "";
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        TableItem item = items.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return item.figuresOrder;
            case 1:
                return item.leagueNum;
            case 2:
                return item.lastName;
            case 3:
                return item.firstName;
            case 4:
                return item.team;
            case 5:
                return item.level;
        }
        return "";
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public void addTableModelListener(TableModelListener l) {
        //We don't update, so ignore listeners.
    }

    public void removeTableModelListener(TableModelListener l) {
        //Do nothing.
    }
}
