package aetat.six.web.transport.http;

import java.text.MessageFormat;

// Inneholder verdier og bygger responsen som sendes tilbake til klienten når en ny stilling legges inn.
public class SoapServletResponse {

    private SoapServletResponse() {}

    public static final String RESPONSE_V1_OK = createResponseMessage(true, "OK_DUMMY");
    public static final String RESPONSE_V1_ERROR = createResponseMessage(false, "OK_DUMMY");

    public static final String RESPONSE_V2_OK = createResponseMessage(true, "true");
    public static final String RESPONSE_V2_ERROR = createResponseMessage(false, "true");

    private static final String MOTTATT = "MOTTATT";
    private static final String IKKE_VALIDERT = "IKKE_VALIDERT";
    private static final String KLIENT_REFERANSE_DUMMY = "KLIENT_DUMMY";
    private static final String BEHANDLET_STATUS_OK_UBEHANDLET = "0";
    private static final String BEHANDLET_STATUS_FEILET_INVALID = "-1";

    private static final String SVAR_TEMPLATE =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<soapenv:Envelope " +
                    "   xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                    "   xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
                    "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                    "   <soapenv:Body>" +
                    "      <LeggInnStillingerResponse xmlns=\"\">" +
                    "         <Id>" +
                    "            <ns1:KlientReferanse xmlns:ns1=\"http://www.nav.no/nav_stilling_typer.xsd\">{0}</ns1:KlientReferanse>" +
                    "            <ns2:EksekveringOK xmlns:ns2=\"http://www.nav.no/nav_stilling_typer.xsd\">{1}</ns2:EksekveringOK>" +
                    "            <ns3:Feilkode xmlns:ns3=\"http://www.nav.no/nav_stilling_typer.xsd\">{2}</ns3:Feilkode>" +
                    "            <ns4:Feilmelding xmlns:ns4=\"http://www.nav.no/nav_stilling_typer.xsd\">{3}</ns4:Feilmelding>" +
                    "         </Id>" +
                    "      </LeggInnStillingerResponse>" +
                    "   </soapenv:Body>" +
                    "</soapenv:Envelope>";

    private static String createResponseMessage(boolean xmlIsWellFormed, String eksekveringOKDummy) {
        Object[] parametre = new Object[]{KLIENT_REFERANSE_DUMMY, eksekveringOKDummy, BEHANDLET_STATUS_OK_UBEHANDLET, MOTTATT};
        if (!xmlIsWellFormed) {
            parametre[2] = BEHANDLET_STATUS_FEILET_INVALID;
            parametre[3] = IKKE_VALIDERT;
        }
        return MessageFormat.format(SVAR_TEMPLATE, parametre);
    }
}
