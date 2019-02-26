package no.nav.xmlstilling.ws.web.transport.http;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SoapServletV1 extends SoapServlet {

    @Override
    protected String getResponseMessage(boolean xmlIsWellFormed) {
        return xmlIsWellFormed ? SoapServletResponse.RESPONSE_V1_OK : SoapServletResponse.RESPONSE_V1_ERROR;
    }
}
