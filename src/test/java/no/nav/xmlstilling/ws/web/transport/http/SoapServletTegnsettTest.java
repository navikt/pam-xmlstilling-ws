package no.nav.xmlstilling.ws.web.transport.http;

import no.nav.xmlstilling.ws.service.MetricsService;
import no.nav.xmlstilling.ws.service.facade.StillingBatchFacadeBean;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.nio.charset.StandardCharsets;

public class SoapServletTegnsettTest {

    @Mock
    private MetricsService metricsService;

    @Mock
    private StillingBatchFacadeBean stillingBatchFacadeBean;

    @Test
    public void skalTolkeISO88591() {
        SoapServlet ss = createServlet();
        String detektertTegnsett =
                ss.detekterTegnsett("Dette hærre e en tekst med ISO-8859-1".getBytes(StandardCharsets.ISO_8859_1));
        Assert.assertEquals("ISO-8859-1", detektertTegnsett);
    }
    @Test
    public void skalTolkeUTF8() {
        SoapServlet ss = createServlet();
        String detektertTegnsett =
                ss.detekterTegnsett("Dette hærre e en tekst med UTF-8".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals("UTF-8", detektertTegnsett);
    }
    @Test
    public void skalTolkeUTF8SomDefault() {
        SoapServlet ss = createServlet();
        String detektertTegnsett =
                ss.detekterTegnsett("Dette herre e en ANSI-tekst".getBytes(StandardCharsets.UTF_8));
        Assert.assertEquals("UTF-8", detektertTegnsett);
    }


    private SoapServlet createServlet() {
        return new SoapServlet(stillingBatchFacadeBean, metricsService) {
            @Override
            protected SoapServletResponse getResponseMessage(boolean xmlIsWellFormed) {
                return null;
            }
        };
    }
}
