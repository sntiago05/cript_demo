package cripto.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {

    private static final EntityManagerFactory entityManager = buildEntityManager();

    //devuelve conexion a db y el entity maneger
    private static EntityManagerFactory buildEntityManager() {
        return Persistence.createEntityManagerFactory("ejemploJpa");
    }

    public static EntityManager getEntityManager() {
        return entityManager.createEntityManager();
    }
}
