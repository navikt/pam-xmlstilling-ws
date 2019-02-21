package no.nav.xmlstilling.ws.dao;

import no.nav.sbl.entity.six.StillingBatchEntity;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class StillingBatchDaoJpa extends BaseDaoJpa<StillingBatchEntity, BigDecimal> {

    public StillingBatchDaoJpa() {
        super();
        setType(StillingBatchEntity.class);
    }

    public Collection<StillingBatchEntity> findByEksternbrukerref(String eksternBrukerRef) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eksternBrukerRef", eksternBrukerRef);

        // stillingXml er en CLOB som tar lang tid å hente fra db. Derfor henter vi bare 'STILLING-XML' i stedet, siden stillingXML ikke brukes av den koden som kaller findByEksternbrukerref
        String namedQuery =
                "select new StillingBatchEntity(o.stillingBatchId, o.eksternBrukerRef, 'STILLING-XML', o.mottattDato, o.behandletDato, o.behandletStatus) " +
                "from StillingBatchEntity as o where o.eksternBrukerRef=:eksternBrukerRef order by o.mottattDato desc" ;
        return findByNamedQuery(namedQuery, map);
    }

    public StillingBatchEntity findByStillingbatchid(BigDecimal stillingBatchId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("stillingBatchId", stillingBatchId);

        String namedQuery = "select object(o) from StillingBatchEntity as o where o.stillingBatchId=:stillingBatchId";
        return (StillingBatchEntity)findObjectByNamedQuery(namedQuery, map);
    }
}
