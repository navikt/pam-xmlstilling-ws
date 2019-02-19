package aetat.six.service.facade;

import aetat.six.common.vo.EksternBrukerVO;
import aetat.six.dao.EksternBrukerDaoJpa;
import aetat.six.dao.ValueObjectConverter;
import aetat.six.spring.SpringContextProvider;
import no.nav.sbl.entity.six.EksternBrukerEntity;
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
