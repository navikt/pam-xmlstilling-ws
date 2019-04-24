package no.nav.xmlstilling.ws.batch;

import no.nav.xmlstilling.ws.dao.StillingBatchDaoJpa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Query;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Component
public class SlettGamleStillingerWorker {

    private int mndTilbake = 6;

    @Autowired
    private StillingBatchDaoJpa stillingerDao;

    private Logger logger = LoggerFactory.getLogger(SlettGamleStillingerWorker.class);
    private Date slettesDato;

    @Transactional
    public void work() {
        slettesDato = beregnSlettesDato();
        logger.info("Sletter stillinger eldre enn {}", new SimpleDateFormat("dd.MM.yyyy").format(slettesDato));
        slettStillingerBatch();
    }

    private void slettStillingerBatch() {
        String query = "delete from StillingBatchEntity as s where s.mottattDato <= ?1";
        Query deleteFromStillingBatchQuery = stillingerDao.getEntityManager().createQuery(query).setParameter(1, slettesDato);
        int antSlettet = deleteFromStillingBatchQuery.executeUpdate();
        logger.info("Slettet {} rader fra STILLING_BATCH", antSlettet);
    }

    private Date beregnSlettesDato() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -mndTilbake);
        return calendar.getTime();
    }
}
