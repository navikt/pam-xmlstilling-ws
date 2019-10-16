package no.nav.xmlstilling.ws.web.transport.http;

import no.nav.xmlstilling.ws.DevApplication;
import no.nav.xmlstilling.ws.common.vo.StillingBatchVO;
import no.nav.xmlstilling.ws.service.MetricsService;
import no.nav.xmlstilling.ws.service.facade.StillingBatchFacadeBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@MockBean({MetricsService.class})
@ActiveProfiles("test")
public class SoapServletTest {

    @Autowired
    private TestRestTemplate template;

    @LocalServerPort
    int randomPort;

    @MockBean
    private StillingBatchFacadeBean stillingBatchFacadeBean;

    private String xmlStub = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<soapenv:Envelope " +
            "   xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
            "   xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
            "   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
            "   <soapenv:Body>" +
            "      <LeggInnStillinger xmlns=\"\">" +
            "         <StillingListe>" +
            "            <m0:PositionOpening>" +
            "               <PositionPostings>" +
            "                  <PositionPosting>" +
            "                     <Id>" +
            "                        <IdValue>1234</IdValue>" +
            "                     </Id>" +
            "                  </PositionPosting>" +
            "               </PositionPostings>" +
            "               <PositionSupplier>" +
            "                  <SupplierId>" +
            "                     <IdValue>TestÆØÅæøå</IdValue>"+
            "                  </SupplierId>" +
            "                  <EntityName>Test AS - Æ Ø Å æ ø å</EntityName>" +
            "               </PositionSupplier>" +
            "            </m0:PositionOpening>" +
            "         </StillingListe>" +
            "      </LeggInnStillinger>" +
            "   </soapenv:Body>" +
            "</soapenv:Envelope>";

    @Test
    public void soapServletVedGyldigRequest() {

        ResponseEntity<String> response = template
                .withBasicAuth("brukerA", "pwdA")
                .postForEntity(rootUrl("/xmlstilling/SixSoap"), xmlStub, String.class);
        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody(), containsString("MOTTATT"));

    }

    @Test
    public void soapServletVedGyldigRequestMedIso8859_1content() {

        template.withBasicAuth("globesoft", "pwd")
                .postForEntity(rootUrl("/xmlstilling/SixSoap"), xmlStub.getBytes(StandardCharsets.ISO_8859_1), String.class);

        ArgumentCaptor<StillingBatchVO> argument = ArgumentCaptor.forClass(StillingBatchVO.class);
        verify(stillingBatchFacadeBean).insertStillingBatch(argument.capture());
        assertEquals(xmlStub, argument.getValue().getStillingXml());

    }

    @Test
    public void soapServletVedGyldigRequestMedUtf_8content() {

        template.withBasicAuth("webcruiter", "pwd")
                .postForEntity(rootUrl("/xmlstilling/SixSoap"), xmlStub.getBytes(StandardCharsets.UTF_8), String.class);

        ArgumentCaptor<StillingBatchVO> argument = ArgumentCaptor.forClass(StillingBatchVO.class);
        verify(stillingBatchFacadeBean).insertStillingBatch(argument.capture());
        assertEquals(xmlStub, argument.getValue().getStillingXml());

    }

    @Test
    public void soapServletNaarRequestInneholderTomXML() {

        ResponseEntity<String> response = template
                .withBasicAuth("brukerA", "pwdA")
                .postForEntity(rootUrl("/xmlstilling/SixSoap"), "", String.class);
        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody(), containsString("IKKE_VALIDERT"));

    }

    @Test
    public void soapServletV1Respons() {

        ResponseEntity<String> response = template
                .withBasicAuth("brukerA", "pwdA")
                .postForEntity(rootUrl("/xmlstilling/SixSoap"), xmlStub, String.class);
        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody(), containsString("OK_DUMMY"));
        assertThat(response.getBody(), not(containsString("true")));

    }

    @Test
    public void soapServletV2Respons() {

        ResponseEntity<String> response = template
                .withBasicAuth("brukerA", "pwdA")
                .postForEntity(rootUrl("/v2/xmlstilling/SixSoap"), xmlStub, String.class);
        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody(), not(containsString("OK_DUMMY")));
        assertThat(response.getBody(), containsString("true"));

    }

    @Test
    public void soapServletOppretterOgProsessererStillingBatch() {
        template.withBasicAuth("brukerA", "pwdA")
                .postForEntity(rootUrl("/xmlstilling/SixSoap"), xmlStub, String.class);

        ArgumentCaptor<StillingBatchVO> argument = ArgumentCaptor.forClass(StillingBatchVO.class);
        verify(stillingBatchFacadeBean).insertStillingBatch(argument.capture());
        assertEquals("0", argument.getValue().getBehandletStatus());
        assertEquals("1234", argument.getValue().getEksternId());

    }

    private String rootUrl(String context) {
        return "http://localhost:" + randomPort + context;
    }

}
