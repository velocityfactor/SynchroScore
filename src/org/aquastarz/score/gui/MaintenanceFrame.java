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

import java.awt.Cursor;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.config.Bootstrap;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Season;
import org.aquastarz.score.manager.MeetManager;
import org.aquastarz.score.manager.SeasonManager;
import org.aquastarz.score.manager.SwimmerManager;

public class MaintenanceFrame extends javax.swing.JFrame {

    private static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(MaintenanceFrame.class.getName());
    Image appIcon = null;

    public MaintenanceFrame() {
        try {
            URL rsrcUrl = Thread.currentThread().getContextClassLoader().getResource("org/aquastarz/score/gui/synchro-icon.png");
            appIcon = ImageIO.read(rsrcUrl);
        } catch (Exception e) {
            logger.error("Error loading app icon.", e);
        }
        initComponents();
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
        updateForm = new javax.swing.JPanel();
        importSwimmersButton = new javax.swing.JButton();
        clearDBButton = new javax.swing.JButton();
        checkLeagueData = new javax.swing.JButton();
        exportSwimmersButton = new javax.swing.JButton();
        importMeetButton = new javax.swing.JButton();
        exportMeetButton = new javax.swing.JButton();
        swimmerForm1 = new org.aquastarz.score.gui.dbform.SwimmerForm();
        teamForm1 = new org.aquastarz.score.gui.dbform.TeamForm();
        figureForm1 = new org.aquastarz.score.gui.dbform.FigureForm();
        levelForm1 = new org.aquastarz.score.gui.dbform.LevelForm();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SynchroScore Maintenance");
        setIconImage(appIcon);

        maintenanceTab.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        importSwimmersButton.setText("Import Swimmers");
        importSwimmersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importSwimmersButtonActionPerformed(evt);
            }
        });

        clearDBButton.setText("Clear Database");
        clearDBButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearDBButtonActionPerformed(evt);
            }
        });

        checkLeagueData.setText("Check League Data");
        checkLeagueData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkLeagueDataActionPerformed(evt);
            }
        });

        exportSwimmersButton.setText("Export Swimmers");
        exportSwimmersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportSwimmersButtonActionPerformed(evt);
            }
        });

        importMeetButton.setText("Import Meet");
        importMeetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importMeetButtonActionPerformed(evt);
            }
        });

        exportMeetButton.setText("Export Meet");
        exportMeetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportMeetButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout updateFormLayout = new javax.swing.GroupLayout(updateForm);
        updateForm.setLayout(updateFormLayout);
        updateFormLayout.setHorizontalGroup(
            updateFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateFormLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(updateFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(updateFormLayout.createSequentialGroup()
                        .addComponent(clearDBButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkLeagueData)
                        .addGap(616, 616, 616))
                    .addGroup(updateFormLayout.createSequentialGroup()
                        .addGroup(updateFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(importMeetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(importSwimmersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(updateFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(exportMeetButton, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                            .addComponent(exportSwimmersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(622, 622, 622))))
        );
        updateFormLayout.setVerticalGroup(
            updateFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updateFormLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(updateFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(importSwimmersButton)
                    .addComponent(exportSwimmersButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(updateFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(importMeetButton)
                    .addComponent(exportMeetButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 339, Short.MAX_VALUE)
                .addGroup(updateFormLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearDBButton)
                    .addComponent(checkLeagueData))
                .addContainerGap())
        );

        maintenanceTab.addTab("Update", updateForm);
        maintenanceTab.addTab("Swimmers", swimmerForm1);
        maintenanceTab.addTab("Teams", teamForm1);
        maintenanceTab.addTab("Figures", figureForm1);
        maintenanceTab.addTab("Levels", levelForm1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 936, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(maintenanceTab, javax.swing.GroupLayout.PREFERRED_SIZE, 936, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void importSwimmersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importSwimmersButtonActionPerformed
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Open Swimmers data file");
        jfc.setFileFilter(new FileNameExtensionFilter("csv file", "csv"));
        int ret = jfc.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                SwimmerManager.importSwimmers(jfc.getSelectedFile(), ScoreApp.getCurrentSeason());
                JOptionPane.showMessageDialog(this, "Done.  Restart program to see changes.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error importing\n" + e.getMessage());
            }
            setCursor(Cursor.getDefaultCursor());
        }
}//GEN-LAST:event_importSwimmersButtonActionPerformed

    private void clearDBButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearDBButtonActionPerformed
        int ret = JOptionPane.showConfirmDialog(maintenanceTab, "Are you sure you want to wipe out all meets, swimmers, scores, etc?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (ret != JOptionPane.YES_OPTION) {
            return;
        }
        ret = JOptionPane.showConfirmDialog(maintenanceTab, "Really?  Everything?  There's no going back.  You may regret this.", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (ret != JOptionPane.YES_OPTION) {
            return;
        }
        org.aquastarz.score.config.Bootstrap.clearDB(ScoreApp.getEntityManager());
        JOptionPane.showMessageDialog(this, "Done.  Program will exit now.");
        System.exit(0);
}//GEN-LAST:event_clearDBButtonActionPerformed

    private void checkLeagueDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkLeagueDataActionPerformed
        int ret = JOptionPane.showConfirmDialog(maintenanceTab, "This will add any missing figures, figure levels, routine levels, or teams that might be missing from the database.\n"
                + "This is typically safe to do at anytime and possibly necessary if upgrading from an older version.  Proceed?", "Confirm Check", JOptionPane.YES_NO_OPTION);
        if (ret != JOptionPane.YES_OPTION) {
            return;
        }
        Bootstrap.loadLeagueData();
}//GEN-LAST:event_checkLeagueDataActionPerformed

    private void exportSwimmersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportSwimmersButtonActionPerformed
        String seasonName = JOptionPane.showInputDialog(this, "Enter season to export:", ScoreApp.getCurrentSeason().getName());
        Season season = SeasonManager.find(seasonName);
        if (season != null) {
            JFileChooser jfc = new JFileChooser();
            jfc.setDialogTitle("Save Swimmers data file");
            jfc.setFileFilter(new FileNameExtensionFilter("csv file", "csv"));
            int ret = jfc.showSaveDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                    File f = jfc.getSelectedFile();
                    if (!f.getName().endsWith(".csv")) {
                        f = new File(f.getAbsolutePath() + ".csv");
                    }
                    SwimmerManager.exportSwimmers(f, season);
                    JOptionPane.showMessageDialog(this, "Done.");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error exporting\n" + e.getMessage());
                }
                setCursor(Cursor.getDefaultCursor());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Unknown season.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_exportSwimmersButtonActionPerformed

    private void exportMeetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMeetButtonActionPerformed
        SimpleMeetSelectionDialog dialog = new SimpleMeetSelectionDialog(this, true);
        dialog.showMeetSelectionDialog();
        Meet meet = dialog.getSelectedMeet();
        if (meet != null) {
            JFileChooser jfc = new JFileChooser();
            jfc.setDialogTitle("Save Meet data file");
            jfc.setFileFilter(new FileNameExtensionFilter("csv file", "csv"));
            int ret = jfc.showSaveDialog(this);
            if (ret == JFileChooser.APPROVE_OPTION) {
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                try {
                    File f = jfc.getSelectedFile();
                    if (!f.getName().endsWith(".csv")) {
                        f = new File(f.getAbsolutePath() + ".csv");
                    }
                    MeetManager.exportMeet(meet, f);
                    JOptionPane.showMessageDialog(this, "Done.");
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error exporting\n" + e.getMessage());
                }
                setCursor(Cursor.getDefaultCursor());
            }
        } else {
            JOptionPane.showMessageDialog(this, "Meet export canceled.", "Canceled", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_exportMeetButtonActionPerformed

    private void importMeetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importMeetButtonActionPerformed
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Open Meet data file");
        jfc.setFileFilter(new FileNameExtensionFilter("csv file", "csv"));
        int ret = jfc.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                MeetManager.importMeet(jfc.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Done.  Restart program to see changes.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error importing\n" + e.getMessage());
            }
            setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_importMeetButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton checkLeagueData;
    private javax.swing.JButton clearDBButton;
    private javax.swing.JButton exportMeetButton;
    private javax.swing.JButton exportSwimmersButton;
    private org.aquastarz.score.gui.dbform.FigureForm figureForm1;
    private javax.swing.JButton importMeetButton;
    private javax.swing.JButton importSwimmersButton;
    private org.aquastarz.score.gui.dbform.LevelForm levelForm1;
    private javax.swing.JTabbedPane maintenanceTab;
    private org.aquastarz.score.gui.dbform.SwimmerForm swimmerForm1;
    private org.aquastarz.score.gui.dbform.TeamForm teamForm1;
    private javax.swing.JPanel updateForm;
    // End of variables declaration//GEN-END:variables
}