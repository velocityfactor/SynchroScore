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

public class SwimmersTableModel implements TableModel {

    class TableItem {

        String leagueNum;
        String lastName;
        String firstName;
        String team;
        String level;

        TableItem(String leagueNum, String lastName, String firstName, String team, String level) {
            this.leagueNum = leagueNum;
            this.lastName = lastName;
            this.firstName = firstName;
            this.team = team;
            this.level = level;
        }
    }
    List<TableItem> items = new ArrayList<TableItem>();

    public SwimmersTableModel(Collection<Swimmer> swimmers) {
        for (Swimmer swimmer : swimmers) {
            TableItem item = new TableItem(swimmer.getSwimmerId().toString(), swimmer.getLastName(), swimmer.getFirstName(), swimmer.getTeam().getTeamId(), swimmer.getLevel().toString());
            items.add(item);
        }
    }

    public int getRowCount() {
        return items.size();
    }

    public int getColumnCount() {
        return 5;
    }

    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "League #";
            case 1:
                return "Last Name";
            case 2:
                return "First Name";
            case 3:
                return "Team";
            case 4:
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
                return item.leagueNum;
            case 1:
                return item.lastName;
            case 2:
                return item.firstName;
            case 3:
                return item.team;
            case 4:
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
