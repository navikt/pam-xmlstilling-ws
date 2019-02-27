package no.nav.xmlstilling.ws.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "STILLING_BATCH")
public class StillingBatchEntity {

    @Id
    @Column(name = "STILLING_BATCH_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stillingbatchseq")
    @SequenceGenerator(name = "stillingbatchseq", sequenceName = "STILLING_BATCH_SEQ", allocationSize = 1)
    private BigDecimal stillingBatchId;

    @Column(name = "EKSTERN_BRUKER_REF")
    private String eksternBrukerRef;

    @Column(name = "STILLING_XML")
    private String stillingXml;

    @Column(name = "MOTTATT_DATO")
    private Timestamp mottattDato;

    @Column(name = "BEHANDLET_DATO")
    private Timestamp behandletDato;

    @Column(name = "BEHANDLET_STATUS")
    private String behandletStatus;

    @Column(name = "ARBEIDSGIVER")
    private String arbeidsgiver;

    // Pga feil i Hibernate (Hibernate ORM / HHH-278) må Date brukes i deklarasjonen i stedet for Timestamp, ellers finner den ikke konstruktøren
    public StillingBatchEntity(BigDecimal stillingBatchId, String eksternBrukerRef, String stillingXml, Date mottattDato, Date behandletDato, String behandletStatus, String arbeidsgiver) {
        this.stillingBatchId = stillingBatchId;
        this.eksternBrukerRef = eksternBrukerRef;
        this.stillingXml = stillingXml;
        this.mottattDato = (Timestamp) mottattDato;
        this.behandletDato = (Timestamp) behandletDato;
        this.behandletStatus = behandletStatus;
        this.arbeidsgiver = arbeidsgiver;
    }

    public StillingBatchEntity() { }

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
}
