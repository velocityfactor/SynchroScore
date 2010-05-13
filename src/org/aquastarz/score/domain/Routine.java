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

import java.io.Serializable;
import java.util.List;
import java.util.Vector;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "routine")
@NamedQueries({@NamedQuery(name = "Routine.findByMeetOrderByRoutineOrder", query = "SELECT r FROM Routine r WHERE r.meet = :meet ORDER BY routineOrder")})
public class Routine implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    private Long routineId;

    @ManyToOne
    @JoinColumn(name = "meet")
    private Season meet;

    @Basic(optional = false)
    @Column(name = "name", nullable = false, length = 60)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "level", referencedColumnName = "routineLevelId", nullable = false)
    private RoutineLevel level;

    @ManyToOne(optional = false)
    @JoinColumn(name = "team", referencedColumnName = "teamId", nullable = false)
    private Team team;

    @Basic(optional = false)
    @Column(name = "routineType", nullable = false, length = 4)
    private String routineType;

    @ManyToMany
    private List<Swimmer> swimmers = new Vector();

    @Basic(optional = true)
    @Column(name = "routineOrder", nullable = true)
    private Integer routineOrder;

    public Routine() {
    }

    public Routine(String name) {
        this.name = name;
    }

}
