package no.nav.xmlstilling.ws.dao;

import no.nav.sbl.entity.six.EksternBrukerEntity;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Repository
public class EksternBrukerDaoJpa extends BaseDaoJpa<EksternBrukerEntity, BigDecimal> {

    public EksternBrukerDaoJpa() {
        super();
        setType(EksternBrukerEntity.class);
    }

    public EksternBrukerEntity findByEksternbrukerref(String eksternBrukerRef) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eksternBrukerRef", eksternBrukerRef);

        String namedQuery = "select object(o) from EksternBrukerEntity as o where o.eksternBrukerRef=:eksternBrukerRef";
        return (EksternBrukerEntity)findObjectByNamedQuery(namedQuery, map);
    }

}