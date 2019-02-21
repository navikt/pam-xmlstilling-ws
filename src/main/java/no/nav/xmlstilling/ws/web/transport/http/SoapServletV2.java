package no.nav.xmlstilling.ws.web.transport.http;

public class SoapServletV2 extends SoapServlet {

    @Override
    protected String getResponseMessage(boolean xmlIsWellFormed) {
        return xmlIsWellFormed ? SoapServletResponse.RESPONSE_V2_OK : SoapServletResponse.RESPONSE_V2_ERROR;
    }
}
