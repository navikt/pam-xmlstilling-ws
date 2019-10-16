package no.nav.xmlstilling.ws.web.transport.http;

import no.nav.xmlstilling.ws.service.MetricsService;
import no.nav.xmlstilling.ws.service.facade.StillingBatchFacadeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SoapServletV1 extends SoapServlet {

    @Autowired
    public SoapServletV1(StillingBatchFacadeBean stillingBatchFacadeBean, MetricsService metricsService) {
        super(stillingBatchFacadeBean, metricsService);
    }

    @Override
    protected SoapServletResponse getResponseMessage(boolean xmlIsWellFormed) {
        return xmlIsWellFormed ? SoapServletResponse.RESPONSE_V1_OK : SoapServletResponse.RESPONSE_V1_ERROR;
    }
}
