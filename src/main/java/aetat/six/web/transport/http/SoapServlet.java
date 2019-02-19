package aetat.six.web.transport.http;

import aetat.six.common.util.ConverterUtils;
import aetat.six.common.util.XMLValidatorHelper;
import aetat.six.common.vo.KallLoggVO;
import aetat.six.common.vo.StillingBatchVO;
import aetat.six.service.facade.KallLoggFacadeBean;
import aetat.six.service.facade.StillingBatchFacadeBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * SoapServlet - egen servlet for hoyvolum sendinger
 */
public abstract class SoapServlet extends HttpServlet {

    private static final String STJERNE = "*";
    private static final String RETNING_INN = "INN";
    private static final String TEKST_OK = "OK";
    private static final String WSDL_NAVN = "six_sokeprofil_adm.wsdl";
    private static final String KALL_NAVN = "LeggInnStillinger";
    private static final String HIGH_CAPACITY_MESSAGE = "Høykapasitetsmelding";
    private static final String CONTENT_TYPE_TEXT_XML = "text/xml";
    private static final String INITIATED_MESSAGE = " initiated";
    public static final String BEHANDLET_STATUS_OK_UBEHANDLET_0 = "0";
    public static final String BEHANDLET_STATUS_FEILET_INVALID_1 = "-1";

    private static transient Logger logger = LoggerFactory.getLogger(SoapServlet.class);

    private StillingBatchFacadeBean stillingBatchFacadeBean;
    private KallLoggFacadeBean kallLoggFacadeBean;

    public void setStillingBatchFacadeBean(StillingBatchFacadeBean stillingBatchFacadeBean) {
        this.stillingBatchFacadeBean = stillingBatchFacadeBean;
    }

    public void setKallLoggFacadeBean(KallLoggFacadeBean kallLoggFacadeBean) {
        this.kallLoggFacadeBean = kallLoggFacadeBean;
    }

    @Override
    public void init() throws javax.servlet.ServletException {
        logger.debug(this.getClass().getName() + INITIATED_MESSAGE);

        if (stillingBatchFacadeBean == null) {
            setStillingBatchFacadeBean(StillingBatchFacadeBean.getInstance());
        }

        if (kallLoggFacadeBean == null) {
            setKallLoggFacadeBean(KallLoggFacadeBean.getInstance());
        }
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long start = System.currentTimeMillis();

        BufferedReader br = req.getReader();
        String stillingXml = ConverterUtils.read(br);

        String soapSvar;
        if (stillingXml.trim().length() == 0) {
            logger.info("stillingxml var tom streng!");
            soapSvar = getResponseMessage(false);

        } else {
            String eksterntBrukerNavn = req.getUserPrincipal().getName();

            XMLValidatorHelper xmlValidatorHelper = new XMLValidatorHelper();
            boolean xmlIsWellFormed = xmlValidatorHelper.isWellFormed(stillingXml);
            logger.debug("xml fra bruker: " + eksterntBrukerNavn + " iswellformed: " + xmlIsWellFormed);
            logger.debug("Stillingxml: \n" + stillingXml);
            opprettOgProsesserStillingbatch(stillingXml, eksterntBrukerNavn, xmlIsWellFormed);
            soapSvar = getResponseMessage(xmlIsWellFormed);
        }

        resp.setContentType(CONTENT_TYPE_TEXT_XML);
        PrintWriter out = resp.getWriter();
        out.write(soapSvar);
        out.flush();
        out.close();

        long slutt = System.currentTimeMillis();

        loggTilKallLogg(stillingXml, soapSvar, slutt - start);
    }

    // Returnerer SoapServletResponse-objektet som gjelder for den aktuelle SoapServlet-versjonen.
    protected abstract String getResponseMessage(boolean xmlIsWellFormed);

    private void opprettOgProsesserStillingbatch(String stillingXml, String eksterntBrukerNavn, boolean xmlIsWellFormed) {

        //Oppretter stillingbatchen
        StillingBatchVO stillingBatchVO = new StillingBatchVO();
        stillingBatchVO.setArbeidsgiver(finnInnholdMellomTag(stillingXml));
        stillingBatchVO.setEksternBrukerRef(eksterntBrukerNavn);
        stillingBatchVO.setStillingXml(stillingXml);
        stillingBatchVO.setMottattDato(new java.sql.Timestamp(System.currentTimeMillis()));
        stillingBatchVO.setBehandletDato(null);
        stillingBatchVO.setBehandletStatus(xmlIsWellFormed ? BEHANDLET_STATUS_OK_UBEHANDLET_0 : BEHANDLET_STATUS_FEILET_INVALID_1);

        // Skriver posten til databasen
        stillingBatchVO = stillingBatchFacadeBean.insertStillingBatch(stillingBatchVO);
    }

    private void loggTilKallLogg(String meldingInn, String meldingUt, long processingTime) {
        KallLoggVO kallLoggVO = new KallLoggVO();

        kallLoggVO.setKallRetning(RETNING_INN);
        kallLoggVO.setKorrelasjonsId(STJERNE);
        kallLoggVO.setWsdlNavn(WSDL_NAVN);
        kallLoggVO.setResultatkode(TEKST_OK);
        kallLoggVO.setResultatbeskjed(HIGH_CAPACITY_MESSAGE);
        kallLoggVO.setKallNavn(KALL_NAVN);
        kallLoggVO.setBehandlingstid(new java.math.BigDecimal(processingTime));
        kallLoggVO.setTimeStampMottatt(new java.sql.Timestamp(System.currentTimeMillis()));
        kallLoggVO.setMeldingInn(meldingInn);
        kallLoggVO.setMeldingUt(meldingUt);

        kallLoggVO = kallLoggFacadeBean.insertKallLogg(kallLoggVO);
        if (null != kallLoggVO) {
            logger.debug("--------------------------> kallLoggVO -> Ny ident = (" + kallLoggVO.getLoggId() + ")");
        }
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
