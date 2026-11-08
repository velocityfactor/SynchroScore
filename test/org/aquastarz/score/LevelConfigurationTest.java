package org.aquastarz.score;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import org.aquastarz.score.config.Bootstrap;
import org.aquastarz.score.controller.ScoreController;
import org.aquastarz.score.domain.*;
import org.aquastarz.score.manager.LevelManager;
import org.aquastarz.score.manager.RoutineLevelManager;
import org.aquastarz.score.report.Label;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LevelConfigurationTest {
    private EntityManager em;

    @Before
    public void beginTransaction() {
        // ScoreApp defaults to an in-memory database unless main() starts production mode.
        em = ScoreApp.getEntityManager();
        em.getTransaction().begin();
    }

    @After
    public void rollback() {
        if (em != null) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            em.clear();
        }
    }

    @Test
    public void configurationUpdatesNamesAndOrdersForExistingLevels() {
        Bootstrap.checkAndPersistLevel(em, "N7", "Old name", 8);
        Bootstrap.checkAndPersistLevel(em, "N8-9", "Novice 8-9", 2);
        Bootstrap.checkAndPersistLevel(em, "N7", "Novice 7 and Under", 1);
        Bootstrap.checkAndPersistRoutineLevel(em, "N14O", "Old name", 8);
        Bootstrap.checkAndPersistRoutineLevel(em, "N11-13", "Novice 11-13", 2);
        Bootstrap.checkAndPersistRoutineLevel(em, "N10U", "Novice 10 and Under", 1);
        Bootstrap.checkAndPersistRoutineLevel(em, "N14O", "Novice 14 and Over", 3);
        // An arbitrary future level must also be available without a code change.
        Bootstrap.checkAndPersistRoutineLevel(em, "NNEW", "Future novice level", 4);
        em.flush();
        em.clear();
        List<Level> figures = LevelManager.findAllOrderBySortOrder();
        assertEquals("N7", figures.get(0).getLevelId());
        assertEquals("Novice 7 and Under", figures.get(0).getName());
        assertEquals("N7", ScoreController.getLevels().get(0).getLevelId());
        List<RoutineLevel> routines = RoutineLevelManager.findAll();
        assertEquals(4, routines.size());
        assertEquals("N10U", routines.get(0).getLevelId());
        assertEquals("N11-13", routines.get(1).getLevelId());
        assertEquals("N14O", routines.get(2).getLevelId());
        assertEquals("Novice 14 and Over", routines.get(2).getName());
        assertEquals("NNEW", routines.get(3).getLevelId());
    }

    @Test
    public void newRoutineLevelsUseConfiguredNamesForEveryCategoryAndLabelMode() {
        Meet meet = persistMeet();
        Team team = new Team("TST", "Test team");
        em.persist(team);
        String[] ids = {"N10U", "N11-13", "N14O", "I11", "I15"};
        String[] categories = {"Solo", "Duet", "Trio", "Team"};
        int order = 1;
        for (String id : ids) {
            RoutineLevel level = new RoutineLevel(id, "Configured " + id, order++);
            em.persist(level);
            for (String category : categories) persistRoutine(meet, team, level, category);
        }
        em.flush();
        for (boolean withNames : new boolean[] {false, true}) {
            List<Label> labels = ScoreController.generateRoutineLabels(
                    meet, true, true, 0, withNames, 1, 10);
            assertEquals(20, labels.size());
            for (String id : ids) {
                for (String category : categories) {
                    boolean found = false;
                    for (Label label : labels) {
                        if (("Configured " + id + " " + category).equals(label.getLevelName())) found = true;
                    }
                    assertTrue(id + " " + category, found);
                }
            }
            assertEquals(5, ScoreController.generateRoutineLabels(
                    meet, true, true, 3, withNames, 1, 10).size());
        }
    }

    @Test
    public void comboLabelsRetainTheirCategoryAndConfiguredName() {
        Meet meet = persistMeet();
        Team team = new Team("TST", "Test team");
        RoutineLevel level = new RoutineLevel("I11-18CT", "Configured intermediate", 1);
        em.persist(team);
        em.persist(level);
        persistRoutine(meet, team, level, "Team");
        em.flush();
        for (boolean withNames : new boolean[] {false, true}) {
            assertTrue(ScoreController.generateRoutineLabels(meet, true, true, 3, withNames, 1, 10).isEmpty());
            List<Label> labels = ScoreController.generateRoutineLabels(meet, true, true, 5, withNames, 1, 10);
            assertEquals(1, labels.size());
            assertEquals("Configured intermediate Combo", labels.get(0).getLevelName());
        }
    }

    @Test
    public void allNoviceLevelsShareFourFiguresAndRequireFourScores() {
        Meet meet = new Meet();
        meet.setSeason(new Season("2009"));
        meet.setType('R');
        Figure[] figures = {new Figure("1"), new Figure("2"), new Figure("3"), new Figure("4")};
        meet.setNov1Figure(figures[0]);
        meet.setNov2Figure(figures[1]);
        meet.setNov3Figure(figures[2]);
        meet.setNov4Figure(figures[3]);
        for (String id : new String[] {"N7", "N8-9", "N10-11", "N12-13", "N14-18", "N8", "N8U"}) {
            Swimmer swimmer = new Swimmer();
            swimmer.setLevel(new Level(id, id, 1));
            FiguresParticipant fp = new FiguresParticipant(meet, swimmer);
            fp.setTotalScore(BigDecimal.TEN);
            assertEquals(Arrays.asList(figures), meet.getFigureList(swimmer));
            assertEquals(4, ScoreController.numberOfFigures(fp));
            fp.getFiguresScores().add(new FigureScore());
            fp.getFiguresScores().add(new FigureScore());
            assertFalse(ScoreController.figuresParticipantHasAllScores(fp));
            fp.getFiguresScores().add(new FigureScore());
            fp.getFiguresScores().add(new FigureScore());
            assertTrue(ScoreController.figuresParticipantHasAllScores(fp));
            fp.setTotalScore(BigDecimal.ZERO);
            assertFalse(ScoreController.figuresParticipantHasAllScores(fp));
        }
    }

    @Test
    public void meetValidationDoesNotRequireLegacyFigureSelections() {
        Meet meet = new Meet();
        meet.setSeason(new Season("2009"));
        meet.setName("Test meet");
        meet.setMeetDate("November 8");
        meet.setHomeTeam(new Team("HOM", "Home"));
        meet.getOpponents().add(new Team("AWY", "Away"));
        Figure figure = new Figure("1");
        meet.setNov1Figure(figure);
        meet.setNov2Figure(figure);
        meet.setNov3Figure(figure);
        meet.setNov4Figure(figure);
        meet.setInt1Figure(figure);
        meet.setInt2Figure(figure);
        meet.setInt3Figure(figure);
        meet.setInt4Figure(figure);
        for (char type : new char[] {'R', 'C'}) {
            meet.setType(type);
            assertTrue(meet.isValid());
        }
        meet.setNov4Figure(null);
        assertFalse(meet.isValid());
    }

    private Meet persistMeet() {
        Meet meet = new Meet();
        meet.setName("Test meet");
        meet.setMeetDate("November 8");
        meet.setType('R');
        em.persist(meet);
        return meet;
    }

    private void persistRoutine(Meet meet, Team team, RoutineLevel level, String category) {
        Routine routine = new Routine();
        routine.setMeet(meet);
        routine.setTeam(team);
        routine.setLevel(level);
        routine.setRoutineType(category);
        routine.setName("Test routine");
        routine.setSwimmers1("Test Swimmer");
        routine.setNumSwimmers(1);
        routine.setPlace(1);
        routine.setTotalScore(BigDecimal.TEN);
        em.persist(routine);
    }
}
