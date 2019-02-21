package no.nav.xmlstilling.ws.web.selftest;

import no.nav.selftest.json.SelftestStatusBuilder;
import no.nav.selftest.servlets.SelftestJSONRequestHandler;
import no.nav.selftest.status.DBStatus;
import no.nav.selftest.status.Status;
import no.nav.selftest.status.StatusCode;
import org.apache.commons.collections15.Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static no.nav.selftest.status.Status.perform;
import static org.apache.commons.collections15.CollectionUtils.select;

@Component
public class XmlStillingSelftestJSONRequestHandler extends SelftestJSONRequestHandler {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    @Qualifier("sixdbDataSource")
    private DataSource sixDb;

    private final List<Factory<Status>> statusChecks = new ArrayList<Factory<Status>>();

    @PostConstruct
    public void init() {
        statusChecks.add(new DBStatus(sixDb, "SIX-DB"));
    }

    @Override
    protected void populateSelftestStatus(SelftestStatusBuilder selftestStatusBuilder) {
        Properties pomProperties = new Properties();
        String version;

        try {
            pomProperties.load(servletContext.getResourceAsStream(
                    "/META-INF/maven/no.nav.sbl.xmlstilling/xmlstilling-ws-web/pom.properties"));
            version = pomProperties.getProperty("version", "ikke funnet!");
        } catch (Exception e) {
            version = e.getMessage();
        }

        selftestStatusBuilder.withVersion(version);
        addStatusAndMessage(selftestStatusBuilder);
    }

    private void addStatusAndMessage(SelftestStatusBuilder selftestStatusBuilder) {
        StatusCode statusCode = StatusCode.OK;
        StringBuilder message = new StringBuilder();

        List<Status> statuses = perform(statusChecks);
        Collection<Status> failedStatuses = select(statuses, Status.HAS_EXCEPTION);

        if (!failedStatuses.isEmpty()) {
            statusCode = StatusCode.ERROR;
            for (Status failedStatus : failedStatuses) {
                message.append(failedStatus.toString());
                message.append(" ");
            }
        }

        selftestStatusBuilder.withStatus(statusCode).withMessage(message.toString().trim());
    }
}
