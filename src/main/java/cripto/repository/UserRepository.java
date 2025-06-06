package cripto.repository;

import cripto.model.User;
import cripto.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class UserRepository {

    private final EntityManager em;

    public UserRepository() {
        this.em = JpaUtil.getEntityManager();
    }

    public UserRepository(EntityManager em) {
        this.em = em;
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public User findById(Integer id) {
        return em.find(User.class, id);
    }

    public void save(User user) {
        EntityTransaction transaction = null;
        try {
            transaction = em.getTransaction();
            transaction.begin();

            if (user.getId() == null) {
                em.persist(user);
            } else {
                em.merge(user);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Error al guardar el usuario", e);
        }
    }

    public void saveWithoutTransaction(User user) {
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
    }
}