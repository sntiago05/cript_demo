package cripto.repository;

import cripto.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class authRepository implements CrudRepository<User> {
    private EntityManager em;
    private CriteriaBuilder cb;
    private Root<User> from;

    public authRepository(EntityManager em) {
        this.em = em;
        this.cb = em.getCriteriaBuilder();
    }

    @Override
    public User findByName(String username, String password) {
        CriteriaQuery<User> query = cb.createQuery(User.class);
        this.from = query.from(User.class);
        query.select(from)
                .where(cb.and(
                        cb.equal(from.get("name"), username),
                        cb.equal(from.get("password"), password)
                ));
        return em.createQuery(query).getSingleResult();
    }

    @Override
    public void save(User user) {
        em.persist(user);
    }

    public EntityManager getEm() {
        return em;
    }
}
