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
package org.aquastarz.score;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;
import javax.persistence.EntityManager;
import org.aquastarz.score.config.Bootstrap;
import org.aquastarz.score.controller.ScoreController;

public class ScoreApp {

    private static String url = "jdbc:hsqldb:file:"+System.getProperty("user.home")+"/.SynchroScore/Data";
    private static Map props=null;

    public static EntityManager getEntityManager() {
        if(props==null) {
            props=new TreeMap();
            props.put("hibernate.connection.url",url);
        }
        return javax.persistence.Persistence.createEntityManagerFactory("synchroPU",props).createEntityManager();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initDB();
        new ScoreController();
    }

    public static void initDB() {
        //TODO check db for adequate data, delete and recreate if not.
        File db=new File(System.getProperty("user.home"),"/.SynchroScore");
        
        //TODO new db each run
        db.delete();
        if(!db.exists()) {
            System.out.println("No db file found, create...");
            db.mkdir();
            Map initProps=new TreeMap();
            initProps.put("hibernate.hbm2ddl.auto","update");
            initProps.put("hibernate.connection.url",url);
            EntityManager entityManager = javax.persistence.Persistence.createEntityManagerFactory("synchroPU", initProps).createEntityManager();
            entityManager.close();
            System.out.println("Load sample data...");
            Bootstrap.loadLeagueData();
            Bootstrap.loadSampleSwimmers();
            System.out.println("DB init done.");
        }
    }
}
