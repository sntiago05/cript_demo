package cripto.repository;

import cripto.model.Transactional;
import cripto.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

public class TransactionalRepository {
    private EntityManager em;

    public TransactionalRepository() {
        this.em = JpaUtil.getEntityManager();
    }

    public TransactionalRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Transactional transactional) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(transactional);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        }
    }

    public void saveWithoutTransaction(Transactional transactional) {
        em.persist(transactional);
    }
}


