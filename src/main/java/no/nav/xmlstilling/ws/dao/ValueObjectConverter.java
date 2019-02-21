package no.nav.xmlstilling.ws.dao;

import no.nav.sbl.entity.six.EksternBrukerEntity;
import no.nav.sbl.entity.six.KallLoggEntity;
import no.nav.sbl.entity.six.KallLoggTypeEntity;
import no.nav.sbl.entity.six.StillingBatchEntity;
import no.nav.xmlstilling.ws.common.vo.EksternBrukerVO;
import no.nav.xmlstilling.ws.common.vo.KallLoggVO;
import no.nav.xmlstilling.ws.common.vo.KallLoggtypeVO;
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

    public static KallLoggVO getKallLoggValueObject(KallLoggEntity kallLoggEntity) {
        KallLoggVO kallLoggVO = new KallLoggVO();
        copyProperties(kallLoggEntity, kallLoggVO);
        return kallLoggVO;
    }

    public static KallLoggEntity getKallLoggEntityForValueObject(KallLoggVO kallLoggVO) {
        KallLoggEntity kallLoggEntity = new KallLoggEntity();
        if (kallLoggVO != null) {
            kallLoggEntity.setLoggId(kallLoggVO.getLoggId());
            kallLoggEntity.setKorrelasjonsId(checkAndAdjustLength(KallLoggVO.class.getName(), "korrelasjonsId", kallLoggVO.getKorrelasjonsId(), 100));
            kallLoggEntity.setWsdlNavn(checkAndAdjustLength(KallLoggVO.class.getName(), "wsdlNavn", kallLoggVO.getWsdlNavn(), 30));
            kallLoggEntity.setKallNavn(checkAndAdjustLength(KallLoggVO.class.getName(), "kallNavn", kallLoggVO.getKallNavn(), 40));
            kallLoggEntity.setKallRetning(checkAndAdjustLength(KallLoggVO.class.getName(), "kallRetning", kallLoggVO.getKallRetning(), 30));
            kallLoggEntity.setTimeStampMottatt(kallLoggVO.getTimeStampMottatt());
            kallLoggEntity.setTimestampEkstern(kallLoggVO.getTimestampEkstern());
            kallLoggEntity.setResultatkode(checkAndAdjustLength(KallLoggVO.class.getName(), "resultatKode", kallLoggVO.getResultatkode(), 30));
            kallLoggEntity.setResultatbeskjed(checkAndAdjustLength(KallLoggVO.class.getName(), "resultatBeskjed", kallLoggVO.getResultatbeskjed(), 4000));
            kallLoggEntity.setBehandlingstid(kallLoggVO.getBehandlingstid());
            kallLoggEntity.setPlsqltid(kallLoggVO.getPlsqltid());
            kallLoggEntity.setMeldingInn(kallLoggVO.getMeldingInn());
            kallLoggEntity.setMeldingUt(checkAndAdjustLength(KallLoggVO.class.getName(), "meldingUt", kallLoggVO.getMeldingUt(), 4000));
        }
        return kallLoggEntity;
    }


    public static KallLoggtypeVO getKallLoggTypeValueObject(KallLoggTypeEntity kallLoggTypeEntity) {
        KallLoggtypeVO kallLoggtypeVO = new KallLoggtypeVO();
        copyProperties(kallLoggTypeEntity, kallLoggtypeVO);
        return kallLoggtypeVO;
    }

    public static KallLoggTypeEntity getKallLoggTypeEntityForValueObject(KallLoggtypeVO kallLoggtypeVO) {
        KallLoggTypeEntity kallLoggTypeEntity = new KallLoggTypeEntity();
        if (kallLoggtypeVO != null) {
            kallLoggTypeEntity.setKallLoggTypeId(kallLoggtypeVO.getKallLoggTypeId());
            kallLoggTypeEntity.setKallNavn(checkAndAdjustLength(KallLoggtypeVO.class.getName(), "kallNavn", kallLoggtypeVO.getKallNavn(), 30));
            kallLoggTypeEntity.setKallretning(checkAndAdjustLength(KallLoggtypeVO.class.getName(), "kallretning", kallLoggtypeVO.getKallretning(), 10));
            kallLoggTypeEntity.setSynkroniseringsType(checkAndAdjustLength(KallLoggtypeVO.class.getName(), "synkroniseringType", kallLoggtypeVO.getSynkroniseringsType(), 10));
            kallLoggTypeEntity.setLoggNivaa(checkAndAdjustLength(KallLoggtypeVO.class.getName(), "loggNivaa", kallLoggtypeVO.getLoggNivaa(), 10));
        }
        return kallLoggTypeEntity;
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
