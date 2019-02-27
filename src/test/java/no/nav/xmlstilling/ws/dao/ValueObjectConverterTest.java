package no.nav.xmlstilling.ws.dao;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import no.nav.xmlstilling.ws.common.vo.StillingBatchVO;
import no.nav.xmlstilling.ws.entity.StillingBatchEntity;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import static no.nav.xmlstilling.ws.dao.ValueObjectConverter.getStillingBatchEntityForValueObject;
import static no.nav.xmlstilling.ws.dao.ValueObjectConverter.getStillingBatchValueObject;
import static org.junit.Assert.assertEquals;

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
