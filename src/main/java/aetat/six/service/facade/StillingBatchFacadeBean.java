package aetat.six.service.facade;

import aetat.arch.vo.ValueObject;
import aetat.six.common.vo.StillingBatchVO;
import aetat.six.dao.StillingBatchDaoJpa;
import aetat.six.dao.ValueObjectConverter;
import aetat.six.exception.SystemException;
import aetat.six.spring.SpringContextProvider;
import no.nav.sbl.entity.six.StillingBatchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * StillingBatchFacadeBean
 *
 * @author VHA
 * @version 1.0
 */
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

    public void endreStillingBatch(StillingBatchVO stillingBatchVO) {
        if (stillingBatchVO == null) {
            throw new IllegalArgumentException(this.getClass().getName() + ".endreStillingBatch: stillingBatchVO er lik null");
        }
        StillingBatchEntity stillingBatchEntity = stillingBatchDaoJpa.findById(stillingBatchVO.getStillingBatchId());

        if (stillingBatchEntity != null) {
            stillingBatchDaoJpa.update(ValueObjectConverter.getStillingBatchEntityForValueObject(stillingBatchVO));
        } else {
            throw new SystemException(this.getClass().getName() + ".endreStillingBatch: FinderException ");
        }
    }

    public Collection<ValueObject> findByEksternbrukerref(String eksternBrukerRef) {

        if (eksternBrukerRef == null) {
            throw new RuntimeException("Ukjent bruker");
        }

        Collection<StillingBatchEntity> stillingBatchEntities = stillingBatchDaoJpa.findByEksternbrukerref(eksternBrukerRef);
        return hentUtValueObject(stillingBatchEntities);
    }

    private Collection<ValueObject> hentUtValueObject(Collection<StillingBatchEntity> stillingBatchEntities) {
        if (stillingBatchEntities != null && !stillingBatchEntities.isEmpty()) {
            List<ValueObject> result = new ArrayList<ValueObject>(stillingBatchEntities.size());
            for (StillingBatchEntity stillingBatchEntity : stillingBatchEntities) {
                result.add(ValueObjectConverter.getStillingBatchValueObject(stillingBatchEntity));
            }
            return result;
        }
        return null;
    }

    public StillingBatchVO findByStillingbatchid(BigDecimal stillingBatchId) {
        StillingBatchEntity stillingBatchEntity = stillingBatchDaoJpa.findByStillingbatchid(stillingBatchId);
        return ValueObjectConverter.getStillingBatchValueObject(stillingBatchEntity);
    }
}
