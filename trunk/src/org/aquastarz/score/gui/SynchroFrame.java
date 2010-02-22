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
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
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
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.controller.ScoreController;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.gui.event.MeetSetupPanelListener;
import org.aquastarz.score.gui.event.FiguresParticipantSearchPanelListener;

public class SynchroFrame extends javax.swing.JFrame {

    private static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(SynchroFrame.class.getName());

    public enum Tab {

        MEET_SETUP, SWIMMERS, FIGURES_ORDER, FIGURES, ROUTINES, REPORTS, LEAGUE
    };
    private ScoreController controller = null;
    private Meet meet = null;
    Image appIcon = null;

    /** Creates new form Synchro */
    public SynchroFrame(ScoreController controller, Meet meet) {
        this.controller = controller;
        this.meet = meet;
        try {
            URL rsrcUrl = Thread.currentThread().getContextClassLoader().getResource("org/aquastarz/score/gui/synchro-icon.png");
            appIcon = ImageIO.read(rsrcUrl);
        } catch (Exception e) {
            logger.error("Error loading app icon.", e);
        }
        initComponents();
        listenForHotKeys();
        registerListeners();
        meetSetup.fillForm(meet, controller.getFigures(), controller.getTeams());
        disableAllTabs();
        setTabEnabled(SynchroFrame.Tab.MEET_SETUP, true);
        updateStatus();
        updateFiguresStatus();
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

    public void setNovFiguresStatus(Color color, int percent) {
        novFiguresProgress.setForeground(color);
        novFiguresProgress.setValue(percent);
    }

    public void setIntFiguresStatus(Color color, int percent) {
        intFiguresProgress.setForeground(color);
        intFiguresProgress.setValue(percent);
    }

    private void updateStatus() {
        updateLeagueList();
        setTabEnabled(Tab.LEAGUE, true);
        if (meet.isValid()) {
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

    public void updateFiguresStatus() {
        setNovFiguresStatus(Color.GREEN, ScoreController.percentCompleteFigures(meet, true));
        setIntFiguresStatus(Color.GREEN, ScoreController.percentCompleteFigures(meet, false));
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
                if (!meet.isValid()) {
                    JOptionPane.showMessageDialog(rootPane, "The Meet setup that you have saved is not complete.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        swimmerSearchPanel.addFiguresParticipantSearchPanelListener(new FiguresParticipantSearchPanelListener() {

            public void figuresParticipantSearchRequested(String figureOrder) {
                doFiguresParticipantSearch(figureOrder);
            }

            public void figuresParticipantSet() {
                figureScorePanel.requestFocus();
            }
        });
    }

    private void doFiguresParticipantSearch(String figureOrder) {
        FiguresParticipant figuresParticipant = controller.findFiguresParticipantByFigureOrder(meet, figureOrder);
        if (figuresParticipant != null) {
            swimmerSearchPanel.setFiguresParticipant(figuresParticipant);
            figureScorePanel.setData(meet.getFigureList(figuresParticipant.getSwimmer()), figuresParticipant);
        } else {
            clearFiguresScorePanel();
            JOptionPane.showMessageDialog(swimmerSearchPanel, "Swimmer not found.", "Invalid Entry", JOptionPane.WARNING_MESSAGE);
        }

    }

    private void clearFiguresScorePanel() {
        swimmerSearchPanel.clear();
        figureScorePanel.clear();
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

    private void updateLeagueList() {
        EntityManager entityManager = ScoreApp.getEntityManager();
        Query swimmerQuery = null;

        String teamId = "%";
        ComboBoxModel cbm = leagueTeamCombo.getModel();
        Object o = cbm.getSelectedItem();
        if (o != null && o instanceof String) {
            teamId = (String) o;
            if ("[All]".equals(o)) {
                teamId = "%";
            }
        }

        if (leagueSortByName.isSelected()) {
            swimmerQuery = entityManager.createNamedQuery("Swimmer.findByTeamOrderByName");
        } else if (leagueSortByTeam.isSelected()) {
            swimmerQuery = entityManager.createNamedQuery("Swimmer.findByTeamOrderByTeamAndName");
        } else if (leagueSortByLevel.isSelected()) {
            swimmerQuery = entityManager.createNamedQuery("Swimmer.findByTeamOrderByLevelAndName");
        } else {
            swimmerQuery = entityManager.createNamedQuery("Swimmer.findByTeamOrderBySwimmerId");
        }
        swimmerQuery.setParameter("teamId", teamId);
        List<Swimmer> swimmerList = swimmerQuery.getResultList();
        swimmerTable.setModel(new SwimmersTableModel(swimmerList));
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
        leagueSortButtonGroup = new javax.swing.ButtonGroup();
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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        leaguePanel = new javax.swing.JPanel();
        leaguePrintButton = new javax.swing.JButton();
        swimmerScrollPane = new javax.swing.JScrollPane();
        swimmerTable = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        leagueSortByNumber = new javax.swing.JRadioButton();
        leagueSortByName = new javax.swing.JRadioButton();
        leagueSortByTeam = new javax.swing.JRadioButton();
        jLabel7 = new javax.swing.JLabel();
        leagueTeamCombo = new javax.swing.JComboBox();
        leagueSortByLevel = new javax.swing.JRadioButton();
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
        setIconImage(appIcon);
        setMinimumSize(new java.awt.Dimension(603, 200));
        setName(""); // NOI18N

        tabPane.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tabPane.setMinimumSize(new java.awt.Dimension(603, 200));
        tabPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabPaneStateChanged(evt);
            }
        });
        tabPane.addTab("Meet Setup", meetSetup);

        saveSwimmersButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        saveSwimmersButton.setText("Save");
        saveSwimmersButton.setToolTipText("Save figures participants.");
        saveSwimmersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveSwimmersButtonActionPerformed(evt);
            }
        });

        teamTabs.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        teamTabs.setFont(new java.awt.Font("Tahoma", 0, 14));

        generateRandomFiguresOrderButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        generateRandomFiguresOrderButton.setText("Randomize");
        generateRandomFiguresOrderButton.setToolTipText("Generate random figures order.");
        generateRandomFiguresOrderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateRandomFiguresOrderButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout swimmersLayout = new javax.swing.GroupLayout(swimmers);
        swimmers.setLayout(swimmersLayout);
        swimmersLayout.setHorizontalGroup(
            swimmersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, swimmersLayout.createSequentialGroup()
                .addComponent(teamTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(swimmersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(saveSwimmersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(generateRandomFiguresOrderButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        swimmersLayout.setVerticalGroup(
            swimmersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(swimmersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveSwimmersButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generateRandomFiguresOrderButton)
                .addContainerGap(452, Short.MAX_VALUE))
            .addComponent(teamTabs, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
        );

        tabPane.addTab("Swimmers", swimmers);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("Sort by:");

        figuresOrderSortButtonGroup.add(figureOrderSortByNumber);
        figureOrderSortByNumber.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        figureScore.add(swimmerSearchPanel, gridBagConstraints);

        figureScorePanel.setMinimumSize(new java.awt.Dimension(725, 150));
        figureScorePanel.setPreferredSize(new java.awt.Dimension(725, 150));
        figureScorePanel.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                figureScorePanelPropertyChange(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
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
        saveFigureScoreButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saveFigureScoreButtonKeyPressed(evt);
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
                .addContainerGap(257, Short.MAX_VALUE))
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

        jButton1.setText("Nov. Figures");

        jButton2.setText("Nov. Meet Sheet");

        jButton3.setText("Nov. Station");

        jButton4.setText("Int. Figures");

        jButton5.setText("Int. Meet Sheet");

        jButton6.setText("Int. Station");

        jButton7.setText("Team Results");

        jButton8.setText("Nov. Figure Labels");

        jButton9.setText("Int. Figure Labels");

        jButton10.setText("Nov. Routines");

        jButton11.setText("Nov. Routine Labels");

        jButton12.setText("Int. Routines");

        jButton13.setText("Int. Routine Labels");

        javax.swing.GroupLayout reportPanelLayout = new javax.swing.GroupLayout(reportPanel);
        reportPanel.setLayout(reportPanelLayout);
        reportPanelLayout.setHorizontalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(reportPanelLayout.createSequentialGroup()
                        .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(jButton2)
                            .addComponent(jButton3))
                        .addGap(18, 18, 18)
                        .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(reportPanelLayout.createSequentialGroup()
                                .addComponent(jButton4)
                                .addGap(18, 18, 18)
                                .addComponent(jButton7))
                            .addComponent(jButton5)
                            .addComponent(jButton6)))
                    .addGroup(reportPanelLayout.createSequentialGroup()
                        .addComponent(jButton8)
                        .addGap(18, 18, 18)
                        .addComponent(jButton9))
                    .addGroup(reportPanelLayout.createSequentialGroup()
                        .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton10)
                            .addComponent(jButton11))
                        .addGap(18, 18, 18)
                        .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton12)
                            .addComponent(jButton13))))
                .addContainerGap(413, Short.MAX_VALUE))
        );

        reportPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton1, jButton10, jButton11, jButton2, jButton3, jButton8});

        reportPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButton12, jButton13, jButton4, jButton5, jButton6, jButton9});

        reportPanelLayout.setVerticalGroup(
            reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(reportPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(reportPanelLayout.createSequentialGroup()
                        .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jButton7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6))
                    .addGroup(reportPanelLayout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8)
                    .addComponent(jButton9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(reportPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(reportPanelLayout.createSequentialGroup()
                        .addComponent(jButton10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton11))
                    .addGroup(reportPanelLayout.createSequentialGroup()
                        .addComponent(jButton12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton13)))
                .addContainerGap(344, Short.MAX_VALUE))
        );

        tabPane.addTab("Reports", reportPanel);

        leaguePrintButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        leaguePrintButton.setText("Print");
        leaguePrintButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leaguePrintButtonActionPerformed(evt);
            }
        });

        swimmerTable.setModel(new javax.swing.table.DefaultTableModel(
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
        swimmerScrollPane.setViewportView(swimmerTable);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Sort by:");

        leagueSortButtonGroup.add(leagueSortByNumber);
        leagueSortByNumber.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        leagueSortByNumber.setSelected(true);
        leagueSortByNumber.setText("Number");
        leagueSortByNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leagueSortByNumberfiguresOrderSortByNumberActionPerformed(evt);
            }
        });

        leagueSortButtonGroup.add(leagueSortByName);
        leagueSortByName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        leagueSortByName.setText("Name");
        leagueSortByName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leagueSortByNamefiguresOrderSortByNameActionPerformed(evt);
            }
        });

        leagueSortButtonGroup.add(leagueSortByTeam);
        leagueSortByTeam.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        leagueSortByTeam.setText("Team");
        leagueSortByTeam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leagueSortByTeamfiguresOrderSortByNameActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setText("Team:");

        Query teamQuery = ScoreApp.getEntityManager().createNamedQuery("Team.findAllOrderByTeamId");
        List<Team> teamList = teamQuery.getResultList();
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        dcbm.addElement("[All]");
        for(Team team : teamList) {
            dcbm.addElement(team.getTeamId());
        }
        leagueTeamCombo.setModel(dcbm);
        leagueTeamCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leagueTeamComboActionPerformed(evt);
            }
        });

        leagueSortButtonGroup.add(leagueSortByLevel);
        leagueSortByLevel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        leagueSortByLevel.setText("Level");
        leagueSortByLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leagueSortByLevelfiguresOrderSortByNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout leaguePanelLayout = new javax.swing.GroupLayout(leaguePanel);
        leaguePanel.setLayout(leaguePanelLayout);
        leaguePanelLayout.setHorizontalGroup(
            leaguePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leaguePanelLayout.createSequentialGroup()
                .addComponent(swimmerScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 733, Short.MAX_VALUE)
                .addGroup(leaguePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(leaguePanelLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(leaguePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(leagueSortByNumber, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(leagueSortByName)
                            .addComponent(leagueSortByTeam)
                            .addComponent(leagueSortByLevel)))
                    .addGroup(leaguePanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(leaguePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(leagueTeamCombo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(leaguePanelLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE))
                            .addComponent(leaguePrintButton))))
                .addContainerGap())
        );
        leaguePanelLayout.setVerticalGroup(
            leaguePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(swimmerScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE)
            .addGroup(leaguePanelLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(1, 1, 1)
                .addComponent(leagueSortByNumber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(leagueSortByName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(leagueSortByTeam)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(leagueSortByLevel)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(leagueTeamCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 304, Short.MAX_VALUE)
                .addComponent(leaguePrintButton)
                .addContainerGap())
        );

        tabPane.addTab("League", leaguePanel);

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
        if (figureScorePanel.scoresValid()) {
            if (controller.saveFigureScores(figureScorePanel.getFiguresParticipant(), figureScorePanel.getFigureScores())) {
                doFiguresParticipantSearch(figureScorePanel.getFiguresParticipant().getFigureOrder());
                swimmerSearchPanel.focus();
            } else {
                JOptionPane.showMessageDialog(figureScorePanel, "Check scores and try again.  Restart program if this error persists.", "Error Saving", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(figureScorePanel, "Check scores and try again.", "Invalid Score", JOptionPane.WARNING_MESSAGE);
        }

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
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("MeetDate", meet.getMeetDate());
            params.put("MeetName", meet.getName());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, data);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception ex) {
            logger.error("Could not create the report.\n" + ex.getLocalizedMessage());
        }
    }//GEN-LAST:event_figuresOrderPrintButtonActionPerformed

    private void figureScorePanelPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_figureScorePanelPropertyChange
        if ("FocusSave".equals(evt.getPropertyName())) {
            saveFigureScoreButton.requestFocusInWindow();
        }
    }//GEN-LAST:event_figureScorePanelPropertyChange

    private void saveFigureScoreButtonKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saveFigureScoreButtonKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            saveFigureScoreButton.doClick();
        }
    }//GEN-LAST:event_saveFigureScoreButtonKeyPressed

    private void tabPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPaneStateChanged
        if (tabPane.getSelectedIndex() == Tab.FIGURES.ordinal()) {
            swimmerSearchPanel.focus();
        }
    }//GEN-LAST:event_tabPaneStateChanged

    private void leaguePrintButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leaguePrintButtonActionPerformed
        try {
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(getClass().getResourceAsStream("/org/aquastarz/score/report/Roster.jasper"));
            JRDataSource data = new JRTableModelDataSource(swimmerTable.getModel());
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("Title", "Roster");
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, data);
            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception ex) {
            logger.error("Could not create the report.\n" + ex.getLocalizedMessage());
        }

    }//GEN-LAST:event_leaguePrintButtonActionPerformed

    private void leagueSortByNumberfiguresOrderSortByNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leagueSortByNumberfiguresOrderSortByNumberActionPerformed
        updateLeagueList();
    }//GEN-LAST:event_leagueSortByNumberfiguresOrderSortByNumberActionPerformed

    private void leagueSortByNamefiguresOrderSortByNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leagueSortByNamefiguresOrderSortByNameActionPerformed
        updateLeagueList();
    }//GEN-LAST:event_leagueSortByNamefiguresOrderSortByNameActionPerformed

    private void leagueSortByTeamfiguresOrderSortByNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leagueSortByTeamfiguresOrderSortByNameActionPerformed
        updateLeagueList();
    }//GEN-LAST:event_leagueSortByTeamfiguresOrderSortByNameActionPerformed

    private void leagueTeamComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leagueTeamComboActionPerformed
        updateLeagueList();
    }//GEN-LAST:event_leagueTeamComboActionPerformed

    private void leagueSortByLevelfiguresOrderSortByNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leagueSortByLevelfiguresOrderSortByNameActionPerformed
        updateLeagueList();
    }//GEN-LAST:event_leagueSortByLevelfiguresOrderSortByNameActionPerformed
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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel leaguePanel;
    private javax.swing.JButton leaguePrintButton;
    private javax.swing.ButtonGroup leagueSortButtonGroup;
    private javax.swing.JRadioButton leagueSortByLevel;
    private javax.swing.JRadioButton leagueSortByName;
    private javax.swing.JRadioButton leagueSortByNumber;
    private javax.swing.JRadioButton leagueSortByTeam;
    private javax.swing.JComboBox leagueTeamCombo;
    private org.aquastarz.score.gui.MeetSetupPanel meetSetup;
    private javax.swing.JProgressBar novFiguresProgress;
    private javax.swing.JPanel reportPanel;
    private javax.swing.JPanel routineScore;
    private javax.swing.JProgressBar routinesProgress;
    private javax.swing.JButton saveFigureScoreButton;
    private javax.swing.JButton saveSwimmersButton;
    private javax.swing.JProgressBar setupProgress;
    private javax.swing.JScrollPane swimmerScrollPane;
    private org.aquastarz.score.gui.FiguresParticipantSearchPanel swimmerSearchPanel;
    private javax.swing.JTable swimmerTable;
    private javax.swing.JPanel swimmers;
    private javax.swing.JTabbedPane tabPane;
    private javax.swing.JTabbedPane teamTabs;
    // End of variables declaration//GEN-END:variables
}
