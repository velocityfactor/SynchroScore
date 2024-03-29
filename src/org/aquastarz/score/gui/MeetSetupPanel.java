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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;

import org.aquastarz.score.domain.Figure;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.gui.event.MeetSetupPanelListener;
import org.aquastarz.score.manager.MeetManager;

public class MeetSetupPanel extends javax.swing.JPanel {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(MeetSetupPanel.class.getName());

	private Meet meet;

	// Event Handling
	private final Vector<MeetSetupPanelListener> listeners = new Vector<MeetSetupPanelListener>();

	public void addMeetSetupPanelListener(MeetSetupPanelListener listener) {
		removeMeetSetupPanelListener(listener);
		listeners.add(listener);
	}

	public void removeMeetSetupPanelListener(MeetSetupPanelListener listener) {
		while (listeners.remove(listener)) {
		}
	}

	private void fireSavedEvent() {
		for (MeetSetupPanelListener listener : listeners) {
			listener.meetSetupSaved();
		}
	}

	/** Creates new form MeetSetupPanel */
	public MeetSetupPanel() {
		initComponents();
		// setFormEnabled(false);
	}

	protected void fillForm(Meet meet, List<Figure> figures, List<Team> teams) {
		this.meet = meet;

		fillFigureCombo(noviceFigName1, figures);
		fillFigureCombo(noviceFigName2, figures);
		fillFigureCombo(noviceFigName3, figures);
		fillFigureCombo(noviceFigName4, figures);
		fillFigureCombo(intFigName1, figures);
		fillFigureCombo(intFigName2, figures);
		fillFigureCombo(intFigName3, figures);
		fillFigureCombo(intFigName4, figures);

		// Fill a Vector for the JList and a ComboBoxModel for the ComboBox of
		// Teams
		DefaultComboBoxModel cbm = new DefaultComboBoxModel();
		Vector<CheckListItem<Team>> v = new Vector<CheckListItem<Team>>();
		for (Team t : teams) {
			CheckListItem cli = new CheckListItem(t, t.getName(), meet
					.getOpponents().contains(t));
			v.add(cli);
			cbm.addElement(t);
		}

		// Set up the JList as a list of checkboxes
		opponents.setListData(v);
		CheckListCellRenderer renderer = new CheckListCellRenderer();
		opponents.setCellRenderer(renderer);
		opponents.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		CheckListener lst = new CheckListener(opponents);
		opponents.addMouseListener(lst);
		opponents.addKeyListener(lst);

		// Set ComboBox model
		homeTeam.setModel(cbm);

		meetName.setText(meet.getName());
		meetDate.setText(meet.getMeetDate());
		if ('C' == meet.getType()) {
			champsButton.setSelected(true);
		} else {
			regularMeetButton.setSelected(true);
		}

		// Set Figures
		noviceFigName1.setSelectedItem(meet.getNov1Figure());
		noviceFigName2.setSelectedItem(meet.getNov2Figure());
		noviceFigName3.setSelectedItem(meet.getNov3Figure());
		noviceFigName4.setSelectedItem(meet.getNov4Figure());
		intFigName1.setSelectedItem(meet.getInt1Figure());
		intFigName2.setSelectedItem(meet.getInt2Figure());
		intFigName3.setSelectedItem(meet.getInt3Figure());
		intFigName4.setSelectedItem(meet.getInt4Figure());

		if (meet.getSeason().getRulesRevision() < 2) {
			// Check 8 and under boxes
			eightAndUnder1.setSelected(meet.isEu1());
			eightAndUnder2.setSelected(meet.isEu2());
			eightAndUnder3.setSelected(meet.isEu3());
			eightAndUnder4.setSelected(meet.isEu4());
		} else {
			// Hide 8 and under boxes
			n8ULabel.setVisible(false);
			eightAndUnder1.setVisible(false);
			eightAndUnder2.setVisible(false);
			eightAndUnder3.setVisible(false);
			eightAndUnder4.setVisible(false);
		}

		homeTeam.setSelectedItem(meet.getHomeTeam());

		setFormEnabled(true);
	}

	private void fillFigureCombo(JComboBox cb, List<Figure> figures) {
		DefaultComboBoxModel cbm = new DefaultComboBoxModel();
		Collections.sort(figures, new Comparator<Figure>() {

			@Override
			public int compare(Figure o1, Figure o2) {
				return o1.getName().compareTo(o2.getName());
			}

		});
		for (Figure f : figures) {
			cbm.addElement(f);
		}
		cb.setModel(cbm);
	}

	private void setFormEnabled(boolean enabled) {
		meetName.setEnabled(enabled);
		meetDate.setEnabled(enabled);
		champsButton.setEnabled(enabled);
		regularMeetButton.setEnabled(enabled);
		noviceFigName1.setEnabled(enabled);
		noviceFigName2.setEnabled(enabled);
		noviceFigName3.setEnabled(enabled);
		noviceFigName4.setEnabled(enabled);
		intFigName1.setEnabled(enabled);
		intFigName2.setEnabled(enabled);
		intFigName3.setEnabled(enabled);
		intFigName4.setEnabled(enabled);
		homeTeam.setEnabled(enabled);
		opponents.setEnabled(enabled);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		meetTypeButtonGroup = new javax.swing.ButtonGroup();
		jLabel1 = new javax.swing.JLabel();
		meetName = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		n8ULabel = new javax.swing.JLabel();
		noviceFigNum1 = new javax.swing.JTextField();
		noviceDD1 = new javax.swing.JTextField();
		eightAndUnder1 = new javax.swing.JCheckBox();
		jLabel9 = new javax.swing.JLabel();
		noviceFigNum2 = new javax.swing.JTextField();
		noviceDD2 = new javax.swing.JTextField();
		eightAndUnder2 = new javax.swing.JCheckBox();
		jLabel10 = new javax.swing.JLabel();
		noviceFigNum3 = new javax.swing.JTextField();
		noviceDD3 = new javax.swing.JTextField();
		eightAndUnder3 = new javax.swing.JCheckBox();
		jLabel11 = new javax.swing.JLabel();
		noviceFigNum4 = new javax.swing.JTextField();
		noviceDD4 = new javax.swing.JTextField();
		eightAndUnder4 = new javax.swing.JCheckBox();
		jLabel12 = new javax.swing.JLabel();
		jLabel13 = new javax.swing.JLabel();
		jLabel14 = new javax.swing.JLabel();
		intFigNum1 = new javax.swing.JTextField();
		intFigNum2 = new javax.swing.JTextField();
		jLabel15 = new javax.swing.JLabel();
		intDD1 = new javax.swing.JTextField();
		intDD2 = new javax.swing.JTextField();
		jLabel16 = new javax.swing.JLabel();
		jLabel17 = new javax.swing.JLabel();
		jLabel19 = new javax.swing.JLabel();
		intFigNum4 = new javax.swing.JTextField();
		intDD4 = new javax.swing.JTextField();
		jLabel20 = new javax.swing.JLabel();
		intFigNum3 = new javax.swing.JTextField();
		intDD3 = new javax.swing.JTextField();
		homeTeam = new javax.swing.JComboBox();
		jLabel18 = new javax.swing.JLabel();
		jLabel21 = new javax.swing.JLabel();
		jScrollPane1 = new javax.swing.JScrollPane();
		opponents = new javax.swing.JList();
		saveButton = new javax.swing.JButton();
		noviceFigName1 = new javax.swing.JComboBox();
		noviceFigName2 = new javax.swing.JComboBox();
		noviceFigName3 = new javax.swing.JComboBox();
		noviceFigName4 = new javax.swing.JComboBox();
		intFigName1 = new javax.swing.JComboBox();
		intFigName2 = new javax.swing.JComboBox();
		intFigName3 = new javax.swing.JComboBox();
		intFigName4 = new javax.swing.JComboBox();
		jLabel22 = new javax.swing.JLabel();
		regularMeetButton = new javax.swing.JRadioButton();
		champsButton = new javax.swing.JRadioButton();
		meetDate = new javax.swing.JTextField();

		jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel1.setText("Title for Meet Report");

		meetName.setFont(new java.awt.Font("Tahoma", 0, 14));

		jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel2.setText("Meet Date");

		jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel3.setText("Novice Figures");

		jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel4.setText("Station 1");

		jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel5.setText("Figure Description");

		jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel6.setText("DD");

		jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel7.setText("Fig. #");

		n8ULabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
		n8ULabel.setText("8 and Under");

		noviceFigNum1.setEditable(false);
		noviceFigNum1.setFont(new java.awt.Font("Tahoma", 0, 14));
		noviceFigNum1.setFocusable(false);

		noviceDD1.setEditable(false);
		noviceDD1.setFont(new java.awt.Font("Tahoma", 0, 14));
		noviceDD1.setFocusable(false);

		eightAndUnder1.setFont(new java.awt.Font("Tahoma", 0, 14));

		jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel9.setText("Station 2");

		noviceFigNum2.setEditable(false);
		noviceFigNum2.setFont(new java.awt.Font("Tahoma", 0, 14));
		noviceFigNum2.setFocusable(false);

		noviceDD2.setEditable(false);
		noviceDD2.setFont(new java.awt.Font("Tahoma", 0, 14));
		noviceDD2.setFocusable(false);

		eightAndUnder2.setFont(new java.awt.Font("Tahoma", 0, 14));

		jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel10.setText("Station 3");

		noviceFigNum3.setEditable(false);
		noviceFigNum3.setFont(new java.awt.Font("Tahoma", 0, 14));
		noviceFigNum3.setFocusable(false);

		noviceDD3.setEditable(false);
		noviceDD3.setFont(new java.awt.Font("Tahoma", 0, 14));
		noviceDD3.setFocusable(false);

		eightAndUnder3.setFont(new java.awt.Font("Tahoma", 0, 14));

		jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel11.setText("Station 4");

		noviceFigNum4.setEditable(false);
		noviceFigNum4.setFont(new java.awt.Font("Tahoma", 0, 14));
		noviceFigNum4.setFocusable(false);

		noviceDD4.setEditable(false);
		noviceDD4.setFont(new java.awt.Font("Tahoma", 0, 14));
		noviceDD4.setFocusable(false);

		eightAndUnder4.setFont(new java.awt.Font("Tahoma", 0, 14));

		jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel12.setText("Station 1");

		jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel13.setText("Station 2");

		jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel14.setText("Fig. #");

		intFigNum1.setEditable(false);
		intFigNum1.setFont(new java.awt.Font("Tahoma", 0, 14));
		intFigNum1.setFocusable(false);

		intFigNum2.setEditable(false);
		intFigNum2.setFont(new java.awt.Font("Tahoma", 0, 14));
		intFigNum2.setFocusable(false);

		jLabel15.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel15.setText("DD");

		intDD1.setEditable(false);
		intDD1.setFont(new java.awt.Font("Tahoma", 0, 14));
		intDD1.setFocusable(false);

		intDD2.setEditable(false);
		intDD2.setFont(new java.awt.Font("Tahoma", 0, 14));
		intDD2.setFocusable(false);

		jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel16.setText("Figure Description");

		jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel17.setText("Intermediate Figures");

		jLabel19.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel19.setText("Station 4");

		intFigNum4.setEditable(false);
		intFigNum4.setFont(new java.awt.Font("Tahoma", 0, 14));
		intFigNum4.setFocusable(false);

		intDD4.setEditable(false);
		intDD4.setFont(new java.awt.Font("Tahoma", 0, 14));
		intDD4.setFocusable(false);

		jLabel20.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel20.setText("Station 3");

		intFigNum3.setEditable(false);
		intFigNum3.setFont(new java.awt.Font("Tahoma", 0, 14));
		intFigNum3.setFocusable(false);

		intDD3.setEditable(false);
		intDD3.setFont(new java.awt.Font("Tahoma", 0, 14));
		intDD3.setFocusable(false);

		homeTeam.setFont(new java.awt.Font("Tahoma", 0, 14));
		homeTeam.setModel(new javax.swing.DefaultComboBoxModel(new String[] {
				"Item 1", "Item 2", "Item 3", "Item 4" }));

		jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel18.setText("Home Team");

		jLabel21.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel21.setText("Opponents");

		opponents.setFont(new java.awt.Font("Tahoma", 0, 14));
		jScrollPane1.setViewportView(opponents);

		saveButton.setFont(new java.awt.Font("Tahoma", 0, 14));
		saveButton.setText("Save");
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveButtonActionPerformed(evt);
			}
		});

		noviceFigName1.setFont(new java.awt.Font("Tahoma", 0, 14));
		noviceFigName1.setRenderer(new FigureListCellRenderer());
		noviceFigName1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				noviceFigName1ActionPerformed(evt);
			}
		});

		noviceFigName2.setFont(new java.awt.Font("Tahoma", 0, 14));
		noviceFigName2.setRenderer(new FigureListCellRenderer());
		noviceFigName2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				noviceFigName2ActionPerformed(evt);
			}
		});

		noviceFigName3.setFont(new java.awt.Font("Tahoma", 0, 14));
		noviceFigName3.setRenderer(new FigureListCellRenderer());
		noviceFigName3.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				noviceFigName3ActionPerformed(evt);
			}
		});

		noviceFigName4.setFont(new java.awt.Font("Tahoma", 0, 14));
		noviceFigName4.setRenderer(new FigureListCellRenderer());
		noviceFigName4.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				noviceFigName4ActionPerformed(evt);
			}
		});

		intFigName1.setFont(new java.awt.Font("Tahoma", 0, 14));
		intFigName1.setRenderer(new FigureListCellRenderer());
		intFigName1.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				intFigName1ActionPerformed(evt);
			}
		});

		intFigName2.setFont(new java.awt.Font("Tahoma", 0, 14));
		intFigName2.setRenderer(new FigureListCellRenderer());
		intFigName2.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				intFigName2ActionPerformed(evt);
			}
		});

		intFigName3.setFont(new java.awt.Font("Tahoma", 0, 14));
		intFigName3.setRenderer(new FigureListCellRenderer());
		intFigName3.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				intFigName3ActionPerformed(evt);
			}
		});

		intFigName4.setFont(new java.awt.Font("Tahoma", 0, 14));
		intFigName4.setRenderer(new FigureListCellRenderer());
		intFigName4.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				intFigName4ActionPerformed(evt);
			}
		});

		jLabel22.setFont(new java.awt.Font("Tahoma", 0, 14));
		jLabel22.setText("Meet type:");

		meetTypeButtonGroup.add(regularMeetButton);
		regularMeetButton.setFont(new java.awt.Font("Tahoma", 0, 14));
		regularMeetButton.setSelected(true);
		regularMeetButton.setText("Regular");

		meetTypeButtonGroup.add(champsButton);
		champsButton.setFont(new java.awt.Font("Tahoma", 0, 14));
		champsButton.setText("Championship");

		meetDate.setFont(new java.awt.Font("Tahoma", 0, 14));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
							.addContainerGap()
							.addComponent(saveButton))
						.addGroup(layout.createSequentialGroup()
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createSequentialGroup()
									.addGap(10)
									.addComponent(jLabel1))
								.addGroup(layout.createSequentialGroup()
									.addContainerGap()
									.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addComponent(jLabel4)
										.addComponent(jLabel9)
										.addComponent(jLabel11)
										.addComponent(jLabel10)
										.addComponent(jLabel12)
										.addComponent(jLabel13)
										.addComponent(jLabel19)
										.addComponent(jLabel20))
									.addGap(26)
									.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addComponent(jLabel7)
										.addComponent(noviceFigNum1, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
										.addComponent(noviceFigNum2, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
										.addComponent(noviceFigNum4, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
										.addComponent(noviceFigNum3, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel14)
										.addComponent(intFigNum1, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
										.addComponent(intFigNum2, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
										.addComponent(intFigNum4, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
										.addComponent(intFigNum3, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addComponent(jLabel6)
										.addComponent(noviceDD1, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
										.addComponent(noviceDD2, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
										.addComponent(noviceDD4, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
										.addComponent(noviceDD3, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel15)
										.addComponent(intDD1, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
										.addComponent(intDD2, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
										.addComponent(intDD4, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
										.addComponent(intDD3, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
										.addComponent(intFigName4, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(intFigName3, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(intFigName2, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(intFigName1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jLabel5)
										.addComponent(jLabel3)
										.addComponent(jLabel16)
										.addComponent(jLabel17)
										.addComponent(noviceFigName1, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(noviceFigName4, 0, 186, Short.MAX_VALUE)
										.addComponent(noviceFigName3, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(noviceFigName2, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(layout.createParallelGroup(Alignment.LEADING)
										.addComponent(n8ULabel)
										.addComponent(eightAndUnder1)
										.addComponent(eightAndUnder2)
										.addComponent(eightAndUnder4)
										.addComponent(eightAndUnder3)))
								.addGroup(layout.createSequentialGroup()
									.addContainerGap()
									.addComponent(jLabel22)
									.addGap(18)
									.addComponent(regularMeetButton)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(champsButton))
								.addGroup(layout.createSequentialGroup()
									.addContainerGap()
									.addComponent(meetName, GroupLayout.PREFERRED_SIZE, 500, GroupLayout.PREFERRED_SIZE)))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(layout.createParallelGroup(Alignment.LEADING, false)
								.addComponent(meetDate)
								.addComponent(jLabel21)
								.addComponent(homeTeam, 0, 215, Short.MAX_VALUE)
								.addComponent(jLabel18)
								.addComponent(jLabel2)
								.addComponent(jScrollPane1, 0, 0, Short.MAX_VALUE))))
					.addContainerGap(18, Short.MAX_VALUE))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addComponent(jLabel1)
						.addComponent(jLabel2))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(meetName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(meetDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(2)
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
							.addGap(18)
							.addComponent(jLabel18)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(homeTeam, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(jLabel21)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 148, GroupLayout.PREFERRED_SIZE))
						.addGroup(layout.createSequentialGroup()
							.addGap(4)
							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(jLabel22)
								.addComponent(regularMeetButton)
								.addComponent(champsButton))
							.addGap(12)
							.addComponent(jLabel3)
							.addGap(4)
							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(jLabel7)
								.addComponent(jLabel6)
								.addComponent(jLabel5)
								.addComponent(n8ULabel))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(layout.createParallelGroup(Alignment.TRAILING)
								.addGroup(layout.createSequentialGroup()
									.addGroup(layout.createParallelGroup(Alignment.BASELINE)
										.addComponent(jLabel4)
										.addComponent(noviceFigNum1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(noviceDD1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(noviceFigName1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
									.addGap(6))
								.addGroup(layout.createSequentialGroup()
									.addComponent(eightAndUnder1)
									.addPreferredGap(ComponentPlacement.UNRELATED)))
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
									.addComponent(jLabel9)
									.addComponent(noviceFigNum2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(noviceDD2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(noviceFigName2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(eightAndUnder2))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
									.addComponent(jLabel10)
									.addComponent(noviceFigNum3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(noviceDD3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(noviceFigName3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(eightAndUnder3))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(layout.createParallelGroup(Alignment.LEADING)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
									.addComponent(jLabel11)
									.addComponent(noviceFigNum4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(noviceDD4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addComponent(noviceFigName4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addComponent(eightAndUnder4))
							.addGap(23)
							.addComponent(jLabel17)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(jLabel14)
								.addComponent(jLabel15)
								.addComponent(jLabel16))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(jLabel12)
								.addComponent(intFigNum1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(intDD1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(intFigName1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(6)
							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(jLabel13)
								.addComponent(intFigNum2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(intDD2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(intFigName2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(6)
							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(jLabel20)
								.addComponent(intFigNum3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(intDD3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(intFigName3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(6)
							.addGroup(layout.createParallelGroup(Alignment.BASELINE)
								.addComponent(jLabel19)
								.addComponent(intFigNum4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(intDD4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(intFigName4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addPreferredGap(ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
					.addComponent(saveButton)
					.addContainerGap())
		);
		this.setLayout(layout);
	}// </editor-fold>//GEN-END:initComponents

	private class FigureListCellRenderer extends DefaultListCellRenderer {
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			if (value instanceof Figure) {
				Figure mec = (Figure) value;
				setText(mec.getName());
			}
			return this;
		}
	}

	private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveButtonActionPerformed
		logger.info("Save meet start.");
		List<Team> op = new ArrayList<Team>();
		for (int i = 0; i < opponents.getModel().getSize(); i++) {
			CheckListItem cli = (CheckListItem) opponents.getModel()
					.getElementAt(i);
			if (cli.isSelected())
				op.add((Team) cli.getItem());
		}
		if (op.size() < 1 || op.contains(homeTeam.getSelectedItem())) {
			JOptionPane
					.showMessageDialog(
							this,
							"You must select an opposing team, and the home team cannot be selected as an opposing team.",
							"Invalid opposing team selection",
							JOptionPane.ERROR_MESSAGE);
			logger.info("Invalid opponent selection, meet save canceled.");
			return;
		}

		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// Update Meet object and pass it to the controller
		meet.setName(meetName.getText());
		meet.setMeetDate(meetDate.getText());
		meet.setType(champsButton.isSelected() ? 'C' : 'R');
		meet.setHomeTeam((Team) homeTeam.getSelectedItem());
		meet.setOpponents(op);
		meet.setNov1Figure((Figure) noviceFigName1.getSelectedItem());
		meet.setNov2Figure((Figure) noviceFigName2.getSelectedItem());
		meet.setNov3Figure((Figure) noviceFigName3.getSelectedItem());
		meet.setNov4Figure((Figure) noviceFigName4.getSelectedItem());
		meet.setInt1Figure((Figure) intFigName1.getSelectedItem());
		meet.setInt2Figure((Figure) intFigName2.getSelectedItem());
		meet.setInt3Figure((Figure) intFigName3.getSelectedItem());
		meet.setInt4Figure((Figure) intFigName4.getSelectedItem());

		meet.setEu1(eightAndUnder1.isSelected());
		meet.setEu2(eightAndUnder2.isSelected());
		meet.setEu3(eightAndUnder3.isSelected());
		meet.setEu4(eightAndUnder4.isSelected());

		fireSavedEvent();
		logger.info("Save meet complete.");

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}// GEN-LAST:event_saveButtonActionPerformed

	private void noviceFigName2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_noviceFigName2ActionPerformed
		Figure f = (Figure) noviceFigName2.getSelectedItem();
		if (f != null) {
			noviceDD2.setText(f.getDegreeOfDifficulty().toPlainString());
			noviceFigNum2.setText(f.getFigureId());
		}
	}// GEN-LAST:event_noviceFigName2ActionPerformed

	private void noviceFigName1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_noviceFigName1ActionPerformed
		Figure f = (Figure) noviceFigName1.getSelectedItem();
		if (f != null) {
			noviceDD1.setText(f.getDegreeOfDifficulty().toPlainString());
			noviceFigNum1.setText(f.getFigureId());
		}
	}// GEN-LAST:event_noviceFigName1ActionPerformed

	private void noviceFigName3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_noviceFigName3ActionPerformed
		Figure f = (Figure) noviceFigName3.getSelectedItem();
		if (f != null) {
			noviceDD3.setText(f.getDegreeOfDifficulty().toPlainString());
			noviceFigNum3.setText(f.getFigureId());
		}

	}// GEN-LAST:event_noviceFigName3ActionPerformed

	private void noviceFigName4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_noviceFigName4ActionPerformed
		Figure f = (Figure) noviceFigName4.getSelectedItem();
		if (f != null) {
			noviceDD4.setText(f.getDegreeOfDifficulty().toPlainString());
			noviceFigNum4.setText(f.getFigureId());
		}
	}// GEN-LAST:event_noviceFigName4ActionPerformed

	private void intFigName1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_intFigName1ActionPerformed
		Figure f = (Figure) intFigName1.getSelectedItem();
		if (f != null) {
			intDD1.setText(f.getDegreeOfDifficulty().toPlainString());
			intFigNum1.setText(f.getFigureId());
		}
	}// GEN-LAST:event_intFigName1ActionPerformed

	private void intFigName2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_intFigName2ActionPerformed
		Figure f = (Figure) intFigName2.getSelectedItem();
		if (f != null) {
			intDD2.setText(f.getDegreeOfDifficulty().toPlainString());
			intFigNum2.setText(f.getFigureId());
		}
	}// GEN-LAST:event_intFigName2ActionPerformed

	private void intFigName3ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_intFigName3ActionPerformed
		Figure f = (Figure) intFigName3.getSelectedItem();
		if (f != null) {
			intDD3.setText(f.getDegreeOfDifficulty().toPlainString());
			intFigNum3.setText(f.getFigureId());
		}
	}// GEN-LAST:event_intFigName3ActionPerformed

	private void intFigName4ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_intFigName4ActionPerformed
		Figure f = (Figure) intFigName4.getSelectedItem();
		if (f != null) {
			intDD4.setText(f.getDegreeOfDifficulty().toPlainString());
			intFigNum4.setText(f.getFigureId());
		}
	}// GEN-LAST:event_intFigName4ActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JRadioButton champsButton;
	private javax.swing.JCheckBox eightAndUnder1;
	private javax.swing.JCheckBox eightAndUnder2;
	private javax.swing.JCheckBox eightAndUnder3;
	private javax.swing.JCheckBox eightAndUnder4;
	private javax.swing.JComboBox homeTeam;
	private javax.swing.JTextField intDD1;
	private javax.swing.JTextField intDD2;
	private javax.swing.JTextField intDD3;
	private javax.swing.JTextField intDD4;
	private javax.swing.JComboBox intFigName1;
	private javax.swing.JComboBox intFigName2;
	private javax.swing.JComboBox intFigName3;
	private javax.swing.JComboBox intFigName4;
	private javax.swing.JTextField intFigNum1;
	private javax.swing.JTextField intFigNum2;
	private javax.swing.JTextField intFigNum3;
	private javax.swing.JTextField intFigNum4;
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
	private javax.swing.JLabel jLabel19;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel20;
	private javax.swing.JLabel jLabel21;
	private javax.swing.JLabel jLabel22;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField meetDate;
	private javax.swing.JTextField meetName;
	private javax.swing.ButtonGroup meetTypeButtonGroup;
	private javax.swing.JLabel n8ULabel;
	private javax.swing.JTextField noviceDD1;
	private javax.swing.JTextField noviceDD2;
	private javax.swing.JTextField noviceDD3;
	private javax.swing.JTextField noviceDD4;
	private javax.swing.JComboBox noviceFigName1;
	private javax.swing.JComboBox noviceFigName2;
	private javax.swing.JComboBox noviceFigName3;
	private javax.swing.JComboBox noviceFigName4;
	private javax.swing.JTextField noviceFigNum1;
	private javax.swing.JTextField noviceFigNum2;
	private javax.swing.JTextField noviceFigNum3;
	private javax.swing.JTextField noviceFigNum4;
	private javax.swing.JList opponents;
	private javax.swing.JRadioButton regularMeetButton;
	private javax.swing.JButton saveButton;
}
