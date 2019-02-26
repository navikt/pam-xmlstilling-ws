package no.nav.xmlstilling.ws.dao;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import no.nav.sbl.entity.six.EksternBrukerEntity;
import no.nav.sbl.entity.six.StillingBatchEntity;
import no.nav.xmlstilling.ws.common.vo.EksternBrukerVO;
import no.nav.xmlstilling.ws.common.vo.StillingBatchVO;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import static no.nav.xmlstilling.ws.dao.ValueObjectConverter.*;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ValueObjectConverterTest {

    private ListAppender<ILoggingEvent> loggingEventListAppender;

    @Before
    public void setUp() {
        loggingEventListAppender = new ListAppender<ILoggingEvent>();
        loggingEventListAppender.start();
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.getLogger(Logger.ROOT_LOGGER_NAME).addAppender(loggingEventListAppender);
    }

    @Test
    public void testSetEksternBrukeEntityForValueObject() throws Exception {
        EksternBrukerVO eksternBrukerVO = new EksternBrukerVO();
        eksternBrukerVO.setAdresseLinje1("Bakkevegen");
        eksternBrukerVO.setEmailMimeType("lang-mimetype-som-er-over-20-bokstaver");
        eksternBrukerVO.setPostNr("123456789012345");
        eksternBrukerVO.setEmailMimeType("lang-mimetype-som-er-over-20-bokstaver");
        eksternBrukerVO.setBrukerStatus("42");
        eksternBrukerVO.setInternBrukerId(new BigDecimal(42));

        EksternBrukerEntity eksternBrukerEntity = setEksternBrukeEntityForValueObject(eksternBrukerVO);

        assertEquals("Bakkevegen", eksternBrukerEntity.getAdresseLinje1());
        assertEquals("lang-mimetype-som-er", eksternBrukerEntity.getEmailMimeType());
        assertEquals("1234567890", eksternBrukerEntity.getPostNr());
        assertEquals("4", eksternBrukerEntity.getBrukerStatus());
        assertEquals(new BigDecimal(42), eksternBrukerEntity.getInternBrukerId());

        String logString = loggingEventListAppender.list.toString();
        assertThat(logString, containsString("for stor"));
        assertThat(logString, containsString("emailMimeType"));
        assertThat(logString, containsString("EksternBrukerVO"));
        assertThat(logString, containsString("20"));

        assertThat(logString, containsString("postNr"));
        assertThat(logString, containsString("10"));
    }

    @Test
    public void testGetEksternBrukerValueObject() throws Exception {
        long timeInMillis = Calendar.getInstance().getTimeInMillis();

        EksternBrukerEntity eksternBrukerEntity = new EksternBrukerEntity();
        eksternBrukerEntity.setAdresseLinje1("Bakkevegen");
        eksternBrukerEntity.setInternBrukerId(new BigDecimal(42));
        eksternBrukerEntity.setEndretDato(new Timestamp(timeInMillis));
        eksternBrukerEntity.setPostNr("0654");
        eksternBrukerEntity.setXslTemplateId(new BigDecimal(123));

        EksternBrukerVO eksternBrukerVO = getEksternBrukerValueObject(eksternBrukerEntity);

        assertEquals("Bakkevegen", eksternBrukerVO.getAdresseLinje1());
        assertEquals(new BigDecimal(42), eksternBrukerVO.getInternBrukerId());
        assertEquals(new Timestamp(timeInMillis), eksternBrukerVO.getEndretDato());
        assertEquals("0654", eksternBrukerVO.getPostNr());
        assertEquals(new BigDecimal(123), eksternBrukerVO.getXslTemplateId());
    }

    @Test
    public void testGetStillingBatchValueObject() throws Exception {
        StillingBatchEntity stillingBatchEntity = new StillingBatchEntity();
        stillingBatchEntity.setStillingXml("FOO");

        StillingBatchVO stillingBatchValueObject = getStillingBatchValueObject(stillingBatchEntity);

        assertEquals("FOO", stillingBatchValueObject.getStillingXml());

    }

    @Test
    public void testGetStillingBatchEntityForValueObject() throws Exception {
        StillingBatchVO stillingBatchVO = new StillingBatchVO();
        stillingBatchVO.setStillingXml("FOO");

        StillingBatchEntity stillingBatchEntityForValueObject = getStillingBatchEntityForValueObject(stillingBatchVO);

        assertEquals("FOO", stillingBatchEntityForValueObject.getStillingXml());

    }

}
