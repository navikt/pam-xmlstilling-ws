package aetat.six.web.selftest;

import no.nav.selftest.SelftestUtils;
import no.nav.selftest.status.DBStatus;
import no.nav.selftest.status.Host;
import no.nav.selftest.status.Status;
import no.nav.selftest.status.VersionFromPomProperties;
import org.apache.commons.collections15.Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static no.nav.selftest.status.Status.perform;

/**
 * Selftestklasse.
 */
@Component
public class SelftestRequestHandler implements HttpRequestHandler {

    private final List<Factory<Status>> statusChecks = new ArrayList<Factory<Status>>();

    @Autowired
    @Qualifier("sixdbDataSource")
    private DataSource sixDb;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ServletContext servletContext;

    @PostConstruct
    public void init() {
        InputStream pomProperties = servletContext.getResourceAsStream(
                "/META-INF/maven/no.nav.sbl.xmlstilling/xmlstilling-ws-web/pom.properties");

        String testSql = "Select 1 from dual";

        statusChecks.add(new VersionFromPomProperties(pomProperties));
        statusChecks.add(new Host());
        statusChecks.add(new DBStatus(sixDb, "SIX-DB", testSql));
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println(
                "<html>" +
                        "<head>" +
                        "<title>Selftest-side for xmlstilling</title>" +
                        "</head>" +
                        "<body>" +
                        "<h1>Selftest xmlstilling</h1>");

        List<Status> statuses = perform(statusChecks);

        SelftestUtils.printStatusesAndExceptions(statuses, out);

        out.println("</body></html>");
        out.close();
    }
}
