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
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.aquastarz.score.config.Bootstrap;
import org.aquastarz.score.controller.ScoreController;
import org.aquastarz.score.domain.Season;
import org.aquastarz.score.gui.MaintenanceDialog;
import org.aquastarz.score.gui.MeetSelectionDialog;

public class ScoreApp {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ScoreApp.class.getName());
	private static final String productionFilename = System.getProperty("user.home") + "/SynchroScore";
	private static final String productionUrl = "jdbc:hsqldb:file:" + productionFilename + "/Synchro;write_delay=false";
	private static final String testUrl = "jdbc:hsqldb:mem:Synchro";
	private static String dbUrl = null;
	private static Map<String, String> props = null;
	private static EntityManager entityManager = null;

	public static EntityManager getEntityManager() {
		if (entityManager == null) {
			if (dbUrl == null) { // test mode
				dbUrl = testUrl;
				Map<String, String> initProps = new TreeMap<String, String>();
				initProps.put("hibernate.hbm2ddl.auto", "create-drop");
				initProps.put("hibernate.connection.url", dbUrl);
				entityManager = javax.persistence.Persistence.createEntityManagerFactory("synchroPU", initProps)
						.createEntityManager();
			} else {
				if (props == null) {
					props = new TreeMap<String, String>();
					props.put("hibernate.hbm2ddl.auto", "update");
					props.put("hibernate.connection.url", dbUrl);
				}
				entityManager = getNewEntityManager();
			}
		}
		return entityManager;
	}

	public static EntityManager getNewEntityManager() {
		return javax.persistence.Persistence.createEntityManagerFactory("synchroPU", props).createEntityManager();
	}

	private static Season curSeason = null;

	public static Season getCurrentSeason() {
		if (curSeason == null) {
			findCurrentSeason();
		}
		return curSeason;
	}

	public static void setCurrentSeason(Season season) {
		curSeason = season;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		logger.info("SynchroScore version " + getVersion() + " Starting...");
		Thread.setDefaultUncaughtExceptionHandler(new LoggingExceptionHandler());

		// Configure fonts
		System.setProperty("awt.useSystemAAFontSettings", "on");
		System.setProperty("swing.aatext", "true");
		
		dbUrl = productionUrl;
		initDB();
		getEntityManager();

		// Set up hooks to shutdown and signals.
		Runtime.getRuntime().addShutdownHook(new AppSignalHandler());
		AppSignalHandler.installAll();
				
		findCurrentSeason();

		int ret=-1;
		MeetSelectionDialog dialog = new MeetSelectionDialog(
				new javax.swing.JFrame(), true);
		do {
			ret = dialog.showMeetSelectionDialog();
			if (ret == MeetSelectionDialog.MSD_CANCEL) {
				System.exit(0);
			} 
			if(ret==MeetSelectionDialog.MSD_MAINTENANCE) {
				logger.info("Start Maintenance");
				new MaintenanceDialog(null,true).showDialog();
			}
		} while (ret == MeetSelectionDialog.MSD_MAINTENANCE);
		logger.info("Start ScoreController");
		new ScoreController(dialog.getSelectedMeet());
	}

	public static void removeDB() {
		File db = new File(productionFilename);

		if (db.exists()) {
			File db2 = new File(productionFilename + "-" + System.currentTimeMillis());
			logger.info("Removing existing db.");
			boolean res = db.renameTo(db2);
			if (res) {
				logger.info("Rename db success.");
			} else {
				logger.info("Rename db FAILED.");
			}
		}
	}

	public static void initDB() {
		File db = new File(productionFilename);

		if (!db.exists()) {
			logger.info("No db file found, create...");
			db.mkdir();
			Map<String, String> initProps = new TreeMap<String, String>();
			initProps.put("hibernate.hbm2ddl.auto", "create");
			initProps.put("hibernate.connection.url", dbUrl);
			EntityManager entityManager = javax.persistence.Persistence
					.createEntityManagerFactory("synchroPU", initProps).createEntityManager();
			entityManager.close();
		}
	}

	public static void shutdownDb() {
		logger.info("Shutting down db.");
		curSeason=null;
		EntityManager em = ScoreApp.getNewEntityManager();
		em.getTransaction().begin();
		em.createNativeQuery("SHUTDOWN").executeUpdate();
		em.getTransaction().commit();
		em.clear();
		em.close();
		entityManager=null;
		logger.info("DB shutdown complete.");
	}

	public static void findCurrentSeason() {
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);

		while (year < 2019) {
			String ys = JOptionPane.showInputDialog(null,
					"The year " + year + " is not valid. Enter the current year.");
			year = Integer.parseInt(ys);

		}

		EntityManager entityManager = getEntityManager();
		Query query = entityManager.createNamedQuery("Season.findByName");
		query.setParameter("name", Integer.toString(year));
		try {
			curSeason = (Season) query.getSingleResult();
			logger.debug("Found Season = " + curSeason.getSeasonId());
		} catch (Exception e) {
		}
		if (curSeason == null) {
			curSeason = new Season(Integer.toString(year));
			entityManager.getTransaction().begin();
			entityManager.persist(curSeason);
			entityManager.getTransaction().commit();
			logger.debug("New Season = " + curSeason.getSeasonId());

			JOptionPane.showMessageDialog(null, "Data for current season not found. Please use Maintenance mode to load file.");
		}
	}

	public static String getVersion() {
		return "1.31";
	}
}
