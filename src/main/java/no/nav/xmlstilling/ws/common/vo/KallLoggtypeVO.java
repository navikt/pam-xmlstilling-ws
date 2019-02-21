package no.nav.xmlstilling.ws.common.vo;

import aetat.arch.vo.AbstractValueObject;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@SuppressWarnings("PMD.UnusedPrivateField")
public class KallLoggtypeVO extends AbstractValueObject {
    private BigDecimal kallLoggTypeId;
    private String kallNavn;
    private String kallretning;
    private String synkroniseringsType;
    private String loggNivaa;
}
