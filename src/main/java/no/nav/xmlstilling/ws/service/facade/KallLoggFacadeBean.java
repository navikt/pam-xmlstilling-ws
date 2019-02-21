package no.nav.xmlstilling.ws.service.facade;

import no.nav.sbl.entity.six.KallLoggTypeEntity;
import no.nav.xmlstilling.ws.common.vo.KallLoggVO;
import no.nav.xmlstilling.ws.common.vo.KallLoggtypeVO;
import no.nav.xmlstilling.ws.dao.KallLoggDaoJpa;
import no.nav.xmlstilling.ws.dao.KallLoggTypeDaoJpa;
import no.nav.xmlstilling.ws.dao.ValueObjectConverter;
import no.nav.xmlstilling.ws.exception.SystemException;
import no.nav.xmlstilling.ws.spring.SpringContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * KallLoggFacadeBean
 *
 * @author VHA
 * @version 1.0
 */
@Component
@Transactional
public class KallLoggFacadeBean {

    @Autowired
    private KallLoggDaoJpa kallLoggDaoJpa;

    @Autowired
    private KallLoggTypeDaoJpa kallLoggTypeDaoJpa;

    public static KallLoggFacadeBean getInstance() {
        return SpringContextProvider.getContext().getBean("kallLoggFacadeBean", KallLoggFacadeBean.class);
    }

    public KallLoggVO insertKallLogg(KallLoggVO kallLoggVO) {
//        if (kallLoggVO == null) {
//            throw new IllegalArgumentException(this.getClass().getName() + ".insertKalllogg: kallLoggVO er lik null");
//        }
//
//        String kallnavn = kallLoggVO.getKallNavn();
//
//        try {
//            KallLoggtypeVO kallLoggtypeVO = getKallLoggType(kallnavn);
//
//            if (kallLoggtypeVO != null) {
//                // Loggniv� er en av f�lgende: 'ALLE', 'INGEN', 'FEIL', 'SYSFEIL'
//                if (kallLoggtypeVO.getLoggNivaa().equalsIgnoreCase("INGEN")) {
//                    return null;
//                }
//            }
//
//            KallLoggEntity kallLoggEntity = ValueObjectConverter.getKallLoggEntityForValueObject(kallLoggVO);
//            kallLoggDaoJpa.create(kallLoggEntity);
//
//        } catch (SystemException se) {
//            throw new SystemException(this.getClass().getName() + ",insertKallLogg: Fant ikke loggniv� for meldingstype [" + kallnavn + "]", se);
//        }

        return kallLoggVO;
    }

    /**
     * Metode som henter fra loggniv�et
     *
     * @param kallnavn String
     * @return KallLoggtypeVO
     */
    private KallLoggtypeVO getKallLoggType(String kallnavn) {
        KallLoggtypeVO kallLoggtypeVO = null;

        KallLoggTypeEntity kallLoggTypeEntity;

        kallLoggTypeEntity = kallLoggTypeDaoJpa.findByKallnavn(kallnavn);
        if (kallLoggTypeEntity != null) {
            // Hent de nye verdiene
            kallLoggtypeVO = ValueObjectConverter.getKallLoggTypeValueObject(kallLoggTypeEntity);
        } else {
            throw new SystemException("getKallLoggtype: FinderException ");
        }

        return kallLoggtypeVO;
    }

}
