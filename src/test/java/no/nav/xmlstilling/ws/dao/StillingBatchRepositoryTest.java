package no.nav.xmlstilling.ws.dao;

import no.nav.xmlstilling.ws.DevApplication;
import no.nav.xmlstilling.ws.entity.StillingBatchEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan
@ContextConfiguration(classes = DevApplication.class)
public class StillingBatchRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StillingBatchDaoJpa stillingBatchDaoJpa;

    @Test
    public void testFindByEksternbrukerref() {
        StillingBatchEntity entity = new StillingBatchEntity();
        entity.setStillingXml("<xml>fooo</xml>");
        entity.setArbeidsgiver("testArbeidsgiver");
        entity.setBehandletDato(new Timestamp(System.currentTimeMillis()));
        entity.setBehandletStatus("0");
        entity.setEksternBrukerRef("testEksternBrukerRef");

        entityManager.persist(entity);

        stillingBatchDaoJpa = new StillingBatchDaoJpa();
        stillingBatchDaoJpa.setEntityManager(entityManager.getEntityManager());

        Collection<StillingBatchEntity> stillinger = stillingBatchDaoJpa.findByEksternbrukerref("testEksternBrukerRef");

        assertEquals(stillinger.size(), 1);
        assertEquals(stillinger.iterator().next().getArbeidsgiver(), "testArbeidsgiver");
    }
}
