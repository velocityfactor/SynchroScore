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
package org.aquastarz.score.domain;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "RoutineLevel")
@NamedQueries({@NamedQuery(name = "RoutineLevel.findAllInOrder", query = "SELECT r FROM RoutineLevel r ORDER BY sortOrder"), @NamedQuery(name = "RoutineLevel.findByRoutineLevelId", query = "SELECT r FROM RoutineLevel r WHERE r.routineLevelId = :routineLevelId"), @NamedQuery(name = "RoutineLevel.findByName", query = "SELECT r FROM RoutineLevel r WHERE r.name = :name")})
public class RoutineLevel implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "routineLevelId", nullable = false, length = 10)
    private String routineLevelId;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @Basic(optional = false)
    @Column(name = "sortOrder", nullable = false)
    private Integer sortOrder;

    public RoutineLevel() {
    }

    public RoutineLevel(String routineLevelId) {
        this.routineLevelId = routineLevelId;
    }

    public RoutineLevel(String routineLevelId, String name, Integer sortOrder) {
        this.routineLevelId = routineLevelId;
        this.name = name;
        this.sortOrder = sortOrder;
    }

    public String getLevelId() {
        return routineLevelId;
    }

    public void setLevelId(String routineLevelId) {
        String oldLevelId = this.routineLevelId;
        this.routineLevelId = routineLevelId;
        changeSupport.firePropertyChange("routineLevelId", oldLevelId, routineLevelId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        changeSupport.firePropertyChange("name", oldName, name);
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (routineLevelId != null ? routineLevelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RoutineLevel)) {
            return false;
        }
        RoutineLevel other = (RoutineLevel) object;
        if ((this.routineLevelId == null && other.routineLevelId != null) || (this.routineLevelId != null && !this.routineLevelId.equals(other.routineLevelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
