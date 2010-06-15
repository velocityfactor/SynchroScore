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

import java.util.Vector;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.gui.event.FiguresParticipantSearchPanelListener;

public class FiguresParticipantSearchPanel extends javax.swing.JPanel {

    Vector<FiguresParticipantSearchPanelListener> listeners = new Vector<FiguresParticipantSearchPanelListener>();

    /** Creates new form SwimmerSearchPanel */
    public FiguresParticipantSearchPanel() {
        initComponents();
        clear();
    }

    public void addFiguresParticipantSearchPanelListener(FiguresParticipantSearchPanelListener listener) {
        listeners.add(listener);
    }

    private void fireFiguresParticipantSearchRequested(String figureOrder) {
        for (FiguresParticipantSearchPanelListener listener : listeners) {
            String s = figureOrder.replaceAll("\\.", "");
            this.figureOrder.setText(s);
            listener.figuresParticipantSearchRequested(s);
        }
    }

    private void fireFiguresParticipantSet() {
        for (FiguresParticipantSearchPanelListener listener : listeners) {
            listener.figuresParticipantSet();
        }
    }

    public void clear() {
        figureOrder.setText("");
        leagueSwimmerNumber.setText("");
        swimmerName.setText("");
        swimmerLevel.setText("");
        swimmerTeam.setText("");
    }

    public void setFiguresParticipant(FiguresParticipant figuresParticipant) {
        figureOrder.setText(figuresParticipant.getFigureOrder());
        Swimmer s = figuresParticipant.getSwimmer();
        leagueSwimmerNumber.setText(s.getSwimmerId().toString());
        swimmerLevel.setText(s.getLevel().getLevelId());
        swimmerName.setText(s.getLastName() + ", " + s.getFirstName());
        swimmerTeam.setText(s.getTeam().getTeamId());
        fireFiguresParticipantSet();
    }

    public void focus() {
        this.requestFocusInWindow();
        figureOrder.requestFocusInWindow();
        figureOrder.selectAll();
    }

    private void figureOrderEntered() {
        figureOrder.setText(figureOrder.getText().toUpperCase());
        fireFiguresParticipantSearchRequested(figureOrder.getText());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        meetLabel = new javax.swing.JLabel();
        figureOrder = new javax.swing.JTextField();
        leagueLabel = new javax.swing.JLabel();
        leagueSwimmerNumber = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        swimmerName = new javax.swing.JLabel();
        teamLabel = new javax.swing.JLabel();
        swimmerTeam = new javax.swing.JLabel();
        levelLabel = new javax.swing.JLabel();
        swimmerLevel = new javax.swing.JLabel();

        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        meetLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        meetLabel.setText("Meet #:");

        figureOrder.setFont(new java.awt.Font("Tahoma", 0, 14));
        figureOrder.setText("99");
        figureOrder.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                figureOrderMouseClicked(evt);
            }
        });
        figureOrder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                figureOrderKeyPressed(evt);
            }
        });

        leagueLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        leagueLabel.setText("League #:");

        leagueSwimmerNumber.setBackground(new java.awt.Color(255, 255, 255));
        leagueSwimmerNumber.setFont(new java.awt.Font("Tahoma", 0, 14));
        leagueSwimmerNumber.setText("999");
        leagueSwimmerNumber.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        leagueSwimmerNumber.setOpaque(true);

        nameLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        nameLabel.setText("Name:");

        swimmerName.setBackground(java.awt.SystemColor.text);
        swimmerName.setFont(new java.awt.Font("Tahoma", 0, 14));
        swimmerName.setText("Swimmer, Suzie");
        swimmerName.setOpaque(true);

        teamLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        teamLabel.setText("Team:");

        swimmerTeam.setBackground(java.awt.SystemColor.text);
        swimmerTeam.setFont(new java.awt.Font("Tahoma", 0, 14));
        swimmerTeam.setText("ABC");
        swimmerTeam.setOpaque(true);

        levelLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        levelLabel.setText("Level:");

        swimmerLevel.setBackground(java.awt.SystemColor.text);
        swimmerLevel.setFont(new java.awt.Font("Tahoma", 0, 14));
        swimmerLevel.setText("Novice 9-10");
        swimmerLevel.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(meetLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(figureOrder, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(leagueLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(leagueSwimmerNumber, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(swimmerName, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(teamLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(swimmerTeam, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(levelLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(swimmerLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(meetLabel)
                    .addComponent(figureOrder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(leagueLabel)
                    .addComponent(leagueSwimmerNumber))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(swimmerName)
                    .addComponent(teamLabel)
                    .addComponent(swimmerTeam)
                    .addComponent(levelLabel)
                    .addComponent(swimmerLevel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void figureOrderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_figureOrderMouseClicked
        figureOrder.selectAll();
    }//GEN-LAST:event_figureOrderMouseClicked

    private void figureOrderKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_figureOrderKeyPressed
        if (evt.getKeyChar() == 10 || evt.getKeyChar() == 9) {
            figureOrderEntered();
        }
    }//GEN-LAST:event_figureOrderKeyPressed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        figureOrder.selectAll();
        figureOrder.requestFocus();
    }//GEN-LAST:event_formFocusGained
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField figureOrder;
    private javax.swing.JLabel leagueLabel;
    private javax.swing.JLabel leagueSwimmerNumber;
    private javax.swing.JLabel levelLabel;
    private javax.swing.JLabel meetLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel swimmerLevel;
    private javax.swing.JLabel swimmerName;
    private javax.swing.JLabel swimmerTeam;
    private javax.swing.JLabel teamLabel;
    // End of variables declaration//GEN-END:variables
}