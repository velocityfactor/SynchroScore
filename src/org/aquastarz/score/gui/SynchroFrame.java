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
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.aquastarz.score.controller.ScoreController;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.gui.event.MeetSetupPanelListener;
import org.aquastarz.score.gui.event.FiguresParticipantSearchPanelListener;

public class SynchroFrame extends javax.swing.JFrame {

    public enum Tab {

        MEET_SETUP, SWIMMERS, FIGURES_ORDER, FIGURES, ROUTINES, REPORTS
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
        Action swimmerSearchAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                if (tabPane.getSelectedComponent() == figureScore) {
                    swimmerSearchPanel.requestFocus();
                }
            }
        };
        Action maintenanceAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                MaintenanceFrame.open();
            }
        };
        String keyStrokeAndKey = "PERIOD";
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyStrokeAndKey);
        tabPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keyStroke, keyStrokeAndKey);
        tabPane.getActionMap().put(keyStrokeAndKey, swimmerSearchAction);
        keyStrokeAndKey = "DECIMAL";
        keyStroke = KeyStroke.getKeyStroke(keyStrokeAndKey);
        tabPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keyStroke, keyStrokeAndKey);
        tabPane.getActionMap().put(keyStrokeAndKey, swimmerSearchAction);
        keyStrokeAndKey = "F10";
        keyStroke = KeyStroke.getKeyStroke(keyStrokeAndKey);
        tabPane.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(keyStroke, keyStrokeAndKey);
        tabPane.getActionMap().put(keyStrokeAndKey, maintenanceAction);
    }

    private void setSetupStatus(Color color, int percent) {
        setupProgress.setForeground(color);
        setupProgress.setValue(percent);
    }

    private void updateStatus() {
        if (meet.isValid(meet)) {
            setTabEnabled(SynchroFrame.Tab.SWIMMERS, true);
            updateSwimmerTab();
            if (meet.hasFiguresParticipants(meet)) {
                if (meet.getFiguresOrderGenerated()) {
                    setSetupStatus(Color.GREEN, 100);
                    setTabEnabled(Tab.FIGURES_ORDER, true);
                    setTabEnabled(Tab.FIGURES, true);
                    setTabEnabled(Tab.ROUTINES, true);
                    setTabEnabled(Tab.REPORTS, true);
                    updateFiguresOrderList();
                } else {
                    setSetupStatus(Color.YELLOW, 90);
                }
            } else {
                setSetupStatus(Color.YELLOW, 45);
                setTabEnabled(Tab.FIGURES_ORDER, false);
                setTabEnabled(Tab.FIGURES, false);
                setTabEnabled(Tab.ROUTINES, false);
                setTabEnabled(Tab.REPORTS, false);
            }
        } else {
            setSetupStatus(Color.RED, 5);
            setTabEnabled(SynchroFrame.Tab.SWIMMERS, false);
        }

    }

    private void disableAllTabs() {
        for (Tab tab : Tab.values()) {
            setTabEnabled(tab, false);
        }
    }

    private void setTabEnabled(Tab tab, boolean enabled) {
        tabPane.setEnabledAt(tab.ordinal(), enabled);
    }

    private void selectTab(Tab tab) {
        tabPane.setSelectedIndex(tab.ordinal());
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
                FiguresParticipant figuresParticipant = controller.findFiguresParticipantByFigureOrder(meet, figureOrder);
                //TODO if null returned, show feedback
                if (figuresParticipant != null) {
                    swimmerSearchPanel.setFiguresParticipant(figuresParticipant);
                    figureScorePanel.setData(meet.getFigureList(figuresParticipant.getSwimmer()),figuresParticipant);
                }
            }

            public void figuresParticipantSet() {
                figureScorePanel.requestFocus();
            }
        });
    }

    private void updateSwimmerTab() {
        teamTabs.removeAll();
        SwimmerSelectionPanel panel = new SwimmerSelectionPanel(meet.getHomeTeam());
        teamTabs.add(meet.getHomeTeam().getTeamId(), panel);
        panel.setSwimmers(controller.getSwimmers(meet.getHomeTeam()), meet.getSwimmers());
        for (Team opponent : meet.getOpponents()) {
            panel = new SwimmerSelectionPanel(opponent);
            teamTabs.add(opponent.getTeamId(), panel);
            panel.setSwimmers(controller.getSwimmers(opponent), meet.getSwimmers());
        }
    }

    private void updateFiguresOrderList() {
        List<FiguresParticipant> figuresParticipants = meet.getFiguresParticipants();
        Map<String, FiguresParticipant> sortedFiguresParticipants = new TreeMap<String, FiguresParticipant>();
        for (FiguresParticipant figuresParticipant : figuresParticipants) {
            String key = figuresParticipant.getFigureOrder();
            if (figureOrderSortByName.isSelected()) {
                key = figuresParticipant.getSwimmer().getLastName() + figuresParticipant.getSwimmer().getFirstName();
            }
            sortedFiguresParticipants.put(key, figuresParticipant);
        }
        figureOrderTable.setModel(new FiguresParticipantsTableModel(sortedFiguresParticipants.values()));
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

        figuresOrderSortButtonGroup = new javax.swing.ButtonGroup();
        tabPane = new javax.swing.JTabbedPane();
        meetSetup = new org.aquastarz.score.gui.MeetSetupPanel();
        swimmers = new javax.swing.JPanel();
        saveSwimmersButton = new javax.swing.JButton();
        teamTabs = new javax.swing.JTabbedPane();
        generateRandomFiguresOrderButton = new javax.swing.JButton();
        figuresOrder = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        figureOrderSortByNumber = new javax.swing.JRadioButton();
        figureOrderSortByName = new javax.swing.JRadioButton();
        figureOrderScrollPane = new javax.swing.JScrollPane();
        figureOrderTable = new javax.swing.JTable();
        figuresOrderPrintButton = new javax.swing.JButton();
        figureScore = new javax.swing.JPanel();
        swimmerSearchPanel = new org.aquastarz.score.gui.FiguresParticipantSearchPanel();
        figureScorePanel = new org.aquastarz.score.gui.FigureScorePanel();
        jPanel3 = new javax.swing.JPanel();
        saveFigureScoreButton = new javax.swing.JButton();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Synchro Scoring");
        setMinimumSize(new java.awt.Dimension(603, 200));
        setName(""); // NOI18N

        tabPane.setFont(new java.awt.Font("Tahoma", 0, 14));
        tabPane.setMinimumSize(new java.awt.Dimension(603, 200));
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
                .addComponent(teamTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(swimmersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveSwimmersButton)
                    .addComponent(generateRandomFiguresOrderButton))
                .addContainerGap())
        );

        tabPane.addTab("Swimmers", swimmers);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel5.setText("Sort by:");

        figuresOrderSortButtonGroup.add(figureOrderSortByNumber);
        figureOrderSortByNumber.setFont(new java.awt.Font("Tahoma", 0, 14));
        figureOrderSortByNumber.setSelected(true);
        figureOrderSortByNumber.setText("Number");
        figureOrderSortByNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                figuresOrderSortByNumberActionPerformed(evt);
            }
        });

        figuresOrderSortButtonGroup.add(figureOrderSortByName);
        figureOrderSortByName.setFont(new java.awt.Font("Tahoma", 0, 14));
        figureOrderSortByName.setText("Name");
        figureOrderSortByName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                figuresOrderSortByNameActionPerformed(evt);
            }
        });

        figureOrderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        figureOrderScrollPane.setViewportView(figureOrderTable);

        figuresOrderPrintButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        figuresOrderPrintButton.setText("Print");
        figuresOrderPrintButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                figuresOrderPrintButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout figuresOrderLayout = new javax.swing.GroupLayout(figuresOrder);
        figuresOrder.setLayout(figuresOrderLayout);
        figuresOrderLayout.setHorizontalGroup(
            figuresOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, figuresOrderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(figureOrderScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 713, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(figuresOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(figureOrderSortByNumber)
                    .addComponent(figureOrderSortByName)
                    .addComponent(figuresOrderPrintButton))
                .addContainerGap())
        );
        figuresOrderLayout.setVerticalGroup(
            figuresOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(figuresOrderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(figuresOrderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(figureOrderScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                    .addGroup(figuresOrderLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(1, 1, 1)
                        .addComponent(figureOrderSortByNumber)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(figureOrderSortByName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(figuresOrderPrintButton)))
                .addContainerGap())
        );

        tabPane.addTab("Figures Order", figuresOrder);

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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveFigureScoreButton)
                .addContainerGap(739, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveFigureScoreButton)
                .addContainerGap(290, Short.MAX_VALUE))
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
            .addGap(0, 523, Short.MAX_VALUE)
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
            .addGap(0, 523, Short.MAX_VALUE)
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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveSwimmersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveSwimmersButtonActionPerformed
        ArrayList<Swimmer> participatingSwimmers = new ArrayList<Swimmer>();
        for (int i = 0; i < teamTabs.getTabCount(); i++) {
            SwimmerSelectionPanel ssp = (SwimmerSelectionPanel) teamTabs.getComponentAt(i);
            participatingSwimmers.addAll(ssp.getSelectedSwimmers());
        }
        controller.updateFiguresSwimmers(meet, participatingSwimmers);
        controller.saveMeet(meet);
        updateStatus();
    }//GEN-LAST:event_saveSwimmersButtonActionPerformed

    private void saveFigureScoreButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveFigureScoreButtonActionPerformed
        controller.saveFigureScores(figureScorePanel.getFigureScores());
    }//GEN-LAST:event_saveFigureScoreButtonActionPerformed

    private void generateRandomFiguresOrderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateRandomFiguresOrderButtonActionPerformed
        if (meet.getFiguresOrderGenerated()) {
            int confirm = JOptionPane.showConfirmDialog(this, "You have already generated the random meet order.  Shall I do it again and overwrite the current ordering?", "Warning", JOptionPane.OK_CANCEL_OPTION);
            if (confirm != JOptionPane.OK_OPTION) {
                return;
            }
        }
        controller.generateRandomFiguresOrder(meet);
        updateStatus();
        selectTab(Tab.FIGURES_ORDER);
    }//GEN-LAST:event_generateRandomFiguresOrderButtonActionPerformed

    private void figuresOrderSortByNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_figuresOrderSortByNumberActionPerformed
        updateFiguresOrderList();
    }//GEN-LAST:event_figuresOrderSortByNumberActionPerformed

    private void figuresOrderSortByNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_figuresOrderSortByNameActionPerformed
        updateFiguresOrderList();
    }//GEN-LAST:event_figuresOrderSortByNameActionPerformed

    private void figuresOrderPrintButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_figuresOrderPrintButtonActionPerformed
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("/org/aquastarz/score/report/FiguresOrder.jasper")); //JasperCompileManager.compileReport(jasperDesign);
            JRDataSource data = new JRTableModelDataSource(figureOrderTable.getModel());
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("MeetDate", meet.getMeetDate());
            params.put("MeetName", meet.getName());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, data);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception ex) {
            String connectMsg = "Could not create the report.\n" + ex.getLocalizedMessage();
            System.out.println(connectMsg);
        }
    }//GEN-LAST:event_figuresOrderPrintButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane figureOrderScrollPane;
    private javax.swing.JRadioButton figureOrderSortByName;
    private javax.swing.JRadioButton figureOrderSortByNumber;
    private javax.swing.JTable figureOrderTable;
    private javax.swing.JPanel figureScore;
    private org.aquastarz.score.gui.FigureScorePanel figureScorePanel;
    private javax.swing.JPanel figuresOrder;
    private javax.swing.JButton figuresOrderPrintButton;
    private javax.swing.ButtonGroup figuresOrderSortButtonGroup;
    private javax.swing.JButton generateRandomFiguresOrderButton;
    private javax.swing.JProgressBar intFiguresProgress;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private org.aquastarz.score.gui.MeetSetupPanel meetSetup;
    private javax.swing.JProgressBar novFiguresProgress;
    private javax.swing.JPanel reportPanel;
    private javax.swing.JPanel routineScore;
    private javax.swing.JProgressBar routinesProgress;
    private javax.swing.JButton saveFigureScoreButton;
    private javax.swing.JButton saveSwimmersButton;
    private javax.swing.JProgressBar setupProgress;
    private org.aquastarz.score.gui.FiguresParticipantSearchPanel swimmerSearchPanel;
    private javax.swing.JPanel swimmers;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTabbedPane teamTabs;
    // End of variables declaration//GEN-END:variables
}
