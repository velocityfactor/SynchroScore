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
package org.aquastarz.score.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.controller.ScoreController;
import org.aquastarz.score.domain.Figure;
import org.aquastarz.score.domain.FigureScore;
import org.aquastarz.score.domain.FiguresParticipant;
import org.aquastarz.score.domain.Level;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Routine;
import org.aquastarz.score.domain.RoutineLevel;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.domain.Team;

import au.com.bytecode.opencsv.CSVReader;

public class MeetManager {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SwimmerManager.class.getName());

	public static void exportMeetExcel(Meet meet, File file) throws IOException {
		Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

		/*
		 * CreationHelper helps us create instances of various things like DataFormat,
		 * Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way
		 */
		CreationHelper createHelper = workbook.getCreationHelper();

		// Create a Meet sheet
		Sheet sheet = workbook.createSheet("Meet");

		// Create a Font for styling header cells
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);

		// Create a CellStyle with the font
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		Row row = sheet.createRow(2);
		Cell cell = row.createCell(0);
		cell.setCellValue("Season:");
		cell = row.createCell(1);
		cell.setCellValue(meet.getSeason().getName());
		cell = row.createCell(2);
		cell.setCellValue("Current year, 4 digits");

		row = sheet.createRow(3);
		cell = row.createCell(0);
		cell.setCellValue("Date:");
		cell = row.createCell(1);
		cell.setCellValue(meet.getMeetDate());
		cell = row.createCell(2);
		cell.setCellValue("Date(s) of meet (\"June 20, 2017\")");

		row = sheet.createRow(4);
		cell = row.createCell(0);
		cell.setCellValue("Name:");
		cell = row.createCell(1);
		cell.setCellValue(meet.getName());
		cell = row.createCell(2);
		cell.setCellValue("Name of the meet (\"Championship\" or \"Davis @ Sunrise\")");

		row = sheet.createRow(5);
		cell = row.createCell(0);
		cell.setCellValue("HomeTeam:");
		cell = row.createCell(1);
		cell.setCellValue(meet.getHomeTeam().getTeamId());
		cell = row.createCell(2);
		cell.setCellValue("Enter a code from the Teams sheet");

		row = sheet.createRow(6);
		cell = row.createCell(0);
		cell.setCellValue("Visitor(s):");
		cell = row.createCell(1);
		StringBuffer sb = new StringBuffer();
		for (Team opp : meet.getOpponents()) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(opp.getTeamId());
		}
		cell.setCellValue(sb.toString());
		cell = row.createCell(2);
		cell.setCellValue("Enter one or more codes from the Teams sheet separated by a comma");

		row = sheet.createRow(7);
		cell = row.createCell(0);
		cell.setCellValue("Type:");
		cell = row.createCell(1);
		cell.setCellValue(Character.toString(meet.getType()));
		cell = row.createCell(2);
		cell.setCellValue("Enter R for regular/dual or C for Champs");

		row = sheet.createRow(8);
		cell = row.createCell(0);
		cell.setCellValue("Novice Figure 1:");
		cell = row.createCell(1);
		cell.setCellValue(meet.getNov1Figure().getFigureId());
		cell = row.createCell(2);
		cell.setCellValue("Enter a figure number from the Figures sheet");

		row = sheet.createRow(9);
		cell = row.createCell(0);
		cell.setCellValue("Novice Figure 2:");
		cell = row.createCell(1);
		cell.setCellValue(meet.getNov2Figure().getFigureId());
		cell = row.createCell(2);
		cell.setCellValue("Enter a figure number from the Figures sheet");

		row = sheet.createRow(10);
		cell = row.createCell(0);
		cell.setCellValue("Novice Figure 3:");
		cell = row.createCell(1);
		cell.setCellValue(meet.getNov3Figure().getFigureId());
		cell = row.createCell(2);
		cell.setCellValue("Enter a figure number from the Figures sheet");

		row = sheet.createRow(11);
		cell = row.createCell(0);
		cell.setCellValue("Novice Figure 4:");
		cell = row.createCell(1);
		cell.setCellValue(meet.getNov4Figure().getFigureId());
		cell = row.createCell(2);
		cell.setCellValue("Enter a figure number from the Figures sheet");

		row = sheet.createRow(12);
		cell = row.createCell(0);
		cell.setCellValue("Int Figure 1:");
		cell = row.createCell(1);
		cell.setCellValue(meet.getInt1Figure().getFigureId());
		cell = row.createCell(2);
		cell.setCellValue("Enter a figure number from the Figures sheet");

		row = sheet.createRow(13);
		cell = row.createCell(0);
		cell.setCellValue("Int Figure 2:");
		cell = row.createCell(1);
		cell.setCellValue(meet.getInt2Figure().getFigureId());
		cell = row.createCell(2);
		cell.setCellValue("Enter a figure number from the Figures sheet");

		row = sheet.createRow(14);
		cell = row.createCell(0);
		cell.setCellValue("Int Figure 3:");
		cell = row.createCell(1);
		cell.setCellValue(meet.getInt3Figure().getFigureId());
		cell = row.createCell(2);
		cell.setCellValue("Enter a figure number from the Figures sheet");

		row = sheet.createRow(15);
		cell = row.createCell(0);
		cell.setCellValue("Int Figure 4:");
		cell = row.createCell(1);
		cell.setCellValue(meet.getInt4Figure().getFigureId());
		cell = row.createCell(2);
		cell.setCellValue("Enter a figure number from the Figures sheet");

		row = sheet.createRow(17);
		cell = row.createCell(2);
		cell.setCellValue(
				"Instructions pre-Meet: Only Season above needs to filled in. The remaining fields can be blank and completed in the scoring program.\n"
						+ "\n"
						+ "Be sure that the Swimmers list is correct. You may indicate which swimmers are present in this file or in the program.\n"
						+ "\n" + "Routines may be entered in the Routines sheet or filled in using the program.");

		// Resize all columns to fit the content size
		for (int i = 0; i < 3; i++) {
			sheet.autoSizeColumn(i);
		}
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue("VFCAL Synchro Meet Template");
		cell.setCellStyle(headerCellStyle);


		// Create a Swimmers sheet
		sheet = workbook.createSheet("Swimmers");

		row = sheet.createRow(0);
		int c = 0;
		for (String header : new String[] { "Order #", "League #", "First", "Last", "Figures Level", "Team Code",
				"*Hover mouse to see comments on column headers.", "Total Score", "Place", "Points", "Score A1",
				"Score A2", "Score A3", "Score A4", "Score A5", "Penalty A", "Total A", "Score B1", "Score B2",
				"Score B3", "Score B4", "Score B5", "Penalty B", "Total B", "Score C1", "Score C2", "Score C3",
				"Score C4", "Score C5", "Penalty C", "Total C", "Score D1", "Score D2", "Score D3", "Score D4",
				"Score D5", "Penalty D", "Total D" }) {
			cell = row.createCell(c++);
			cell.setCellValue(header);
			cell.setCellStyle(headerCellStyle);
		}

		HashMap<Swimmer, FiguresParticipant> fpmap = new HashMap<Swimmer, FiguresParticipant>();
		for (FiguresParticipant fp : meet.getFiguresParticipants()) {
			fpmap.put(fp.getSwimmer(), fp);
		}

		int r=1;
		for (Swimmer swimmer : SwimmerManager.getSwimmersOrderByLeagueNum(meet.getSeason())) {
			row = sheet.createRow(r++);
			cell = row.createCell(1);
			cell.setCellValue(swimmer.getLeagueNum());
			cell = row.createCell(2);
			cell.setCellValue(swimmer.getFirstName());
			cell = row.createCell(3);
			cell.setCellValue(swimmer.getLastName());
			cell = row.createCell(4);
			cell.setCellValue(swimmer.getLevel().getLevelId());
			cell = row.createCell(5);
			cell.setCellValue(swimmer.getTeam().getTeamId());

			FiguresParticipant fp = fpmap.get(swimmer);
			if (fp != null) {
				cell = row.createCell(0);
				String fo = fp.getFigureOrder();
				if (fo == null || fo.trim().isEmpty()) {
					fo = "X";
				}
				cell.setCellValue(fo);

				cell = row.createCell(7);
				cell.setCellValue(fp.getTotalScore().setScale(4).toPlainString());
				cell = row.createCell(8);
				cell.setCellValue(fp.getPlace());
				cell = row.createCell(9);
				cell.setCellValue(fp.getPoints().setScale(4).toPlainString());

				c = 10;
				for (FigureScore fs : fp.getFiguresScores()) {
					List<Figure> figures = meet.getFigureList(swimmer);
					int station = fs.getStation();
					cell = row.createCell((station - 1) * 7 + c);
					cell.setCellValue(fs.getScore1().setScale(4).toPlainString());
					cell = row.createCell((station - 1) * 7 + c + 1);
					cell.setCellValue(fs.getScore2().setScale(4).toPlainString());
					cell = row.createCell((station - 1) * 7 + c + 2);
					cell.setCellValue(fs.getScore3().setScale(4).toPlainString());
					cell = row.createCell((station - 1) * 7 + c + 3);
					cell.setCellValue(fs.getScore4().setScale(4).toPlainString());
					cell = row.createCell((station - 1) * 7 + c + 4);
					cell.setCellValue(fs.getScore5().setScale(4).toPlainString());
					cell = row.createCell((station - 1) * 7 + c + 5);
					cell.setCellValue(fs.getPenalty().setScale(4).toPlainString());
					cell = row.createCell((station - 1) * 7 + c + 6);
					cell.setCellValue(fs.getTotalScore().setScale(4).toPlainString());
				}
			}
		}
		// Resize all columns to fit the content size
		for (int i = 0; i < 38; i++) { 
			sheet.autoSizeColumn(i);
		}


		// Create a Routines sheet
		sheet = workbook.createSheet("Routines");
		row = sheet.createRow(0);
		c = 0;
		for (String header : new String[] { "Name", "Type", "TeamCode", "LevelCode", "#Swimmers", "Swimmers1",
				"Swimmers2", "Order#", "Earns Points", "TScore1", "TScore2", "TScore3", "TScore4", "TScore5", "TScore6",
				"TScore7", "AScore1", "AScore2", "AScore3", "AScore4", "AScore5", "AScore6", "AScore7", "Penalty",
				"Total", "Place", "Points", "TechScore", "ArtScore" }) {
			cell = row.createCell(c++);
			cell.setCellValue(header);
			cell.setCellStyle(headerCellStyle);
		}
		r = 1;
		for (Routine routine : meet.getRoutines()) {
			row = sheet.createRow(r++);
			c=0;
			cell = row.createCell(c++);
			cell.setCellValue(routine.getName());
			cell = row.createCell(c++);
			cell.setCellValue(routine.getRoutineType());
			cell = row.createCell(c++);
			cell.setCellValue(routine.getTeam().getTeamId());
			cell = row.createCell(c++);
			cell.setCellValue(routine.getLevel().getLevelId());
			cell = row.createCell(c++);
			cell.setCellValue(routine.getNumSwimmers());
			cell = row.createCell(c++);
			cell.setCellValue(routine.getSwimmers1());
			cell = row.createCell(c++);
			cell.setCellValue(routine.getSwimmers2());
			cell = row.createCell(c++);
			cell.setCellValue(routine.getRoutineOrder());
			cell = row.createCell(c++);
			cell.setCellValue(routine.getEarnsPoints());
			cell = row.createCell(c++);
			if (routine.getTScore1() != null)
				cell.setCellValue(routine.getTScore1().toString());
			cell = row.createCell(c++);
			if (routine.getTScore2() != null)
				cell.setCellValue(routine.getTScore2().toString());
			cell = row.createCell(c++);
			if (routine.getTScore3() != null)
				cell.setCellValue(routine.getTScore3().toString());
			cell = row.createCell(c++);
			if (routine.getTScore4() != null)
				cell.setCellValue(routine.getTScore4().toString());
			cell = row.createCell(c++);
			if (routine.getTScore5() != null)
				cell.setCellValue(routine.getTScore5().toString());
			cell = row.createCell(c++);
			if (routine.getTScore6() != null)
				cell.setCellValue(routine.getTScore6().toString());
			cell = row.createCell(c++);
			if (routine.getTScore7() != null)
				cell.setCellValue(routine.getTScore7().toString());
			cell = row.createCell(c++);
			if (routine.getAScore1() != null)
				cell.setCellValue(routine.getAScore1().toString());
			cell = row.createCell(c++);
			if (routine.getAScore2() != null)
				cell.setCellValue(routine.getAScore2().toString());
			cell = row.createCell(c++);
			if (routine.getAScore3() != null)
				cell.setCellValue(routine.getAScore3().toString());
			cell = row.createCell(c++);
			if (routine.getAScore4() != null)
				cell.setCellValue(routine.getAScore4().toString());
			cell = row.createCell(c++);
			if (routine.getAScore5() != null)
				cell.setCellValue(routine.getAScore5().toString());
			cell = row.createCell(c++);
			if (routine.getAScore6() != null)
				cell.setCellValue(routine.getAScore6().toString());
			cell = row.createCell(c++);
			if (routine.getAScore7() != null)
				cell.setCellValue(routine.getAScore7().toString());
			cell = row.createCell(c++);
			if (routine.getPenalty() != null)
				cell.setCellValue(routine.getPenalty().toString());
			cell = row.createCell(c++);
			if (routine.getTotalScore() != null)
				cell.setCellValue(routine.getTotalScore().toString());
			cell = row.createCell(c++);
			if (routine.getPlace() != null)
				cell.setCellValue(routine.getPlace());
			cell = row.createCell(c++);
			if (routine.getPoints() != null)
				cell.setCellValue(routine.getPoints().toString());
			cell = row.createCell(c++);
			if (routine.getArtScore() != null)
				cell.setCellValue(routine.getArtScore().toString());
			cell = row.createCell(c++);
			if (routine.getTechScore() != null)
				cell.setCellValue(routine.getTechScore().toString());
		}
		// Resize all columns to fit the content size
		for (int i = 0; i < c; i++) { 
			sheet.autoSizeColumn(i);
		}


		// Create a Teams sheet
		sheet = workbook.createSheet("Teams");
		row = sheet.createRow(0);
		c = 0;
		for (String header : new String[] { "Code", "Team Name" }) {
			cell = row.createCell(c++);
			cell.setCellValue(header);
			cell.setCellStyle(headerCellStyle);
		}
		r = 1;
		for (Team team : TeamManager.findAllTeams()) {
			row = sheet.createRow(r++);
			cell = row.createCell(0);
			cell.setCellValue(team.getTeamId());
			cell = row.createCell(1);
			cell.setCellValue(team.getName());
		}
		// Resize all columns to fit the content size
		for (int i = 0; i < 2; i++) { 
			sheet.autoSizeColumn(i);
		}

		// Create a Figures sheet
		sheet = workbook.createSheet("Figures");
		row = sheet.createRow(0);
		c = 0;
		for (String header : new String[] { "Number", "Difficulty", "Name" }) {
			cell = row.createCell(c++);
			cell.setCellValue(header);
			cell.setCellStyle(headerCellStyle);
		}
		r = 1;
		for (Figure figure:FigureManager.getFigures()) {
			row = sheet.createRow(r++);
			cell = row.createCell(0);
			cell.setCellValue(figure.getFigureId());
			cell = row.createCell(1);
			if(figure.getDegreeOfDifficulty()!=null) 
				cell.setCellValue(figure.getDegreeOfDifficulty().toString());
			cell = row.createCell(2);
			cell.setCellValue(figure.getName());
		}
		// Resize all columns to fit the content size
		for (int i = 0; i < 3; i++) { 
			sheet.autoSizeColumn(i);
		}

		// Create a Figures Levels sheet
		sheet = workbook.createSheet("Figures Levels");
		row = sheet.createRow(0);
		c = 0;
		for (String header : new String[] { "Code", "Level" }) {
			cell = row.createCell(c++);
			cell.setCellValue(header);
			cell.setCellStyle(headerCellStyle);
		}
		r = 1;
		for (Level level : LevelManager.findAllOrderBySortOrder()) {
			row = sheet.createRow(r++);
			cell = row.createCell(0);
			cell.setCellValue(level.getLevelId());
			cell = row.createCell(1);
			cell.setCellValue(level.getName());
		}
		// Resize all columns to fit the content size
		for (int i = 0; i < 2; i++) { 
			sheet.autoSizeColumn(i);
		}
		
		// Create a Routine Levels sheet
		sheet = workbook.createSheet("Routines Levels");
		row = sheet.createRow(0);
		c = 0;
		for (String header : new String[] { "Code", "Level" }) {
			cell = row.createCell(c++);
			cell.setCellValue(header);
			cell.setCellStyle(headerCellStyle);
		}
		r = 1;
		for (RoutineLevel routineLevel:RoutineLevelManager.findAll()) {
			row = sheet.createRow(r++);
			cell = row.createCell(0);
			cell.setCellValue(routineLevel.getLevelId());
			cell = row.createCell(1);
			cell.setCellValue(routineLevel.getName());
		}
		// Resize all columns to fit the content size
		for (int i = 0; i < 2; i++) { 
			sheet.autoSizeColumn(i);
		}

		// Write the output to a file
		FileOutputStream fileOut = new FileOutputStream(file);
		workbook.write(fileOut);
		fileOut.close();

		// Closing the workbook
		workbook.close();
	}

	public static void exportMeet(Meet meet, File file) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write("Meet,\"" + meet.getName() + "\",");
		writer.write("\"" + meet.getMeetDate() + "\",");
		writer.write("\"" + meet.getType() + "\",");
		writer.write("\"" + meet.getSeason().getName() + "\",");
		writer.write("\"" + meet.getFiguresOrderGenerated() + "\",");
		writer.write("\"" + meet.isRoutinesOrderGenerated() + "\",");
		writer.write("\"" + meet.getHomeTeam().getTeamId() + "\"");
		for (Team team : meet.getOpponents()) {
			writer.write(",\"" + team.getTeamId() + "\"");
		}
		writer.write("\n");
		writer.write("NovFigure1,\"" + meet.getNov1Figure().getFigureId() + "\",\"" + meet.getNov1Figure().getName()
				+ "\"\n");
		writer.write("NovFigure2,\"" + meet.getNov2Figure().getFigureId() + "\",\"" + meet.getNov2Figure().getName()
				+ "\"\n");
		writer.write("NovFigure3,\"" + meet.getNov3Figure().getFigureId() + "\",\"" + meet.getNov3Figure().getName()
				+ "\"\n");
		writer.write("NovFigure4,\"" + meet.getNov4Figure().getFigureId() + "\",\"" + meet.getNov4Figure().getName()
				+ "\"\n");
		writer.write("IntFigure1,\"" + meet.getInt1Figure().getFigureId() + "\",\"" + meet.getInt1Figure().getName()
				+ "\"\n");
		writer.write("IntFigure2,\"" + meet.getInt2Figure().getFigureId() + "\",\"" + meet.getInt2Figure().getName()
				+ "\"\n");
		writer.write("IntFigure3,\"" + meet.getInt3Figure().getFigureId() + "\",\"" + meet.getInt3Figure().getName()
				+ "\"\n");
		writer.write("IntFigure4,\"" + meet.getInt4Figure().getFigureId() + "\",\"" + meet.getInt4Figure().getName()
				+ "\"\n");
		writer.write("EUFigs,\"" + meet.isEu1() + "\",\"" + meet.isEu2() + "\",\"" + meet.isEu3() + "\",\""
				+ meet.isEu4() + "\"\n");
		for (FiguresParticipant fp : meet.getFiguresParticipants()) {
			writer.write(getFiguresParticipantExport(fp));
		}
		for (Routine r : meet.getRoutines()) {
			writer.write("Routine," + r.getRoutineOrder() + "," + r.getTeam().getTeamId() + ","
					+ r.getLevel().getLevelId() + "," + r.getRoutineType() + "," + r.getNumSwimmers() + ",\""
					+ r.getName() + "\",\"" + r.getSwimmers1() + "\",\"" + r.getSwimmers2() + "\"," + r.getAScore1()
					+ "," + r.getAScore2() + "," + r.getAScore3() + "," + r.getAScore4() + "," + r.getAScore5() + ","
					+ r.getAScore6() + "," + r.getAScore7() + "," + r.getTScore1() + "," + r.getTScore2() + ","
					+ r.getTScore3() + "," + r.getTScore4() + "," + r.getTScore5() + "," + r.getTScore6() + ","
					+ r.getTScore7() + "," + r.getPenalty() + "," + r.getEarnsPoints() + "\n");
		}
		writer.close();
	}

	public static String getFiguresParticipantExport(FiguresParticipant fp) {
		StringBuilder sb = new StringBuilder();
		sb.append("FiguresParticipant,\"").append(fp.getFigureOrder()).append("\",")
				.append(fp.getSwimmer().getLeagueNum()).append(",\"").append(fp.getSwimmer().getFirstName())
				.append("\",\"").append(fp.getSwimmer().getLastName()).append("\",\"")
				.append(fp.getSwimmer().getLevel().getLevelId()).append("\",\"")
				.append(fp.getSwimmer().getTeam().getTeamId()).append("\"\n");
		for (FigureScore fs : fp.getFiguresScores()) {
			sb.append("FigureScore,\"").append(fp.getFigureOrder()).append("\",").append(fs.getStation()).append(",")
					.append(fs.getScore1()).append(",").append(fs.getScore2()).append(",").append(fs.getScore3())
					.append(",").append(fs.getScore4()).append(",").append(fs.getScore5()).append(",")
					.append(fs.getPenalty()).append("\n");
		}
		return sb.toString();
	}

	public static Meet readMeet(File file) {
		Meet meet = null;
		try {
			CSVReader csv = new CSVReader(new FileReader(file));
			List<String[]> rows = csv.readAll();
			csv.close();
			HashMap<String, FiguresParticipant> map = new HashMap<String, FiguresParticipant>();
			for (String[] sa : rows) {
				if ("Meet".equals(sa[0])) {
					meet = new Meet();
					meet.setName(sa[1]);
					meet.setMeetDate(sa[2]);
					meet.setType(sa[3].charAt(0));
					meet.setSeason(SeasonManager.findOrCreate(sa[4]));
					meet.setFiguresOrderGenerated(Boolean.parseBoolean(sa[5]));
					meet.setRoutinesOrderGenerated(Boolean.parseBoolean(sa[6]));
					meet.setHomeTeam(TeamManager.findById(sa[7]));
					ArrayList<Team> opponents = new ArrayList<Team>();
					for (int i = 8; i < sa.length; i++) {
						opponents.add(TeamManager.findById(sa[i]));
					}
					meet.setOpponents(opponents);
				}
				if ("NovFigure1".equals(sa[0])) {
					meet.setNov1Figure(FigureManager.findById(sa[1]));
				}
				if ("NovFigure2".equals(sa[0])) {
					meet.setNov2Figure(FigureManager.findById(sa[1]));
				}
				if ("NovFigure3".equals(sa[0])) {
					meet.setNov3Figure(FigureManager.findById(sa[1]));
				}
				if ("NovFigure4".equals(sa[0])) {
					meet.setNov4Figure(FigureManager.findById(sa[1]));
				}
				if ("IntFigure1".equals(sa[0])) {
					meet.setInt1Figure(FigureManager.findById(sa[1]));
				}
				if ("IntFigure2".equals(sa[0])) {
					meet.setInt2Figure(FigureManager.findById(sa[1]));
				}
				if ("IntFigure3".equals(sa[0])) {
					meet.setInt3Figure(FigureManager.findById(sa[1]));
				}
				if ("IntFigure4".equals(sa[0])) {
					meet.setInt4Figure(FigureManager.findById(sa[1]));
				}
				if ("EUFigs".equals(sa[0])) {
					meet.setEu1(Boolean.parseBoolean(sa[1]));
					meet.setEu2(Boolean.parseBoolean(sa[2]));
					meet.setEu3(Boolean.parseBoolean(sa[3]));
					meet.setEu4(Boolean.parseBoolean(sa[4]));
				}
				if ("FiguresParticipant".equals(sa[0])) {
					FiguresParticipant fp = new FiguresParticipant();
					fp.setMeet(meet);
					fp.setFigureOrder(sa[1]);
					fp.setSwimmer(SwimmerManager.findByLeagueNum(Integer.parseInt(sa[2]), meet.getSeason()));
					meet.getFiguresParticipants().add(fp);
					map.put(fp.getFigureOrder(), fp);
				}
				if ("FigureScore".equals(sa[0])) {
					FigureScore fs = new FigureScore();
					FiguresParticipant fp = map.get(sa[1]);
					fs.setFiguresParticipant(fp);
					fs.setStation(Integer.parseInt(sa[2]));
					fs.setScore1("null".equals(sa[3]) ? null : new BigDecimal(sa[3]));
					fs.setScore2("null".equals(sa[4]) ? null : new BigDecimal(sa[4]));
					fs.setScore3("null".equals(sa[5]) ? null : new BigDecimal(sa[5]));
					fs.setScore4("null".equals(sa[6]) ? null : new BigDecimal(sa[6]));
					fs.setScore5("null".equals(sa[7]) ? null : new BigDecimal(sa[7]));
					fs.setPenalty("null".equals(sa[8]) ? null : new BigDecimal(sa[8]));
					fs.setTotalScore(ScoreController.totalScore(fs));
					fp.getFiguresScores().add(fs);
				}
				if ("Routine".equals(sa[0])) {
					Routine r = new Routine();
					r.setMeet(meet);
					r.setRoutineOrder(Integer.parseInt(sa[1]));
					r.setTeam(TeamManager.findById(sa[2]));
					r.setLevel(RoutineLevelManager.find(sa[3]));
					r.setRoutineType(sa[4]);
					r.setNumSwimmers(Integer.parseInt(sa[5]));
					r.setName(sa[6]);
					r.setSwimmers1(sa[7]);
					r.setSwimmers2(sa[8]);
					r.setAScore1("null".equals(sa[9]) ? null : new BigDecimal(sa[9]));
					r.setAScore2("null".equals(sa[10]) ? null : new BigDecimal(sa[10]));
					r.setAScore3("null".equals(sa[11]) ? null : new BigDecimal(sa[11]));
					r.setAScore4("null".equals(sa[12]) ? null : new BigDecimal(sa[12]));
					r.setAScore5("null".equals(sa[13]) ? null : new BigDecimal(sa[13]));
					r.setAScore6("null".equals(sa[14]) ? null : new BigDecimal(sa[14]));
					r.setAScore7("null".equals(sa[15]) ? null : new BigDecimal(sa[15]));
					r.setTScore1("null".equals(sa[16]) ? null : new BigDecimal(sa[16]));
					r.setTScore2("null".equals(sa[17]) ? null : new BigDecimal(sa[17]));
					r.setTScore3("null".equals(sa[18]) ? null : new BigDecimal(sa[18]));
					r.setTScore4("null".equals(sa[19]) ? null : new BigDecimal(sa[19]));
					r.setTScore5("null".equals(sa[20]) ? null : new BigDecimal(sa[20]));
					r.setTScore6("null".equals(sa[21]) ? null : new BigDecimal(sa[21]));
					r.setTScore7("null".equals(sa[22]) ? null : new BigDecimal(sa[22]));
					r.setPenalty("null".equals(sa[23]) ? null : new BigDecimal(sa[23]));
					r.setEarnsPoints((sa.length < 25 || "null".equals(sa[24])) ? true : Boolean.getBoolean(sa[24]));
					meet.getRoutines().add(r);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			meet = null;
		}
		return meet;
	}

	public static List<String> compareMeets(Meet meet1, Meet meet2) {
		ArrayList<String> errors = new ArrayList<String>();

		// Compare figures
		HashMap<String, FigureScore> fsMap = new HashMap<String, FigureScore>();
		for (FiguresParticipant fp1 : meet1.getFiguresParticipants()) {
			for (FigureScore fs1 : fp1.getFiguresScores()) {
				fsMap.put(fp1.getFigureOrder() + "-" + fs1.getStation(), fs1);
			}
		}
		for (FiguresParticipant fp2 : meet2.getFiguresParticipants()) {
			for (FigureScore fs2 : fp2.getFiguresScores()) {
				FigureScore fs1 = fsMap.get(fp2.getFigureOrder() + "-" + fs2.getStation());
				if (fs1 == null) {
					errors.add("Missing figure score " + fs2.getStation() + " for swimmer #" + fp2.getFigureOrder());
				} else {
					if (!fs1.equals(fs2)) {
						errors.add("Mismatched figure score " + fs2.getStation() + " for swimmer #"
								+ fp2.getFigureOrder());
					}
					fsMap.remove(fp2.getFigureOrder() + "-" + fs2.getStation());
				}
			}
		}
		for (FigureScore fs1 : fsMap.values()) {
			errors.add("Extra figure score " + fs1.getStation() + " for swimmer #"
					+ fs1.getFiguresParticipant().getFigureOrder());
		}

		// Compare routines
		HashMap<String, Routine> routineMap = new HashMap<String, Routine>();
		for (Routine routine1 : meet1.getRoutines()) {
			routineMap.put(routine1.getName(), routine1);
		}
		for (Routine routine2 : meet2.getRoutines()) {
			Routine routine1 = routineMap.get(routine2.getName());
			if (routine1 == null) {
				errors.add("Missing routine \"" + routine2.getName() + "\"");
			} else {
				if (!routine1.equals(routine2)) {
					errors.add("Mismatched routine score for \"" + routine1.getName() + "\"");
				}
				routineMap.remove(routine2.getName());
			}
		}
		for (Routine routine1 : routineMap.values()) {
			errors.add("Extra routine score for \"" + routine1.getName() + "\"");
		}
		return errors;
	}

	public static void importMeet(File file) throws IOException {
		try {
			Meet meet = readMeet(file);
			try {
				boolean inTransaction = ScoreApp.getEntityManager().getTransaction().isActive();
				if (inTransaction) {
					ScoreApp.getEntityManager().getTransaction().commit();
				}
				ScoreApp.getEntityManager().getTransaction().begin();
				ScoreApp.getEntityManager().persist(meet);
				for (FiguresParticipant fp : meet.getFiguresParticipants()) {
					ScoreApp.getEntityManager().persist(fp);
					for (FigureScore fs : fp.getFiguresScores()) {
						ScoreApp.getEntityManager().persist(fs);
					}
				}
				for (Routine r : meet.getRoutines()) {
					ScoreApp.getEntityManager().persist(r);
				}
				ScoreApp.getEntityManager().getTransaction().commit();
				if (inTransaction)
					ScoreApp.getEntityManager().getTransaction().begin();
			} catch (Exception e) {
				ScoreApp.getEntityManager().getTransaction().rollback();
				throw new IOException("Error commmitting meet to database.  " + e.getMessage());
			}
		} catch (Exception e) {
			throw new IOException("Error reading file.  " + e.getMessage());
		}
	}
}
