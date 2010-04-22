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
    private static org.apache.log4j.Logger logger =
            org.apache.log4j.Logger.getLogger(ScoreApp.class.getName());
    private static boolean dbProductionMode=false;
    private static final String productionFilename = System.getProperty("user.home")+"/.SynchroScore/Data";
    private static final String productionUrl = "jdbc:hsqldb:file:"+productionFilename;
    private static final String testUrl = "jdbc:hsqldb:mem:Synchro";
    private static Map props=null;

    public static EntityManager getEntityManager() {
        if(props==null) {
            props=new TreeMap();
            if(dbProductionMode) {
                props.put("hibernate.connection.url",productionUrl);
            }
            else {
                props.put("hibernate.connection.url",testUrl);
            }
        }
        return javax.persistence.Persistence.createEntityManagerFactory("synchroPU",props).createEntityManager();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        dbProductionMode=true;
        initDB(productionUrl);
        new ScoreController();
    }

    public static void initDB(String url) {
        //TODO check db for adequate data, delete and recreate if not.
        File db=new File(productionFilename);
        
        //TODO new db each run
        db.delete();
        if(!db.exists()) {
            logger.info("No db file found, create...");
            db.mkdir();
            Map initProps=new TreeMap();
            initProps.put("hibernate.hbm2ddl.auto","update");
            initProps.put("hibernate.connection.url",url);
            EntityManager entityManager = javax.persistence.Persistence.createEntityManagerFactory("synchroPU", initProps).createEntityManager();
            entityManager.close();
            logger.info("Load sample data...");
            entityManager = ScoreApp.getEntityManager();
            Bootstrap.loadLeagueData(entityManager);
            Bootstrap.loadSampleSwimmers(entityManager);
            logger.info("DB init done.");
        }
    }
}
