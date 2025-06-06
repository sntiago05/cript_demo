package service;

import cripto.model.User;

public interface UsuarioServiceRepository {
    User login(String name, String password);

    void save(User user);
}
