package no.nav.xmlstilling.ws.web.transport.http;

import no.nav.xmlstilling.ws.common.util.ConverterUtils;
import no.nav.xmlstilling.ws.common.util.XMLValidatorHelper;
import no.nav.xmlstilling.ws.common.vo.StillingBatchVO;
import no.nav.xmlstilling.ws.service.MetricsService;
import no.nav.xmlstilling.ws.service.facade.StillingBatchFacadeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class SoapServlet extends HttpServlet {
    private static final String CONTENT_TYPE_TEXT_XML = "text/xml";
    public static final String BEHANDLET_STATUS_OK_UBEHANDLET_0 = "0";
    public static final String BEHANDLET_STATUS_FEILET_INVALID_1 = "-1";

    private static transient Logger logger = LoggerFactory.getLogger(SoapServlet.class);

    @Autowired
    private StillingBatchFacadeBean stillingBatchFacadeBean;

    @Autowired
    private MetricsService metricsService;


    public void setStillingBatchFacadeBean(StillingBatchFacadeBean stillingBatchFacadeBean) {
        this.stillingBatchFacadeBean = stillingBatchFacadeBean;
    }

    public void setMetricsService(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @Override
    @PostMapping("/SixSoap")
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader br = req.getReader();
        String stillingXml = ConverterUtils.read(br);

        SoapServletResponse soapSvar;
        String eksterntBrukerNavn = req.getUserPrincipal().getName();
        //String eksterntBrukerNavn = "test" + System.currentTimeMillis();

        if (stillingXml.trim().length() == 0) {
            logger.info("stillingxml var tom streng!");
            soapSvar = getResponseMessage(false);
        } else {
            XMLValidatorHelper xmlValidatorHelper = new XMLValidatorHelper();
            boolean xmlIsWellFormed = xmlValidatorHelper.isWellFormed(stillingXml);
            logger.debug("xml fra bruker: " + eksterntBrukerNavn + " iswellformed: " + xmlIsWellFormed);
            logger.debug("Stillingxml: \n" + stillingXml);
            opprettOgProsesserStillingbatch(stillingXml, eksterntBrukerNavn, xmlIsWellFormed);
            soapSvar = getResponseMessage(xmlIsWellFormed);
        }

        metricsService.registerFor(eksterntBrukerNavn, soapSvar.isOkResponse());

        resp.setContentType(CONTENT_TYPE_TEXT_XML);
        PrintWriter out = resp.getWriter();
        out.write(soapSvar.getMessage());
        out.flush();
        out.close();
    }

    // Returnerer SoapServletResponse-objektet som gjelder for den aktuelle SoapServlet-versjonen.
    protected abstract SoapServletResponse getResponseMessage(boolean xmlIsWellFormed);

    private void opprettOgProsesserStillingbatch(String stillingXml, String eksterntBrukerNavn, boolean xmlIsWellFormed) {
        StillingBatchVO stillingBatchVO = new StillingBatchVO();
        stillingBatchVO.setArbeidsgiver(finnInnholdMellomTag(stillingXml));
        stillingBatchVO.setEksternBrukerRef(eksterntBrukerNavn);
        stillingBatchVO.setStillingXml(stillingXml);
        stillingBatchVO.setMottattDato(new java.sql.Timestamp(System.currentTimeMillis()));
        stillingBatchVO.setBehandletDato(null);
        stillingBatchVO.setBehandletStatus(xmlIsWellFormed ? BEHANDLET_STATUS_OK_UBEHANDLET_0 : BEHANDLET_STATUS_FEILET_INVALID_1);
        stillingBatchFacadeBean.insertStillingBatch(stillingBatchVO);
    }

    private String finnInnholdMellomTag(String stillingXml) {
        try {
            stillingXml = stillingXml.split("<EntityName>")[1];
            stillingXml = stillingXml.split("</EntityName>")[0];
            return stillingXml;
        } catch ( Exception e) {
            return "";
        }
    }
}
