package no.nav.xmlstilling.ws.web.transport.http;

import org.junit.Assert;
import org.junit.Test;

public class SoapServletTegnsettTest {
    @Test
    public void skalTolkeISO88591() throws Exception {
        SoapServlet ss = createServlet();
        String detektertTegnsett =
                ss.detekterTegnsett("Dette hærre e en tekst med ISO-8859-1".getBytes("ISO-8859-1"));
        Assert.assertEquals("ISO-8859-1", detektertTegnsett);
    }
    @Test
    public void skalTolkeUTF8() throws Exception {
        SoapServlet ss = createServlet();
        String detektertTegnsett =
                ss.detekterTegnsett("Dette hærre e en tekst med UTF-8".getBytes("UTF-8"));
        Assert.assertEquals("UTF-8", detektertTegnsett);
    }
    @Test
    public void skalTolkeUTF8SomDefault() throws Exception {
        SoapServlet ss = createServlet();
        String detektertTegnsett =
                ss.detekterTegnsett("Dette herre e en ANSI-tekst".getBytes("UTF-8"));
        Assert.assertEquals("UTF-8", detektertTegnsett);
    }


    private SoapServlet createServlet() {
        return new SoapServlet() {
            @Override
            protected SoapServletResponse getResponseMessage(boolean xmlIsWellFormed) {
                return null;
            }
        };
    }
}
