package no.nav.xmlstilling.ws.common.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@SuppressWarnings("PMD.UnusedPrivateField")
public class StillingBatchVO {
    private BigDecimal stillingBatchId;
    private String eksternBrukerRef;
    private String stillingXml;
    private Timestamp mottattDato;
    private Timestamp behandletDato;
    private String behandletStatus;
    private String arbeidsgiver;
}
