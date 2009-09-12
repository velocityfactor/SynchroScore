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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import org.aquastarz.score.controller.ScoreController;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.gui.event.MeetSetupPanelListener;
import org.aquastarz.score.gui.event.FiguresParticipantSearchPanelListener;

public class SynchroFrame extends javax.swing.JFrame {

    public enum Tab {

        MEET_SETUP, SWIMMERS, FIGURES, ROUTINES, REPORTS
    };
    private ScoreController controller = null;
    private Meet meet = null;

    /** Creates new form Synchro */
    public SynchroFrame(ScoreController controller, Meet meet) {
        this.controller = controller;
        this.meet = meet;
        initComponents();
        listenForHotKeys();
        registerListeners();
        meetSetup.fillForm(meet, controller.getFigures(), controller.getTeams());
        disableAllTabs();
        setTabEnabled(SynchroFrame.Tab.MEET_SETUP, true);
        updateStatus();
    }

    private void listenForHotKeys() {

        //Pressing period or decimal on the keypad moves to swimmer search
        // when on figure score tab
        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(tabPane.getSelectedComponent()==figureScore) {
                    swimmerSearchPanel.requestFocus();
                }
            }
        };
        String keyStrokeAndKey = "PERIOD";
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyStrokeAndKey);
        tabPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keyStroke, keyStrokeAndKey);
        tabPane.getActionMap().put(keyStrokeAndKey, action);
        keyStrokeAndKey = "DECIMAL";
        keyStroke = KeyStroke.getKeyStroke(keyStrokeAndKey);
        tabPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keyStroke, keyStrokeAndKey);
        tabPane.getActionMap().put(keyStrokeAndKey, action);
    }

    private void setSetupStatus(Color color, int percent) {
        setupProgress.setForeground(color);
        setupProgress.setValue(percent);
    }

    private void disableAllTabs() {
        for (Tab tab : Tab.values()) {
            setTabEnabled(tab, false);
        }
    }

    private void updateStatus() {
        if (controller.isMeetSetupValid(meet)) {
            setTabEnabled(SynchroFrame.Tab.SWIMMERS, true);
            updateSwimmerTab();
            if (controller.hasFiguresParticipants(meet)) {
                if(meet.getFiguresOrderGenerated()) {
                    setSetupStatus(Color.GREEN, 100);
                }
                else {
                    setSetupStatus(Color.YELLOW, 90);
                }
                setTabEnabled(SynchroFrame.Tab.FIGURES, true);
                setTabEnabled(SynchroFrame.Tab.ROUTINES, true);
                setTabEnabled(SynchroFrame.Tab.REPORTS, true);
            } else {
                setSetupStatus(Color.YELLOW, 45);
                setTabEnabled(SynchroFrame.Tab.FIGURES, false);
                setTabEnabled(SynchroFrame.Tab.ROUTINES, false);
                setTabEnabled(SynchroFrame.Tab.REPORTS, false);
            }
        } else {
            setSetupStatus(Color.RED, 5);
            setTabEnabled(SynchroFrame.Tab.SWIMMERS, false);
        }

    }

    private void setTabEnabled(Tab tab, boolean enabled) {
        tabPane.setEnabledAt(tab.ordinal(), enabled);
    }

    private void registerListeners() {
        meetSetup.addMeetSetupPanelListener(new MeetSetupPanelListener() {

            public void meetSetupSaved() {
                controller.saveMeet(meet);
                updateStatus();
                //TODO show error if meet setup not complete
            }
        });

        swimmerSearchPanel.addFiguresParticipantSearchPanelListener(new FiguresParticipantSearchPanelListener() {
            public void figuresParticipantSearchRequested(String figureOrder) {
                FiguresParticipant figuresParticipant = controller.findFiguresParticipantByFigureOrder(meet,figureOrder);
                //TODO if null returned, show feedback
                if(figuresParticipant!=null) {
                    swimmerSearchPanel.setFiguresParticipant(figuresParticipant);
                }
            }

            public void figuresParticipantSet() {
                figureScorePanel.requestFocus();
            }
        });
    }

    private void updateSwimmerTab() {
        teamTabs.removeAll();
        SwimmerSelectionPanel panel=new SwimmerSelectionPanel(meet.getHomeTeam());
        teamTabs.add(meet.getHomeTeam().getTeamId(), panel);
        panel.setSwimmers(controller.getSwimmers(meet.getHomeTeam()), meet.getSwimmers());
        for (Team opponent : meet.getOpponents()) {
            panel=new SwimmerSelectionPanel(opponent);
            teamTabs.add(opponent.getTeamId(), panel);
            panel.setSwimmers(controller.getSwimmers(opponent), meet.getSwimmers());
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        tabPane = new javax.swing.JTabbedPane();
        meetSetup = new org.aquastarz.score.gui.MeetSetupPanel();
        swimmers = new javax.swing.JPanel();
        saveSwimmersButton = new javax.swing.JButton();
        teamTabs = new javax.swing.JTabbedPane();
        generateRandomFiguresOrderButton = new javax.swing.JButton();
        figureScore = new javax.swing.JPanel();
        swimmerSearchPanel = new org.aquastarz.score.gui.FiguresParticipantSearchPanel();
        figureScorePanel = new org.aquastarz.score.gui.FigureScorePanel();
        saveFigureScoreButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        routineScore = new javax.swing.JPanel();
        reportPanel = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel1 = new javax.swing.JLabel();
        setupProgress = new javax.swing.JProgressBar();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        novFiguresProgress = new javax.swing.JProgressBar();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel3 = new javax.swing.JLabel();
        intFiguresProgress = new javax.swing.JProgressBar();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jLabel4 = new javax.swing.JLabel();
        routinesProgress = new javax.swing.JProgressBar();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        maintenanceMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Synchro Scoring");
        setMinimumSize(new java.awt.Dimension(603, 200));
        setName(""); // NOI18N

        tabPane.setFont(new java.awt.Font("Tahoma", 0, 14));
        tabPane.setMinimumSize(new java.awt.Dimension(603, 200));
        tabPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabPaneStateChanged(evt);
            }
        });
        tabPane.addTab("Meet Setup", meetSetup);

        saveSwimmersButton.setFont(new java.awt.Font("Tahoma", 0, 14));
        saveSwimmersButton.setText("Save");
        saveSwimmersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveSwimmersButtonActionPerformed(evt);
            }
        });

        teamTabs.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        teamTabs.setFont(new java.awt.Font("Tahoma", 0, 14));

        generateRandomFiguresOrderButton.setFont(new java.awt.Font("Tahoma", 0, 14));
        generateRandomFiguresOrderButton.setText("Generate Random Figures Order");
        generateRandomFiguresOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateRandomFiguresOrderButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout swimmersLayout = new javax.swing.GroupLayout(swimmers);
        swimmers.setLayout(swimmersLayout);
        swimmersLayout.setHorizontalGroup(
            swimmersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(swimmersLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(swimmersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(swimmersLayout.createSequentialGroup()
                        .addComponent(saveSwimmersButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generateRandomFiguresOrderButton)
                        .addContainerGap(502, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, swimmersLayout.createSequentialGroup()
                        .addComponent(teamTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 596, Short.MAX_VALUE)
                        .addGap(208, 208, 208))))
        );
        swimmersLayout.setVerticalGroup(
            swimmersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, swimmersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(teamTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(swimmersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveSwimmersButton)
                    .addComponent(generateRandomFiguresOrderButton))
                .addContainerGap())
        );

        tabPane.addTab("Swimmers", swimmers);

        figureScore.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        figureScore.add(swimmerSearchPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        figureScore.add(figureScorePanel, gridBagConstraints);

        saveFigureScoreButton.setFont(new java.awt.Font("Tahoma", 0, 14));
        saveFigureScoreButton.setText("Save");
        saveFigureScoreButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveFigureScoreButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        figureScore.add(saveFigureScoreButton, gridBagConstraints);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 814, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 270, Short.MAX_VALUE)
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        figureScore.add(jPanel3, gridBagConstraints);

        tabPane.addTab("Figures", figureScore);

        javax.swing.GroupLayout routineScoreLayout = new javax.swing.GroupLayout(routineScore);
        routineScore.setLayout(routineScoreLayout);
        routineScoreLayout.setHorizontalGroup(
            routineScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 814, Short.MAX_VALUE)
        );
        routineScoreLayout.setVerticalGroup(
            routineScoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 502, Short.MAX_VALUE)
        );

        tabPane.addTab("Routines", routineScore);

        javax.swing.GroupLayout reportPanelLayout = new javax.swing.GroupLayout(reportPanel);
        reportPanel.setLayout(reportPanelLayout);
        reportPanelLayout.setHorizontalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 814, Short.MAX_VALUE)
        );
        reportPanelLayout.setVerticalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 502, Short.MAX_VALUE)
        );

        tabPane.addTab("Reports", reportPanel);

        getContentPane().add(tabPane, java.awt.BorderLayout.CENTER);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel1.setText("Setup");
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.add(jLabel1);
        jToolBar1.add(setupProgress);

        jSeparator1.setPreferredSize(new java.awt.Dimension(12, 0));
        jToolBar1.add(jSeparator1);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel2.setText("Nov. Figures");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.add(jLabel2);
        jToolBar1.add(novFiguresProgress);

        jSeparator2.setPreferredSize(new java.awt.Dimension(12, 0));
        jToolBar1.add(jSeparator2);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel3.setText("Int. Figures");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.add(jLabel3);
        jToolBar1.add(intFiguresProgress);

        jSeparator3.setPreferredSize(new java.awt.Dimension(12, 0));
        jToolBar1.add(jSeparator3);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel4.setText("Routines");
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jToolBar1.add(jLabel4);
        jToolBar1.add(routinesProgress);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.SOUTH);

        fileMenu.setText("File");

        openMenuItem.setText("Open");
        fileMenu.add(openMenuItem);

        saveMenuItem.setText("Save");
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setText("Save As ...");
        fileMenu.add(saveAsMenuItem);

        maintenanceMenuItem.setText("Maintenance");
        maintenanceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maintenanceMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(maintenanceMenuItem);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");

        cutMenuItem.setText("Cut");
        editMenu.add(cutMenuItem);

        copyMenuItem.setText("Copy");
        editMenu.add(copyMenuItem);

        pasteMenuItem.setText("Paste");
        editMenu.add(pasteMenuItem);

        deleteMenuItem.setText("Delete");
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        helpMenu.setText("Help");

        contentsMenuItem.setText("Contents");
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void maintenanceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maintenanceMenuItemActionPerformed
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        MaintenanceFrame.open();
        setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_maintenanceMenuItemActionPerformed

    private void tabPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPaneStateChanged
    }//GEN-LAST:event_tabPaneStateChanged

    private void saveSwimmersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveSwimmersButtonActionPerformed
        ArrayList<Swimmer> participatingSwimmers = new ArrayList<Swimmer>();
        for (int i = 0; i < teamTabs.getTabCount(); i++) {
            SwimmerSelectionPanel ssp = (SwimmerSelectionPanel) teamTabs.getComponentAt(i);
            participatingSwimmers.addAll(ssp.getSelectedSwimmers());
        }
        controller.updateFiguresSwimmers(meet,participatingSwimmers);
        controller.saveMeet(meet);
        updateStatus();
        //TODO show errors if no swimmers selected
    }//GEN-LAST:event_saveSwimmersButtonActionPerformed

    private void saveFigureScoreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFigureScoreButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveFigureScoreButtonActionPerformed

    private void generateRandomFiguresOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateRandomFiguresOrderButtonActionPerformed
        if(meet.getFiguresOrderGenerated()) {
            int confirm = JOptionPane.showConfirmDialog(this, "You have already generated the random meet order.  Shall I do it again and overwrite the current ordering?", "Warning", JOptionPane.OK_CANCEL_OPTION);
            if(confirm != JOptionPane.OK_OPTION) return;
        }

        controller.generateRandomFiguresOrder(meet);
        updateStatus();
        //TODO give some feedback
    }//GEN-LAST:event_generateRandomFiguresOrderButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JPanel figureScore;
    private org.aquastarz.score.gui.FigureScorePanel figureScorePanel;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JButton generateRandomFiguresOrderButton;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JProgressBar intFiguresProgress;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem maintenanceMenuItem;
    private org.aquastarz.score.gui.MeetSetupPanel meetSetup;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar novFiguresProgress;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JPanel reportPanel;
    private javax.swing.JPanel routineScore;
    private javax.swing.JProgressBar routinesProgress;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JButton saveFigureScoreButton;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JButton saveSwimmersButton;
    private javax.swing.JProgressBar setupProgress;
    private org.aquastarz.score.gui.FiguresParticipantSearchPanel swimmerSearchPanel;
    private javax.swing.JPanel swimmers;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTabbedPane teamTabs;
    // End of variables declaration//GEN-END:variables
}
