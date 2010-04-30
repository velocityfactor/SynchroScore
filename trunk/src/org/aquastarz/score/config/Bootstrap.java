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
package org.aquastarz.score.config;

import au.com.bytecode.opencsv.CSVReader;
import java.io.InputStreamReader;
import org.aquastarz.score.domain.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.aquastarz.score.ScoreApp;
import org.aquastarz.score.controller.ScoreController;
import org.aquastarz.score.db.SwimmerDB;

public class Bootstrap {

    public static void clearDB(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        entityManager.createQuery("delete from FigureScore").executeUpdate();
        entityManager.createQuery("delete from FiguresParticipant").executeUpdate();
        entityManager.createQuery("delete from Swimmer").executeUpdate();
        entityManager.createQuery("delete from Meet").executeUpdate();
        entityManager.createQuery("delete from Level").executeUpdate();
        entityManager.createQuery("delete from Team").executeUpdate();
        entityManager.createQuery("delete from Figure").executeUpdate();
        entityManager.createQuery("delete from Season").executeUpdate();

        entityManager.getTransaction().commit();

    }

    public static void loadLeagueData() {
        EntityManager entityManager = ScoreApp.getEntityManager();
        entityManager.getTransaction().begin();

        entityManager.persist(new Level("N8", "Novice 8 and Under",1));
        entityManager.persist(new Level("N9-10", "Novice 9-10",2));
        entityManager.persist(new Level("N11-12", "Novice 11-12",3));
        entityManager.persist(new Level("N13-14", "Novice 13-14",4));
        entityManager.persist(new Level("N15-18", "Novice 15-18",5));
        entityManager.persist(new Level("I11-12", "Intermediate 11-12",6));
        entityManager.persist(new Level("I13-14", "Intermediate 13-14",7));
        entityManager.persist(new Level("I15-16", "Intermediate 15-16",8));
        entityManager.persist(new Level("I17-18", "Intermediate 17-18",9));

        entityManager.getTransaction().commit();
        entityManager.getTransaction().begin();

        Team aub=new Team("AUB", "Auburn Mermaids");
        Team cor=new Team("COR", "Cordova Cordettes");
        Team fec=new Team("FEC", "Fulton El Camino Stingrays");
        Team dav=new Team("DAV", "Davis AquaStarz");
        Team ros=new Team("ROS", "Roseville Aquabunnies");
        Team sun=new Team("SUN", "Sunrise Swans");
        entityManager.persist(aub);
        entityManager.persist(cor);
        entityManager.persist(fec);
        entityManager.persist(dav);
        entityManager.persist(ros);
        entityManager.persist(sun);

        entityManager.getTransaction().commit();
        entityManager.getTransaction().begin();

        entityManager.persist(new Figure("101", new BigDecimal("1.6"), "Ballet Leg, R/L"));
        entityManager.persist(new Figure("311", new BigDecimal("1.8"), "Kip"));
        entityManager.persist(new Figure("360", new BigDecimal("2.1"), "Walkover, Front"));
        entityManager.persist(new Figure("349", new BigDecimal("1.8"), "Tower"));
        entityManager.persist(new Figure("342", new BigDecimal("2.1"), "Heron"));
        entityManager.persist(new Figure("316", new BigDecimal("2.4"), "Kip, Split, Walkout"));
        entityManager.persist(new Figure("140d", new BigDecimal("2.5"), "Flamingo, Bent Knee, Spin 180"));
        entityManager.persist(new Figure("345", new BigDecimal("2.2"), "Ariana"));
        entityManager.persist(new Figure("350", new BigDecimal("2.2"), "Minerva"));
        entityManager.persist(new Figure("240", new BigDecimal("2.2"), "Albatross"));

        entityManager.getTransaction().commit();
    }

    public static void loadSampleSwimmers() {
        EntityManager entityManager = ScoreApp.getEntityManager();
        saveSwimmer(entityManager,1, "Peauroi", "Elise", "I15-16", "DAV");
        saveSwimmer(entityManager,2, "Eernisse", "Jessylyn", "I15-16", "DAV");
        saveSwimmer(entityManager,3, "Cholewinski", "Krysten", "I15-16", "DAV");
        saveSwimmer(entityManager,4, "Hollander", "Anna", "I13-14", "DAV");
        saveSwimmer(entityManager,5, "Wong", "Alex", "I13-14", "DAV");
        saveSwimmer(entityManager,6, "Meads", "Emma", "I11-12", "DAV");
        saveSwimmer(entityManager,7, "Peauroi", "Maddie", "I11-12", "DAV");
        saveSwimmer(entityManager,8, "Hughes", "Jessica", "N11-12", "DAV");
        saveSwimmer(entityManager,9, "Kubota", "Christi", "N11-12", "DAV");
        saveSwimmer(entityManager,10, "Otto Cutting", "Amelia", "N9-10", "DAV");
        saveSwimmer(entityManager,11, "O'Brien", "Katie", "N15-18", "DAV");
        saveSwimmer(entityManager,12, "Riggle", "Clara", "N11-12", "DAV");
        saveSwimmer(entityManager,13, "Thomas", "Mollie", "N11-12", "DAV");
        saveSwimmer(entityManager,14, "Cavins", "Jocelyn", "N9-10", "DAV");
        saveSwimmer(entityManager,15, "Hyson", "Kellee", "N11-12", "DAV");
        saveSwimmer(entityManager,16, "Shuman", "Natalie", "N11-12", "DAV");
        saveSwimmer(entityManager,17, "Riesenberg", "Anna", "N9-10", "DAV");
        saveSwimmer(entityManager,18, "Ramirez", "Sofia Garcia", "N9-10", "DAV");
        saveSwimmer(entityManager,19, "Christensen", "Tara", "N9-10", "DAV");
        saveSwimmer(entityManager,20, "Shu", "Gwendolyn", "N11-12", "DAV");
        saveSwimmer(entityManager,21, "Plaskett", "Kaitlynn", "N11-12", "DAV");
        saveSwimmer(entityManager,22, "Kaufman", "Corina", "N11-12", "DAV");
        saveSwimmer(entityManager,23, "Chappelle", "Marcella", "N8", "SUN");
        saveSwimmer(entityManager,24, "Clark", "Julia", "N8", "SUN");
        saveSwimmer(entityManager,25, "Fleig", "Hayden", "N8", "SUN");
        saveSwimmer(entityManager,26, "Gutierrez", "Esmeralda", "N8", "SUN");
        saveSwimmer(entityManager,27, "Pirnik", "Molly", "N8", "SUN");
        saveSwimmer(entityManager,28, "Wayne", "Elaine", "N8", "SUN");
        saveSwimmer(entityManager,29, "Adams", "Claire", "N9-10", "SUN");
        saveSwimmer(entityManager,30, "Fuentes", "Sydney", "N9-10", "SUN");
        saveSwimmer(entityManager,31, "Gutierrez", "Olivia", "N9-10", "SUN");
        saveSwimmer(entityManager,32, "Hubbard", "Lexie", "N9-10", "SUN");
        saveSwimmer(entityManager,33, "Jones", "Molly", "N9-10", "SUN");
        saveSwimmer(entityManager,34, "Lance", "Annie", "N9-10", "SUN");
        saveSwimmer(entityManager,35, "Pirnik", "Sara-Kate", "N9-10", "SUN");
        saveSwimmer(entityManager,36, "Toffoletti", "Summer", "N9-10", "SUN");
        saveSwimmer(entityManager,37, "Toney", "Kiley", "N9-10", "SUN");
        saveSwimmer(entityManager,38, "Ahrens", "Sierra", "N11-12", "SUN");
        saveSwimmer(entityManager,39, "Schramm", "Rachel", "N11-12", "SUN");
        saveSwimmer(entityManager,40, "Sear", "Taylor", "N11-12", "SUN");
        saveSwimmer(entityManager,41, "Swaringen", "Jamie", "N11-12", "SUN");
        saveSwimmer(entityManager,42, "Toney", "Mikayla", "N11-12", "SUN");
        saveSwimmer(entityManager,43, "Bogle", "Lauren", "N11-12", "SUN");
        saveSwimmer(entityManager,44, "Clark", "Jennifer", "N11-12", "SUN");
        saveSwimmer(entityManager,45, "Lathrop", "Beth", "N11-12", "SUN");
        saveSwimmer(entityManager,46, "Black", "Brianna", "N13-14", "SUN");
        saveSwimmer(entityManager,47, "Santoyo", "Ashley", "N13-14", "SUN");
        saveSwimmer(entityManager,48, "Lathrop", "Katie", "N13-14", "SUN");
        saveSwimmer(entityManager,49, "Hartog", "Alexandra", "N15-18", "SUN");
        saveSwimmer(entityManager,50, "Fleig", "Barbara", "N9-10", "SUN");
        saveSwimmer(entityManager,51, "Fastiggi", "Stephanie", "I11-12", "SUN");
        saveSwimmer(entityManager,52, "Hoehenrieder", "Emily", "I11-12", "SUN");
        saveSwimmer(entityManager,53, "Lollis", "Alexis", "I11-12", "SUN");
        saveSwimmer(entityManager,54, "Peterson", "Anika", "I11-12", "SUN");
        saveSwimmer(entityManager,55, "Stachey", "Bridget", "I11-12", "SUN");
        saveSwimmer(entityManager,56, "Mimi", "Higgins", "I11-12", "SUN");
        saveSwimmer(entityManager,57, "Pereira", "Kirsten", "I11-12", "SUN");
        saveSwimmer(entityManager,58, "Ayala", "Rebekah", "I13-14", "SUN");
        saveSwimmer(entityManager,59, "Bair", "Alyssa", "I13-14", "SUN");
        saveSwimmer(entityManager,60, "Berger", "Renee", "I13-14", "SUN");
        saveSwimmer(entityManager,61, "Fastiggi", "Samantha", "I13-14", "SUN");
        saveSwimmer(entityManager,62, "Gibson", "Allison", "I13-14", "SUN");
        saveSwimmer(entityManager,63, "Peterson", "Erika", "I13-14", "SUN");
        saveSwimmer(entityManager,64, "Higgins", "Arika", "I13-14", "SUN");
        saveSwimmer(entityManager,65, "Critelli", "Sofie", "I13-14", "SUN");
        saveSwimmer(entityManager,66, "Haack", "Amanda", "I13-14", "SUN");
        saveSwimmer(entityManager,67, "Burke", "Molly", "I15-16", "SUN");
        saveSwimmer(entityManager,68, "Cantrell", "Caroline", "I15-16", "SUN");
        saveSwimmer(entityManager,69, "Dedrick", "Anna", "I15-16", "SUN");
        saveSwimmer(entityManager,70, "Ellsworth", "Sarah", "I15-16", "SUN");
        saveSwimmer(entityManager,71, "Felicia", "Mcphaul", "I15-16", "SUN");
        saveSwimmer(entityManager,72, "Messina", "Rachel", "I17-18", "SUN");
        saveSwimmer(entityManager,73, "Heggen", "Ariel", "I17-18", "SUN");
        saveSwimmer(entityManager,74, "Jacobs", "Jenna", "I11-12", "FEC");
        saveSwimmer(entityManager,75, "Simon", "Alexzandria", "I11-12", "FEC");
        saveSwimmer(entityManager,76, "Higgins", "Marisa", "I13-14", "FEC");
        saveSwimmer(entityManager,77, "Magaziner", "Jennifer", "I13-14", "FEC");
        saveSwimmer(entityManager,78, "Mather", "Caley", "I13-14", "FEC");
        saveSwimmer(entityManager,79, "Williams", "Jamison", "I13-14", "FEC");
        saveSwimmer(entityManager,80, "Young", "Sadie", "I13-14", "FEC");
        saveSwimmer(entityManager,81, "Carney", "Shelby", "I15-16", "FEC");
        saveSwimmer(entityManager,82, "Crockett", "Brittany", "I15-16", "FEC");
        saveSwimmer(entityManager,83, "Hastings", "Brienna", "I15-16", "FEC");
        saveSwimmer(entityManager,84, "O'Neil", "Annalise", "I15-16", "FEC");
        saveSwimmer(entityManager,85, "Brousseau", "Jennifer", "I17-18", "FEC");
        saveSwimmer(entityManager,86, "Higgins", "Alina", "I17-18", "FEC");
        saveSwimmer(entityManager,87, "O'Neil", "Madeline", "I17-18", "FEC");
        saveSwimmer(entityManager,88, "Simes", "Madison", "I17-18", "FEC");
        saveSwimmer(entityManager,89, "Smith", "Molly", "I17-18", "FEC");
        saveSwimmer(entityManager,90, "Whalen", "Andi", "I17-18", "FEC");
        saveSwimmer(entityManager,91, "Williams", "Valerie", "I17-18", "FEC");
        saveSwimmer(entityManager,92, "Crockett", "Jolie", "N8", "FEC");
        saveSwimmer(entityManager,93, "McJimsey", "Katherine", "N8", "FEC");
        saveSwimmer(entityManager,94, "Simon", "Isabella", "N8", "FEC");
        saveSwimmer(entityManager,95, "Branine", "Tara", "N9-10", "FEC");
        saveSwimmer(entityManager,96, "Held", "Grace", "N9-10", "FEC");
        saveSwimmer(entityManager,97, "Mangino", "Rain", "N9-10", "FEC");
        saveSwimmer(entityManager,98, "Simon", "Georgia", "N9-10", "FEC");
        saveSwimmer(entityManager,99, "Vann", "Tyler", "N9-10", "FEC");
        saveSwimmer(entityManager,100, "Blaine", "Jennifer", "N11-12", "FEC");
        saveSwimmer(entityManager,101, "Edwards", "Joci", "N11-12", "FEC");
        saveSwimmer(entityManager,102, "Fowler", "Mo", "N11-12", "FEC");
        saveSwimmer(entityManager,103, "Rowland", "Amber", "N11-12", "FEC");
        saveSwimmer(entityManager,104, "Severeid", "Emily", "N11-12", "FEC");
        saveSwimmer(entityManager,105, "Montgomery", "Kristen", "N13-14", "FEC");
        saveSwimmer(entityManager,106, "Rangel", "Paula", "N13-14", "FEC");
        saveSwimmer(entityManager,107, "Sandberg", "Melissa", "N13-14", "FEC");
        saveSwimmer(entityManager,108, "Anderson", "Kelsey", "N15-18", "FEC");
        saveSwimmer(entityManager,109, "Crayne", "Lisa", "N15-18", "FEC");
        saveSwimmer(entityManager,110, "Hoecker-Caietti", "Frankie", "N15-18", "FEC");
        saveSwimmer(entityManager,111, "Johnson", "Anna", "N15-18", "FEC");
        saveSwimmer(entityManager,112, "Abitz", "Emily", "N8", "COR");
        saveSwimmer(entityManager,113, "Abitz", "Macy", "N11-12", "COR");
        saveSwimmer(entityManager,114, "Alward", "Amber", "N11-12", "COR");
        saveSwimmer(entityManager,115, "Balan", "Kristina", "N11-12", "COR");
        saveSwimmer(entityManager,116, "Bankson", "Crystal", "N13-14", "COR");
        saveSwimmer(entityManager,117, "Caldwell", "Chloe", "I17-18", "COR");
        saveSwimmer(entityManager,118, "Campos", "Mariaeulalia", "N8", "COR");
        saveSwimmer(entityManager,119, "Carlson", "Kirstin", "N11-12", "COR");
        saveSwimmer(entityManager,120, "Coley", "Natalie", "I13-14", "COR");
        saveSwimmer(entityManager,121, "Dillon", "Nikki", "N8", "COR");
        saveSwimmer(entityManager,122, "Dillon", "Lexi", "N8", "COR");
        saveSwimmer(entityManager,123, "Ervin", "Holly", "I13-14", "COR");
        saveSwimmer(entityManager,124, "Fuller", "Kristin", "I17-18", "COR");
        saveSwimmer(entityManager,125, "Hackle", "Teela", "N11-12", "COR");
        saveSwimmer(entityManager,126, "Harlte", "Katie", "I17-18", "COR");
        saveSwimmer(entityManager,127, "Heminway", "Cassie", "N9-10", "COR");
        saveSwimmer(entityManager,128, "Holt", "Kit", "N9-10", "COR");
        saveSwimmer(entityManager,129, "Ivey", "Abigail", "N15-18", "COR");
        saveSwimmer(entityManager,130, "Kurkchi", "Tatyana", "N11-12", "COR");
        saveSwimmer(entityManager,131, "Meroux", "Amanda", "I13-14", "COR");
        saveSwimmer(entityManager,132, "Merrill", "Hannah", "N9-10", "COR");
        saveSwimmer(entityManager,133, "Morris-Little", "Hannah", "I11-12", "COR");
        saveSwimmer(entityManager,134, "Reyes", "Alicia", "N8", "COR");
        saveSwimmer(entityManager,135, "Salazar", "Delainy", "N9-10", "COR");
        saveSwimmer(entityManager,136, "Smothers", "Chalene", "N11-12", "COR");
        saveSwimmer(entityManager,137, "Thompson", "Felicia", "N11-12", "COR");
        saveSwimmer(entityManager,138, "Tran", "Mylei", "N11-12", "COR");
        saveSwimmer(entityManager,139, "Walker", "Leah", "I17-18", "COR");
        saveSwimmer(entityManager,140, "Wilson", "Kayla", "N11-12", "COR");
        saveSwimmer(entityManager,141, "Woosley", "Danielle", "N9-10", "COR");
        saveSwimmer(entityManager,142, "Ashford", "Lexie", "N8", "AUB");
        saveSwimmer(entityManager,143, "Campbell", "Destiny", "N8", "AUB");
        saveSwimmer(entityManager,144, "Campbell", "Savannah", "N8", "AUB");
        saveSwimmer(entityManager,145, "Dolley", "Grace", "N8", "AUB");
        saveSwimmer(entityManager,146, "Dunham", "Avery", "N8", "AUB");
        saveSwimmer(entityManager,147, "Gorceac", "Laura", "N8", "AUB");
        saveSwimmer(entityManager,148, "Kelly", "Taylor", "N8", "AUB");
        saveSwimmer(entityManager,149, "Sorensen", "Jillian", "N8", "AUB");
        saveSwimmer(entityManager,150, "Thompson", "Meridian", "N8", "AUB");
        saveSwimmer(entityManager,151, "Trnka", "Hanna", "N8", "AUB");
        saveSwimmer(entityManager,152, "Voss", "Samantha", "N8", "AUB");
        saveSwimmer(entityManager,153, "Arndt", "Megan", "N9-10", "AUB");
        saveSwimmer(entityManager,154, "Eastman", "Alexis", "N9-10", "AUB");
        saveSwimmer(entityManager,155, "Eichenhofer", "Julianna", "N9-10", "AUB");
        saveSwimmer(entityManager,156, "Guevel", "Lindsey", "N9-10", "AUB");
        saveSwimmer(entityManager,157, "James", "Katy", "N9-10", "AUB");
        saveSwimmer(entityManager,158, "Jensen", "Paige", "N9-10", "AUB");
        saveSwimmer(entityManager,159, "Sisk", "Delaney", "N9-10", "AUB");
        saveSwimmer(entityManager,160, "Summers", "Haley", "N9-10", "AUB");
        saveSwimmer(entityManager,161, "Yurdana", "Molly", "N9-10", "AUB");
        saveSwimmer(entityManager,162, "Colvin", "Sarah", "N11-12", "AUB");
        saveSwimmer(entityManager,163, "Gorceac", "Marianna", "N11-12", "AUB");
        saveSwimmer(entityManager,164, "Summers", "Emily", "N11-12", "AUB");
        saveSwimmer(entityManager,165, "Voss", "Alena", "N11-12", "AUB");
        saveSwimmer(entityManager,166, "Wessman", "Natalie", "N11-12", "AUB");
        saveSwimmer(entityManager,167, "Lardner", "Kayla", "N13-14", "AUB");
        saveSwimmer(entityManager,168, "Sabol", "Sadie", "I11-12", "AUB");
        saveSwimmer(entityManager,169, "Beentjes", "Morgan", "I13-14", "AUB");
        saveSwimmer(entityManager,170, "Guevel", "Lauren", "I13-14", "AUB");
        saveSwimmer(entityManager,171, "Lee", "Shelby", "I13-14", "AUB");
        saveSwimmer(entityManager,172, "Rodgers", "Emily", "I13-14", "AUB");
        saveSwimmer(entityManager,173, "Thomas", "Samantha", "I13-14", "AUB");
        saveSwimmer(entityManager,174, "Turner", "Marissa", "I13-14", "AUB");
        saveSwimmer(entityManager,175, "Eastman", "Sara", "I15-16", "AUB");
        saveSwimmer(entityManager,176, "Haddad", "Tasha", "I15-16", "AUB");
        saveSwimmer(entityManager,177, "Bivins", "Anna", "I17-18", "AUB");
        saveSwimmer(entityManager,178, "Hogan", "Mary Cait", "I17-18", "AUB");
        saveSwimmer(entityManager,179, "Rulifson", "Kristen", "I17-18", "AUB");
        saveSwimmer(entityManager,180, "Smyth", "Madeleine", "I11-12", "AUB");
        saveSwimmer(entityManager,181, "Helman", "Morgan", "N11-12", "AUB");
        saveSwimmer(entityManager,182, "Colvin", "Shannon", "N13-14", "AUB");
        saveSwimmer(entityManager,183, "Mays", "Mikayla", "I13-14", "AUB");
        saveSwimmer(entityManager,184, "Wurster", "Darrian", "N11-12", "FEC");
        saveSwimmer(entityManager,185, "Leash", "Danielle", "N13-14", "FEC");
        saveSwimmer(entityManager,186, "Frazier", "Helena", "N9-10", "DAV");

    }

    private static void saveSwimmer(EntityManager entityManager, Integer leagueNum, String lastName, String firstName, String levelId, String teamId) {
        Team team=(Team)entityManager.createNamedQuery("Team.findByTeamId").setParameter("teamId",teamId).getSingleResult();
        Level level=(Level)entityManager.createNamedQuery("Level.findByLevelId").setParameter("levelId", levelId).getSingleResult();
        Swimmer swimmer=new Swimmer(leagueNum, ScoreApp.getCurrentSeason(), team, level, firstName, lastName);

        entityManager.getTransaction().begin();
        entityManager.persist(swimmer);
        entityManager.getTransaction().commit();
    }

    public static void loadLegacyMeet(Season season, String name) {
        List<FigureScoreTracker> figureScoreTrackers = new ArrayList<FigureScoreTracker>();
        List<FiguresParticipantTracker> figuresParticipantTrackers = new ArrayList<FiguresParticipantTracker>();
        Map<String, LegacyResult> legacyResults = new HashMap<String, LegacyResult>();
        LegacyMeet legacyMeet = null;
        Meet meet = null;
        EntityManager entityManager = ScoreApp.getEntityManager();
        Bootstrap.loadLeagueData();

        CSVReader csv = new CSVReader(new InputStreamReader(Bootstrap.class.getResourceAsStream("Season"+season.getName()+"/"+name+"/results.csv")));
        String[] nextLine;
        try {
            csv.readNext(); //skip header
            while ((nextLine = csv.readNext()) != null) {
                LegacyResult lr = new LegacyResult(nextLine);
                legacyResults.put(lr.swmrNo, lr);
            }
            csv.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        csv = new CSVReader(new InputStreamReader(Bootstrap.class.getResourceAsStream("Season"+season.getName()+"/"+name+"/figstat.csv")));

        try {
            csv.readNext(); //skip header
            nextLine = csv.readNext();
            legacyMeet = new LegacyMeet(nextLine);
            csv.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        entityManager.getTransaction().begin();

        meet = new Meet();
        meet.setName(legacyMeet.meetTitle);
        meet.setSeason(season);
        meet.setMeetDate(legacyMeet.meetDate);
        if ("Dual".equals(legacyMeet.meetType)) {
            meet.setType('R');
        } else {
            meet.setType('C');
        }

        meet.setHomeTeam(entityManager.find(Team.class, legacyMeet.homeTm));
        List<Team> oppList = new ArrayList<Team>();
        oppList.add(entityManager.find(Team.class, legacyMeet.oppnTm));
        meet.setOpponents(oppList);
        meet.setNov1Figure(entityManager.find(Figure.class, legacyMeet.nRestSta1));
        meet.setNov2Figure(entityManager.find(Figure.class, legacyMeet.nRestSta2));
        meet.setNov3Figure(entityManager.find(Figure.class, legacyMeet.nRestSta3));
        meet.setNov4Figure(entityManager.find(Figure.class, legacyMeet.nRestSta4));
        meet.setInt1Figure(entityManager.find(Figure.class, legacyMeet.iSta1));
        meet.setInt2Figure(entityManager.find(Figure.class, legacyMeet.iSta2));
        meet.setInt3Figure(entityManager.find(Figure.class, legacyMeet.iSta3));
        meet.setInt4Figure(entityManager.find(Figure.class, legacyMeet.iSta4));
        if (legacyMeet.n8USta1) {
            meet.setEu1Figure(meet.getNov1Figure());
        }
        if (legacyMeet.n8USta2) {
            if (meet.getEu1Figure() == null) {
                meet.setEu1Figure(meet.getNov2Figure());
            } else {
                meet.setEu2Figure(meet.getNov2Figure());
            }
        }
        if (legacyMeet.n8USta3) {
            if (meet.getEu1Figure() == null) {
                meet.setEu1Figure(meet.getNov3Figure());
            } else {
                meet.setEu2Figure(meet.getNov3Figure());
            }
        }
        if (legacyMeet.n8USta4) {
            meet.setEu2Figure(meet.getNov4Figure());
        }
        meet.setFiguresOrderGenerated(true);

        //Map legacy level names to current
        Map<String, Level> levels = new HashMap<String, Level>();
        levels.put("NOV8 & Under", entityManager.find(Level.class, "N8"));
        levels.put("NOV9-10", entityManager.find(Level.class, "N9-10"));
        levels.put("NOV11-12", entityManager.find(Level.class, "N11-12"));
        levels.put("NOV13-14", entityManager.find(Level.class, "N13-14"));
        levels.put("NOV15-18 (N)", entityManager.find(Level.class, "N15-18"));
        levels.put("INT11-12", entityManager.find(Level.class, "I11-12"));
        levels.put("INT13-14", entityManager.find(Level.class, "I13-14"));
        levels.put("INT15-16 (I)", entityManager.find(Level.class, "I15-16"));
        levels.put("INT17-18 (I)", entityManager.find(Level.class, "I17-18"));

        //Load league swimmer list from meet participants
        for (LegacyResult lr : legacyResults.values()) {
            Team team = entityManager.find(Team.class, lr.team);
            if (team == null) {
                System.out.println("Team not found [" + lr.team + "]");
            }
            Level level = levels.get(lr.novInt + lr.ageGrp);
            if (level == null) {
                System.out.println("Level not found [" + lr.novInt + lr.ageGrp + "]");
            }
            Swimmer swimmer = new Swimmer(lr.leagueNo, season, team, level, lr.gName, lr.fName);
            entityManager.persist(swimmer);
        }
        for (LegacyResult lr : legacyResults.values()) {
            FiguresParticipant fp = new FiguresParticipant();
            fp.setFigureOrder(lr.swmrNo);

            fp.setSwimmer(SwimmerDB.findByLeagueNum(lr.leagueNo));
            fp.setMeet(meet);
            entityManager.persist(fp);

            FiguresParticipantTracker fpt = new FiguresParticipantTracker();
            fpt.figuresParticipant = fp;
            fpt.place = lr.place;
            fpt.points = lr.points;
            fpt.total = lr.finTot;
            figuresParticipantTrackers.add(fpt);

            List<FigureScore> scores = new ArrayList<FigureScore>();
            int figNum = 1;
            for (int i = 0; i < 4; i++) {
                if (!"N8".equals(fp.getSwimmer().getLevel().getLevelId()) || ((i == 0 && legacyMeet.n8USta1)
                        || (i == 1 && legacyMeet.n8USta2)
                        || (i == 2 && legacyMeet.n8USta3)
                        || (i == 3 && legacyMeet.n8USta4))) {
                    FigureScore fs = new FigureScore();
                    fs.setFiguresParticipant(fp);
                    if ("N8".equals(fp.getSwimmer().getLevel().getLevelId())) {
                        switch (figNum) {
                            case 1:
                                fs.setFigure(meet.getEu1Figure());
                                break;
                            case 2:
                                fs.setFigure(meet.getEu2Figure());
                                break;
                        }

                    } else if (fp.getSwimmer().getLevel().getLevelId().startsWith("N")) {
                        switch (figNum) {
                            case 1:
                                fs.setFigure(meet.getNov1Figure());
                                break;
                            case 2:
                                fs.setFigure(meet.getNov2Figure());
                                break;
                            case 3:
                                fs.setFigure(meet.getNov3Figure());
                                break;
                            case 4:
                                fs.setFigure(meet.getNov4Figure());
                                break;
                        }
                    } else {
                        switch (figNum) {
                            case 1:
                                fs.setFigure(meet.getInt1Figure());
                                break;
                            case 2:
                                fs.setFigure(meet.getInt2Figure());
                                break;
                            case 3:
                                fs.setFigure(meet.getInt3Figure());
                                break;
                            case 4:
                                fs.setFigure(meet.getInt4Figure());
                                break;
                        }
                    }
                    try {
                        fs.setScore1((BigDecimal) lr.getClass().getDeclaredField("s" + (i + 1) + "j1").get(lr));
                        fs.setScore2((BigDecimal) lr.getClass().getDeclaredField("s" + (i + 1) + "j2").get(lr));
                        fs.setScore3((BigDecimal) lr.getClass().getDeclaredField("s" + (i + 1) + "j3").get(lr));
                        fs.setScore4((BigDecimal) lr.getClass().getDeclaredField("s" + (i + 1) + "j4").get(lr));
                        fs.setScore5((BigDecimal) lr.getClass().getDeclaredField("s" + (i + 1) + "j5").get(lr));
                        fs.setPenalty((BigDecimal) lr.getClass().getDeclaredField("s" + (i + 1) + "Pen").get(lr));
                        fs.setTotalScore(ScoreController.totalScore(fs));
                        FigureScoreTracker fst = new FigureScoreTracker();
                        fst.figureScore = fs;
                        fst.total = (BigDecimal) lr.getClass().getDeclaredField("s" + (i + 1) + "Tot").get(lr);
                        figureScoreTrackers.add(fst);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    entityManager.persist(fs);
                    scores.add(fs);
                    figNum++;
                }
            }
            fp.setFiguresScores(scores);
            meet.getFiguresParticipants().add(fp);
        }
        ScoreController.calculateMeetResults(meet);
        entityManager.persist(meet);

        entityManager.flush();
        entityManager.getTransaction().commit();

    }

    public static class LegacyResult {

        int niCat;
        int ageCat;
        int dFinTot;
        String swmrNo;
        BigDecimal finTot;
        String novInt;
        String ageGrp;
        String fName;
        String gName;
        String team;
        int leagueNo;
        BigDecimal s1j1;
        BigDecimal s1j2;
        BigDecimal s1j3;
        BigDecimal s1j4;
        BigDecimal s1j5;
        BigDecimal s1Tot;
        BigDecimal s1Pen;
        BigDecimal s2j1;
        BigDecimal s2j2;
        BigDecimal s2j3;
        BigDecimal s2j4;
        BigDecimal s2j5;
        BigDecimal s2Tot;
        BigDecimal s2Pen;
        BigDecimal s3j1;
        BigDecimal s3j2;
        BigDecimal s3j3;
        BigDecimal s3j4;
        BigDecimal s3j5;
        BigDecimal s3Tot;
        BigDecimal s3Pen;
        BigDecimal s4j1;
        BigDecimal s4j2;
        BigDecimal s4j3;
        BigDecimal s4j4;
        BigDecimal s4j5;
        BigDecimal s4Tot;
        BigDecimal s4Pen;
        BigDecimal totPen;
        int place;
        BigDecimal points;

        LegacyResult(String[] line) {
            niCat = Integer.valueOf(line[0]);
            ageCat = Integer.valueOf(line[1]);
            dFinTot = Integer.valueOf(line[2]);
            swmrNo = line[3];
            finTot = new BigDecimal(line[4]);
            novInt = line[5];
            ageGrp = line[6];
            fName = line[7];
            gName = line[8];
            team = line[9];
            leagueNo = Integer.valueOf(line[10]);
            s1j1 = new BigDecimal(line[11]);
            s1j2 = new BigDecimal(line[12]);
            s1j3 = new BigDecimal(line[13]);
            s1j4 = new BigDecimal(line[14]);
            s1j5 = new BigDecimal(line[15]);
            s1Tot = new BigDecimal(line[16]);
            s1Pen = new BigDecimal(line[17]);
            s2j1 = new BigDecimal(line[18]);
            s2j2 = new BigDecimal(line[19]);
            s2j3 = new BigDecimal(line[20]);
            s2j4 = new BigDecimal(line[21]);
            s2j5 = new BigDecimal(line[22]);
            s2Tot = new BigDecimal(line[23]);
            s2Pen = new BigDecimal(line[24]);
            s3j1 = new BigDecimal(line[25]);
            s3j2 = new BigDecimal(line[26]);
            s3j3 = new BigDecimal(line[27]);
            s3j4 = new BigDecimal(line[28]);
            s3j5 = new BigDecimal(line[29]);
            s3Tot = new BigDecimal(line[30]);
            s3Pen = new BigDecimal(line[31]);
            s4j1 = new BigDecimal(line[32]);
            s4j2 = new BigDecimal(line[33]);
            s4j3 = new BigDecimal(line[34]);
            s4j4 = new BigDecimal(line[35]);
            s4j5 = new BigDecimal(line[36]);
            s4Tot = new BigDecimal(line[37]);
            s4Pen = new BigDecimal(line[38]);
            totPen = new BigDecimal(line[39]);
            place = Integer.valueOf(line[40]);
            points = new BigDecimal(line[41]);
        }
    }

    public static class LegacyMeet {

        String meetType;
        String homeTm;
        String oppnTm;
        String meetDate;
        String meetTitle;
        boolean n8USta1;
        boolean n8USta2;
        boolean n8USta3;
        boolean n8USta4;
        String nRestSta1;
        BigDecimal nRestS1DD;
        String nRestS1Des;
        String nRestSta2;
        BigDecimal nRestS2DD;
        String nRestS2Des;
        String nRestSta3;
        BigDecimal nRestS3DD;
        String nRestS3Des;
        String nRestSta4;
        BigDecimal nRestS4DD;
        String nRestS4Des;
        String iSta1;
        BigDecimal iS1DD;
        String iS1Des;
        String iSta2;
        BigDecimal iS2DD;
        String iS2Des;
        String iSta3;
        BigDecimal iS3DD;
        String iS3Des;
        String iSta4;
        BigDecimal iS4DD;
        String iS4Des;

        LegacyMeet(String line[]) {
            meetType = line[0];
            homeTm = line[1];
            oppnTm = line[2];
            meetDate = line[3];
            meetTitle = line[4];
            n8USta1 = "1".equals(line[5]);
            n8USta2 = "1".equals(line[6]);
            n8USta3 = "1".equals(line[7]);
            n8USta4 = "1".equals(line[8]);
            nRestSta1 = line[9];
            nRestS1DD = new BigDecimal(line[10]);
            nRestS1Des = line[11];
            nRestSta2 = line[12];
            nRestS2DD = new BigDecimal(line[13]);
            nRestS2Des = line[14];
            nRestSta3 = line[15];
            nRestS3DD = new BigDecimal(line[16]);
            nRestS3Des = line[17];
            nRestSta4 = line[18];
            nRestS4DD = new BigDecimal(line[19]);
            nRestS4Des = line[20];
            iSta1 = line[21];
            iS1DD = new BigDecimal(line[22]);
            iS1Des = line[23];
            iSta2 = line[24];
            iS2DD = new BigDecimal(line[25]);
            iS2Des = line[26];
            iSta3 = line[27];
            iS3DD = new BigDecimal(line[28]);
            iS3Des = line[29];
            iSta4 = line[30];
            iS4DD = new BigDecimal(line[31]);
            iS4Des = line[32];
        }
    }

    public static class FigureScoreTracker {

        public FigureScore figureScore;
        public BigDecimal total;
    }

    public static class FiguresParticipantTracker {

        public FiguresParticipant figuresParticipant;
        public BigDecimal total;
        public BigDecimal points;
        public int place;
    }
}
