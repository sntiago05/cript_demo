package service;

import cripto.model.User;
import cripto.repository.authRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class UsuarioService implements UsuarioServiceRepository {
    private authRepository userRepository;

    public UsuarioService(EntityManager em) {
        this.userRepository = new authRepository(em);
    }

    @Override
    public User login(String name, String password) {
        try {
            return userRepository.findByName(name, password);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(User user) {
        try {
            userRepository.getEm().getTransaction().begin();
            userRepository.save(user);
            userRepository.getEm().getTransaction().commit();

        } catch (Exception e) {
            if (userRepository.getEm().getTransaction().isActive()) {
                userRepository.getEm().getTransaction().rollback();
            }
            throw new RuntimeException(e);
        }
    }
}
