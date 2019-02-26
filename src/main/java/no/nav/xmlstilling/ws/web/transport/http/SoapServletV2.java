package no.nav.xmlstilling.ws.web.transport.http;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2")
public class SoapServletV2 extends SoapServlet {

    @Override
    protected String getResponseMessage(boolean xmlIsWellFormed) {
        return xmlIsWellFormed ? SoapServletResponse.RESPONSE_V2_OK : SoapServletResponse.RESPONSE_V2_ERROR;
    }
}
