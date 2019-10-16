package no.nav.xmlstilling.ws.web.transport.http;

import no.nav.xmlstilling.ws.common.util.XMLValidatorHelper;
import no.nav.xmlstilling.ws.common.vo.StillingBatchVO;
import no.nav.xmlstilling.ws.service.MetricsService;
import no.nav.xmlstilling.ws.service.facade.StillingBatchFacadeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
    @PostMapping("/xmlstilling/SixSoap")
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        SoapServletResponse soapSvar;
        String eksterntBrukerNavn = req.getUserPrincipal().getName();

        String stillingXml = parseStrategy(eksterntBrukerNavn).parse(req.getInputStream().readAllBytes());

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

    public interface ContentParseStrategy {

            String parse(byte[] content);

            ContentParseStrategy UTF_8_PARSER = content -> new String(content, StandardCharsets.UTF_8);
            ContentParseStrategy ISO_8859_1_PARSER = content -> new String(content, StandardCharsets.ISO_8859_1);

    }
    private static List<String> UTF_8_USERS = Arrays.asList("karriereno", "mynetwork", "stepstone", "webcruiter");
    private static List<String> ISO_8859_1_USERS = Arrays.asList("globesoft", "hrmanager", "jobbnorge");


    private ContentParseStrategy parseStrategy(String eksterntBrukerNavn) {

        if(UTF_8_USERS.contains(eksterntBrukerNavn)) {
            logger.info("Using UTF_8_PARSER for " + eksterntBrukerNavn);
            return ContentParseStrategy.UTF_8_PARSER;
        }
        if(ISO_8859_1_USERS.contains(eksterntBrukerNavn)) {
            logger.info("Using ISO_8859_1_PARSER for " + eksterntBrukerNavn);
            return ContentParseStrategy.ISO_8859_1_PARSER;
        }

        logger.info("Using UTF_8_PARSER for unknown user, " + eksterntBrukerNavn);
        return ContentParseStrategy.UTF_8_PARSER;
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
