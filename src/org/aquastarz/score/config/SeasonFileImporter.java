package org.aquastarz.score.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.aquastarz.score.domain.Level;
import org.aquastarz.score.domain.Meet;
import org.aquastarz.score.domain.Season;
import org.aquastarz.score.domain.Swimmer;
import org.aquastarz.score.domain.Team;

/** Validates a season file before atomically replacing the stored season and meet data. */
public final class SeasonFileImporter {
    private final List<String[]> teams;
    private final List<String[]> figures;
    private final List<String[]> figureLevels;
    private final List<String[]> routineLevels;
    private final List<String[]> swimmers;

    private SeasonFileImporter(Workbook workbook) {
        teams = readRows(workbook, "Teams", 2);
        figures = readRows(workbook, "Figures", 3);
        figureLevels = readRows(workbook, "Figures Levels", 2);
        routineLevels = readRows(workbook, "Routines Levels", 2);
        swimmers = readRows(workbook, "Swimmers", 5);
        Set<String> teamIds = ids(teams);
        Set<String> levelIds = ids(figureLevels);
        for (String[] row : figures) {
            try {
                new BigDecimal(row[1]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid degree of difficulty for figure " + row[0] + ".", e);
            }
        }
        Set<Integer> leagueNumbers = new HashSet<Integer>();
        for (String[] row : swimmers) {
            int number;
            try {
                number = Integer.parseInt(row[0]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid swimmer league number: " + row[0] + ".", e);
            }
            if (!leagueNumbers.add(number)) {
                throw new IllegalArgumentException("Duplicate swimmer league number: " + number + ".");
            }
            if (!levelIds.contains(row[3])) {
                throw new IllegalArgumentException("Swimmer " + number + " uses an unknown figure level: " + row[3] + ".");
            }
            if (!teamIds.contains(row[4])) {
                throw new IllegalArgumentException("Swimmer " + number + " uses an unknown team: " + row[4] + ".");
            }
        }
    }

    public static SeasonFileImporter read(Workbook workbook) {
        return new SeasonFileImporter(workbook);
    }

    private static List<String[]> readRows(Workbook workbook, String name, int columns) {
        Sheet sheet = workbook.getSheet(name);
        if (sheet == null) throw new IllegalArgumentException("Missing " + name + " sheet.");
        List<String[]> rows = new ArrayList<String[]>();
        Set<String> keys = new HashSet<String>();
        DataFormatter formatter = new DataFormatter();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue;
            String[] values = new String[columns];
            boolean blank = true;
            for (int column = 0; column < columns; column++) {
                values[column] = formatter.formatCellValue(row.getCell(column)).trim();
                if (!values[column].isEmpty()) blank = false;
            }
            if (blank) continue;
            if (values[0].isEmpty()) {
                throw new IllegalArgumentException("Missing code in " + name + " row " + (row.getRowNum() + 1) + ".");
            }
            if (!keys.add(values[0])) {
                throw new IllegalArgumentException("Duplicate code " + values[0] + " in " + name + ".");
            }
            rows.add(values);
        }
        return rows;
    }

    private static Set<String> ids(List<String[]> rows) {
        Set<String> ids = new HashSet<String>();
        for (String[] row : rows) ids.add(row[0]);
        return ids;
    }

    /** Call only after the user confirms replacement. Failed imports roll back all deletions. */
    public Season replaceAllData(EntityManager entityManager, String seasonName) {
        if (seasonName == null || seasonName.trim().isEmpty()) {
            throw new IllegalArgumentException("A season name is required.");
        }
        EntityTransaction transaction = entityManager.getTransaction();
        if (transaction.isActive()) throw new IllegalStateException("An import cannot run inside another transaction.");
        transaction.begin();
        try {
            entityManager.clear();
            entityManager.createQuery("DELETE FROM FigureScore").executeUpdate();
            entityManager.createQuery("DELETE FROM FiguresParticipant").executeUpdate();
            entityManager.createQuery("DELETE FROM Routine").executeUpdate();
            // Removing meets through JPA also removes their opponent join-table rows.
            for (Meet meet : entityManager.createQuery("SELECT m FROM Meet m", Meet.class).getResultList()) {
                entityManager.remove(meet);
            }
            entityManager.flush();
            for (String entity : new String[] {"Swimmer", "Team", "Figure", "Level", "RoutineLevel", "Season"}) {
                entityManager.createQuery("DELETE FROM " + entity).executeUpdate();
            }
            // Bulk deletes bypass the persistence context; old entities must not be reused.
            entityManager.clear();
            Season season = new Season(seasonName);
            entityManager.persist(season);
            for (String[] row : teams) Bootstrap.checkAndPersistTeam(entityManager, row[0], row[1]);
            for (String[] row : figures) {
                Bootstrap.checkAndPersistFigure(entityManager, row[0], new BigDecimal(row[1]), row[2]);
            }
            int order = 1;
            for (String[] row : figureLevels) Bootstrap.checkAndPersistLevel(entityManager, row[0], row[1], order++);
            order = 1;
            for (String[] row : routineLevels) Bootstrap.checkAndPersistRoutineLevel(entityManager, row[0], row[1], order++);
            for (String[] row : swimmers) {
                Swimmer swimmer = new Swimmer(Integer.parseInt(row[0]), season);
                swimmer.setFirstName(row[1]);
                swimmer.setLastName(row[2]);
                swimmer.setLevel(entityManager.find(Level.class, row[3]));
                swimmer.setTeam(entityManager.find(Team.class, row[4]));
                entityManager.persist(swimmer);
            }
            transaction.commit();
            return season;
        } catch (RuntimeException e) {
            if (transaction.isActive()) transaction.rollback();
            entityManager.clear();
            throw e;
        }
    }
}
