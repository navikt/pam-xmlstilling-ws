package aetat.six.dao;

import no.nav.sbl.entity.six.KallLoggTypeEntity;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Repository
public class KallLoggTypeDaoJpa extends BaseDaoJpa<KallLoggTypeEntity, BigDecimal> {

    public KallLoggTypeDaoJpa() {
        super();
        setType(KallLoggTypeEntity.class);
    }

    public KallLoggTypeEntity findByKallnavn(String kallNavn) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("kallNavn", kallNavn);

        String namedQuery = "select object(o) from KallLoggTypeEntity as o where o.kallNavn=:kallNavn";
        return (KallLoggTypeEntity)findObjectByNamedQuery(namedQuery, map);
    }
}
