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
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "figure")
@NamedQueries({@NamedQuery(name = "Figure.findAll", query = "SELECT f FROM Figure f"), @NamedQuery(name = "Figure.findByFigureId", query = "SELECT f FROM Figure f WHERE f.figureId = :figureId"), @NamedQuery(name = "Figure.findByDegreeOfDifficulty", query = "SELECT f FROM Figure f WHERE f.degreeOfDifficulty = :degreeOfDifficulty"), @NamedQuery(name = "Figure.findByName", query = "SELECT f FROM Figure f WHERE f.name = :name")})
public class Figure implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "figureId", nullable = false, length = 10)
    private String figureId;
    @Basic(optional = false)
    @Column(name = "degreeOfDifficulty", nullable = false, precision = 2, scale = 1)
    private BigDecimal degreeOfDifficulty;
    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 45)
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nov3Figure")
    private Collection<Meet> meetCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nov4Figure")
    private Collection<Meet> meetCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eu1Figure")
    private Collection<Meet> meetCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "eu2Figure")
    private Collection<Meet> meetCollection3;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "int1Figure")
    private Collection<Meet> meetCollection4;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "int2Figure")
    private Collection<Meet> meetCollection5;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "int3Figure")
    private Collection<Meet> meetCollection6;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "int4Figure")
    private Collection<Meet> meetCollection7;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nov1Figure")
    private Collection<Meet> meetCollection8;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nov2Figure")
    private Collection<Meet> meetCollection9;

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

    public Collection<Meet> getMeetCollection() {
        return meetCollection;
    }

    public void setMeetCollection(Collection<Meet> meetCollection) {
        this.meetCollection = meetCollection;
    }

    public Collection<Meet> getMeetCollection1() {
        return meetCollection1;
    }

    public void setMeetCollection1(Collection<Meet> meetCollection1) {
        this.meetCollection1 = meetCollection1;
    }

    public Collection<Meet> getMeetCollection2() {
        return meetCollection2;
    }

    public void setMeetCollection2(Collection<Meet> meetCollection2) {
        this.meetCollection2 = meetCollection2;
    }

    public Collection<Meet> getMeetCollection3() {
        return meetCollection3;
    }

    public void setMeetCollection3(Collection<Meet> meetCollection3) {
        this.meetCollection3 = meetCollection3;
    }

    public Collection<Meet> getMeetCollection4() {
        return meetCollection4;
    }

    public void setMeetCollection4(Collection<Meet> meetCollection4) {
        this.meetCollection4 = meetCollection4;
    }

    public Collection<Meet> getMeetCollection5() {
        return meetCollection5;
    }

    public void setMeetCollection5(Collection<Meet> meetCollection5) {
        this.meetCollection5 = meetCollection5;
    }

    public Collection<Meet> getMeetCollection6() {
        return meetCollection6;
    }

    public void setMeetCollection6(Collection<Meet> meetCollection6) {
        this.meetCollection6 = meetCollection6;
    }

    public Collection<Meet> getMeetCollection7() {
        return meetCollection7;
    }

    public void setMeetCollection7(Collection<Meet> meetCollection7) {
        this.meetCollection7 = meetCollection7;
    }

    public Collection<Meet> getMeetCollection8() {
        return meetCollection8;
    }

    public void setMeetCollection8(Collection<Meet> meetCollection8) {
        this.meetCollection8 = meetCollection8;
    }

    public Collection<Meet> getMeetCollection9() {
        return meetCollection9;
    }

    public void setMeetCollection9(Collection<Meet> meetCollection9) {
        this.meetCollection9 = meetCollection9;
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
