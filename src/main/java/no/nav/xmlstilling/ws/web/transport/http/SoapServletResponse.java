package no.nav.xmlstilling.ws.web.transport.http;

class SoapServletResponse {

    private boolean okResponse;
    private String message;

    private SoapServletResponse(String eksekveringOkTekst, boolean okResponse) {
        this.message = createResponseMessage(okResponse, eksekveringOkTekst);
        this.okResponse = okResponse;
    }

    static final SoapServletResponse RESPONSE_V1_OK =
            new SoapServletResponse("OK_DUMMY", true);
    static final SoapServletResponse RESPONSE_V1_ERROR =
            new SoapServletResponse("OK_DUMMY", false);
    static final SoapServletResponse RESPONSE_V2_OK =
            new SoapServletResponse("true", true);
    static final SoapServletResponse RESPONSE_V2_ERROR =
            new SoapServletResponse("true", false);

    private static final String MOTTATT = "MOTTATT";
    private static final String IKKE_VALIDERT = "IKKE_VALIDERT";
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
                    "            <ns1:KlientReferanse xmlns:ns1=\"http://www.nav.no/nav_stilling_typer.xsd\">KLIENT_DUMMY</ns1:KlientReferanse>" +
                    "            <ns2:EksekveringOK xmlns:ns2=\"http://www.nav.no/nav_stilling_typer.xsd\">%s</ns2:EksekveringOK>" +
                    "            <ns3:Feilkode xmlns:ns3=\"http://www.nav.no/nav_stilling_typer.xsd\">%s</ns3:Feilkode>" +
                    "            <ns4:Feilmelding xmlns:ns4=\"http://www.nav.no/nav_stilling_typer.xsd\">%s</ns4:Feilmelding>" +
                    "         </Id>" +
                    "      </LeggInnStillingerResponse>" +
                    "   </soapenv:Body>" +
                    "</soapenv:Envelope>";

    private static String createResponseMessage(boolean okResponse, String eksekveringOKDummy) {
        return String.format(
                SVAR_TEMPLATE,
                eksekveringOKDummy,
                okResponse ? BEHANDLET_STATUS_OK_UBEHANDLET : BEHANDLET_STATUS_FEILET_INVALID,
                okResponse ? MOTTATT : IKKE_VALIDERT);
    }

    String getMessage() {
        return message;
    }

    boolean isOkResponse() {
        return okResponse;
    }
}
