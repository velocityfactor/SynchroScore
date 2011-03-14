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

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import org.aquastarz.score.controller.ScoreController;
import org.aquastarz.score.domain.Figure;
import org.aquastarz.score.domain.FigureScore;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.util.TwoDigitScore;

public class FigureScorePanel extends javax.swing.JPanel {

    private static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(FigureScorePanel.class.getName());
    private int currentRow;
    private FiguresParticipant figuresParticipant;
    private Map<Figure, FigureScore> figureScoreMap;
    private Figure[] figures;

    /** Creates new form FigureScorePanel */
    public FigureScorePanel() {
        initComponents();
// Try to block these fields from getting a period, but this isn't reliable
//        scoreS1J1.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0), "none");
        scoreS1J1.getKeymap().removeKeyStrokeBinding(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD,0));
        clearScores();
    }

    public final void clearScores() {
        figuresParticipant = null;
        figureScoreMap = null;

        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 5; j++) {
                setScoreText(i, j, "");
            }
        }
        penaltyHS1.setSelected(false);
        penalty1S1.setSelected(false);
        penalty2S1.setSelected(false);
        penaltyHS2.setSelected(false);
        penalty1S2.setSelected(false);
        penalty2S2.setSelected(false);
        penaltyHS3.setSelected(false);
        penalty1S3.setSelected(false);
        penalty2S3.setSelected(false);
        penaltyHS4.setSelected(false);
        penalty1S4.setSelected(false);
        penalty2S4.setSelected(false);
        dd1.setText("");
        total1.setText("");
        dd2.setText("");
        total2.setText("");
        dd3.setText("");
        total3.setText("");
        dd4.setText("");
        total4.setText("");
        figuresTotal.setText("");

        setEditableRow(0);
    }

    public void setData(List<Figure> figures, FiguresParticipant figuresParticipant) {
        this.figuresParticipant = figuresParticipant;
        Collection<FigureScore> figureScores = figuresParticipant.getFiguresScores();
        figureScoreMap = new HashMap<Figure, FigureScore>();
        for (FigureScore figureScore : figureScores) {
            figureScoreMap.put(ScoreController.getFigure(figureScore), figureScore);
        }
        Figure[] fig = figures.toArray(new Figure[4]);
        this.figures = fig;

        total1.setText("--");
        total2.setText("--");
        total3.setText("--");
        total4.setText("--");
        if (figuresParticipant.getTotalScore() != null) {
            figuresTotal.setText(figuresParticipant.getTotalScore().toString());
        } else {
            figuresTotal.setText("--");
        }
        if (fig[0] != null) {
            station1Button.setEnabled(true);
            station1Button.setText(fig[0].getName());
            dd1.setText(fig[0].getDegreeOfDifficulty().toString());
            penaltyHS1.setSelected(false);
            penalty1S1.setSelected(false);
            penalty2S1.setSelected(false);
            FigureScore figureScore = figureScoreMap.get(fig[0]);
            if (figureScore != null) {
                if (figureScore.getTotalScore() != null) {
                    total1.setText(figureScore.getTotalScore().toPlainString());
                } else {
                    total1.setText("NA");
                }
                if (figureScore.getPenalty() != null) {
                    BigDecimal penalty = figureScoreMap.get(fig[0]).getPenalty();
                    int ip = penalty.movePointRight(1).intValue();
                    switch (ip) {
                        case 5:
                            penaltyHS1.setSelected(true);
                            break;
                        case 10:
                            penalty1S1.setSelected(true);
                            break;
                        case 20:
                            penalty2S1.setSelected(true);
                            break;
                    }
                }
            }
            if (station1Button.isSelected()) {
                setEditableRow(1);
            }
        } else {
            if (station1Button.isSelected()) {
                setEditableRow(0);
            }
            station1Button.setEnabled(false);
            station1Button.setText("--");
            dd1.setText("--");
            penaltyHS1.setSelected(false);
            penalty1S1.setSelected(false);
            penalty2S1.setSelected(false);
            total1.setText("--");
        }
        if (fig[1] != null) {
            station2Button.setEnabled(true);
            station2Button.setText(fig[1].getName());
            dd2.setText(fig[1].getDegreeOfDifficulty().toString());
            penaltyHS2.setSelected(false);
            penalty1S2.setSelected(false);
            penalty2S2.setSelected(false);
            FigureScore figureScore = figureScoreMap.get(fig[1]);
            if (figureScore != null) {
                if (figureScore.getTotalScore() != null) {
                    total2.setText(figureScore.getTotalScore().toPlainString());
                } else {
                    total2.setText("NA");
                }
                if (figureScore.getPenalty() != null) {
                    BigDecimal penalty = figureScoreMap.get(fig[1]).getPenalty();
                    int ip = penalty.movePointRight(1).intValue();
                    switch (ip) {
                        case 5:
                            penaltyHS2.setSelected(true);
                            break;
                        case 10:
                            penalty1S2.setSelected(true);
                            break;
                        case 20:
                            penalty2S2.setSelected(true);
                            break;
                    }
                }
            }
            if (station2Button.isSelected()) {
                setEditableRow(2);
            }
        } else {
            if (station2Button.isSelected()) {
                setEditableRow(0);
            }
            station2Button.setEnabled(false);
            station2Button.setText("--");
            dd2.setText("--");
            penaltyHS2.setSelected(false);
            penalty1S2.setSelected(false);
            penalty2S2.setSelected(false);
            total2.setText("--");
        }
        if (fig[2] != null) {
            station3Button.setEnabled(true);
            station3Button.setText(fig[2].getName());
            dd3.setText(fig[2].getDegreeOfDifficulty().toString());
            penaltyHS3.setSelected(false);
            penalty1S3.setSelected(false);
            penalty2S3.setSelected(false);
            FigureScore figureScore = figureScoreMap.get(fig[2]);
            if (figureScore != null) {
                if (figureScore.getTotalScore() != null) {
                    total3.setText(figureScore.getTotalScore().toPlainString());
                } else {
                    total3.setText("NA");
                }
                if (figureScore.getPenalty() != null) {
                    BigDecimal penalty = figureScoreMap.get(fig[2]).getPenalty();
                    int ip = penalty.movePointRight(1).intValue();
                    switch (ip) {
                        case 5:
                            penaltyHS3.setSelected(true);
                            break;
                        case 10:
                            penalty1S3.setSelected(true);
                            break;
                        case 20:
                            penalty2S3.setSelected(true);
                            break;
                    }
                }
            }
            if (station3Button.isSelected()) {
                setEditableRow(3);
            }
        } else {
            if (station3Button.isSelected()) {
                setEditableRow(0);
            }
            station3Button.setEnabled(false);
            station3Button.setText("--");
            dd3.setText("--");
            penaltyHS3.setSelected(false);
            penalty1S3.setSelected(false);
            penalty2S3.setSelected(false);
            total3.setText("--");
        }
        if (fig[3] != null) {
            station4Button.setEnabled(true);
            station4Button.setText(fig[3].getName());
            dd4.setText(fig[3].getDegreeOfDifficulty().toString());
            penaltyHS4.setSelected(false);
            penalty1S4.setSelected(false);
            penalty2S4.setSelected(false);
            FigureScore figureScore = figureScoreMap.get(fig[3]);
            if (figureScore != null) {
                if (figureScore.getTotalScore() != null) {
                    total4.setText(figureScore.getTotalScore().toPlainString());
                } else {
                    total4.setText("NA");
                }
                if (figureScore.getPenalty() != null) {
                    BigDecimal penalty = figureScoreMap.get(fig[3]).getPenalty();
                    int ip = penalty.movePointRight(1).intValue();
                    switch (ip) {
                        case 5:
                            penaltyHS4.setSelected(true);
                            break;
                        case 10:
                            penalty1S4.setSelected(true);
                            break;
                        case 20:
                            penalty2S4.setSelected(true);
                            break;
                    }
                }
            }
            if (station4Button.isSelected()) {
                setEditableRow(4);
            }
        } else {
            if (station4Button.isSelected()) {
                setEditableRow(0);
            }
            station4Button.setEnabled(false);
            station4Button.setText("--");
            dd4.setText("--");
            penaltyHS4.setSelected(false);
            penalty1S4.setSelected(false);
            penalty2S4.setSelected(false);
            total4.setText("--");
        }
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 5; j++) {
                setScoreText(i, j, getScore(i, j));
            }
        }
    }

    private String getScore(int fig, int judge) {
        FigureScore score = figureScoreMap.get(figures[fig - 1]);
        if (score != null) {
            switch (judge) {
                case 1:
                    return score.getScore1().toString();
                case 2:
                    return score.getScore2().toString();
                case 3:
                    return score.getScore3().toString();
                case 4:
                    return score.getScore4().toString();
                case 5:
                    return score.getScore5().toString();
            }
        }
        return "";
    }

    public Collection<FigureScore> getFigureScores() {
        storeScores();
        return figureScoreMap.values();
    }

    public FiguresParticipant getFiguresParticipant() {
        return figuresParticipant;
    }

    private void storeScores() {
        for (int row = 1; row <= 4; row++) {
            storeScore(row);
        }
    }

    private void storeScore(int fig) {
        FigureScore score = figureScoreMap.get(figures[fig - 1]);
        if (score == null) {
            if (getScoreValue(fig, 1) == null && getScoreValue(fig, 2) == null
                    && getScoreValue(fig, 3) == null && getScoreValue(fig, 4) == null
                    && getScoreValue(fig, 5) == null) {
                return;  //Don't make a new one if there's no score entered.
            }
            score = new FigureScore();
            score.setStation(fig);
            score.setFiguresParticipant(figuresParticipant);
            figureScoreMap.put(figures[fig - 1], score);
        }
        score.setScore1(getScoreValue(fig, 1));
        score.setScore2(getScoreValue(fig, 2));
        score.setScore3(getScoreValue(fig, 3));
        score.setScore4(getScoreValue(fig, 4));
        score.setScore5(getScoreValue(fig, 5));
        score.setPenalty(getPenaltyValue(fig));
        score.setTotalScore(new BigDecimal("0.0"));
    }

    public boolean scoresValid() {
        if (figuresParticipant == null || figureScoreMap == null) {
            return false;
        }
        boolean valid = true;
        for (FigureScore score : figureScoreMap.values()) {
            valid &= ScoreController.isValid(score);
        }
        return valid;
    }

    private JTextField getScoreField(int fig, int judge) {
        String compName = "scoreS" + fig + "J" + judge;
        Object comp = null;
        try {
            Field f = this.getClass().getDeclaredField(compName);
            comp = f.get(this);
        } catch (Exception e) {
            logger.error("", e);
        }
        if (comp instanceof JTextField) {
            return (JTextField) comp;
        }
        return null;
    }

    private BigDecimal getScoreValue(int fig, int judge) {
        JTextField f = getScoreField(fig, judge);
        BigDecimal score = null;
        if (f != null) {
            String s = f.getText();
            score = TwoDigitScore.convert(s);
        }
        return score;
    }

    private void setScoreText(int fig, int judge, String text) {
        JTextField f = getScoreField(fig, judge);
        if (f != null) {
            f.setText(text);
        }
    }

    private BigDecimal getPenaltyValue(int fig) {
        BigDecimal penalty = null;
        switch (fig) {
            case 1:
                penalty = getPenalty1();
                break;
            case 2:
                penalty = getPenalty2();
                break;
            case 3:
                penalty = getPenalty3();
                break;
            case 4:
                penalty = getPenalty4();
                break;
        }
        return penalty;
    }

    private BigDecimal getPenalty1() {
        if (penaltyHS1.isSelected()) {
            return new BigDecimal("0.5");
        }
        if (penalty1S1.isSelected()) {
            return new BigDecimal("1.0");
        }
        if (penalty2S1.isSelected()) {
            return new BigDecimal("2.0");
        }
        return new BigDecimal("0.0");
    }

    private BigDecimal getPenalty2() {
        if (penaltyHS2.isSelected()) {
            return new BigDecimal("0.5");
        }
        if (penalty1S2.isSelected()) {
            return new BigDecimal("1.0");
        }
        if (penalty2S2.isSelected()) {
            return new BigDecimal("2.0");
        }
        return new BigDecimal("0.0");
    }

    private BigDecimal getPenalty3() {
        if (penaltyHS3.isSelected()) {
            return new BigDecimal("0.5");
        }
        if (penalty1S3.isSelected()) {
            return new BigDecimal("1.0");
        }
        if (penalty2S3.isSelected()) {
            return new BigDecimal("2.0");
        }
        return new BigDecimal("0.0");
    }

    private BigDecimal getPenalty4() {
        if (penaltyHS4.isSelected()) {
            return new BigDecimal("0.5");
        }
        if (penalty1S4.isSelected()) {
            return new BigDecimal("1.0");
        }
        if (penalty2S4.isSelected()) {
            return new BigDecimal("2.0");
        }
        return new BigDecimal("0.0");
    }

    private void readyToSave() {
        storeScores();
        firePropertyChange("FocusSave", null, null);
    }

    private void setEditableRow(int row) {
        currentRow = row;
        boolean row1 = (row == 1);
        boolean row2 = (row == 2);
        boolean row3 = (row == 3);
        boolean row4 = (row == 4);
        scoreS1J1.setEditable(row1);
        scoreS1J2.setEditable(row1);
        scoreS1J3.setEditable(row1);
        scoreS1J4.setEditable(row1);
        scoreS1J5.setEditable(row1);
        penalty2S1.setEnabled(row1);
        penalty1S1.setEnabled(row1);
        penaltyHS1.setEnabled(row1);

        scoreS2J1.setEditable(row2);
        scoreS2J2.setEditable(row2);
        scoreS2J3.setEditable(row2);
        scoreS2J4.setEditable(row2);
        scoreS2J5.setEditable(row2);
        penalty2S2.setEnabled(row2);
        penalty1S2.setEnabled(row2);
        penaltyHS2.setEnabled(row2);

        scoreS3J1.setEditable(row3);
        scoreS3J2.setEditable(row3);
        scoreS3J3.setEditable(row3);
        scoreS3J4.setEditable(row3);
        scoreS3J5.setEditable(row3);
        penalty2S3.setEnabled(row3);
        penalty1S3.setEnabled(row3);
        penaltyHS3.setEnabled(row3);

        scoreS4J1.setEditable(row4);
        scoreS4J2.setEditable(row4);
        scoreS4J3.setEditable(row4);
        scoreS4J4.setEditable(row4);
        scoreS4J5.setEditable(row4);
        penalty2S4.setEnabled(row4);
        penalty1S4.setEnabled(row4);
        penaltyHS4.setEnabled(row4);

        if (row1) {
            scoreS1J1.requestFocusInWindow();
            scoreS1J1.selectAll();
        }
        if (row2) {
            scoreS2J1.requestFocusInWindow();
            scoreS2J1.selectAll();
        }
        if (row3) {
            scoreS3J1.requestFocusInWindow();
            scoreS3J1.selectAll();
        }
        if (row4) {
            scoreS4J1.requestFocusInWindow();
            scoreS4J1.selectAll();
        }
    }

    protected void moveToNextEditableRow() {
        int newRow = currentRow+1;
        if(currentRow==0) newRow=1;
        else if(currentRow>0 && currentRow<5) {
                if(newRow>4) newRow=1;
        }
        switch(newRow) {
            case 1: station1Button.setSelected(true);
                    break;
            case 2: station2Button.setSelected(true);
                    break;
            case 3: station3Button.setSelected(true);
                    break;
            case 4: station4Button.setSelected(true);
                    break;
        }
        setEditableRow(newRow);
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

        stationSelected = new javax.swing.ButtonGroup();
        station1Button = new javax.swing.JRadioButton();
        station2Button = new javax.swing.JRadioButton();
        station3Button = new javax.swing.JRadioButton();
        station4Button = new javax.swing.JRadioButton();
        scoreS1J1 = new javax.swing.JTextField();
        scoreS1J2 = new javax.swing.JTextField();
        scoreS1J3 = new javax.swing.JTextField();
        scoreS1J4 = new javax.swing.JTextField();
        scoreS1J5 = new javax.swing.JTextField();
        penalty2S1 = new javax.swing.JCheckBox();
        penalty1S1 = new javax.swing.JCheckBox();
        penaltyHS1 = new javax.swing.JCheckBox();
        dd1 = new javax.swing.JLabel();
        total1 = new javax.swing.JLabel();
        scoreS2J1 = new javax.swing.JTextField();
        scoreS2J2 = new javax.swing.JTextField();
        scoreS2J3 = new javax.swing.JTextField();
        scoreS2J4 = new javax.swing.JTextField();
        scoreS2J5 = new javax.swing.JTextField();
        penalty2S2 = new javax.swing.JCheckBox();
        penalty1S2 = new javax.swing.JCheckBox();
        penaltyHS2 = new javax.swing.JCheckBox();
        dd2 = new javax.swing.JLabel();
        total2 = new javax.swing.JLabel();
        scoreS3J1 = new javax.swing.JTextField();
        scoreS3J2 = new javax.swing.JTextField();
        scoreS3J3 = new javax.swing.JTextField();
        scoreS3J4 = new javax.swing.JTextField();
        scoreS3J5 = new javax.swing.JTextField();
        penalty2S3 = new javax.swing.JCheckBox();
        penalty1S3 = new javax.swing.JCheckBox();
        penaltyHS3 = new javax.swing.JCheckBox();
        dd3 = new javax.swing.JLabel();
        total3 = new javax.swing.JLabel();
        scoreS4J1 = new javax.swing.JTextField();
        scoreS4J2 = new javax.swing.JTextField();
        scoreS4J3 = new javax.swing.JTextField();
        scoreS4J4 = new javax.swing.JTextField();
        scoreS4J5 = new javax.swing.JTextField();
        penalty2S4 = new javax.swing.JCheckBox();
        penalty1S4 = new javax.swing.JCheckBox();
        penaltyHS4 = new javax.swing.JCheckBox();
        dd4 = new javax.swing.JLabel();
        total4 = new javax.swing.JLabel();
        ddLabel = new javax.swing.JLabel();
        totalLabel = new javax.swing.JLabel();
        judge1Label = new javax.swing.JLabel();
        judge2Label = new javax.swing.JLabel();
        judge3Label = new javax.swing.JLabel();
        judge4Label = new javax.swing.JLabel();
        judge5Label = new javax.swing.JLabel();
        penaltyLabel = new javax.swing.JLabel();
        figuresTotalLabel = new javax.swing.JLabel();
        figuresTotal = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setMinimumSize(new java.awt.Dimension(725, 127));
        setPreferredSize(new java.awt.Dimension(725, 127));
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });
        setLayout(new java.awt.GridBagLayout());

        stationSelected.add(station1Button);
        station1Button.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        station1Button.setText("One");
        station1Button.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        station1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                station1ButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(station1Button, gridBagConstraints);

        stationSelected.add(station2Button);
        station2Button.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        station2Button.setText("Two");
        station2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                station2ButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(station2Button, gridBagConstraints);

        stationSelected.add(station3Button);
        station3Button.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        station3Button.setText("Three");
        station3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                station3ButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(station3Button, gridBagConstraints);

        stationSelected.add(station4Button);
        station4Button.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        station4Button.setText("Four");
        station4Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                station4ButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(station4Button, gridBagConstraints);

        scoreS1J1.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS1J1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS1J1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS1J1, gridBagConstraints);

        scoreS1J2.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS1J2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS1J2KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS1J2, gridBagConstraints);

        scoreS1J3.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS1J3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS1J3KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS1J3, gridBagConstraints);

        scoreS1J4.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS1J4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS1J4KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS1J4, gridBagConstraints);

        scoreS1J5.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS1J5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS1J5KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS1J5, gridBagConstraints);

        penalty2S1.setFont(new java.awt.Font("Tahoma", 0, 14));
        penalty2S1.setText("2 Point");
        penalty2S1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penalty2S1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(penalty2S1, gridBagConstraints);

        penalty1S1.setFont(new java.awt.Font("Tahoma", 0, 14));
        penalty1S1.setText("1 Point");
        penalty1S1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penalty1S1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 1;
        add(penalty1S1, gridBagConstraints);

        penaltyHS1.setFont(new java.awt.Font("Tahoma", 0, 14));
        penaltyHS1.setText("1/2 Point");
        penaltyHS1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penaltyHS1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        add(penaltyHS1, gridBagConstraints);

        dd1.setFont(new java.awt.Font("Tahoma", 0, 14));
        dd1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dd1.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(dd1, gridBagConstraints);

        total1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        total1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total1.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(total1, gridBagConstraints);

        scoreS2J1.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS2J1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS2J1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS2J1, gridBagConstraints);

        scoreS2J2.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS2J2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS2J2KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS2J2, gridBagConstraints);

        scoreS2J3.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS2J3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS2J3KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS2J3, gridBagConstraints);

        scoreS2J4.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS2J4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS2J4KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS2J4, gridBagConstraints);

        scoreS2J5.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS2J5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS2J5KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS2J5, gridBagConstraints);

        penalty2S2.setFont(new java.awt.Font("Tahoma", 0, 14));
        penalty2S2.setText("2 Point");
        penalty2S2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penalty2S2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(penalty2S2, gridBagConstraints);

        penalty1S2.setFont(new java.awt.Font("Tahoma", 0, 14));
        penalty1S2.setText("1 Point");
        penalty1S2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penalty1S2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        add(penalty1S2, gridBagConstraints);

        penaltyHS2.setFont(new java.awt.Font("Tahoma", 0, 14));
        penaltyHS2.setText("1/2 Point");
        penaltyHS2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penaltyHS2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 10;
        add(penaltyHS2, gridBagConstraints);

        dd2.setFont(new java.awt.Font("Tahoma", 0, 14));
        dd2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dd2.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(dd2, gridBagConstraints);

        total2.setFont(new java.awt.Font("Tahoma", 0, 14));
        total2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total2.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(total2, gridBagConstraints);

        scoreS3J1.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS3J1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS3J1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS3J1, gridBagConstraints);

        scoreS3J2.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS3J2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS3J2KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS3J2, gridBagConstraints);

        scoreS3J3.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS3J3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS3J3KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS3J3, gridBagConstraints);

        scoreS3J4.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS3J4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS3J4KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS3J4, gridBagConstraints);

        scoreS3J5.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS3J5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS3J5KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS3J5, gridBagConstraints);

        penalty2S3.setFont(new java.awt.Font("Tahoma", 0, 14));
        penalty2S3.setText("2 Point");
        penalty2S3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penalty2S3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(penalty2S3, gridBagConstraints);

        penalty1S3.setFont(new java.awt.Font("Tahoma", 0, 14));
        penalty1S3.setText("1 Point");
        penalty1S3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penalty1S3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 3;
        add(penalty1S3, gridBagConstraints);

        penaltyHS3.setFont(new java.awt.Font("Tahoma", 0, 14));
        penaltyHS3.setText("1/2 Point");
        penaltyHS3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penaltyHS3ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 10;
        add(penaltyHS3, gridBagConstraints);

        dd3.setFont(new java.awt.Font("Tahoma", 0, 14));
        dd3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dd3.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(dd3, gridBagConstraints);

        total3.setFont(new java.awt.Font("Tahoma", 0, 14));
        total3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total3.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(total3, gridBagConstraints);

        scoreS4J1.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS4J1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS4J1KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS4J1, gridBagConstraints);

        scoreS4J2.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS4J2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS4J2KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS4J2, gridBagConstraints);

        scoreS4J3.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS4J3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS4J3KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS4J3, gridBagConstraints);

        scoreS4J4.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS4J4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS4J4KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS4J4, gridBagConstraints);

        scoreS4J5.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreS4J5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreS4J5KeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(scoreS4J5, gridBagConstraints);

        penalty2S4.setFont(new java.awt.Font("Tahoma", 0, 14));
        penalty2S4.setText("2 Point");
        penalty2S4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penalty2S4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(penalty2S4, gridBagConstraints);

        penalty1S4.setFont(new java.awt.Font("Tahoma", 0, 14));
        penalty1S4.setText("1 Point");
        penalty1S4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penalty1S4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        add(penalty1S4, gridBagConstraints);

        penaltyHS4.setFont(new java.awt.Font("Tahoma", 0, 14));
        penaltyHS4.setText("1/2 Point");
        penaltyHS4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                penaltyHS4ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 10;
        add(penaltyHS4, gridBagConstraints);

        dd4.setFont(new java.awt.Font("Tahoma", 0, 14));
        dd4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        dd4.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(dd4, gridBagConstraints);

        total4.setFont(new java.awt.Font("Tahoma", 0, 14));
        total4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        total4.setText("0");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(total4, gridBagConstraints);

        ddLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        ddLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        ddLabel.setText("DD");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 10;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        add(ddLabel, gridBagConstraints);

        totalLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        totalLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalLabel.setText("Total");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 5;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        add(totalLabel, gridBagConstraints);

        judge1Label.setFont(new java.awt.Font("Tahoma", 0, 14));
        judge1Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        judge1Label.setText("Judge 1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(judge1Label, gridBagConstraints);

        judge2Label.setFont(new java.awt.Font("Tahoma", 0, 14));
        judge2Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        judge2Label.setText("Judge 2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(judge2Label, gridBagConstraints);

        judge3Label.setFont(new java.awt.Font("Tahoma", 0, 14));
        judge3Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        judge3Label.setText("Judge 3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(judge3Label, gridBagConstraints);

        judge4Label.setFont(new java.awt.Font("Tahoma", 0, 14));
        judge4Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        judge4Label.setText("Judge 4");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(judge4Label, gridBagConstraints);

        judge5Label.setFont(new java.awt.Font("Tahoma", 0, 14));
        judge5Label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        judge5Label.setText("Judge 5");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 4);
        add(judge5Label, gridBagConstraints);

        penaltyLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        penaltyLabel.setText("Penalty");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
        add(penaltyLabel, gridBagConstraints);

        figuresTotalLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        figuresTotalLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        figuresTotalLabel.setText("Figures Total:");
        figuresTotalLabel.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        add(figuresTotalLabel, gridBagConstraints);

        figuresTotal.setFont(new java.awt.Font("Tahoma", 0, 14));
        figuresTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        figuresTotal.setText("--");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(figuresTotal, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    private void penalty2S3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penalty2S3ActionPerformed
        if (penalty2S3.isSelected()) {
            penalty1S3.setSelected(false);
            penaltyHS3.setSelected(false);
        }
}//GEN-LAST:event_penalty2S3ActionPerformed

    private void station1ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_station1ButtonActionPerformed
        setEditableRow(1);
}//GEN-LAST:event_station1ButtonActionPerformed

    private void penalty2S2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penalty2S2ActionPerformed
        if (penalty2S2.isSelected()) {
            penalty1S2.setSelected(false);
            penaltyHS2.setSelected(false);
        }
}//GEN-LAST:event_penalty2S2ActionPerformed

    private void penalty2S4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penalty2S4ActionPerformed
        if (penalty2S4.isSelected()) {
            penalty1S4.setSelected(false);
            penaltyHS4.setSelected(false);
        }
}//GEN-LAST:event_penalty2S4ActionPerformed

    private void penalty2S1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penalty2S1ActionPerformed
        if (penalty2S1.isSelected()) {
            penalty1S1.setSelected(false);
            penaltyHS1.setSelected(false);
        }
}//GEN-LAST:event_penalty2S1ActionPerformed

    private void station2ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_station2ButtonActionPerformed
        setEditableRow(2);
    }//GEN-LAST:event_station2ButtonActionPerformed

    private void station3ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_station3ButtonActionPerformed
        setEditableRow(3);
    }//GEN-LAST:event_station3ButtonActionPerformed

    private void station4ButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_station4ButtonActionPerformed
        setEditableRow(4);
    }//GEN-LAST:event_station4ButtonActionPerformed

    private void penalty1S1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penalty1S1ActionPerformed
        if (penalty1S1.isSelected()) {
            penalty2S1.setSelected(false);
            penaltyHS1.setSelected(false);
        }
    }//GEN-LAST:event_penalty1S1ActionPerformed

    private void penaltyHS1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penaltyHS1ActionPerformed
        if (penaltyHS1.isSelected()) {
            penalty2S1.setSelected(false);
            penalty1S1.setSelected(false);
        }
    }//GEN-LAST:event_penaltyHS1ActionPerformed

    private void penalty1S2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penalty1S2ActionPerformed
        if (penalty1S2.isSelected()) {
            penalty2S2.setSelected(false);
            penaltyHS2.setSelected(false);
        }
    }//GEN-LAST:event_penalty1S2ActionPerformed

    private void penaltyHS2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penaltyHS2ActionPerformed
        if (penaltyHS2.isSelected()) {
            penalty2S2.setSelected(false);
            penalty1S2.setSelected(false);
        }
    }//GEN-LAST:event_penaltyHS2ActionPerformed

    private void penalty1S3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penalty1S3ActionPerformed
        if (penalty1S3.isSelected()) {
            penalty2S3.setSelected(false);
            penaltyHS3.setSelected(false);
        }
    }//GEN-LAST:event_penalty1S3ActionPerformed

    private void penaltyHS3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penaltyHS3ActionPerformed
        if (penaltyHS3.isSelected()) {
            penalty2S3.setSelected(false);
            penalty1S3.setSelected(false);
        }
    }//GEN-LAST:event_penaltyHS3ActionPerformed

    private void penalty1S4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penalty1S4ActionPerformed
        if (penalty1S4.isSelected()) {
            penalty2S4.setSelected(false);
            penaltyHS4.setSelected(false);
        }
    }//GEN-LAST:event_penalty1S4ActionPerformed

    private void penaltyHS4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_penaltyHS4ActionPerformed
        if (penaltyHS4.isSelected()) {
            penalty2S4.setSelected(false);
            penalty1S4.setSelected(false);
        }
    }//GEN-LAST:event_penaltyHS4ActionPerformed

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        setEditableRow(currentRow);
    }//GEN-LAST:event_formFocusGained

    private void scoreS1J1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS1J1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS1J2.requestFocusInWindow();
            scoreS1J2.selectAll();
        }
    }//GEN-LAST:event_scoreS1J1KeyPressed

    private void scoreS1J2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS1J2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS1J3.requestFocusInWindow();
            scoreS1J3.selectAll();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS1J2.getText().length()==0) {
            scoreS1J1.requestFocusInWindow();
            scoreS1J1.selectAll();
        }
    }//GEN-LAST:event_scoreS1J2KeyPressed

    private void scoreS1J3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS1J3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS1J4.requestFocusInWindow();
            scoreS1J4.selectAll();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS1J3.getText().length()==0) {
            scoreS1J2.requestFocusInWindow();
            scoreS1J2.selectAll();
        }
    }//GEN-LAST:event_scoreS1J3KeyPressed

    private void scoreS1J4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS1J4KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS1J5.requestFocusInWindow();
            scoreS1J5.selectAll();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS1J4.getText().length()==0) {
            scoreS1J3.requestFocusInWindow();
            scoreS1J3.selectAll();
        }
    }//GEN-LAST:event_scoreS1J4KeyPressed

    private void scoreS1J5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS1J5KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            readyToSave();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS1J5.getText().length()==0) {
            scoreS1J4.requestFocusInWindow();
            scoreS1J4.selectAll();
        }
    }//GEN-LAST:event_scoreS1J5KeyPressed

    private void scoreS2J1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS2J1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS2J2.requestFocusInWindow();
            scoreS2J2.selectAll();
        }
    }//GEN-LAST:event_scoreS2J1KeyPressed

    private void scoreS2J2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS2J2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS2J3.requestFocusInWindow();
            scoreS2J3.selectAll();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS2J2.getText().length()==0) {
            scoreS2J1.requestFocusInWindow();
            scoreS2J1.selectAll();
        }
    }//GEN-LAST:event_scoreS2J2KeyPressed

    private void scoreS2J3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS2J3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS2J4.requestFocusInWindow();
            scoreS2J4.selectAll();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS2J3.getText().length()==0) {
            scoreS2J2.requestFocusInWindow();
            scoreS2J2.selectAll();
        }
    }//GEN-LAST:event_scoreS2J3KeyPressed

    private void scoreS2J4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS2J4KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS2J5.requestFocusInWindow();
            scoreS2J5.selectAll();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS2J4.getText().length()==0) {
            scoreS2J3.requestFocusInWindow();
            scoreS2J3.selectAll();
        }
    }//GEN-LAST:event_scoreS2J4KeyPressed

    private void scoreS2J5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS2J5KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            readyToSave();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS2J5.getText().length()==0) {
            scoreS2J4.requestFocusInWindow();
            scoreS2J4.selectAll();
        }
    }//GEN-LAST:event_scoreS2J5KeyPressed

    private void scoreS3J1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS3J1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS3J2.requestFocusInWindow();
            scoreS3J2.selectAll();
        }
    }//GEN-LAST:event_scoreS3J1KeyPressed

    private void scoreS3J2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS3J2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS3J3.requestFocusInWindow();
            scoreS3J3.selectAll();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS3J2.getText().length()==0) {
            scoreS3J1.requestFocusInWindow();
            scoreS3J1.selectAll();
        }
    }//GEN-LAST:event_scoreS3J2KeyPressed

    private void scoreS3J3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS3J3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS3J4.requestFocusInWindow();
            scoreS3J4.selectAll();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS3J3.getText().length()==0) {
            scoreS3J2.requestFocusInWindow();
            scoreS3J2.selectAll();
        }
    }//GEN-LAST:event_scoreS3J3KeyPressed

    private void scoreS3J4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS3J4KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS3J5.requestFocusInWindow();
            scoreS3J5.selectAll();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS3J4.getText().length()==0) {
            scoreS3J3.requestFocusInWindow();
            scoreS3J3.selectAll();
        }
    }//GEN-LAST:event_scoreS3J4KeyPressed

    private void scoreS3J5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS3J5KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            readyToSave();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS3J5.getText().length()==0) {
            scoreS3J4.requestFocusInWindow();
            scoreS3J4.selectAll();
        }
    }//GEN-LAST:event_scoreS3J5KeyPressed

    private void scoreS4J1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS4J1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS4J2.requestFocusInWindow();
            scoreS4J2.selectAll();
        }
    }//GEN-LAST:event_scoreS4J1KeyPressed

    private void scoreS4J2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS4J2KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS4J3.requestFocusInWindow();
            scoreS4J3.selectAll();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS4J2.getText().length()==0) {
            scoreS4J1.requestFocusInWindow();
            scoreS4J1.selectAll();
        }
    }//GEN-LAST:event_scoreS4J2KeyPressed

    private void scoreS4J3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS4J3KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS4J4.requestFocusInWindow();
            scoreS4J4.selectAll();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS4J3.getText().length()==0) {
            scoreS4J2.requestFocusInWindow();
            scoreS4J2.selectAll();
        }
    }//GEN-LAST:event_scoreS4J3KeyPressed

    private void scoreS4J4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS4J4KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreS4J5.requestFocusInWindow();
            scoreS4J5.selectAll();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS4J4.getText().length()==0) {
            scoreS4J3.requestFocusInWindow();
            scoreS4J3.selectAll();
        }
    }//GEN-LAST:event_scoreS4J4KeyPressed

    private void scoreS4J5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreS4J5KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            readyToSave();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreS4J5.getText().length()==0) {
            scoreS4J4.requestFocusInWindow();
            scoreS4J4.selectAll();
        }
    }//GEN-LAST:event_scoreS4J5KeyPressed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dd1;
    private javax.swing.JLabel dd2;
    private javax.swing.JLabel dd3;
    private javax.swing.JLabel dd4;
    private javax.swing.JLabel ddLabel;
    private javax.swing.JLabel figuresTotal;
    private javax.swing.JLabel figuresTotalLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel judge1Label;
    private javax.swing.JLabel judge2Label;
    private javax.swing.JLabel judge3Label;
    private javax.swing.JLabel judge4Label;
    private javax.swing.JLabel judge5Label;
    private javax.swing.JCheckBox penalty1S1;
    private javax.swing.JCheckBox penalty1S2;
    private javax.swing.JCheckBox penalty1S3;
    private javax.swing.JCheckBox penalty1S4;
    private javax.swing.JCheckBox penalty2S1;
    private javax.swing.JCheckBox penalty2S2;
    private javax.swing.JCheckBox penalty2S3;
    private javax.swing.JCheckBox penalty2S4;
    private javax.swing.JCheckBox penaltyHS1;
    private javax.swing.JCheckBox penaltyHS2;
    private javax.swing.JCheckBox penaltyHS3;
    private javax.swing.JCheckBox penaltyHS4;
    private javax.swing.JLabel penaltyLabel;
    private javax.swing.JTextField scoreS1J1;
    private javax.swing.JTextField scoreS1J2;
    private javax.swing.JTextField scoreS1J3;
    private javax.swing.JTextField scoreS1J4;
    private javax.swing.JTextField scoreS1J5;
    private javax.swing.JTextField scoreS2J1;
    private javax.swing.JTextField scoreS2J2;
    private javax.swing.JTextField scoreS2J3;
    private javax.swing.JTextField scoreS2J4;
    private javax.swing.JTextField scoreS2J5;
    private javax.swing.JTextField scoreS3J1;
    private javax.swing.JTextField scoreS3J2;
    private javax.swing.JTextField scoreS3J3;
    private javax.swing.JTextField scoreS3J4;
    private javax.swing.JTextField scoreS3J5;
    private javax.swing.JTextField scoreS4J1;
    private javax.swing.JTextField scoreS4J2;
    private javax.swing.JTextField scoreS4J3;
    private javax.swing.JTextField scoreS4J4;
    private javax.swing.JTextField scoreS4J5;
    private javax.swing.JRadioButton station1Button;
    private javax.swing.JRadioButton station2Button;
    private javax.swing.JRadioButton station3Button;
    private javax.swing.JRadioButton station4Button;
    private javax.swing.ButtonGroup stationSelected;
    private javax.swing.JLabel total1;
    private javax.swing.JLabel total2;
    private javax.swing.JLabel total3;
    private javax.swing.JLabel total4;
    private javax.swing.JLabel totalLabel;
    // End of variables declaration//GEN-END:variables
}
