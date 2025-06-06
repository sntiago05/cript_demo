package cripto.controller;

import cripto.model.User;
import cripto.util.AlertManager;
import cripto.util.JpaUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import service.UsuarioService;

public class RegisterController {
    @FXML
    public TextField userTxt;

    @FXML
    public PasswordField passwordTxt;

    @FXML
    public Button btnRegister;

    private UsuarioService service;

    @FXML
    public void initialize() {
        this.service = new UsuarioService(JpaUtil.getEntityManager());
    }

    @FXML
    private void register() {
        try {
            service.save(new User(userTxt.getText(), passwordTxt.getText(), 0D));
            AlertManager.showSuccesAlert("usuario registrado con exito");
        } catch (RuntimeException e) {
            AlertManager.showErrorAlert("Error al registrar usuario");
        }
    }
}
