package no.nav.xmlstilling.ws.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StillingBatchTask {

    private Logger logger = LoggerFactory.getLogger(StillingBatchTask.class);

    @Autowired
    private SlettGamleStillingerWorker slettGamleStillingerWorker;

    @Scheduled(cron = "0 0 2 * * SUN")
    public void deleteStillingBatch() {
        if (isOnCorrectNode()) {
            logger.debug("Starter batch");
            slettGamleStillingerWorker.work();
            logger.debug("Avslutter batch");
        }
    }

    private boolean isOnCorrectNode() {
        // todo - reimplementer denne dersom nodene går i beina på hverandre. Bør ikke være et problem.
        return true;
    }

}
