package no.nav.xmlstilling.ws.common.vo;

import java.math.BigDecimal;
import java.sql.Timestamp;

@SuppressWarnings("PMD.UnusedPrivateField")
public class StillingBatchVO {
    private BigDecimal stillingBatchId;
    private String eksternBrukerRef;
    private String stillingXml;
    private Timestamp mottattDato;
    private Timestamp behandletDato;
    private String behandletStatus;
    private String arbeidsgiver;
    private String eksternId;

    public BigDecimal getStillingBatchId() {
        return stillingBatchId;
    }

    public void setStillingBatchId(BigDecimal stillingBatchId) {
        this.stillingBatchId = stillingBatchId;
    }

    public String getEksternBrukerRef() {
        return eksternBrukerRef;
    }

    public void setEksternBrukerRef(String eksternBrukerRef) {
        this.eksternBrukerRef = eksternBrukerRef;
    }

    public String getStillingXml() {
        return stillingXml;
    }

    public void setStillingXml(String stillingXml) {
        this.stillingXml = stillingXml;
    }

    public Timestamp getMottattDato() {
        return mottattDato;
    }

    public void setMottattDato(Timestamp mottattDato) {
        this.mottattDato = mottattDato;
    }

    public Timestamp getBehandletDato() {
        return behandletDato;
    }

    public void setBehandletDato(Timestamp behandletDato) {
        this.behandletDato = behandletDato;
    }

    public String getBehandletStatus() {
        return behandletStatus;
    }

    public void setBehandletStatus(String behandletStatus) {
        this.behandletStatus = behandletStatus;
    }

    public String getArbeidsgiver() {
        return arbeidsgiver;
    }

    public void setArbeidsgiver(String arbeidsgiver) {
        this.arbeidsgiver = arbeidsgiver;
    }

    public String getEksternId() {
        return eksternId;
    }

    public void setEksternId(String eksternId) {
        this.eksternId = eksternId;
    }
}
