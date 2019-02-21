package no.nav.xmlstilling.ws.common.vo;

import aetat.arch.vo.AbstractValueObject;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@SuppressWarnings("PMD.UnusedPrivateField")
public class KallLoggVO extends AbstractValueObject {
    private BigDecimal loggId;
    private String korrelasjonsId;
    private String wsdlNavn;
    private String kallNavn;
    private String kallRetning;
    private Timestamp timeStampMottatt;
    private Timestamp timestampEkstern;
    private String resultatkode;
    private String resultatbeskjed;
    private BigDecimal behandlingstid;
    private BigDecimal plsqltid;
    private String meldingInn;
    private String meldingUt;
}
