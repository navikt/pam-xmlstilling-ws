package no.nav.xmlstilling.ws.dao;

import no.nav.xmlstilling.ws.common.vo.StillingBatchVO;
import no.nav.xmlstilling.ws.entity.StillingBatchEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.beans.BeanUtils.copyProperties;

public class ValueObjectConverter {
    private static final Logger LOG = LoggerFactory.getLogger(ValueObjectConverter.class);

    public static StillingBatchVO getStillingBatchValueObject(StillingBatchEntity stillingBatchEntity) {
        StillingBatchVO stillingBatchVO = new StillingBatchVO();
        copyProperties(stillingBatchEntity, stillingBatchVO);
        return stillingBatchVO;
    }

    public static StillingBatchEntity getStillingBatchEntityForValueObject(StillingBatchVO stillingBatchVO) {
        StillingBatchEntity stillingBatchEntity = new StillingBatchEntity();
        if (stillingBatchVO != null) {
            stillingBatchEntity.setStillingBatchId(stillingBatchVO.getStillingBatchId());
            stillingBatchEntity.setEksternBrukerRef(checkAndAdjustLength(StillingBatchVO.class.getName(), "eksternBrukerRef", stillingBatchVO.getEksternBrukerRef(), 150));
            stillingBatchEntity.setStillingXml(stillingBatchVO.getStillingXml());
            stillingBatchEntity.setMottattDato(stillingBatchVO.getMottattDato());
            stillingBatchEntity.setBehandletDato(stillingBatchVO.getBehandletDato());
            stillingBatchEntity.setBehandletStatus(checkAndAdjustLength(StillingBatchVO.class.getName(), "behandletStatus", stillingBatchVO.getBehandletStatus(), 3));
            stillingBatchEntity.setArbeidsgiver(stillingBatchVO.getArbeidsgiver());
        }

        return stillingBatchEntity;
    }

    private static String checkAndAdjustLength(String entitetnavn, String feltnavn, String tekst, int maxLength) {
        String resValue = tekst;

        if (tekst != null) {
            if (tekst.length() > maxLength) {
                resValue = tekst.substring(0, maxLength);
                LOG.warn("I entiteten [" + entitetnavn + "] er verdien i feltet [" + feltnavn + "] for stor -> trunkeres (lengden skal være maks [" + maxLength + "] tegn).");
            }
        }
        return resValue;
    }
}
