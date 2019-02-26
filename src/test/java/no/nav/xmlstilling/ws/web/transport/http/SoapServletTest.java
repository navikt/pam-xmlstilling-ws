package no.nav.xmlstilling.ws.web.transport.http;

import no.nav.xmlstilling.ws.common.vo.StillingBatchVO;
import no.nav.xmlstilling.ws.service.facade.StillingBatchFacadeBean;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.security.Principal;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class SoapServletTest {

    private SoapServlet soapServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;
    private StillingBatchFacadeBean stillingBatchFacadeBean;

    private String xmlStub = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<soapenv:Envelope " +
            "   xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "   xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
            "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
            "   <soapenv:Body>" +
            "      <LeggInnStillinger xmlns=\"\">" +
            "      </LeggInnStillinger>" +
            "   </soapenv:Body>" +
            "</soapenv:Envelope>";

    @Before
    public void setUp() throws IOException, ServletException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
        stillingBatchFacadeBean = mock(StillingBatchFacadeBean.class);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(xmlStub)));
        when(request.getUserPrincipal()).thenReturn(mock(Principal.class));
        when(response.getWriter()).thenReturn(writer);

        soapServlet = new SoapServletV2();
        soapServlet.setStillingBatchFacadeBean(stillingBatchFacadeBean);
        soapServlet.init();
    }

    @Test
    public void soapServletVedGyldigRequest() throws ServletException, IOException {
        soapServlet.service(request, response);

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(writer).write(argument.capture());

        assertThat(argument.getValue(), containsString("MOTTATT"));
    }

    @Test
    public void soapServletNaarRequestInneholderTomXML() throws ServletException, IOException {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("")));
        soapServlet.service(request, response);

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(writer).write(argument.capture());

        assertThat(argument.getValue(), containsString("IKKE_VALIDERT"));
    }

    @Test
    public void soapServletV1Respons() throws ServletException, IOException {
        soapServlet = new SoapServletV1();
        soapServlet.setStillingBatchFacadeBean(stillingBatchFacadeBean);
        soapServlet.init();

        soapServlet.service(request, response);

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(writer).write(argument.capture());

        assertThat(argument.getValue(), containsString("OK_DUMMY"));
        assertThat(argument.getValue(), not(containsString("true")));
    }

    @Test
    public void soapServletV2Respons() throws ServletException, IOException {
        soapServlet.service(request, response);

        ArgumentCaptor<String> argument = ArgumentCaptor.forClass(String.class);
        verify(writer).write(argument.capture());

        assertThat(argument.getValue(), containsString("true"));
        assertThat(argument.getValue(), not(containsString("OK_DUMMY")));
    }

    @Test
    public void soapServletOppretterOgProsessererStillingBatch() throws ServletException, IOException {
        soapServlet.service(request, response);

        ArgumentCaptor<StillingBatchVO> argument = ArgumentCaptor.forClass(StillingBatchVO.class);
        verify(stillingBatchFacadeBean).insertStillingBatch(argument.capture());
        assertEquals("0", argument.getValue().getBehandletStatus());

    }

}
