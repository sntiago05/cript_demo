package cripto.repository;

public interface CrudRepository<T> {
    T findByName(String username, String password);
    void save(T obj);
}
