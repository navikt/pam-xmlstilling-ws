package no.nav.xmlstilling.ws.dao;

import no.nav.sbl.entity.six.EksternBrukerEntity;
import no.nav.sbl.entity.six.StillingBatchEntity;
import no.nav.xmlstilling.ws.common.vo.EksternBrukerVO;
import no.nav.xmlstilling.ws.common.vo.StillingBatchVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.beans.BeanUtils.copyProperties;

public class ValueObjectConverter {
    private static final Logger LOG = LoggerFactory.getLogger(ValueObjectConverter.class);

    public static EksternBrukerEntity setEksternBrukeEntityForValueObject(EksternBrukerVO eksternBrukerVO) {
        EksternBrukerEntity eksternBrukerEntity = new EksternBrukerEntity();
        eksternBrukerEntity.setInternBrukerId( eksternBrukerVO.getInternBrukerId() );
        eksternBrukerEntity.setEksternBrukerRef(checkAndAdjustLength(EksternBrukerVO.class.getName(), "eksternBrukerRef", eksternBrukerVO.getEksternBrukerRef(), 150));
        eksternBrukerEntity.setAdresseLinje1(checkAndAdjustLength(EksternBrukerVO.class.getName(), "adresseLinje1", eksternBrukerVO.getAdresseLinje1(), 100));
        eksternBrukerEntity.setAdresseLinje2(checkAndAdjustLength(EksternBrukerVO.class.getName(), "adresseLinje2", eksternBrukerVO.getAdresseLinje2(), 100));
        eksternBrukerEntity.setPostNr(checkAndAdjustLength(EksternBrukerVO.class.getName(), "postNr", eksternBrukerVO.getPostNr(), 10));
        eksternBrukerEntity.setPostSted(checkAndAdjustLength(EksternBrukerVO.class.getName(), "postSted", eksternBrukerVO.getPostSted(), 30));
        eksternBrukerEntity.setEmailAdresse(checkAndAdjustLength(EksternBrukerVO.class.getName(), "emailAdresse", eksternBrukerVO.getEmailAdresse(), 50));
        eksternBrukerEntity.setEmailMimeType(checkAndAdjustLength(EksternBrukerVO.class.getName(), "emailMimeType", eksternBrukerVO.getEmailMimeType(), 20));
        eksternBrukerEntity.setFastTlf(checkAndAdjustLength(EksternBrukerVO.class.getName(), "fastTlf", eksternBrukerVO.getFastTlf(), 20));
        eksternBrukerEntity.setMobilTlf(checkAndAdjustLength(EksternBrukerVO.class.getName(), "mobilTlfNr", eksternBrukerVO.getMobilTlf(), 20));
        eksternBrukerEntity.setKontaktPerson(checkAndAdjustLength(EksternBrukerVO.class.getName(), "kontaktPerson", eksternBrukerVO.getKontaktPerson(), 50));
        eksternBrukerEntity.setOpprettetDato(eksternBrukerVO.getOpprettetDato());
        eksternBrukerEntity.setEndretDato(eksternBrukerVO.getEndretDato());
        eksternBrukerEntity.setBrukerStatus(checkAndAdjustLength(EksternBrukerVO.class.getName(), "brukerStatus", eksternBrukerVO.getBrukerStatus(), 1));
        eksternBrukerEntity.setXslTemplateId(eksternBrukerVO.getXslTemplateId());
        return eksternBrukerEntity;
    }

    public static EksternBrukerVO getEksternBrukerValueObject(EksternBrukerEntity eksternBrukerEntity) {
        EksternBrukerVO eksternBrukerVO = new EksternBrukerVO();

        if (eksternBrukerEntity != null) {
            copyProperties(eksternBrukerEntity, eksternBrukerVO);
        }

        return eksternBrukerVO;
    }

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
