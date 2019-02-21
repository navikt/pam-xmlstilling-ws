package no.nav.xmlstilling.ws.web.transport.http;

public class SoapServletV1 extends SoapServlet {

    @Override
    protected String getResponseMessage(boolean xmlIsWellFormed) {
        return xmlIsWellFormed ? SoapServletResponse.RESPONSE_V1_OK : SoapServletResponse.RESPONSE_V1_ERROR;
    }
}
