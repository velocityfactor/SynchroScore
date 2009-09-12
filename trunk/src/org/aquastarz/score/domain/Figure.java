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
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@Entity
@NamedQueries({@NamedQuery(name = "Figure.findAll", query = "SELECT f FROM Figure f"), @NamedQuery(name = "Figure.findByFigureId", query = "SELECT f FROM Figure f WHERE f.figureId = :figureId"), @NamedQuery(name = "Figure.findByDegreeOfDifficulty", query = "SELECT f FROM Figure f WHERE f.degreeOfDifficulty = :degreeOfDifficulty"), @NamedQuery(name = "Figure.findByName", query = "SELECT f FROM Figure f WHERE f.name = :name")})
public class Figure implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;

    @Id
    private String figureId;

    @Column(precision = 2, scale = 1)
    private BigDecimal degreeOfDifficulty;

    @Column(length = 45)
    private String name;

    public Figure() {
    }

    public Figure(String figureId) {
        this.figureId = figureId;
    }

    public Figure(String figureId, BigDecimal degreeOfDifficulty, String name) {
        this.figureId = figureId;
        this.degreeOfDifficulty = degreeOfDifficulty;
        this.name = name;
    }

    public String getFigureId() {
        return figureId;
    }

    public void setFigureId(String figureId) {
        String oldFigureId = this.figureId;
        this.figureId = figureId;
        changeSupport.firePropertyChange("figureId", oldFigureId, figureId);
    }

    public BigDecimal getDegreeOfDifficulty() {
        return degreeOfDifficulty;
    }

    public void setDegreeOfDifficulty(BigDecimal degreeOfDifficulty) {
        BigDecimal oldDegreeOfDifficulty = this.degreeOfDifficulty;
        this.degreeOfDifficulty = degreeOfDifficulty;
        changeSupport.firePropertyChange("degreeOfDifficulty", oldDegreeOfDifficulty, degreeOfDifficulty);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        changeSupport.firePropertyChange("name", oldName, name);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (figureId != null ? figureId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Figure)) {
            return false;
        }
        Figure other = (Figure) object;
        if ((this.figureId == null && other.figureId != null) || (this.figureId != null && !this.figureId.equals(other.figureId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.aquastarz.score.domain.Figure[figureId=" + figureId + "]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
