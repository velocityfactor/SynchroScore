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

import javax.swing.JOptionPane;

public class MaintenanceFrame extends javax.swing.JFrame {
    private static MaintenanceFrame instance;

    /** Creates new form MaintenanceFrame */
    protected MaintenanceFrame() {
        initComponents();
    }

    public static synchronized void open() {
        if(instance==null) instance=new MaintenanceFrame();
        instance.setVisible(true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        maintenanceTab = new javax.swing.JTabbedPane();
        teamForm1 = new org.aquastarz.score.gui.dbform.TeamForm();
        figureForm1 = new org.aquastarz.score.gui.dbform.FigureForm();
        levelForm1 = new org.aquastarz.score.gui.dbform.LevelForm();
        swimmerForm1 = new org.aquastarz.score.gui.dbform.SwimmerForm();
        jPanel1 = new javax.swing.JPanel();
        loadDBButton = new javax.swing.JButton();
        clearDBButton = new javax.swing.JButton();
        loadSwimmersButton = new javax.swing.JButton();

        setTitle("Syncho Maintenance");
        setName("maintenanceFrame"); // NOI18N

        maintenanceTab.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        maintenanceTab.addTab("Swimmers", swimmerForm1);
        maintenanceTab.addTab("Teams", teamForm1);
        maintenanceTab.addTab("Figures", figureForm1);
        maintenanceTab.addTab("Levels", levelForm1);

        loadDBButton.setText("Load Team, Level, Figure");
        loadDBButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadDBButtonActionPerformed(evt);
            }
        });

        clearDBButton.setText("Clear Database");
        clearDBButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearDBButtonActionPerformed(evt);
            }
        });

        loadSwimmersButton.setText("Load Swimmer Sample Data");
        loadSwimmersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadSwimmersButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(loadSwimmersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(loadDBButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(clearDBButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(709, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(clearDBButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loadDBButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loadSwimmersButton)
                .addContainerGap(373, Short.MAX_VALUE))
        );

        maintenanceTab.addTab("Advanced", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 951, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(maintenanceTab, javax.swing.GroupLayout.PREFERRED_SIZE, 951, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 463, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(maintenanceTab, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadDBButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadDBButtonActionPerformed
        org.aquastarz.score.config.Bootstrap.loadLeagueData();
        JOptionPane.showMessageDialog(this, "Done.");
}//GEN-LAST:event_loadDBButtonActionPerformed

    private void loadSwimmersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadSwimmersButtonActionPerformed
        org.aquastarz.score.config.Bootstrap.loadSampleSwimmers();
        JOptionPane.showMessageDialog(this, "Done.");
    }//GEN-LAST:event_loadSwimmersButtonActionPerformed

    private void clearDBButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearDBButtonActionPerformed
        org.aquastarz.score.config.Bootstrap.clearDB();
        JOptionPane.showMessageDialog(this, "Done.");
    }//GEN-LAST:event_clearDBButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearDBButton;
    private org.aquastarz.score.gui.dbform.FigureForm figureForm1;
    private javax.swing.JPanel jPanel1;
    private org.aquastarz.score.gui.dbform.LevelForm levelForm1;
    private javax.swing.JButton loadDBButton;
    private javax.swing.JButton loadSwimmersButton;
    private javax.swing.JTabbedPane maintenanceTab;
    private org.aquastarz.score.gui.dbform.SwimmerForm swimmerForm1;
    private org.aquastarz.score.gui.dbform.TeamForm teamForm1;
    // End of variables declaration//GEN-END:variables

}