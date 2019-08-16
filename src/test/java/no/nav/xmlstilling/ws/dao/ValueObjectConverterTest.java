package no.nav.xmlstilling.ws.dao;

import no.nav.xmlstilling.ws.common.vo.StillingBatchVO;
import no.nav.xmlstilling.ws.entity.StillingBatchEntity;
import org.junit.Test;

import static no.nav.xmlstilling.ws.dao.ValueObjectConverter.getStillingBatchEntityForValueObject;
import static no.nav.xmlstilling.ws.dao.ValueObjectConverter.getStillingBatchValueObject;
import static org.junit.Assert.assertEquals;

public class ValueObjectConverterTest {

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
