package no.nav.xmlstilling.ws.dao;

import no.nav.sbl.entity.six.KallLoggEntity;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class KallLoggDaoJpa extends BaseDaoJpa<KallLoggEntity, BigDecimal> {

    public KallLoggDaoJpa() {
        super();
        setType(KallLoggEntity.class);
    }
}
