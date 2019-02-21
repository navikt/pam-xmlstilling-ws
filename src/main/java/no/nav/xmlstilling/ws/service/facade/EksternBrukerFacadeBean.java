package no.nav.xmlstilling.ws.service.facade;

import no.nav.sbl.entity.six.EksternBrukerEntity;
import no.nav.xmlstilling.ws.common.vo.EksternBrukerVO;
import no.nav.xmlstilling.ws.dao.EksternBrukerDaoJpa;
import no.nav.xmlstilling.ws.dao.ValueObjectConverter;
import no.nav.xmlstilling.ws.spring.SpringContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class EksternBrukerFacadeBean {

    @Autowired
    private EksternBrukerDaoJpa eksternBrukerDaoJpa;

    public static EksternBrukerFacadeBean getInstance() {
        return SpringContextProvider.getContext().getBean("eksternBrukerFacadeBean", EksternBrukerFacadeBean.class);
    }

    public EksternBrukerVO findByEksternbrukerref(String eksternBrukerRef) {
        EksternBrukerEntity eksternBrukerEntity = eksternBrukerDaoJpa.findByEksternbrukerref(eksternBrukerRef);
        return ValueObjectConverter.getEksternBrukerValueObject(eksternBrukerEntity);
    }
}
