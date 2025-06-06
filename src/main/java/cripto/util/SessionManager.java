package cripto.util;

import cripto.model.User;

public class SessionManager {
    private static User currentUser;

    private SessionManager() {
        // Constructor privado para que no se pueda instanciar
    }

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
