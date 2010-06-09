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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import org.aquastarz.score.controller.RoutinesController;
import org.aquastarz.score.domain.Routine;
import org.aquastarz.score.domain.RoutineLevel;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.util.TwoDigitScore;

public class RoutinesPanel extends javax.swing.JPanel {

    private boolean modified = false;
    private List<Routine> routines = null;
    private Routine curRoutine = null;
    private RoutinesController controller = new RoutinesController(this);

    /** Creates new form RoutinesPanel */
    public RoutinesPanel() {
        initComponents();
        updateRoutinesList();
        updateComponentEnabledStatus();
        updateCombos();
    }

    private ListSelectionModel getRoutineListSelectionModel() {
        VetoableListSelectionModel vtsm = new VetoableListSelectionModel();
        vtsm.addVetoableChangeListener(new VetoableChangeListener() {

            public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
                if (modified && curRoutine != null) {

                    //Ignore "changes" to the current routine
                    if (routineList.getModel().getElementAt((Integer) evt.getNewValue()) == curRoutine) {
                        return;
                    }

                    boolean success = offerSave();
                    if (!success) {
                        throw new PropertyVetoException("Change canceled", evt);
                    }
                }
            }
        });
        return vtsm;
    }

    private void updateRoutinesList() {
        Object o = routineList.getSelectedValue();
        Routine selectedRoutine = null;
        if (o instanceof Routine) {
            selectedRoutine = (Routine) o;
        }
        routines = controller.getRoutinesList();
        DefaultListModel dlm = new DefaultListModel();
        for (Routine routine : routines) {
            dlm.addElement(routine);
        }
        routineList.setModel(dlm);
        if (selectedRoutine != null) {
            routineList.setSelectedValue(selectedRoutine, true);
        }
    }

    private void updateComponentEnabledStatus() {
        title.setEnabled(curRoutine != null);
        names1.setEnabled(curRoutine != null);
        names2.setEnabled(curRoutine != null);
        scoreTJ1.setEnabled(curRoutine != null);
        scoreTJ2.setEnabled(curRoutine != null);
        scoreTJ3.setEnabled(curRoutine != null);
        scoreTJ4.setEnabled(curRoutine != null);
        scoreTJ5.setEnabled(curRoutine != null);
        scoreTJ6.setEnabled(curRoutine != null);
        scoreTJ7.setEnabled(curRoutine != null);
        scoreAJ1.setEnabled(curRoutine != null);
        scoreAJ2.setEnabled(curRoutine != null);
        scoreAJ3.setEnabled(curRoutine != null);
        scoreAJ4.setEnabled(curRoutine != null);
        scoreAJ5.setEnabled(curRoutine != null);
        scoreAJ6.setEnabled(curRoutine != null);
        scoreAJ7.setEnabled(curRoutine != null);
        penalty.setEnabled(curRoutine != null);
        levelCombo.setEnabled(curRoutine != null);
        routineTypeCombo.setEnabled(curRoutine != null);
        teamCombo.setEnabled(curRoutine != null);
        if (routines != null && routines.size() > 0) {
            randomizeButton.setEnabled(true);
            printButton.setEnabled(true);
            if (routineList.getSelectedValue() != null) {
                deleteButton.setEnabled(true);
                saveButton.setEnabled(modified);
            } else {
                deleteButton.setEnabled(false);
                saveButton.setEnabled(false);
            }
        } else {
            randomizeButton.setEnabled(false);
            printButton.setEnabled(false);
            deleteButton.setEnabled(false);
            saveButton.setEnabled(false);
        }
    }

    private void updateCombos() {
        // Routine Levels
        List<RoutineLevel> routineLevels = controller.getRoutineLevels();
        DefaultComboBoxModel dcbm = new DefaultComboBoxModel();
        for (RoutineLevel rl : routineLevels) {
            dcbm.addElement(rl);
        }
        levelCombo.setModel(dcbm);

        //Teams
        List<Team> teams = controller.getTeams();
        dcbm = new DefaultComboBoxModel();
        for (Team team : teams) {
            dcbm.addElement(team);
        }
        teamCombo.setModel(dcbm);
    }

    //Returns false if the user cancels, otherwise true
    private boolean offerSave() {
        int confirm = JOptionPane.showConfirmDialog(this, "This routine has been modified.  Do you want to save changes?", "Save Changes?", JOptionPane.YES_NO_CANCEL_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            save();
            return true;
        } else if (confirm == JOptionPane.NO_OPTION) {
            revert();
            return true;
        }
        return false;
    }

    private void save() {
        try {
            updateRoutine(curRoutine);
            controller.save(curRoutine);
            modified = false;
            updateRoutinesList();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "There is an entry error.  Please check and try again.", "Entry Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void revert() {
        updateFields();
    }

    private void calculate() {
        try {
            updateRoutine(curRoutine);
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.OK_OPTION);
            return;
        }
        controller.calculate(curRoutine);
        updateFields();
    }

    private void updateRoutine(Routine routine) throws Exception {
        routine.setName(title.getText());
        routine.setSwimmers1(names1.getText());
        routine.setSwimmers2(names2.getText());
        routine.setLevel((RoutineLevel) levelCombo.getSelectedItem());
        routine.setRoutineType((String) routineTypeCombo.getSelectedItem());
        routine.setTeam((Team) teamCombo.getSelectedItem());
        BigDecimal score = TwoDigitScore.convert(scoreTJ1.getText());
        if (score == null && scoreTJ1.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid score entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            scoreTJ1.selectAll();
            throw new Exception("Invalid entry");
        }
        routine.setTScore1(score);
        score = TwoDigitScore.convert(scoreTJ2.getText());
        if (score == null && scoreTJ2.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid score entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            scoreTJ2.selectAll();
            throw new Exception("Invalid entry");
        }
        routine.setTScore2(score);
        score = TwoDigitScore.convert(scoreTJ3.getText());
        if (score == null && scoreTJ3.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid score entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            scoreTJ3.selectAll();
            throw new Exception("Invalid entry");
        }
        routine.setTScore3(score);
        score = TwoDigitScore.convert(scoreTJ4.getText());
        if (score == null && scoreTJ4.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid score entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            scoreTJ4.selectAll();
            throw new Exception("Invalid entry");
        }
        routine.setTScore4(score);
        score = TwoDigitScore.convert(scoreTJ5.getText());
        if (score == null && scoreTJ5.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid score entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            scoreTJ5.selectAll();
            throw new Exception("Invalid entry");
        }
        routine.setTScore5(score);
        score = TwoDigitScore.convert(scoreTJ6.getText());
        if (score == null && scoreTJ6.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid score entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            scoreTJ6.selectAll();
            throw new Exception("Invalid entry");
        }
        routine.setTScore6(score);
        score = TwoDigitScore.convert(scoreTJ7.getText());
        if (score == null && scoreTJ7.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid score entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            scoreTJ7.selectAll();
            throw new Exception("Invalid entry");
        }
        routine.setTScore7(score);
        score = TwoDigitScore.convert(scoreAJ1.getText());
        if (score == null && scoreAJ1.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid score entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            scoreAJ1.selectAll();
            throw new Exception("Invalid entry");
        }
        routine.setAScore1(score);
        score = TwoDigitScore.convert(scoreAJ2.getText());
        if (score == null && scoreAJ2.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid score entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            scoreAJ2.selectAll();
            throw new Exception("Invalid entry");
        }
        routine.setAScore2(score);
        score = TwoDigitScore.convert(scoreAJ3.getText());
        if (score == null && scoreAJ3.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid score entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            scoreAJ3.selectAll();
            throw new Exception("Invalid entry");
        }
        routine.setAScore3(score);
        score = TwoDigitScore.convert(scoreAJ4.getText());
        if (score == null && scoreAJ4.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid score entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            scoreAJ4.selectAll();
            throw new Exception("Invalid entry");
        }
        routine.setAScore4(score);
        score = TwoDigitScore.convert(scoreAJ5.getText());
        if (score == null && scoreAJ5.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid score entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            scoreAJ5.selectAll();
            throw new Exception("Invalid entry");
        }
        routine.setAScore5(score);
        score = TwoDigitScore.convert(scoreAJ6.getText());
        if (score == null && scoreAJ6.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid score entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            scoreAJ6.selectAll();
            throw new Exception("Invalid entry");
        }
        routine.setAScore6(score);
        score = TwoDigitScore.convert(scoreAJ7.getText());
        if (score == null && scoreAJ7.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid score entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            scoreAJ7.selectAll();
            throw new Exception("Invalid entry");
        }
        routine.setAScore7(score);
        score = TwoDigitScore.convert(penalty.getText());
        if (score == null && penalty.getText().trim().length() > 0) {
            JOptionPane.showMessageDialog(this, "Invalid penalty entry.", "Entry Error", JOptionPane.WARNING_MESSAGE);
            penalty.selectAll();
            throw new Exception("Invalid entry");
        }
        if(score==null) score = BigDecimal.ZERO.setScale(1);
        routine.setPenalty(score);
    }

    private void updateFields() {
        title.setText(curRoutine.getName());
        names1.setText(curRoutine.getSwimmers1());
        names2.setText(curRoutine.getSwimmers2());
        levelCombo.setSelectedItem(curRoutine.getLevel());
        routineTypeCombo.setSelectedItem(curRoutine.getRoutineType());
        teamCombo.setSelectedItem(curRoutine.getTeam());
        if (curRoutine.getTScore1() != null) {
            scoreTJ1.setText(curRoutine.getTScore1().toString());
        }
        else {
            scoreTJ1.setText("");
        }
        if (curRoutine.getTScore2() != null) {
            scoreTJ2.setText(curRoutine.getTScore2().toString());
        }
        else {
            scoreTJ2.setText("");
        }
        if (curRoutine.getTScore3() != null) {
            scoreTJ3.setText(curRoutine.getTScore3().toString());
        }
        else {
            scoreTJ3.setText("");
        }
        if (curRoutine.getTScore4() != null) {
            scoreTJ4.setText(curRoutine.getTScore4().toString());
        }
        else {
            scoreTJ4.setText("");
        }
        if (curRoutine.getTScore5() != null) {
            scoreTJ5.setText(curRoutine.getTScore5().toString());
        }
        else {
            scoreTJ5.setText("");
        }
        if (curRoutine.getTScore6() != null) {
            scoreTJ6.setText(curRoutine.getTScore6().toString());
        }
        else {
            scoreTJ6.setText("");
        }
        if (curRoutine.getTScore7() != null) {
            scoreTJ7.setText(curRoutine.getTScore7().toString());
        }
        else {
            scoreTJ7.setText("");
        }
        if (curRoutine.getAScore1() != null) {
            scoreAJ1.setText(curRoutine.getAScore1().toString());
        }
        else {
            scoreAJ1.setText("");
        }
        if (curRoutine.getAScore2() != null) {
            scoreAJ2.setText(curRoutine.getAScore2().toString());
        }
        else {
            scoreAJ2.setText("");
        }
        if (curRoutine.getAScore3() != null) {
            scoreAJ3.setText(curRoutine.getAScore3().toString());
        }
        else {
            scoreAJ3.setText("");
        }
        if (curRoutine.getAScore4() != null) {
            scoreAJ4.setText(curRoutine.getAScore4().toString());
        }
        else {
            scoreAJ4.setText("");
        }
        if (curRoutine.getAScore5() != null) {
            scoreAJ5.setText(curRoutine.getAScore5().toString());
        }
        else {
            scoreAJ5.setText("");
        }
        if (curRoutine.getAScore6() != null) {
            scoreAJ6.setText(curRoutine.getAScore6().toString());
        }
        else {
            scoreAJ6.setText("");
        }
        if (curRoutine.getAScore7() != null) {
            scoreAJ7.setText(curRoutine.getAScore7().toString());
        }
        else {
            scoreAJ7.setText("");
        }
        if (curRoutine.getPenalty() != null) {
            penalty.setText(curRoutine.getPenalty().toString());
        }
        else {
            penalty.setText("");
        }
        if (curRoutine.getTotalScore() != null) {
            totalScore.setText(curRoutine.getTotalScore().toString());
        }
        else {
            totalScore.setText("");
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

        jScrollPane1 = new javax.swing.JScrollPane();
        routineList = new javax.swing.JList();
        addButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        levelCombo = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        routineTypeCombo = new javax.swing.JComboBox();
        teamCombo = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        title = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        names1 = new javax.swing.JTextField();
        names2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        scoreTJ1 = new javax.swing.JTextField();
        scoreTJ2 = new javax.swing.JTextField();
        scoreTJ3 = new javax.swing.JTextField();
        scoreTJ4 = new javax.swing.JTextField();
        scoreTJ5 = new javax.swing.JTextField();
        scoreTJ6 = new javax.swing.JTextField();
        scoreTJ7 = new javax.swing.JTextField();
        scoreAJ7 = new javax.swing.JTextField();
        scoreAJ2 = new javax.swing.JTextField();
        scoreAJ3 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        scoreAJ1 = new javax.swing.JTextField();
        scoreAJ4 = new javax.swing.JTextField();
        scoreAJ6 = new javax.swing.JTextField();
        scoreAJ5 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        penalty = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        totalScore = new javax.swing.JTextField();
        printButton = new javax.swing.JButton();
        randomizeButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        routineList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        routineList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        routineList.setSelectionModel(getRoutineListSelectionModel());
        routineList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                routineListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(routineList);

        addButton.setFont(new java.awt.Font("Tahoma", 0, 14));
        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        deleteButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        deleteButton.setText("Delete");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        levelCombo.setFont(new java.awt.Font("Tahoma", 0, 14));
        levelCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        levelCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                levelComboActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel1.setText("Level");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel2.setText("Type");

        routineTypeCombo.setFont(new java.awt.Font("Tahoma", 0, 14));
        routineTypeCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Solo", "Duet", "Trio", "Team" }));
        routineTypeCombo.setMinimumSize(new java.awt.Dimension(59, 20));
        routineTypeCombo.setPreferredSize(new java.awt.Dimension(63, 22));
        routineTypeCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                routineTypeComboActionPerformed(evt);
            }
        });

        teamCombo.setFont(new java.awt.Font("Tahoma", 0, 14));
        teamCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        teamCombo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                teamComboActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel3.setText("Team");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel4.setText("Title");

        title.setFont(new java.awt.Font("Tahoma", 0, 14));
        title.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                handleFieldChange(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel5.setText("Swimmer Names");

        names1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        names1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                handleFieldChange(evt);
            }
        });

        names2.setFont(new java.awt.Font("Tahoma", 0, 14));
        names2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                handleFieldChange(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel6.setText("Technical Merit:");

        scoreTJ1.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreTJ1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreTJ1KeyPressed(evt);
            }
        });

        scoreTJ2.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreTJ2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreTJ2KeyPressed(evt);
            }
        });

        scoreTJ3.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreTJ3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreTJ3KeyPressed(evt);
            }
        });

        scoreTJ4.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreTJ4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreTJ4KeyPressed(evt);
            }
        });

        scoreTJ5.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreTJ5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreTJ5KeyPressed(evt);
            }
        });

        scoreTJ6.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreTJ6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreTJ6KeyPressed(evt);
            }
        });

        scoreTJ7.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreTJ7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreTJ7KeyPressed(evt);
            }
        });

        scoreAJ7.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreAJ7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreAJ7KeyPressed(evt);
            }
        });

        scoreAJ2.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreAJ2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreAJ2KeyPressed(evt);
            }
        });

        scoreAJ3.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreAJ3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreAJ3KeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel7.setText("Artistic Impression:");

        scoreAJ1.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreAJ1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreAJ1KeyPressed(evt);
            }
        });

        scoreAJ4.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreAJ4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreAJ4KeyPressed(evt);
            }
        });

        scoreAJ6.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreAJ6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreAJ6KeyPressed(evt);
            }
        });

        scoreAJ5.setFont(new java.awt.Font("Tahoma", 0, 14));
        scoreAJ5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                scoreAJ5KeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel8.setText("Judge 1");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel9.setText("Judge 2");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel10.setText("Judge 3");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel11.setText("Judge 4");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel12.setText("Judge 5");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel13.setText("Judge 6");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel14.setText("Judge 7");

        jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel15.setText("Penalty:");

        penalty.setFont(new java.awt.Font("Tahoma", 0, 14));
        penalty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                penaltyKeyPressed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel16.setText("Score:");

        totalScore.setEditable(false);
        totalScore.setFont(new java.awt.Font("Tahoma", 0, 14));

        printButton.setFont(new java.awt.Font("Tahoma", 0, 14));
        printButton.setText("Print");
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        randomizeButton.setFont(new java.awt.Font("Tahoma", 0, 14));
        randomizeButton.setText("Randomize");
        randomizeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomizeButtonActionPerformed(evt);
            }
        });

        saveButton.setFont(new java.awt.Font("Tahoma", 0, 14));
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(189, 189, 189)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(levelCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(routineTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(teamCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(jLabel15))
                    .addComponent(jLabel5)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(217, 217, 217)
                        .addComponent(penalty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(383, 383, 383)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(totalScore, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6)
                                .addComponent(jLabel7))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                                .addComponent(scoreTJ1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel8)
                                .addComponent(scoreAJ1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(21, 21, 21)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER, false)
                                .addComponent(scoreAJ2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(scoreTJ2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel9))
                            .addGap(21, 21, 21)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER, false)
                                .addComponent(scoreAJ3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(scoreTJ3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel10))
                            .addGap(21, 21, 21)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER, false)
                                .addComponent(scoreAJ4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(scoreTJ4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11))
                            .addGap(21, 21, 21)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER, false)
                                .addComponent(scoreAJ5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(scoreTJ5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12))
                            .addGap(21, 21, 21)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER, false)
                                .addComponent(scoreAJ6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(scoreTJ6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel13))
                            .addGap(21, 21, 21)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER, false)
                                .addComponent(scoreAJ7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(scoreTJ7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel14)))
                        .addComponent(names1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(names2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(title, javax.swing.GroupLayout.Alignment.LEADING))))
            .addGroup(layout.createSequentialGroup()
                .addComponent(addButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(randomizeButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(printButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 268, Short.MAX_VALUE)
                .addComponent(saveButton)
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {addButton, deleteButton, printButton, randomizeButton, saveButton});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel10, jLabel11, jLabel12, jLabel13, jLabel14, jLabel8, jLabel9, scoreAJ1, scoreAJ2, scoreAJ3, scoreAJ4, scoreAJ5, scoreAJ6, scoreAJ7, scoreTJ1, scoreTJ2, scoreTJ3, scoreTJ4, scoreTJ5, scoreTJ6, scoreTJ7});

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel15, penalty});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(levelCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(routineTypeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(teamCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(names1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(names2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(35, 35, 35)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel14))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(scoreTJ1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(scoreTJ2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(scoreTJ3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(scoreTJ4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(scoreTJ5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(scoreTJ6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(scoreTJ7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(scoreAJ1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(scoreAJ2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(scoreAJ3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(scoreAJ4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(scoreAJ5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(scoreAJ6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(scoreAJ7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel15)
                                    .addComponent(penalty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16)
                                    .addComponent(totalScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(deleteButton)
                                    .addComponent(randomizeButton)
                                    .addComponent(printButton)))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(saveButton))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(352, 352, 352)
                        .addComponent(addButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {levelCombo, routineTypeCombo, teamCombo});

    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        boolean cont = true;
        if (modified) {
            cont = offerSave();
        }
        if (cont) {
            curRoutine = controller.add();
            updateRoutinesList();
            routineList.setSelectedValue(curRoutine, true);
        }
    }//GEN-LAST:event_addButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        Object o = routineList.getSelectedValue();
        if (o == null || !(o instanceof Routine)) {
            JOptionPane.showMessageDialog(this, "Please select a routine to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
        Routine routine = (Routine) o;
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the routine entitled \"" + routine.getName() + "\"?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            controller.delete(routine);
            updateRoutinesList();
            updateComponentEnabledStatus();
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void randomizeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomizeButtonActionPerformed
        boolean cont = true;
        if (modified) {
            cont = offerSave();
        }
        if (cont) {
            controller.randomize();
            updateRoutinesList();
            updateComponentEnabledStatus();
        }
    }//GEN-LAST:event_randomizeButtonActionPerformed

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        boolean cont = true;
        if (modified) {
            cont = offerSave();
        }
        if (cont) {
            controller.print();
        }
    }//GEN-LAST:event_printButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        if (modified && curRoutine != null) {
            save();
            updateRoutinesList();
            routineList.setSelectedValue(curRoutine, true);
            updateComponentEnabledStatus();
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void routineListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_routineListValueChanged
        Object o = routineList.getSelectedValue();
        if (o != null && (o instanceof Routine)) {
            curRoutine = (Routine) o;
            updateFields();
        }
        modified = false;
        updateComponentEnabledStatus();
    }//GEN-LAST:event_routineListValueChanged

    private void levelComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_levelComboActionPerformed
        handleFieldChange(null);
        this.updateComponentEnabledStatus();
    }//GEN-LAST:event_levelComboActionPerformed

    private void routineTypeComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_routineTypeComboActionPerformed
        handleFieldChange(null);
        this.updateComponentEnabledStatus();
    }//GEN-LAST:event_routineTypeComboActionPerformed

    private void teamComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_teamComboActionPerformed
        handleFieldChange(null);
        this.updateComponentEnabledStatus();
    }//GEN-LAST:event_teamComboActionPerformed

    private void scoreTJ1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreTJ1KeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreTJ2.requestFocusInWindow();
            scoreTJ2.selectAll();
        }
    }//GEN-LAST:event_scoreTJ1KeyPressed

    private void scoreTJ2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreTJ2KeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreTJ3.requestFocusInWindow();
            scoreTJ3.selectAll();
        }
    }//GEN-LAST:event_scoreTJ2KeyPressed

    private void scoreTJ3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreTJ3KeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreTJ4.requestFocusInWindow();
            scoreTJ4.selectAll();
        }
    }//GEN-LAST:event_scoreTJ3KeyPressed

    private void scoreTJ4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreTJ4KeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreTJ5.requestFocusInWindow();
            scoreTJ5.selectAll();
        }
    }//GEN-LAST:event_scoreTJ4KeyPressed

    private void scoreTJ5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreTJ5KeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreTJ6.requestFocusInWindow();
            scoreTJ6.selectAll();
        }
    }//GEN-LAST:event_scoreTJ5KeyPressed

    private void scoreTJ6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreTJ6KeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreTJ7.requestFocusInWindow();
            scoreTJ7.selectAll();
        }
    }//GEN-LAST:event_scoreTJ6KeyPressed

    private void scoreTJ7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreTJ7KeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreAJ1.requestFocusInWindow();
            scoreAJ1.selectAll();
        }
    }//GEN-LAST:event_scoreTJ7KeyPressed

    private void scoreAJ1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreAJ1KeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreAJ2.requestFocusInWindow();
            scoreAJ2.selectAll();
        }
    }//GEN-LAST:event_scoreAJ1KeyPressed

    private void scoreAJ2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreAJ2KeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreAJ3.requestFocusInWindow();
            scoreAJ3.selectAll();
        }
    }//GEN-LAST:event_scoreAJ2KeyPressed

    private void scoreAJ3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreAJ3KeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreAJ4.requestFocusInWindow();
            scoreAJ4.selectAll();
        }
    }//GEN-LAST:event_scoreAJ3KeyPressed

    private void scoreAJ4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreAJ4KeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreAJ5.requestFocusInWindow();
            scoreAJ5.selectAll();
        }
    }//GEN-LAST:event_scoreAJ4KeyPressed

    private void scoreAJ5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreAJ5KeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreAJ6.requestFocusInWindow();
            scoreAJ6.selectAll();
        }
    }//GEN-LAST:event_scoreAJ5KeyPressed

    private void scoreAJ6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreAJ6KeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            scoreAJ7.requestFocusInWindow();
            scoreAJ7.selectAll();
        }
    }//GEN-LAST:event_scoreAJ6KeyPressed

    private void scoreAJ7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_scoreAJ7KeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            penalty.requestFocusInWindow();
            penalty.selectAll();
        }
    }//GEN-LAST:event_scoreAJ7KeyPressed

    private void penaltyKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_penaltyKeyPressed
        handleFieldChange(evt);
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            calculate();
            saveButton.requestFocusInWindow();
        }
    }//GEN-LAST:event_penaltyKeyPressed

    private void handleFieldChange(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_handleFieldChange
        if (!modified) {
            modified = true;
            this.updateComponentEnabledStatus();
        }
    }//GEN-LAST:event_handleFieldChange
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox levelCombo;
    private javax.swing.JTextField names1;
    private javax.swing.JTextField names2;
    private javax.swing.JTextField penalty;
    private javax.swing.JButton printButton;
    private javax.swing.JButton randomizeButton;
    private javax.swing.JList routineList;
    private javax.swing.JComboBox routineTypeCombo;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField scoreAJ1;
    private javax.swing.JTextField scoreAJ2;
    private javax.swing.JTextField scoreAJ3;
    private javax.swing.JTextField scoreAJ4;
    private javax.swing.JTextField scoreAJ5;
    private javax.swing.JTextField scoreAJ6;
    private javax.swing.JTextField scoreAJ7;
    private javax.swing.JTextField scoreTJ1;
    private javax.swing.JTextField scoreTJ2;
    private javax.swing.JTextField scoreTJ3;
    private javax.swing.JTextField scoreTJ4;
    private javax.swing.JTextField scoreTJ5;
    private javax.swing.JTextField scoreTJ6;
    private javax.swing.JTextField scoreTJ7;
    private javax.swing.JComboBox teamCombo;
    private javax.swing.JTextField title;
    private javax.swing.JTextField totalScore;
    // End of variables declaration//GEN-END:variables
}
