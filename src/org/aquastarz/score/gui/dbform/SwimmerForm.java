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
package org.aquastarz.score.gui.dbform;

import java.awt.Component;
import java.awt.EventQueue;
import java.beans.Beans;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;

import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.domain.Level;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.manager.SwimmerManager;

public class SwimmerForm extends JPanel {

	EntityManager entityManager = null;

	public SwimmerForm() {
		if (!Beans.isDesignTime()) {
			entityManager = ScoreApp.getEntityManager();
			entityManager.getTransaction().begin();
		}
		initComponents();
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
		bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

		swimmerQuery = java.beans.Beans.isDesignTime() ? null
				: entityManager
						.createQuery("SELECT s FROM Swimmer s where s.season like :season order by leagueNum");
		if (!java.beans.Beans.isDesignTime())
			swimmerQuery.setParameter("season", ScoreApp.getCurrentSeason());
		swimmerList = java.beans.Beans.isDesignTime() ? java.util.Collections
				.emptyList()
				: org.jdesktop.observablecollections.ObservableCollections
						.observableList(swimmerQuery.getResultList());
		levelQuery = java.beans.Beans.isDesignTime() ? null : entityManager
				.createQuery("SELECT l FROM Level l");
		levelList = java.beans.Beans.isDesignTime() ? java.util.Collections
				.emptyList() : levelQuery.getResultList();
		teamQuery = java.beans.Beans.isDesignTime() ? null : entityManager
				.createQuery("SELECT t FROM Team t");
		teamList = java.beans.Beans.isDesignTime() ? java.util.Collections
				.emptyList() : teamQuery.getResultList();
		masterScrollPane = new javax.swing.JScrollPane();
		masterTable = new javax.swing.JTable();
		swimmerIdLabel = new javax.swing.JLabel();
		lastNameLabel = new javax.swing.JLabel();
		firstNameLabel = new javax.swing.JLabel();
		levelLabel = new javax.swing.JLabel();
		teamLabel = new javax.swing.JLabel();
		swimmerIdField = new javax.swing.JTextField();
		lastNameField = new javax.swing.JTextField();
		firstNameField = new javax.swing.JTextField();
		saveButton = new javax.swing.JButton();
		refreshButton = new javax.swing.JButton();
		newButton = new javax.swing.JButton();
		deleteButton = new javax.swing.JButton();
		jComboBox1 = new javax.swing.JComboBox();
		jComboBox2 = new javax.swing.JComboBox();

		FormListener formListener = new FormListener();

		org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings
				.createJTableBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						swimmerList, masterTable);
		org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding
				.addColumnBinding(org.jdesktop.beansbinding.ELProperty
						.create("${leagueNum}"));
		columnBinding.setColumnName("League #");
		columnBinding.setColumnClass(Integer.class);
		columnBinding.setEditable(false);
		columnBinding = jTableBinding
				.addColumnBinding(org.jdesktop.beansbinding.ELProperty
						.create("${lastName}"));
		columnBinding.setColumnName("Last Name");
		columnBinding.setColumnClass(String.class);
		columnBinding.setEditable(false);
		columnBinding = jTableBinding
				.addColumnBinding(org.jdesktop.beansbinding.ELProperty
						.create("${firstName}"));
		columnBinding.setColumnName("First Name");
		columnBinding.setColumnClass(String.class);
		columnBinding.setEditable(false);
		columnBinding = jTableBinding
				.addColumnBinding(org.jdesktop.beansbinding.ELProperty
						.create("${level.name}"));
		columnBinding.setColumnName("Level");
		columnBinding.setColumnClass(String.class);
		columnBinding.setEditable(false);
		columnBinding = jTableBinding
				.addColumnBinding(org.jdesktop.beansbinding.ELProperty
						.create("${team.name}"));
		columnBinding.setColumnName("Team");
		columnBinding.setColumnClass(String.class);
		columnBinding.setEditable(false);
		bindingGroup.addBinding(jTableBinding);
		jTableBinding.bind();
		masterScrollPane.setViewportView(masterTable);

		swimmerIdLabel.setText("League #:");

		lastNameLabel.setText("Last Name:");

		firstNameLabel.setText("First Name:");

		levelLabel.setText("Level:");

		teamLabel.setText("Team:");

		org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings
				.createAutoBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						masterTable, org.jdesktop.beansbinding.ELProperty
								.create("${selectedElement.leagueNum}"),
						swimmerIdField, org.jdesktop.beansbinding.BeanProperty
								.create("text"));
		binding.setSourceUnreadableValue(null);
		bindingGroup.addBinding(binding);
		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ,
				masterTable, org.jdesktop.beansbinding.ELProperty
						.create("${selectedElement != null}"), swimmerIdField,
				org.jdesktop.beansbinding.BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		binding = org.jdesktop.beansbinding.Bindings
				.createAutoBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						masterTable, org.jdesktop.beansbinding.ELProperty
								.create("${selectedElement.lastName}"),
						lastNameField, org.jdesktop.beansbinding.BeanProperty
								.create("text"));
		binding.setSourceUnreadableValue(null);
		bindingGroup.addBinding(binding);
		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ,
				masterTable, org.jdesktop.beansbinding.ELProperty
						.create("${selectedElement != null}"), lastNameField,
				org.jdesktop.beansbinding.BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		binding = org.jdesktop.beansbinding.Bindings
				.createAutoBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						masterTable, org.jdesktop.beansbinding.ELProperty
								.create("${selectedElement.firstName}"),
						firstNameField, org.jdesktop.beansbinding.BeanProperty
								.create("text"));
		binding.setSourceUnreadableValue(null);
		bindingGroup.addBinding(binding);
		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ,
				masterTable, org.jdesktop.beansbinding.ELProperty
						.create("${selectedElement != null}"), firstNameField,
				org.jdesktop.beansbinding.BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		saveButton.setText("Save");
		saveButton.addActionListener(formListener);

		refreshButton.setText("Refresh");
		refreshButton.addActionListener(formListener);

		newButton.setText("New");
		newButton.addActionListener(formListener);

		deleteButton.setText("Delete");

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ,
				masterTable, org.jdesktop.beansbinding.ELProperty
						.create("${selectedElement != null}"), deleteButton,
				org.jdesktop.beansbinding.BeanProperty.create("enabled"));
		bindingGroup.addBinding(binding);

		deleteButton.addActionListener(formListener);

		jComboBox1.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);
				if (value instanceof Level) {
					Level level = (Level) value;
					setText(level.getName());
				}
				return this;
			}
		});

		org.jdesktop.swingbinding.JComboBoxBinding jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings
				.createJComboBoxBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						levelList, jComboBox1);
		bindingGroup.addBinding(jComboBoxBinding);
		binding = org.jdesktop.beansbinding.Bindings
				.createAutoBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						masterTable, org.jdesktop.beansbinding.ELProperty
								.create("${selectedElement.level}"),
						jComboBox1, org.jdesktop.beansbinding.BeanProperty
								.create("selectedItem"));
		bindingGroup.addBinding(binding);

		jComboBox2.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list,
					Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index,
						isSelected, cellHasFocus);
				if (value instanceof Team) {
					Team team = (Team) value;
					setText(team.getName());
				}
				return this;
			}
		});

		jComboBoxBinding = org.jdesktop.swingbinding.SwingBindings
				.createJComboBoxBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						teamList, jComboBox2);
		bindingGroup.addBinding(jComboBoxBinding);
		binding = org.jdesktop.beansbinding.Bindings
				.createAutoBinding(
						org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
						masterTable, org.jdesktop.beansbinding.ELProperty
								.create("${selectedElement.team}"), jComboBox2,
						org.jdesktop.beansbinding.BeanProperty
								.create("selectedItem"));
		bindingGroup.addBinding(binding);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
		this.setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addGroup(
														javax.swing.GroupLayout.Alignment.TRAILING,
														layout.createSequentialGroup()
																.addComponent(
																		newButton)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		deleteButton)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		refreshButton)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		saveButton))
												.addGroup(
														layout.createSequentialGroup()
																.addContainerGap()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(
																						swimmerIdLabel)
																				.addComponent(
																						lastNameLabel)
																				.addComponent(
																						firstNameLabel)
																				.addComponent(
																						levelLabel)
																				.addComponent(
																						teamLabel))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																				.addComponent(
																						swimmerIdField,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						269,
																						Short.MAX_VALUE)
																				.addComponent(
																						lastNameField,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						269,
																						Short.MAX_VALUE)
																				.addComponent(
																						firstNameField,
																						javax.swing.GroupLayout.DEFAULT_SIZE,
																						269,
																						Short.MAX_VALUE)
																				.addComponent(
																						jComboBox1,
																						0,
																						269,
																						Short.MAX_VALUE)
																				.addComponent(
																						jComboBox2,
																						0,
																						269,
																						Short.MAX_VALUE)))
												.addGroup(
														layout.createSequentialGroup()
																.addContainerGap()
																.addComponent(
																		masterScrollPane,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		328,
																		Short.MAX_VALUE)))
								.addContainerGap()));

		layout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
				new java.awt.Component[] { deleteButton, newButton,
						refreshButton, saveButton });

		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(masterScrollPane,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										126, Short.MAX_VALUE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(swimmerIdLabel)
												.addComponent(
														swimmerIdField,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(lastNameLabel)
												.addComponent(
														lastNameField,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(firstNameLabel)
												.addComponent(
														firstNameField,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(levelLabel)
												.addComponent(
														jComboBox1,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(teamLabel)
												.addComponent(
														jComboBox2,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(saveButton)
												.addComponent(refreshButton)
												.addComponent(deleteButton)
												.addComponent(newButton))
								.addContainerGap()));

		bindingGroup.bind();
	}

	// Code for dispatching events from components to event handlers.

	private class FormListener implements java.awt.event.ActionListener {
		FormListener() {
		}

		@Override
		public void actionPerformed(java.awt.event.ActionEvent evt) {
			if (evt.getSource() == saveButton) {
				SwimmerForm.this.saveButtonActionPerformed(evt);
			} else if (evt.getSource() == refreshButton) {
				SwimmerForm.this.refreshButtonActionPerformed(evt);
			} else if (evt.getSource() == newButton) {
				SwimmerForm.this.newButtonActionPerformed(evt);
			} else if (evt.getSource() == deleteButton) {
				SwimmerForm.this.deleteButtonActionPerformed(evt);
			}
		}
	}// </editor-fold>//GEN-END:initComponents

	@SuppressWarnings("unchecked")
	private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_refreshButtonActionPerformed
		entityManager.getTransaction().rollback();
		entityManager.getTransaction().begin();
		java.util.Collection data = swimmerQuery.getResultList();
		for (Object entity : data) {
			entityManager.refresh(entity);
		}
		swimmerList.clear();
		swimmerList.addAll(data);
	}// GEN-LAST:event_refreshButtonActionPerformed

	private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_deleteButtonActionPerformed
		int[] selected = masterTable.getSelectedRows();
		List<org.aquastarz.score.domain.Swimmer> toRemove = new ArrayList<org.aquastarz.score.domain.Swimmer>(
				selected.length);
		for (int idx = 0; idx < selected.length; idx++) {
			org.aquastarz.score.domain.Swimmer s = swimmerList.get(masterTable
					.convertRowIndexToModel(selected[idx]));
			toRemove.add(s);
			entityManager.remove(s);
		}
		swimmerList.removeAll(toRemove);
	}// GEN-LAST:event_deleteButtonActionPerformed

	private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_newButtonActionPerformed
		org.aquastarz.score.domain.Swimmer s = new org.aquastarz.score.domain.Swimmer();
		s.setSeason(ScoreApp.getCurrentSeason());
		s.setFirstName("");
		s.setLastName("");
		s.setLeagueNum(SwimmerManager.getNextLeagueNumber());
		s.setLevel(levelList.get(0));
		s.setTeam(teamList.get(0));
		entityManager.persist(s);

		swimmerList.add(s);
		int row = swimmerList.size() - 1;
		masterTable.setRowSelectionInterval(row, row);
		masterTable.scrollRectToVisible(masterTable.getCellRect(row, 0, true));
	}// GEN-LAST:event_newButtonActionPerformed

	private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveButtonActionPerformed
		try {
			entityManager.getTransaction().commit();
			entityManager.getTransaction().begin();
		} catch (RollbackException rex) {
			rex.printStackTrace();
			entityManager.getTransaction().begin();
			List<org.aquastarz.score.domain.Swimmer> merged = new ArrayList<org.aquastarz.score.domain.Swimmer>(
					swimmerList.size());
			for (org.aquastarz.score.domain.Swimmer s : swimmerList) {
				merged.add(entityManager.merge(s));
			}
			swimmerList.clear();
			swimmerList.addAll(merged);
		}
		refreshButtonActionPerformed(null);
	}// GEN-LAST:event_saveButtonActionPerformed
		// Variables declaration - do not modify//GEN-BEGIN:variables

	private javax.swing.JButton deleteButton;
	private javax.swing.JTextField firstNameField;
	private javax.swing.JLabel firstNameLabel;
	private javax.swing.JComboBox jComboBox1;
	private javax.swing.JComboBox jComboBox2;
	private javax.swing.JTextField lastNameField;
	private javax.swing.JLabel lastNameLabel;
	private javax.swing.JLabel levelLabel;
	private java.util.List<org.aquastarz.score.domain.Level> levelList;
	private javax.persistence.Query levelQuery;
	private javax.swing.JScrollPane masterScrollPane;
	private javax.swing.JTable masterTable;
	private javax.swing.JButton newButton;
	private javax.swing.JButton refreshButton;
	private javax.swing.JButton saveButton;
	private javax.swing.JTextField swimmerIdField;
	private javax.swing.JLabel swimmerIdLabel;
	private java.util.List<org.aquastarz.score.domain.Swimmer> swimmerList;
	private javax.persistence.Query swimmerQuery;
	private javax.swing.JLabel teamLabel;
	private java.util.List<org.aquastarz.score.domain.Team> teamList;
	private javax.persistence.Query teamQuery;
	private org.jdesktop.beansbinding.BindingGroup bindingGroup;

	// End of variables declaration//GEN-END:variables

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame frame = new JFrame();
				frame.setContentPane(new SwimmerForm());
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}
