package org.aquastarz.score;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aquastarz.score.config.Bootstrap;
import org.aquastarz.score.config.SeasonFileImporter;
import org.aquastarz.score.domain.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

public class SeasonFileImporterTest {
    @Rule public TemporaryFolder temporary = new TemporaryFolder();
    private EntityManagerFactory factory;
    private EntityManager em;
    private Integer oldMeetId;

    @Before
    public void seedExistingMeetWithScoresAndOldLevels() {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("hibernate.hbm2ddl.auto", "create-drop");
        properties.put("hibernate.connection.url", "jdbc:hsqldb:mem:season-import-" + System.nanoTime());
        factory = Persistence.createEntityManagerFactory("synchroPU", properties);
        em = factory.createEntityManager();
        em.getTransaction().begin();
        Season season = new Season("2025");
        Team team = new Team("OLD", "Old team");
        Team opponent = new Team("OPP", "Old opponent");
        Level level = new Level("N8", "Old novice level", 1);
        RoutineLevel routineLevel = new RoutineLevel("N12U", "Old routine level", 1);
        Figure figure = new Figure("OLD", BigDecimal.ONE, "Old figure");
        em.persist(season);
        em.persist(team);
        em.persist(opponent);
        em.persist(level);
        em.persist(routineLevel);
        em.persist(figure);
        Swimmer swimmer = new Swimmer(1, season);
        swimmer.setFirstName("Old");
        swimmer.setLastName("Swimmer");
        swimmer.setTeam(team);
        swimmer.setLevel(level);
        em.persist(swimmer);
        Meet meet = new Meet();
        meet.setName("Old meet");
        meet.setMeetDate("November 1, 2025");
        meet.setType('R');
        meet.setSeason(season);
        meet.setHomeTeam(team);
        meet.getOpponents().add(opponent);
        meet.setNov1Figure(figure);
        em.persist(meet);
        oldMeetId = meet.getMeetId();
        FiguresParticipant participant = new FiguresParticipant(meet, swimmer);
        em.persist(participant);
        FigureScore score = new FigureScore(participant, 1);
        score.setScore1(BigDecimal.ONE);
        score.setScore2(BigDecimal.ONE);
        score.setScore3(BigDecimal.ONE);
        score.setScore4(BigDecimal.ONE);
        score.setScore5(BigDecimal.ONE);
        score.setPenalty(BigDecimal.ZERO);
        score.setTotalScore(BigDecimal.ONE);
        em.persist(score);
        Routine routine = new Routine();
        routine.setMeet(meet);
        routine.setTeam(team);
        routine.setLevel(routineLevel);
        routine.setName("Old routine");
        routine.setRoutineType("Team");
        routine.setNumSwimmers(4);
        em.persist(routine);
        em.getTransaction().commit();
    }

    @After
    public void closeDatabase() {
        if (em != null) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            em.close();
        }
        if (factory != null) factory.close();
    }

    @Test
    public void successfulImportReplacesAllDataAndCanBeRepeated() throws Exception {
        try (Workbook workbook = newSeasonWorkbook()) {
            SeasonFileImporter importer = SeasonFileImporter.read(workbook);
            for (int attempt = 0; attempt < 2; attempt++) {
                Season season = importer.replaceAllData(em, "2026");
                em.clear();
                assertEquals("2026", season.getName());
                for (String entity : new String[] {"Meet", "Routine", "FiguresParticipant", "FigureScore"}) {
                    assertEquals(entity, 0, count(entity));
                }
                assertNull(em.find(RoutineLevel.class, "N12U"));
                assertNull(em.find(Level.class, "N8"));
                assertNull(em.find(Team.class, "OLD"));
                assertNull(em.find(Figure.class, "OLD"));
                assertEquals(1, count("Season"));
                assertEquals(1, count("Team"));
                assertEquals(1, count("Figure"));
                assertEquals(1, count("Swimmer"));
                assertEquals(5, count("Level"));
                assertEquals(3, count("RoutineLevel"));
                List<RoutineLevel> levels = em.createNamedQuery("RoutineLevel.findAllInOrder", RoutineLevel.class).getResultList();
                assertEquals("N10U", levels.get(0).getLevelId());
                assertEquals("N11-13", levels.get(1).getLevelId());
                assertEquals("N14O", levels.get(2).getLevelId());
                Swimmer swimmer = em.createQuery("SELECT s FROM Swimmer s", Swimmer.class).getSingleResult();
                assertEquals("N7", swimmer.getLevel().getLevelId());
                assertEquals("NEW", swimmer.getTeam().getTeamId());
                assertEquals(season, swimmer.getSeason());
            }
        }
    }

    @Test
    public void cancelledReplacementDoesNotChangeExistingData() throws Exception {
        File file;
        try (Workbook workbook = newSeasonWorkbook()) { file = save(workbook); }
        AtomicBoolean prompted = new AtomicBoolean();
        assertFalse(Bootstrap.loadLeagueData(file, () -> { prompted.set(true); return false; }));
        assertTrue(prompted.get());
        assertOldDataIntact();
    }

    @Test
    public void malformedFileIsRejectedBeforeConfirmationOrDeletion() throws Exception {
        try (Workbook workbook = newSeasonWorkbook()) {
            workbook.removeSheetAt(workbook.getSheetIndex("Swimmers"));
            AtomicBoolean prompted = new AtomicBoolean();
            try {
                Bootstrap.loadLeagueData(save(workbook), () -> { prompted.set(true); return true; });
                fail("Missing sheet should be rejected");
            } catch (IllegalArgumentException expected) {
                assertTrue(expected.getMessage().contains("Swimmers"));
            }
            assertFalse(prompted.get());
            assertOldDataIntact();
        }
    }

    @Test
    public void unknownSwimmerLevelIsRejectedWithoutTouchingExistingData() throws Exception {
        try (Workbook workbook = newSeasonWorkbook()) {
            workbook.getSheet("Swimmers").getRow(1).getCell(3).setCellValue("MISSING");
            try {
                SeasonFileImporter.read(workbook);
                fail("Unknown level should be rejected");
            } catch (IllegalArgumentException expected) {
                assertTrue(expected.getMessage().contains("MISSING"));
            }
            assertOldDataIntact();
        }
    }

    @Test
    public void databaseFailureRollsBackDeletionsAndPreservesMeetRelationships() throws Exception {
        try (Workbook workbook = newSeasonWorkbook()) {
            // Parses correctly, but exceeds the database's three-character team ID limit.
            workbook.getSheet("Teams").getRow(1).getCell(0).setCellValue("TOOLONG");
            workbook.getSheet("Swimmers").getRow(1).getCell(4).setCellValue("TOOLONG");
            SeasonFileImporter importer = SeasonFileImporter.read(workbook);
            try {
                importer.replaceAllData(em, "2026");
                fail("Database constraint should reject the replacement");
            } catch (RuntimeException expected) {
                assertFalse(em.getTransaction().isActive());
            }
            assertOldDataIntact();
            assertEquals(0, em.createQuery("SELECT COUNT(s) FROM Season s WHERE s.name = '2026'", Long.class).getSingleResult().longValue());
        }
    }

    private void assertOldDataIntact() {
        em.clear();
        assertNotNull(em.find(RoutineLevel.class, "N12U"));
        assertNull(em.find(RoutineLevel.class, "N14O"));
        for (String entity : new String[] {"Season", "Meet", "Swimmer", "Figure", "Routine", "FiguresParticipant", "FigureScore"}) {
            assertEquals(entity, 1, count(entity));
        }
        assertEquals(2, count("Team"));
        Meet meet = em.find(Meet.class, oldMeetId);
        assertEquals("Old meet", meet.getName());
        assertEquals("OPP", meet.getOpponents().get(0).getTeamId());
        assertEquals("OLD", meet.getNov1Figure().getFigureId());
    }

    private long count(String entity) {
        return em.createQuery("SELECT COUNT(e) FROM " + entity + " e", Long.class).getSingleResult();
    }

    private File save(Workbook workbook) throws Exception {
        File file = temporary.newFile("season.xlsx");
        try (FileOutputStream output = new FileOutputStream(file)) { workbook.write(output); }
        return file;
    }

    private Workbook newSeasonWorkbook() {
        Workbook workbook = new XSSFWorkbook();
        sheet(workbook, "Teams", new String[][] {{"Code", "Name"}, {"NEW", "New team"}});
        sheet(workbook, "Figures", new String[][] {{"Code", "DD", "Name"}, {"101", "1.0", "Ballet leg"}});
        sheet(workbook, "Figures Levels", new String[][] {{"Code", "Name"},
                {"N7", "Novice 7 and Under"}, {"N8-9", "Novice 8-9"}, {"N10-11", "Novice 10-11"},
                {"N12-13", "Novice 12-13"}, {"N14-18", "Novice 14-18"}});
        sheet(workbook, "Routines Levels", new String[][] {{"Code", "Name"},
                {"N10U", "Novice 10 and Under"}, {"N11-13", "Novice 11-13"}, {"N14O", "Novice 14 and Over"}});
        // Physically present blank rows, as retained by Excel formatting, must not create empty levels.
        workbook.getSheet("Routines Levels").createRow(12).createCell(0).setCellValue("");
        sheet(workbook, "Swimmers", new String[][] {{"Number", "First", "Last", "Level", "Team"},
                {"2", "New", "Swimmer", "N7", "NEW"}});
        return workbook;
    }

    private void sheet(Workbook workbook, String name, String[][] rows) {
        Sheet sheet = workbook.createSheet(name);
        for (int r = 0; r < rows.length; r++) {
            Row row = sheet.createRow(r);
            for (int c = 0; c < rows[r].length; c++) row.createCell(c).setCellValue(rows[r][c]);
        }
    }
}
