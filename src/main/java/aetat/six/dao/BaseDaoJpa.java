/*
 * Borrowed from Dudney.Net
 */
package aetat.six.dao;


import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Generisk implementasjon av dao.
 *
 * @param <T>  type data som skal hentes.
 * @param <PK> idType for data som skal hentes (Long).
 */
public class BaseDaoJpa<T, PK extends Serializable> implements BaseDao<T, PK> {
    private EntityManager entityManager;
    private Class<T> type = null;

    public BaseDaoJpa() {
    }

    public T findById(PK id) {
        return getEntityManager().find(type, id);
    }

    public void refresh(T o, LockModeType lockModeType) {
        getEntityManager().refresh(o, lockModeType);
    }

    public void create(T o) {
        getEntityManager().persist(o);
    }

    public void update(T o) {
        getEntityManager().merge(o);
    }

    public void delete(T instance) {
        getEntityManager().remove(instance);
    }

    public List<T> findByNamedQuery(String namedQuery) {
        return findByNamedQuery(namedQuery, null);
    }

    @SuppressWarnings("unchecked")
    public List<T> findByNamedQuery(String namedQuery, Map<String, Object> parameters) {
        try {
            Query query = getEntityManager().createQuery(namedQuery);

            if (parameters != null && !parameters.isEmpty()) {
                setQueryParameter(query, parameters);
            }

            return query.getResultList();
        } catch (Exception ex) {
            throw new RuntimeException("Could not create named query :" + namedQuery, ex);
        }
    }

    public Object findObjectByNamedQuery(String namedQuery, Map<String, Object> parameters) {
        try {
            Query query = getEntityManager().createQuery(namedQuery);

            if (parameters != null && !parameters.isEmpty()) {
                setQueryParameter(query, parameters);
            }

            return query.getSingleResult();
        } catch (Exception ex) {
            throw new RuntimeException("Could not create named query :" + namedQuery, ex);
        }
    }

    private void setQueryParameter(Query query, Map<String, Object> parameterValues) {
        for (String parameterName : parameterValues.keySet()) {
            Object parameterValue = parameterValues.get(parameterName);

            if (parameterValue != null) {
                query.setParameter(parameterName, parameterValue);
            }
        }
    }

    public void setType(Class<T> type) {
        this.type = type;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}