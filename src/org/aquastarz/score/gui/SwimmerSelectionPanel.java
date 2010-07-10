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

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.swing.ListSelectionModel;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.domain.Team;

public class SwimmerSelectionPanel extends javax.swing.JPanel {

    private Vector<CheckListItem<Swimmer>> checkListItems = null;

    /** Creates new form SwimmerSelectionPanel */
    public SwimmerSelectionPanel(Team team) {
        initComponents();
        teamNameLabel.setText("Participating swimmers for " + team.getName() + ":");
    }

    public void setSwimmers(List<Swimmer> roster, List<Swimmer> participatingSwimmers) {
        swimmerList.removeAll();
        checkListItems = new Vector<CheckListItem<Swimmer>>();
        for (Swimmer swimmer : roster) {
            CheckListItem<Swimmer> item = new CheckListItem<Swimmer>(swimmer, swimmer.toString());
            if (participatingSwimmers == null || participatingSwimmers.contains(swimmer)) {
                item.setSelected(true);
            }
            checkListItems.add(item);
        }
        updateCheckList();

        CheckListCellRenderer renderer = new CheckListCellRenderer();
        swimmerList.setCellRenderer(renderer);
        swimmerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        CheckListener lst = new CheckListener(swimmerList) {

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                numSelected.setText(Integer.toString(countSelectedSwimmers()));
            }
        };
        swimmerList.addMouseListener(lst);
        swimmerList.addKeyListener(lst);
    }

    private void updateCheckList() {
        checkListItems = sortList(checkListItems);
        swimmerList.setListData(checkListItems);
        numSelected.setText(Integer.toString(countSelectedSwimmers()));
    }

    private Vector<CheckListItem<Swimmer>> sortList(Vector<CheckListItem<Swimmer>> list) {
        LinkedList<CheckListItem<Swimmer>> sortList = new LinkedList<CheckListItem<Swimmer>>();
        sortList.addAll(list);
        Collections.sort(sortList, new Comparator() {

            public int compare(Object o1, Object o2) {
                CheckListItem<Swimmer> c1 = (CheckListItem<Swimmer>) o1;
                CheckListItem<Swimmer> c2 = (CheckListItem<Swimmer>) o2;
                if (sortByName.isSelected()) {
                    int c = c1.getItem().getLastName().compareTo(c2.getItem().getLastName());
                    if (c == 0) {
                        return c1.getItem().getFirstName().compareTo(c2.getItem().getFirstName());
                    } else {
                        return c;
                    }
                } else if (sortByNumber.isSelected()) {
                    return c1.getItem().getLeagueNum().compareTo(c2.getItem().getLeagueNum());
                } else {
                    int c = c1.getItem().getLevel().getSortOrder().compareTo(c2.getItem().getLevel().getSortOrder());
                    if (c == 0) {
                        return c1.getItem().getLeagueNum().compareTo(c2.getItem().getLeagueNum());
                    } else {
                        return c;
                    }
                }
            }
        });
        Vector<CheckListItem<Swimmer>> result = new Vector<CheckListItem<Swimmer>>();
        result.addAll(sortList);
        return result;
    }

    public Collection<Swimmer> getSelectedSwimmers() {
        ArrayList<Swimmer> swimmers = new ArrayList<Swimmer>();
        for (int i = 0; i < swimmerList.getModel().getSize(); i++) {
            CheckListItem<Swimmer> item = (CheckListItem<Swimmer>) swimmerList.getModel().getElementAt(i);
            if (item.isSelected()) {
                swimmers.add(item.getItem());
            }
        }
        return swimmers;
    }

    public int countSelectedSwimmers() {
        int c = 0;
        for (int i = 0; i < swimmerList.getModel().getSize(); i++) {
            CheckListItem<Swimmer> item = (CheckListItem<Swimmer>) swimmerList.getModel().getElementAt(i);
            if (item.isSelected()) {
                c++;
            }
        }
        return c;
    }

    private void setAll(boolean selected) {
        for (int i = 0; i < swimmerList.getModel().getSize(); i++) {
            CheckListItem<Swimmer> item = (CheckListItem<Swimmer>) swimmerList.getModel().getElementAt(i);
            item.setSelected(selected);
        }
        updateCheckList();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sortByGroup = new javax.swing.ButtonGroup();
        teamNameLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        swimmerList = new javax.swing.JList();
        selectAllButton = new javax.swing.JButton();
        selectNoneButton = new javax.swing.JButton();
        sortByLevel = new javax.swing.JRadioButton();
        sortByNumber = new javax.swing.JRadioButton();
        jLabel1 = new javax.swing.JLabel();
        sortByName = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        numSelected = new javax.swing.JTextField();

        teamNameLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        teamNameLabel.setText("Swimmers for Team Name");

        swimmerList.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        swimmerList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(swimmerList);

        selectAllButton.setFont(new java.awt.Font("Tahoma", 0, 14));
        selectAllButton.setText("Select All");
        selectAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllButtonActionPerformed(evt);
            }
        });

        selectNoneButton.setFont(new java.awt.Font("Tahoma", 0, 14));
        selectNoneButton.setText("Select None");
        selectNoneButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectNoneButtonActionPerformed(evt);
            }
        });

        sortByGroup.add(sortByLevel);
        sortByLevel.setFont(new java.awt.Font("Tahoma", 0, 14));
        sortByLevel.setSelected(true);
        sortByLevel.setText("Level");
        sortByLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortByLevelActionPerformed(evt);
            }
        });

        sortByGroup.add(sortByNumber);
        sortByNumber.setFont(new java.awt.Font("Tahoma", 0, 14));
        sortByNumber.setText("Number");
        sortByNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortByNumberActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel1.setText("Sort by:");

        sortByGroup.add(sortByName);
        sortByName.setFont(new java.awt.Font("Tahoma", 0, 14));
        sortByName.setText("Name");
        sortByName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sortByNameActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setLabelFor(numSelected);
        jLabel2.setText("Number Selected:");

        numSelected.setEditable(false);
        numSelected.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        numSelected.setText("0");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(teamNameLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, 0, 0, Short.MAX_VALUE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(selectAllButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(selectNoneButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabel1)
                            .addComponent(sortByLevel)
                            .addComponent(sortByName)
                            .addComponent(numSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sortByNumber))
                        .addGap(14, 14, 14))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(teamNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(selectAllButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectNoneButton)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addGap(1, 1, 1)
                        .addComponent(sortByLevel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sortByNumber)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sortByName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(numSelected, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllButtonActionPerformed
        setAll(true);
    }//GEN-LAST:event_selectAllButtonActionPerformed

    private void selectNoneButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectNoneButtonActionPerformed
        setAll(false);
    }//GEN-LAST:event_selectNoneButtonActionPerformed

    private void sortByNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortByNumberActionPerformed
        updateCheckList();
    }//GEN-LAST:event_sortByNumberActionPerformed

    private void sortByLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortByLevelActionPerformed
        updateCheckList();
    }//GEN-LAST:event_sortByLevelActionPerformed

    private void sortByNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortByNameActionPerformed
        updateCheckList();
    }//GEN-LAST:event_sortByNameActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField numSelected;
    private javax.swing.JButton selectAllButton;
    private javax.swing.JButton selectNoneButton;
    private javax.swing.ButtonGroup sortByGroup;
    private javax.swing.JRadioButton sortByLevel;
    private javax.swing.JRadioButton sortByName;
    private javax.swing.JRadioButton sortByNumber;
    private javax.swing.JList swimmerList;
    private javax.swing.JLabel teamNameLabel;
    // End of variables declaration//GEN-END:variables
}
