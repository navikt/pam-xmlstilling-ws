package no.nav.xmlstilling.ws.dao;

import javax.persistence.LockModeType;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Interface for generisk dao.
 *
 * @param <T>  type data som skal hentes (domeneobjekt).
 * @param <PK> idType for data som skal hentes (Long).
 */
public interface BaseDao<T, PK extends Serializable> {

    void create(T o);

    void update(T o);

    void delete(T o);

    T findById(PK id);

    void refresh(T o, LockModeType lockModeType);

    List<T> findByNamedQuery(String namedQuery) throws IllegalArgumentException;

    List<T> findByNamedQuery(String namedQuery, Map<String, Object> propertyValues) throws IllegalArgumentException;

}
