package no.nav.xmlstilling.ws.common.vo;

import aetat.arch.vo.AbstractValueObject;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@SuppressWarnings("PMD.UnusedPrivateField")
public class EksternBrukerVO extends AbstractValueObject {
    private BigDecimal internBrukerId;
    private String eksternBrukerRef;
    private String adresseLinje1;
    private String adresseLinje2;
    private String postNr;
    private String postSted;
    private String emailAdresse;
    private String emailMimeType;
    private String fastTlf;
    private String mobilTlf;
    private String kontaktPerson;
    private Timestamp opprettetDato;
    private Timestamp endretDato;
    private String brukerStatus;
    private BigDecimal xslTemplateId;
}
