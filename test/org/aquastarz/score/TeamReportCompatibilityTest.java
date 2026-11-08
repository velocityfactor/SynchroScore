package org.aquastarz.score;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.aquastarz.score.domain.Team;
import org.aquastarz.score.report.TeamPoints;
import org.junit.Test;
import static org.junit.Assert.*;

public class TeamReportCompatibilityTest {
    @Test
    public void regeneratedReportPreservesTiedTeamsAndPlacesAndExportsPdf() throws Exception {
        List<TeamPoints> teams = Arrays.asList(
                new TeamPoints(new Team("AAA", "Team Alpha"), BigDecimal.TEN, 1),
                new TeamPoints(new Team("BBB", "Team Beta"), BigDecimal.TEN, 1),
                new TeamPoints(new Team("CCC", "Team Gamma"), BigDecimal.ONE, 3));
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("MeetName", "Report compatibility test");
        parameters.put("MeetDate", "November 8, 2026");
        JasperPrint print;
        try (InputStream input = getClass().getResourceAsStream(
                "/org/aquastarz/score/report/TeamResults.jasper")) {
            assertNotNull("Team report must be regenerated during the build", input);
            JasperReport report = (JasperReport) JRLoader.loadObject(input);
            print = JasperFillManager.fillReport(report, parameters, new JRBeanCollectionDataSource(teams));
        }
        List<String> names = new ArrayList<String>();
        List<String> places = new ArrayList<String>();
        for (JRPrintPage page : print.getPages()) {
            for (JRPrintElement element : page.getElements()) {
                if (!(element instanceof JRPrintText)) continue;
                String text = ((JRPrintText) element).getFullText();
                if (text.startsWith("Team ")) names.add(text);
                if ("f8e3e4a3-9e4a-4d3e-8b4b-0c9a8d7e6d5e".equals(String.valueOf(element.getUUID()))) {
                    places.add(text.trim());
                }
            }
        }
        assertTrue(names.containsAll(Arrays.asList("Team Alpha", "Team Beta", "Team Gamma")));
        assertEquals(Arrays.asList("1", "1", "3"), places);
        byte[] pdf = JasperExportManager.exportReportToPdf(print);
        assertTrue(pdf.length > 4);
        assertEquals("%PDF", new String(pdf, 0, 4, "US-ASCII"));
    }
}
