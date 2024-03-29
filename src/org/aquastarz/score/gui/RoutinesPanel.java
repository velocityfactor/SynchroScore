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

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.controller.RoutinesController;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Routine;
import org.aquastarz.score.domain.RoutineLevel;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.gui.event.RoutinesPanelEventListener;
import org.aquastarz.score.manager.RoutineManager;
import org.aquastarz.score.manager.SwimmerManager;
import org.aquastarz.score.report.RoutineScoreSheet;
import org.aquastarz.score.util.TwoDigitScore;
import javax.swing.JCheckBox;
import java.awt.event.KeyAdapter;

public class RoutinesPanel extends javax.swing.JPanel {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(RoutinesPanel.class.getName());
	private boolean modified = false;
	private List<Routine> routines = null;
	private Routine curRoutine = null;
	private RoutinesController controller = null;

	/** Creates new form RoutinesPanel */
	public RoutinesPanel() {
		initComponents();
		if (!Beans.isDesignTime()) {
			controller = new RoutinesController(this);
			routineList.setSelectionModel(getRoutineListSelectionModel());
			updateRoutinesList();
			updateComponentEnabledStatus();
			updateCombos();
		}
	}

	// Event Handling
	protected javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

	public void addRoutinesPanelEventListener(RoutinesPanelEventListener listener) {
		listenerList.add(RoutinesPanelEventListener.class, listener);
	}

	public void removeRoutinesPanelEventListener(RoutinesPanelEventListener listener) {
		listenerList.remove(RoutinesPanelEventListener.class, listener);
	}

	void fireRoutineSaved() {
		for (RoutinesPanelEventListener listener : listenerList.getListeners(RoutinesPanelEventListener.class)) {
			listener.routineSaved();
		}
	}

	private ListSelectionModel getRoutineListSelectionModel() {
		VetoableListSelectionModel vtsm = new VetoableListSelectionModel();
		vtsm.addVetoableChangeListener(new VetoableChangeListener() {

			@Override
			public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
				if (modified && curRoutine != null) {

					// Ignore "changes" to the current routine
					if (((Integer) evt.getNewValue() >= 0)
							&& routineList.getModel().getElementAt((Integer) evt.getNewValue()) == curRoutine) {
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

	public void updateRoutinesList() {
		// Object o = routineList.getSelectedValue();
		int oldIndex = routineList.getSelectedIndex();
		int oldCount = routineList.getModel().getSize();
		// Routine selectedRoutine = null;
		// if (o instanceof Routine) {
		// selectedRoutine = (Routine) o;
		// }
		routines = controller.getRoutinesList();
		DefaultListModel dlm = new DefaultListModel();
		Meet meet=null;
		for (Routine routine : routines) {
			dlm.addElement(routine);
			meet=routine.getMeet();
		}
		routineList.setModel(dlm);
		// if (selectedRoutine != null) {
		// //routineList.setSelectedValue(selectedRoutine, true);
		// }
		if (oldCount == routineList.getModel().getSize()) {
			routineList.setSelectedIndex(oldIndex);
		}
		if(meet!=null) {
			earnsPointsCheckBox.setVisible(!meet.isChamps());
		}
	}

	private void updateComponentEnabledStatus() {
		boolean enable = curRoutine != null;
		title.setEnabled(enable);
		names1.setEnabled(enable);
		names2.setEnabled(enable);
		scoreTJ1.setEnabled(enable);
		scoreTJ2.setEnabled(enable);
		scoreTJ3.setEnabled(enable);
		scoreTJ4.setEnabled(enable);
		scoreTJ5.setEnabled(enable);
		scoreTJ6.setEnabled(enable);
		scoreTJ7.setEnabled(enable);
		scoreAJ1.setEnabled(enable);
		scoreAJ2.setEnabled(enable);
		scoreAJ3.setEnabled(enable);
		scoreAJ4.setEnabled(enable);
		scoreAJ5.setEnabled(enable);
		scoreAJ6.setEnabled(enable);
		scoreAJ7.setEnabled(enable);
		if ("Team".equals(routineTypeCombo.getSelectedItem())) {
			numSwimmers.setEnabled(enable);
			if (curRoutine != null && curRoutine.getMeet().isChamps()) {
				scoreAJ6.setEnabled(false);
				scoreTJ6.setEnabled(false);
				scoreAJ7.setEnabled(false);
				scoreTJ7.setEnabled(false);
			}
		} else {
			numSwimmers.setEnabled(false);
		}
		penalty.setEnabled(enable);
		earnsPointsCheckBox.setVisible(!controller.isChamps());
		levelCombo.setEnabled(enable);
		routineTypeCombo.setEnabled(enable);
		teamCombo.setEnabled(enable);
		importButton.setEnabled(true);
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

		// Teams
		List<Team> teams = controller.getTeams();
		dcbm = new DefaultComboBoxModel();
		for (Team team : teams) {
			dcbm.addElement(team);
		}
		teamCombo.setModel(dcbm);
	}

	// Returns false if the user cancels, otherwise true
	private boolean offerSave() {
		int confirm = JOptionPane.showConfirmDialog(this,
				"This routine has been modified.  Do you want to save changes?", "Save Changes?",
				JOptionPane.YES_NO_CANCEL_OPTION);
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
		if (updateRoutine(curRoutine)) {
			controller.save(curRoutine);
			modified = false;
			updateRoutinesList();
		}
	}

	private void revert() {
		updateFields();
	}

	private void calculate() {
		if (updateRoutine(curRoutine)) {
			controller.calculate(curRoutine);
			updateFields();
		}
	}

	private boolean updateRoutine(Routine routine) {
		routine.setEarnsPoints(earnsPointsCheckBox.isSelected());
		routine.setName(title.getText());
		routine.setSwimmers1(names1.getText());
		routine.setSwimmers2(names2.getText());
		routine.setLevel((RoutineLevel) levelCombo.getSelectedItem());
		routine.setRoutineType((String) routineTypeCombo.getSelectedItem());
		if ("Solo".equals(routineTypeCombo.getSelectedItem())) {
			numSwimmers.setText("1");
		} else if ("Duet".equals(routineTypeCombo.getSelectedItem())) {
			numSwimmers.setText("2");
		} else if ("Trio".equals(routineTypeCombo.getSelectedItem())) {
			numSwimmers.setText("3");
		}
		try {
			int numSwimmersValue = Integer.parseInt(numSwimmers.getText());
			routine.setNumSwimmers(numSwimmersValue);
		} catch (Exception e) {
			numSwimmers.selectAll();
			JOptionPane.showMessageDialog(this, "Invalid #Swmrs Entry", "Entry Error", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		try {
			routine.setTeam((Team) teamCombo.getSelectedItem());
			BigDecimal score = TwoDigitScore.convert(scoreTJ1.getText());
			if (score == null && scoreTJ1.getText().trim().length() > 0) {
				scoreTJ1.selectAll();
				throw new Exception("Invalid TJ1 entry");
			}
			routine.setTScore1(score);
			score = TwoDigitScore.convert(scoreTJ2.getText());
			if (score == null && scoreTJ2.getText().trim().length() > 0) {
				scoreTJ2.selectAll();
				throw new Exception("Invalid TJ2 entry");
			}
			routine.setTScore2(score);
			score = TwoDigitScore.convert(scoreTJ3.getText());
			if (score == null && scoreTJ3.getText().trim().length() > 0) {
				scoreTJ3.selectAll();
				throw new Exception("Invalid TJ3 entry");
			}
			routine.setTScore3(score);
			score = TwoDigitScore.convert(scoreTJ4.getText());
			if (score == null && scoreTJ4.getText().trim().length() > 0) {
				scoreTJ4.selectAll();
				throw new Exception("Invalid TJ4 entry");
			}
			routine.setTScore4(score);
			score = TwoDigitScore.convert(scoreTJ5.getText());
			if (score == null && scoreTJ5.getText().trim().length() > 0) {
				scoreTJ5.selectAll();
				throw new Exception("Invalid TJ5 entry");
			}
			routine.setTScore5(score);
			score = TwoDigitScore.convert(scoreTJ6.getText());
			if (score == null && scoreTJ6.getText().trim().length() > 0) {
				scoreTJ6.selectAll();
				throw new Exception("Invalid TJ6 entry");
			}
			routine.setTScore6(score);
			score = TwoDigitScore.convert(scoreTJ7.getText());
			if (score == null && scoreTJ7.getText().trim().length() > 0) {
				scoreTJ7.selectAll();
				throw new Exception("Invalid TJ7 entry");
			}
			routine.setTScore7(score);
			score = TwoDigitScore.convert(scoreAJ1.getText());
			if (score == null && scoreAJ1.getText().trim().length() > 0) {
				scoreAJ1.selectAll();
				throw new Exception("Invalid AJ1 entry");
			}
			routine.setAScore1(score);
			score = TwoDigitScore.convert(scoreAJ2.getText());
			if (score == null && scoreAJ2.getText().trim().length() > 0) {
				scoreAJ2.selectAll();
				throw new Exception("Invalid AJ2 entry");
			}
			routine.setAScore2(score);
			score = TwoDigitScore.convert(scoreAJ3.getText());
			if (score == null && scoreAJ3.getText().trim().length() > 0) {
				scoreAJ3.selectAll();
				throw new Exception("Invalid AJ3 entry");
			}
			routine.setAScore3(score);
			score = TwoDigitScore.convert(scoreAJ4.getText());
			if (score == null && scoreAJ4.getText().trim().length() > 0) {
				scoreAJ4.selectAll();
				throw new Exception("Invalid AJ4 entry");
			}
			routine.setAScore4(score);
			score = TwoDigitScore.convert(scoreAJ5.getText());
			if (score == null && scoreAJ5.getText().trim().length() > 0) {
				scoreAJ5.selectAll();
				throw new Exception("Invalid AJ5 entry");
			}
			routine.setAScore5(score);
			score = TwoDigitScore.convert(scoreAJ6.getText());
			if (score == null && scoreAJ6.getText().trim().length() > 0) {
				scoreAJ6.selectAll();
				throw new Exception("Invalid AJ6 entry");
			}
			routine.setAScore6(score);
			score = TwoDigitScore.convert(scoreAJ7.getText());
			if (score == null && scoreAJ7.getText().trim().length() > 0) {
				scoreAJ7.selectAll();
				throw new Exception("Invalid AJ7 entry");
			}
			routine.setAScore7(score);

			score = TwoDigitScore.convert(penalty.getText());
			if (score == null && penalty.getText().trim().length() > 0) {
				penalty.selectAll();
				throw new Exception("Invalid penalty entry");
			}
			// blank penalty should get 0 if there are scores, but null
			// otherwise
			if (score == null && routine.getAScore1() != null) {
				score = BigDecimal.ZERO.setScale(1);
			}
			routine.setPenalty(score);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "Entry Error", JOptionPane.WARNING_MESSAGE);
			return false;
		}
		String spellingErrors = RoutineManager.getMisspelledNames(routine);
		if (!spellingErrors.isEmpty()) {
			JOptionPane.showMessageDialog(this,
					"Please check spelling of " + spellingErrors
							+ ".  This is just a warning and does not prevent saving.",
					"Spell check", JOptionPane.WARNING_MESSAGE);
		}
		return true;
	}

	private void updateFields() {
		earnsPointsCheckBox.setSelected(curRoutine.getEarnsPoints());
		title.setText(curRoutine.getName());
		names1.setText(curRoutine.getSwimmers1());
		names2.setText(curRoutine.getSwimmers2());
		levelCombo.setSelectedItem(curRoutine.getLevel());
		routineTypeCombo.setSelectedItem(curRoutine.getRoutineType());
		numSwimmers.setText(Integer.toString(curRoutine.getNumSwimmers()));
		teamCombo.setSelectedItem(curRoutine.getTeam());
		if (curRoutine.getTScore1() != null) {
			scoreTJ1.setText(curRoutine.getTScore1().toString());
		} else {
			scoreTJ1.setText("");
		}
		if (curRoutine.getTScore2() != null) {
			scoreTJ2.setText(curRoutine.getTScore2().toString());
		} else {
			scoreTJ2.setText("");
		}
		if (curRoutine.getTScore3() != null) {
			scoreTJ3.setText(curRoutine.getTScore3().toString());
		} else {
			scoreTJ3.setText("");
		}
		if (curRoutine.getTScore4() != null) {
			scoreTJ4.setText(curRoutine.getTScore4().toString());
		} else {
			scoreTJ4.setText("");
		}
		if (curRoutine.getTScore5() != null) {
			scoreTJ5.setText(curRoutine.getTScore5().toString());
		} else {
			scoreTJ5.setText("");
		}
		if (curRoutine.getTScore6() != null) {
			scoreTJ6.setText(curRoutine.getTScore6().toString());
		} else {
			scoreTJ6.setText("");
		}
		if (curRoutine.getTScore7() != null) {
			scoreTJ7.setText(curRoutine.getTScore7().toString());
		} else {
			scoreTJ7.setText("");
		}
		if (curRoutine.getAScore1() != null) {
			scoreAJ1.setText(curRoutine.getAScore1().toString());
		} else {
			scoreAJ1.setText("");
		}
		if (curRoutine.getAScore2() != null) {
			scoreAJ2.setText(curRoutine.getAScore2().toString());
		} else {
			scoreAJ2.setText("");
		}
		if (curRoutine.getAScore3() != null) {
			scoreAJ3.setText(curRoutine.getAScore3().toString());
		} else {
			scoreAJ3.setText("");
		}
		if (curRoutine.getAScore4() != null) {
			scoreAJ4.setText(curRoutine.getAScore4().toString());
		} else {
			scoreAJ4.setText("");
		}
		if (curRoutine.getAScore5() != null) {
			scoreAJ5.setText(curRoutine.getAScore5().toString());
		} else {
			scoreAJ5.setText("");
		}
		if (curRoutine.getAScore6() != null) {
			scoreAJ6.setText(curRoutine.getAScore6().toString());
		} else {
			scoreAJ6.setText("");
		}
		if (curRoutine.getAScore7() != null) {
			scoreAJ7.setText(curRoutine.getAScore7().toString());
		} else {
			scoreAJ7.setText("");
		}
		if (curRoutine.getPenalty() != null) {
			penalty.setText(curRoutine.getPenalty().toString());
		} else {
			penalty.setText("");
		}
		if (curRoutine.getTotalScore() != null) {
			totalScore.setText(curRoutine.getTotalScore().toString());
		} else {
			totalScore.setText("");
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	// GEN-BEGIN:initComponents
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
		printButton.setToolTipText("Print the list of routines");
		importButton = new javax.swing.JButton();
		importButton.setToolTipText("Load CSV file of routines. Hold Shift and click to Export. Format is \"Team\", \"Level\", \"#Swmrs\", \"Type\", \"Swimmers1\", \"Swimmers2\"");
		randomizeButton = new javax.swing.JButton();
		randomizeButton.setToolTipText("Assign random order of figures, for Champs");
		saveButton = new javax.swing.JButton();
		numSwimmers = new javax.swing.JTextField();
		jLabel17 = new javax.swing.JLabel();
		jLabel18 = new javax.swing.JLabel();
		spellCheckButton = new javax.swing.JButton();
		spellCheckButton.setToolTipText("Check the swimmer names against the swimmer database.");

		routineList.setModel(new javax.swing.AbstractListModel() {
			String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

			@Override
			public int getSize() {
				return strings.length;
			}

			@Override
			public Object getElementAt(int i) {
				return strings[i];
			}
		});
		routineList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			@Override
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				routineListValueChanged(evt);
			}
		});
		jScrollPane1.setViewportView(routineList);

		addButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		addButton.setText("Add");
		addButton.setFocusable(false);
		addButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addButtonActionPerformed(evt);
			}
		});

		deleteButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		deleteButton.setText("Delete");
		deleteButton.setFocusable(false);
		deleteButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deleteButtonActionPerformed(evt);
			}
		});

		levelCombo.setFont(new java.awt.Font("Tahoma", 0, 14));
		levelCombo.setModel(
				new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		levelCombo.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				levelComboActionPerformed(evt);
			}
		});

		jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel1.setText("Level");

		jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel2.setText("Type");

		routineTypeCombo.setFont(new java.awt.Font("Tahoma", 0, 14));
		routineTypeCombo
				.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Solo", "Duet", "Trio", "Team" }));
		routineTypeCombo.setMinimumSize(new java.awt.Dimension(59, 20));
		routineTypeCombo.setPreferredSize(new java.awt.Dimension(63, 22));
		routineTypeCombo.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				routineTypeComboActionPerformed(evt);
			}
		});

		teamCombo.setFont(new java.awt.Font("Tahoma", 0, 14));
		teamCombo.setModel(
				new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		teamCombo.addActionListener(new java.awt.event.ActionListener() {
			@Override
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
			@Override
			public void keyTyped(java.awt.event.KeyEvent evt) {
				handleFieldChange(evt);
			}
		});

		jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel5.setText("Swimmer Names");

		names1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		names1.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent evt) {
				handleFieldChange(evt);
				swimmerSearch(evt);
			}
		});

		names2.setFont(new java.awt.Font("Tahoma", 0, 14));
		names2.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyTyped(java.awt.event.KeyEvent evt) {
				handleFieldChange(evt);
				swimmerSearch(evt);
			}
		});

		jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel6.setText("Technical Merit:");

		scoreTJ1.setFont(new java.awt.Font("Tahoma", 0, 14));
		scoreTJ1.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				scoreTJ1KeyPressed(evt);
			}
		});

		scoreTJ2.setFont(new java.awt.Font("Tahoma", 0, 14));
		scoreTJ2.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				scoreTJ2KeyPressed(evt);
			}
		});

		scoreTJ3.setFont(new java.awt.Font("Tahoma", 0, 14));
		scoreTJ3.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				scoreTJ3KeyPressed(evt);
			}
		});

		scoreTJ4.setFont(new java.awt.Font("Tahoma", 0, 14));
		scoreTJ4.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				scoreTJ4KeyPressed(evt);
			}
		});

		scoreTJ5.setFont(new java.awt.Font("Tahoma", 0, 14));
		scoreTJ5.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				scoreTJ5KeyPressed(evt);
			}
		});

		scoreTJ6.setFont(new java.awt.Font("Tahoma", 0, 14));
		scoreTJ6.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				scoreTJ6KeyPressed(evt);
			}
		});

		scoreTJ7.setFont(new java.awt.Font("Tahoma", 0, 14));
		scoreTJ7.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				scoreTJ7KeyPressed(evt);
			}
		});

		scoreAJ7.setFont(new java.awt.Font("Tahoma", 0, 14));
		scoreAJ7.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				scoreAJ7KeyPressed(evt);
			}
		});

		scoreAJ2.setFont(new java.awt.Font("Tahoma", 0, 14));
		scoreAJ2.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				scoreAJ2KeyPressed(evt);
			}
		});

		scoreAJ3.setFont(new java.awt.Font("Tahoma", 0, 14));
		scoreAJ3.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				scoreAJ3KeyPressed(evt);
			}
		});

		jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel7.setText("Artistic Impression:");

		scoreAJ1.setFont(new java.awt.Font("Tahoma", 0, 14));
		scoreAJ1.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				scoreAJ1KeyPressed(evt);
			}
		});

		scoreAJ4.setFont(new java.awt.Font("Tahoma", 0, 14));
		scoreAJ4.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				scoreAJ4KeyPressed(evt);
			}
		});

		scoreAJ6.setFont(new java.awt.Font("Tahoma", 0, 14));
		scoreAJ6.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				scoreAJ6KeyPressed(evt);
			}
		});

		scoreAJ5.setFont(new java.awt.Font("Tahoma", 0, 14));
		scoreAJ5.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
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
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				penaltyKeyPressed(evt);
			}
		});

		jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel16.setText("Score:");

		totalScore.setEditable(false);
		totalScore.setFont(new java.awt.Font("Tahoma", 0, 14));
		totalScore.setFocusable(false);

		printButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		printButton.setText("Print List");
		printButton.setFocusable(false);
		printButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				printButtonActionPerformed(evt);
			}
		});

		importButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		importButton.setText("Import...");
		importButton.setFocusable(false);
		importButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				importButtonActionPerformed(evt);
			}
		});

		randomizeButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		randomizeButton.setText("Randomize");
		randomizeButton.setFocusable(false);
		randomizeButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				randomizeButtonActionPerformed(evt);
			}
		});

		saveButton.setFont(new java.awt.Font("Tahoma", 0, 14));
		saveButton.setText("Save");
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveButtonActionPerformed(evt);
			}
		});
		saveButton.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				saveButtonKeyPressed(evt);
			}
		});

		numSwimmers.setFont(new java.awt.Font("Tahoma", 0, 14));
		numSwimmers.addKeyListener(new java.awt.event.KeyAdapter() {
			@Override
			public void keyPressed(java.awt.event.KeyEvent evt) {
				numSwimmersKeyPressed(evt);
			}
		});

		jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel17.setText("# Swmrs");

		jLabel18.setText("0.5, 1.0, etc.");

		spellCheckButton.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		spellCheckButton.setText("Spell Check");
		spellCheckButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				spellCheckButtonActionPerformed(evt);
			}
		});

		JLabel lblHintTypeTwo = new JLabel(
				"Hint: Type two letters of first name, two letters of last name, and press comma");

		JButton btnPrintSheets = new JButton("Print Sheets");
		btnPrintSheets.setToolTipText("Generate all judge and ref sheets");
		btnPrintSheets.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				try {
					Collection<RoutineScoreSheet> allSheets = controller.generateRoutineScoreSheets();
					Collection<RoutineScoreSheet> routineSoloScoreSheets = new ArrayList<RoutineScoreSheet>();
					Collection<RoutineScoreSheet> routineDuetScoreSheets = new ArrayList<RoutineScoreSheet>();
					Collection<RoutineScoreSheet> routineTrioScoreSheets = new ArrayList<RoutineScoreSheet>();
					Collection<RoutineScoreSheet> routineTeamScoreSheets = new ArrayList<RoutineScoreSheet>();
					Collection<RoutineScoreSheet> routineComboScoreSheets = new ArrayList<RoutineScoreSheet>();
					Collection<RoutineScoreSheet> routineRefSheets = new ArrayList<RoutineScoreSheet>();
					for (RoutineScoreSheet rss : allSheets) {
						if ("0".equals(rss.getJudgeNum())) {
							routineRefSheets.add(rss);
						} else {
							if ("Solo".equals(rss.getEvent())) {
								routineSoloScoreSheets.add(rss);
							} else if ("Duet".equals(rss.getEvent())) {
								routineDuetScoreSheets.add(rss);
							} else if ("Trio".equals(rss.getEvent())) {
								routineTrioScoreSheets.add(rss);
							} else if ("Team".equals(rss.getEvent())) {
								routineTeamScoreSheets.add(rss);
							} else if ("Combo".equals(rss.getEvent())) {
								routineComboScoreSheets.add(rss);
							}
						}
					}
					JasperReport jasperReport1 = (JasperReport) JRLoader.loadObject(
							getClass().getResourceAsStream("/org/aquastarz/score/report/RoutineRefSheet.jasper"));
					JRDataSource data1 = new JRBeanCollectionDataSource(routineRefSheets);
					JasperReport jasperReport2 = (JasperReport) JRLoader.loadObject(
							getClass().getResourceAsStream("/org/aquastarz/score/report/RoutineScoreSheet.jasper"));
					JRDataSource dataSolo = new JRBeanCollectionDataSource(routineSoloScoreSheets);
					JRDataSource dataDuet = new JRBeanCollectionDataSource(routineDuetScoreSheets);
					JRDataSource dataTrio = new JRBeanCollectionDataSource(routineTrioScoreSheets);
					JRDataSource dataTeam = new JRBeanCollectionDataSource(routineTeamScoreSheets);
					JRDataSource dataCombo = new JRBeanCollectionDataSource(routineComboScoreSheets);
					Map<String, Object> params = new HashMap<String, Object>();
					JasperPrint jasperPrint1 = JasperFillManager.fillReport(jasperReport1, params, data1);
					JasperViewer.viewReport(jasperPrint1, false);
					JasperPrint jasperPrintSolo = JasperFillManager.fillReport(jasperReport2, params, dataSolo);
					JasperViewer.viewReport(jasperPrintSolo, false);
					JasperPrint jasperPrintDuet = JasperFillManager.fillReport(jasperReport2, params, dataDuet);
					JasperViewer.viewReport(jasperPrintDuet, false);
					JasperPrint jasperPrintTrio = JasperFillManager.fillReport(jasperReport2, params, dataTrio);
					JasperViewer.viewReport(jasperPrintTrio, false);
					JasperPrint jasperPrintTeam = JasperFillManager.fillReport(jasperReport2, params, dataTeam);
					JasperViewer.viewReport(jasperPrintTeam, false);
					JasperPrint jasperPrintCombo = JasperFillManager.fillReport(jasperReport2, params, dataCombo);
					JasperViewer.viewReport(jasperPrintCombo, false);
				} catch (Exception ex) {
					logger.error("Could not create the report", ex);
				}
				setCursor(Cursor.getDefaultCursor());
			}
		});
		btnPrintSheets.setFont(new Font("Tahoma", Font.PLAIN, 14));

		earnsPointsCheckBox = new JCheckBox("Earns Team Points");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		layout.setHorizontalGroup(
				layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addContainerGap().addComponent(addButton)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(deleteButton)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(spellCheckButton)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(randomizeButton)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(printButton)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(importButton)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnPrintSheets)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(saveButton).addContainerGap())
						.addGroup(
								layout.createSequentialGroup()
										.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 171,
												GroupLayout.PREFERRED_SIZE)
										.addGap(18)
										.addGroup(layout
												.createParallelGroup(Alignment.LEADING).addGroup(layout
														.createSequentialGroup().addGroup(layout
																.createParallelGroup(Alignment.LEADING).addComponent(
																		jLabel1)
																.addComponent(
																		levelCombo, GroupLayout.PREFERRED_SIZE, 188,
																		GroupLayout.PREFERRED_SIZE))
														.addPreferredGap(ComponentPlacement.RELATED)
														.addGroup(layout.createParallelGroup(Alignment.LEADING)
																.addComponent(jLabel2).addComponent(routineTypeCombo,
																		GroupLayout.PREFERRED_SIZE, 80,
																		GroupLayout.PREFERRED_SIZE))
														.addPreferredGap(ComponentPlacement.RELATED).addGroup(layout
																.createParallelGroup(Alignment.LEADING, false)
																.addComponent(numSwimmers).addComponent(jLabel17,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
														.addPreferredGap(ComponentPlacement.RELATED)
														.addGroup(layout.createParallelGroup(Alignment.LEADING)
																.addComponent(jLabel3).addComponent(teamCombo,
																		GroupLayout.PREFERRED_SIZE, 181,
																		GroupLayout.PREFERRED_SIZE)))
												.addGroup(layout
														.createSequentialGroup().addGap(10)
														.addComponent(lblHintTypeTwo))
												.addComponent(jLabel5).addComponent(jLabel4)
												.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
														.addGroup(layout.createSequentialGroup()
																.addGroup(layout.createParallelGroup(Alignment.LEADING)
																		.addComponent(jLabel6).addComponent(jLabel7))
																.addGap(18).addGroup(layout
																		.createParallelGroup(Alignment.CENTER)
																		.addComponent(scoreTJ1,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(jLabel8).addComponent(scoreAJ1,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE))
																.addGap(21)
																.addGroup(layout
																		.createParallelGroup(Alignment.CENTER, false)
																		.addComponent(scoreAJ2,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				scoreTJ2, GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(jLabel9))
																.addGap(21)
																.addGroup(layout
																		.createParallelGroup(Alignment.CENTER, false)
																		.addComponent(scoreAJ3,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				scoreTJ3, GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(jLabel10))
																.addGap(21)
																.addGroup(layout
																		.createParallelGroup(Alignment.CENTER, false)
																		.addComponent(scoreAJ4,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				scoreTJ4, GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(jLabel11))
																.addGap(21).addGroup(layout
																		.createParallelGroup(Alignment.CENTER, false)
																		.addComponent(scoreAJ5,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				scoreTJ5, GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(jLabel12))
																.addGap(21)
																.addGroup(layout
																		.createParallelGroup(Alignment.CENTER, false)
																		.addComponent(scoreAJ6,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				scoreTJ6, GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(jLabel13))
																.addGap(21)
																.addGroup(layout
																		.createParallelGroup(Alignment.CENTER, false)
																		.addComponent(scoreAJ7,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				scoreTJ7, GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)
																		.addComponent(jLabel14)))
														.addComponent(names1).addComponent(names2).addComponent(title))
												.addGroup(layout.createSequentialGroup().addGap(10).addGroup(layout
														.createParallelGroup(Alignment.LEADING).addComponent(jLabel15)
														.addGroup(layout.createSequentialGroup().addGap(57)
																.addComponent(penalty, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)))
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(jLabel18, GroupLayout.PREFERRED_SIZE, 106,
																GroupLayout.PREFERRED_SIZE)
														.addGap(18).addComponent(earnsPointsCheckBox)
														.addPreferredGap(ComponentPlacement.RELATED, 127,
																Short.MAX_VALUE)
														.addComponent(jLabel16)
														.addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(totalScore, GroupLayout.PREFERRED_SIZE, 82,
																GroupLayout.PREFERRED_SIZE)
														.addGap(112)))));
		layout.setVerticalGroup(layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup().addContainerGap()
										.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel1)
												.addComponent(jLabel2).addComponent(jLabel17).addComponent(jLabel3))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(layout.createParallelGroup(Alignment.BASELINE)
												.addComponent(levelCombo, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(routineTypeCombo, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(teamCombo, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(numSwimmers, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(jLabel4)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(title, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(jLabel5)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(names1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(names2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblHintTypeTwo)
										.addGap(15)
										.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel8)
												.addComponent(jLabel9).addComponent(jLabel10).addComponent(jLabel11)
												.addComponent(jLabel12).addComponent(jLabel13).addComponent(jLabel14))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel6)
												.addComponent(scoreTJ1, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(scoreTJ2, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(scoreTJ3, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(scoreTJ4, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(scoreTJ5, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(scoreTJ6, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(scoreTJ7, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(jLabel7)
												.addComponent(scoreAJ1, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(scoreAJ2, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(scoreAJ3, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(scoreAJ4, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(scoreAJ5, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(scoreAJ6, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(scoreAJ7, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(layout
												.createParallelGroup(Alignment.LEADING)
												.addGroup(layout.createParallelGroup(Alignment.BASELINE)
														.addComponent(totalScore, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(jLabel16))
												.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
														.addGroup(layout.createParallelGroup(Alignment.BASELINE)
																.addComponent(jLabel18, GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(earnsPointsCheckBox))
														.addComponent(penalty, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(jLabel15))))
								.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 348,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(deleteButton)
										.addComponent(randomizeButton).addComponent(printButton)
										.addComponent(importButton).addComponent(addButton)
										.addComponent(spellCheckButton).addComponent(btnPrintSheets))
								.addComponent(saveButton))
						.addContainerGap()));
		layout.linkSize(SwingConstants.VERTICAL, new Component[] { levelCombo, routineTypeCombo, teamCombo });
		layout.linkSize(SwingConstants.HORIZONTAL, new Component[] { addButton, deleteButton, printButton, importButton,
				randomizeButton, saveButton, spellCheckButton, btnPrintSheets });
		layout.linkSize(SwingConstants.HORIZONTAL,
				new Component[] { scoreTJ1, scoreTJ2, scoreTJ3, scoreTJ4, scoreTJ5, scoreTJ6, scoreTJ7, scoreAJ7,
						scoreAJ2, scoreAJ3, scoreAJ1, scoreAJ4, scoreAJ6, scoreAJ5, jLabel8, jLabel9, jLabel10,
						jLabel11, jLabel12, jLabel13, jLabel14 });
		layout.linkSize(SwingConstants.HORIZONTAL, new Component[] { jLabel15, penalty });
		this.setLayout(layout);

	}// </editor-fold>//GEN-END:initComponents

	private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_addButtonActionPerformed
		boolean cont = true;
		if (modified) {
			cont = offerSave();
		}
		if (cont) {
			curRoutine = controller.add();
			updateRoutinesList();
			routineList.setSelectedValue(curRoutine, true);
		}
	}// GEN-LAST:event_addButtonActionPerformed

	private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_deleteButtonActionPerformed
		Object o = routineList.getSelectedValue();
		if (o == null || !(o instanceof Routine)) {
			JOptionPane.showMessageDialog(this, "Please select a routine to delete.", "No Selection",
					JOptionPane.WARNING_MESSAGE);
		}
		Routine routine = (Routine) o;
		int confirm = JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete the routine entitled \"" + routine.getName() + "\"?", "Confirm Delete",
				JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			controller.delete(routine);
			updateRoutinesList();
			updateComponentEnabledStatus();
		}
	}// GEN-LAST:event_deleteButtonActionPerformed

	private void randomizeButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_randomizeButtonActionPerformed
		boolean cont = true;
		if (modified) {
			cont = offerSave();
		}
		if (cont) {
			controller.randomize();
			updateRoutinesList();
			updateComponentEnabledStatus();
		}
	}// GEN-LAST:event_randomizeButtonActionPerformed

	private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_printButtonActionPerformed
		boolean cont = true;
		if (modified) {
			cont = offerSave();
		}
		if (cont) {
			controller.print();
		}
	}// GEN-LAST:event_printButtonActionPerformed

	private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_importButtonActionPerformed
		boolean cont = true;
		if (modified) {
			cont = offerSave();
		}
		if (cont) {
			if((evt.getModifiers()&ActionEvent.SHIFT_MASK)>0) {
				//Export
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Save Routines data file");
				jfc.setFileFilter(new FileNameExtensionFilter("csv file", "csv"));
				int ret = jfc.showSaveDialog(this);
				if (ret == JFileChooser.APPROVE_OPTION) {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					controller.exportRoutines(jfc.getSelectedFile());
					setCursor(Cursor.getDefaultCursor());
				}
			}
			else {
				//Import
				JFileChooser jfc = new JFileChooser();
				jfc.setDialogTitle("Open Routines data file");
				jfc.setFileFilter(new FileNameExtensionFilter("csv file", "csv"));
				int ret = jfc.showOpenDialog(this);
				if (ret == JFileChooser.APPROVE_OPTION) {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					controller.importRoutines(jfc.getSelectedFile());
					setCursor(Cursor.getDefaultCursor());
				}
				updateRoutinesList();
			}
		}
	}// GEN-LAST:event_importButtonActionPerformed

	private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveButtonActionPerformed
		if (modified && curRoutine != null) {
			save();
			// routineList.setSelectedValue(curRoutine, true);
			updateComponentEnabledStatus();
			fireRoutineSaved();
		}
	}// GEN-LAST:event_saveButtonActionPerformed

	private void levelComboActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_levelComboActionPerformed
		handleFieldChange(null);
		this.updateComponentEnabledStatus();
	}// GEN-LAST:event_levelComboActionPerformed

	private void routineTypeComboActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_routineTypeComboActionPerformed
		handleFieldChange(null);
		this.updateComponentEnabledStatus();
		if ("Solo".equals(routineTypeCombo.getSelectedItem())) {
			numSwimmers.setText("1");
		} else if ("Duet".equals(routineTypeCombo.getSelectedItem())) {
			numSwimmers.setText("2");
		} else if ("Trio".equals(routineTypeCombo.getSelectedItem())) {
			numSwimmers.setText("3");
		} else if ("Team".equals(routineTypeCombo.getSelectedItem())) {
			int num = 4;
			try {
				num = Integer.parseInt(numSwimmers.getText());
			} catch (Exception e) {
				// Invalid just defaults to 4
			}
			if (num < 4 || num > 8) {
				num = 4;
			}
			numSwimmers.setText(Integer.toString(num));
		}
	}// GEN-LAST:event_routineTypeComboActionPerformed

	private void teamComboActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_teamComboActionPerformed
		handleFieldChange(null);
		this.updateComponentEnabledStatus();
	}// GEN-LAST:event_teamComboActionPerformed

	private void scoreTJ1KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_scoreTJ1KeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			scoreTJ2.requestFocusInWindow();
			scoreTJ2.selectAll();
		}
	}// GEN-LAST:event_scoreTJ1KeyPressed

	private void scoreTJ2KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_scoreTJ2KeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			scoreTJ3.requestFocusInWindow();
			scoreTJ3.selectAll();
		}
		if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreTJ2.getText().length() == 0) {
			scoreTJ1.requestFocusInWindow();
			scoreTJ1.selectAll();
		}
	}// GEN-LAST:event_scoreTJ2KeyPressed

	private void scoreTJ3KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_scoreTJ3KeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			scoreTJ4.requestFocusInWindow();
			scoreTJ4.selectAll();
		}
		if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreTJ3.getText().length() == 0) {
			scoreTJ2.requestFocusInWindow();
			scoreTJ2.selectAll();
		}
	}// GEN-LAST:event_scoreTJ3KeyPressed

	private void scoreTJ4KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_scoreTJ4KeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			scoreTJ5.requestFocusInWindow();
			scoreTJ5.selectAll();
		}
		if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreTJ4.getText().length() == 0) {
			scoreTJ3.requestFocusInWindow();
			scoreTJ3.selectAll();
		}
	}// GEN-LAST:event_scoreTJ4KeyPressed

	private void scoreTJ5KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_scoreTJ5KeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			if (scoreTJ6.isEnabled()) {
				scoreTJ6.requestFocusInWindow();
				scoreTJ6.selectAll();
			} else {
				scoreAJ1.requestFocusInWindow();
				scoreAJ1.selectAll();
			}
		}
		if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreTJ5.getText().length() == 0) {
			scoreTJ4.requestFocusInWindow();
			scoreTJ4.selectAll();
		}
	}// GEN-LAST:event_scoreTJ5KeyPressed

	private void scoreTJ6KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_scoreTJ6KeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			scoreTJ7.requestFocusInWindow();
			scoreTJ7.selectAll();
		}
		if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreTJ6.getText().length() == 0) {
			scoreTJ5.requestFocusInWindow();
			scoreTJ5.selectAll();
		}
	}// GEN-LAST:event_scoreTJ6KeyPressed

	private void scoreTJ7KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_scoreTJ7KeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			scoreAJ1.requestFocusInWindow();
			scoreAJ1.selectAll();
		}
		if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreTJ7.getText().length() == 0) {
			scoreTJ6.requestFocusInWindow();
			scoreTJ6.selectAll();
		}
	}// GEN-LAST:event_scoreTJ7KeyPressed

	private void scoreAJ1KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_scoreAJ1KeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			scoreAJ2.requestFocusInWindow();
			scoreAJ2.selectAll();
		}
		if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreAJ1.getText().length() == 0) {
			if (scoreTJ7.isEnabled()) {
				scoreTJ7.requestFocusInWindow();
				scoreTJ7.selectAll();
			} else {
				scoreTJ5.requestFocusInWindow();
				scoreTJ5.selectAll();
			}
		}
	}// GEN-LAST:event_scoreAJ1KeyPressed

	private void scoreAJ2KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_scoreAJ2KeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			scoreAJ3.requestFocusInWindow();
			scoreAJ3.selectAll();
		}
		if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreAJ2.getText().length() == 0) {
			scoreAJ1.requestFocusInWindow();
			scoreAJ1.selectAll();
		}
	}// GEN-LAST:event_scoreAJ2KeyPressed

	private void scoreAJ3KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_scoreAJ3KeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			scoreAJ4.requestFocusInWindow();
			scoreAJ4.selectAll();
		}
		if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreAJ3.getText().length() == 0) {
			scoreAJ2.requestFocusInWindow();
			scoreAJ2.selectAll();
		}
	}// GEN-LAST:event_scoreAJ3KeyPressed

	private void scoreAJ4KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_scoreAJ4KeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			scoreAJ5.requestFocusInWindow();
			scoreAJ5.selectAll();
		}
		if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreAJ4.getText().length() == 0) {
			scoreAJ3.requestFocusInWindow();
			scoreAJ3.selectAll();
		}
	}// GEN-LAST:event_scoreAJ4KeyPressed

	private void scoreAJ5KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_scoreAJ5KeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			if (scoreAJ6.isEnabled()) {
				scoreAJ6.requestFocusInWindow();
				scoreAJ6.selectAll();
			} else {
				penalty.requestFocusInWindow();
				penalty.selectAll();
			}
		}
		if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreAJ5.getText().length() == 0) {
			scoreAJ4.requestFocusInWindow();
			scoreAJ4.selectAll();
		}
	}// GEN-LAST:event_scoreAJ5KeyPressed

	private void scoreAJ6KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_scoreAJ6KeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			scoreAJ7.requestFocusInWindow();
			scoreAJ7.selectAll();
		}
		if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreAJ6.getText().length() == 0) {
			scoreAJ5.requestFocusInWindow();
			scoreAJ5.selectAll();
		}
	}// GEN-LAST:event_scoreAJ6KeyPressed

	private void scoreAJ7KeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_scoreAJ7KeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			penalty.requestFocusInWindow();
			penalty.selectAll();
		}
		if (evt.getKeyCode() == KeyEvent.VK_BACK_SPACE && scoreAJ7.getText().length() == 0) {
			scoreAJ6.requestFocusInWindow();
			scoreAJ6.selectAll();
		}
	}// GEN-LAST:event_scoreAJ7KeyPressed

	private void penaltyKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_penaltyKeyPressed
		handleFieldChange(evt);
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			calculate();
			saveButton.requestFocusInWindow();
		}
	}// GEN-LAST:event_penaltyKeyPressed

	private void handleFieldChange(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_handleFieldChange
		if (!modified) {
			modified = true;
			this.updateComponentEnabledStatus();
		}
	}// GEN-LAST:event_handleFieldChange

	private void routineListValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_routineListValueChanged
		updateCombos();
		Object o = routineList.getSelectedValue();
		if (o != null && (o instanceof Routine)) {
			curRoutine = (Routine) o;
			updateFields();
		}
		modified = false;
		updateComponentEnabledStatus();
	}// GEN-LAST:event_routineListValueChanged

	private void numSwimmersKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_numSwimmersKeyPressed
		handleFieldChange(null);
		updateComponentEnabledStatus();
	}// GEN-LAST:event_numSwimmersKeyPressed

	private void saveButtonKeyPressed(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_saveButtonKeyPressed
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			saveButton.doClick();
		}
	}// GEN-LAST:event_saveButtonKeyPressed

	private void swimmerSearch(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_swimmerSearch
		try {
			if (evt.getKeyChar() == ',') {
				JTextField tf = (JTextField) evt.getComponent();
				String s = tf.getText();
				if (tf.getCaretPosition() == s.length()) {
					int prevComma = s.lastIndexOf(',', s.length() - 1) + 1;
					String search = s.substring(prevComma, s.length()).trim();
					String result;
					int halfLen = search.length() / 2;
					if (halfLen * 2 == search.length()) {
						String firstName = search.substring(0, halfLen);
						String lastName = search.substring(halfLen);
						result = SwimmerManager.findNameLikeName(firstName, lastName, ScoreApp.getCurrentSeason());
						if (result == null) {
							result = search;
						}
					} else {
						result = search;
					}
					if (prevComma == 0) {
						tf.setText(result);
					} else {
						tf.setText(s.substring(0, prevComma) + " " + result);
					}
				}
			}
		} catch (Exception e) {
			logger.error("Swimmer search failed", e);
		}
	}// GEN-LAST:event_swimmerSearch

	private void spellCheckButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_spellCheckButtonActionPerformed
		String spellingErrors = RoutineManager.getMisspelledNamesHtmlMessage(controller.getRoutinesList());
		if (!spellingErrors.isEmpty()) {
			JScrollPane scrollPane = new JScrollPane(new JLabel(spellingErrors));
			scrollPane.setPreferredSize(new Dimension(600, 100));
			Object message = scrollPane;
			JOptionPane.showMessageDialog(this, message, "Spell Check", JOptionPane.WARNING_MESSAGE);
		}
	}// GEN-LAST:event_spellCheckButtonActionPerformed
		// Variables declaration - do not modify

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
	private javax.swing.JLabel jLabel17;
	private javax.swing.JLabel jLabel18;
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
	private javax.swing.JTextField numSwimmers;
	private javax.swing.JTextField penalty;
	private javax.swing.JButton printButton;
	private javax.swing.JButton importButton;
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
	private javax.swing.JButton spellCheckButton;
	private javax.swing.JComboBox teamCombo;
	private javax.swing.JTextField title;
	private javax.swing.JTextField totalScore;
	private JCheckBox earnsPointsCheckBox;
}
