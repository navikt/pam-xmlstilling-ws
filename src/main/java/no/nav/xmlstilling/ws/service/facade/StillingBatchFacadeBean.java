package no.nav.xmlstilling.ws.service.facade;

import no.nav.xmlstilling.ws.common.vo.StillingBatchVO;
import no.nav.xmlstilling.ws.dao.StillingBatchDaoJpa;
import no.nav.xmlstilling.ws.dao.ValueObjectConverter;
import no.nav.xmlstilling.ws.entity.StillingBatchEntity;
import no.nav.xmlstilling.ws.spring.SpringContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
public class StillingBatchFacadeBean {

    private static final long serialVersionUID = 3923050514900305593L;

    public static StillingBatchFacadeBean getInstance() {
        return SpringContextProvider.getContext().getBean("stillingBatchFacadeBean", StillingBatchFacadeBean.class);
    }

    @Autowired
    private StillingBatchDaoJpa stillingBatchDaoJpa;

    public StillingBatchVO insertStillingBatch(StillingBatchVO stillingBatchVO) {
        if (stillingBatchVO == null) {
            throw new IllegalArgumentException(this.getClass().getName() + ".insertStillingBatch: stillingBatchVO er lik null");
        }

        StillingBatchEntity stillingBatchEntity = ValueObjectConverter.getStillingBatchEntityForValueObject(stillingBatchVO);
        stillingBatchDaoJpa.create(stillingBatchEntity);

        //entityen får satt ID
        //returner det nye objektet med ID.
        return ValueObjectConverter.getStillingBatchValueObject(stillingBatchEntity);
    }

}
