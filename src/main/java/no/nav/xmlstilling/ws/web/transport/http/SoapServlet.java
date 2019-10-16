package no.nav.xmlstilling.ws.web.transport.http;

import no.nav.xmlstilling.ws.common.util.XMLValidatorHelper;
import no.nav.xmlstilling.ws.common.vo.StillingBatchVO;
import no.nav.xmlstilling.ws.service.MetricsService;
import no.nav.xmlstilling.ws.service.facade.StillingBatchFacadeBean;
import org.mozilla.universalchardet.UniversalDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

public abstract class SoapServlet extends HttpServlet {
    private static final String CONTENT_TYPE_TEXT_XML = "text/xml";
    private static final String BEHANDLET_STATUS_OK_UBEHANDLET_0 = "0";
    private static final String BEHANDLET_STATUS_FEILET_INVALID_1 = "-1";

    private static transient Logger logger = LoggerFactory.getLogger(SoapServlet.class);

    private final StillingBatchFacadeBean stillingBatchFacadeBean;

    private final MetricsService metricsService;

    private final ContentParsers contentParsers;

    public SoapServlet(StillingBatchFacadeBean stillingBatchFacadeBean, MetricsService metricsService) {

        this.stillingBatchFacadeBean = stillingBatchFacadeBean;
        this.metricsService = metricsService;
        this.contentParsers = new ContentParsers();
    }

    @Override
    @PostMapping("/xmlstilling/SixSoap")
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        SoapServletResponse soapSvar;
        String eksterntBrukerNavn = req.getUserPrincipal().getName();

        byte[] soapRequestBytes = req.getInputStream().readAllBytes();
        detekterTegnsett(soapRequestBytes);
        String stillingXml = contentParsers.forCompany(eksterntBrukerNavn).parse(soapRequestBytes);

        if (stillingXml.trim().length() == 0) {
            logger.info("stillingxml fra " + eksterntBrukerNavn + " var tom streng! contentType = " + req.getContentType() + ", characterEncoding " + req.getCharacterEncoding());
            soapSvar = getResponseMessage(false);
        } else {
            XMLValidatorHelper xmlValidatorHelper = new XMLValidatorHelper();
            boolean xmlIsWellFormed = xmlValidatorHelper.isWellFormed(stillingXml);
            logger.debug("xml fra bruker: " + eksterntBrukerNavn + " iswellformed: " + xmlIsWellFormed + " contentType = " + req.getContentType() + ", characterEncoding " + req.getCharacterEncoding());
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

    String detekterTegnsett(byte[] tegn) {
        String detectedCharset = null;
        String defaultCharset = "UTF-8";
        if (tegn != null && tegn.length > 0) {
            UniversalDetector detector = new UniversalDetector();
            detector.handleData(tegn);
            detector.dataEnd();
            detectedCharset = detector.getDetectedCharset();
            if ("WINDOWS-1252".equals(detectedCharset)) {
                detectedCharset = "ISO-8859-1";
            }
        }

        if (detectedCharset == null) {
            logger.info("Greide ikke å detektere tegnsett i SOAP-request, bruker default {}.", defaultCharset);
            return defaultCharset;
        } else {
            logger.info("Detekterte at tegnsett {} er brukt i SOAP-request", detectedCharset);
            return detectedCharset;
        }
    }

    // Returnerer SoapServletResponse-objektet som gjelder for den aktuelle SoapServlet-versjonen.
    protected abstract SoapServletResponse getResponseMessage(boolean xmlIsWellFormed);

    private void opprettOgProsesserStillingbatch(String stillingXml, String eksterntBrukerNavn, boolean xmlIsWellFormed) {
        StillingBatchVO stillingBatchVO = new StillingBatchVO();
        stillingBatchVO.setArbeidsgiver(parseValue(stillingXml, "//EntityName"));
        stillingBatchVO.setEksternBrukerRef(eksterntBrukerNavn);
        stillingBatchVO.setStillingXml(stillingXml);
        stillingBatchVO.setMottattDato(new java.sql.Timestamp(System.currentTimeMillis()));
        stillingBatchVO.setBehandletDato(null);
        stillingBatchVO.setBehandletStatus(xmlIsWellFormed ? BEHANDLET_STATUS_OK_UBEHANDLET_0 : BEHANDLET_STATUS_FEILET_INVALID_1);
        stillingBatchVO.setEksternId(parseValue(stillingXml, "//Id/IdValue"));
        stillingBatchFacadeBean.insertStillingBatch(stillingBatchVO);
    }

    private String parseValue(String xml, String xpath) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
            return (String) XPathFactory.newInstance().newXPath().compile(xpath).evaluate(doc, XPathConstants.STRING);
        } catch (Exception e) {
            logger.warn("Feil oppsto ved uthenting av verdi vha xpath " + xpath, e);
            return "";
        }
    }
}
