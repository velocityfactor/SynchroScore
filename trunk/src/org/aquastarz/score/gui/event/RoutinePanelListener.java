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
package org.aquastarz.score.gui.event;

import org.aquastarz.score.domain.Routine;

public interface RoutinePanelListener {
    public void addRoutine();
    public void deleteRoutine(Routine routine);
    public void randomizeRoutines();
    public void printRoutines();
    public void saveRoutine(Routine routine);
}
