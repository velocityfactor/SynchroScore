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
package org.aquastarz.score;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class LoggingExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(LoggingExceptionHandler.class.getName());

    public void uncaughtException(final Thread t, final Throwable e) {
        logger.fatal("Unhandled on "+t.getName(),e);
        if (SwingUtilities.isEventDispatchThread()) {
            showException(t, e);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    showException(t, e);
                }
            });
        }
    }

    private void showException(Thread t, Throwable e) {
        String msg = String.format("ERROR: You should close and restart the program.  There was an unexpected problem.");

        // note: in a real app, you should locate the currently focused frame
        // or dialog and use it as the parent. In this example, I'm just passing
        // a null owner, which means this dialog may get buried behind
        // some other screen.
        JOptionPane.showMessageDialog(null, msg);
    }
}