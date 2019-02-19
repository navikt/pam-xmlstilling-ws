package aetat.six.batch;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Service
public class StillingBatchTask {

    private Logger logger = LoggerFactory.getLogger(StillingBatchTask.class);

    @Autowired
    private SlettGamleStillingerWorker slettGamleStillingerWorker;

    @Value("${xmlstilling.batch.node}")
    private String node;

    @Scheduled(cron = "${xmlstilling.batch.cron}")
    public void deleteStillingBatch() {
        if (isOnCorrectNode()) {
            logger.debug("Starter batch");
            slettGamleStillingerWorker.work();
            logger.debug("Avslutter batch");
        }
    }

    private boolean isOnCorrectNode() {
        try {
            String hostname = InetAddress.getLocalHost().getCanonicalHostName();
            return hostname != null && hostname.equals(node);
        } catch (UnknownHostException e) {
            logger.warn("Unknown host", e);
            return false;
        }
    }

}
