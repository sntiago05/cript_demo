package cripto.controller;

import cripto.model.User;
import cripto.util.AlertManager;
import cripto.util.JpaUtil;
import cripto.util.SessionManager;
import cripto.util.WindowManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.UsuarioService;

public class LoginController {
    @FXML
    private TextField userTxt;
    @FXML
    private PasswordField passwordTxt;
    @FXML
    private Button btnLogin;
    @FXML
    private Label registerLabel;

    private UsuarioService service;

    @FXML
    public void initialize() {
        this.service = new UsuarioService(JpaUtil.getEntityManager());
        System.out.println("holi");
    }

    @FXML
    protected void login() {
        User user = service.login(userTxt.getText(), passwordTxt.getText());
        if (user != null) {
            SessionManager.login(user);
            AlertManager.showSuccesAlert("inicio sesion con exito");
            Stage stage = (Stage) registerLabel.getScene().getWindow();//la ventana actual
            WindowManager.changeWindow(stage, "main.fxml", "cripto list");
        } else {
            System.out.println("ñopi");
            AlertManager.showErrorAlert("error al iniciar sesion");
        }
    }

    @FXML
    protected void switchToCreate() {
        System.out.println("llipi");
        Stage stage = (Stage) registerLabel.getScene().getWindow();
        WindowManager.changeWindow(stage, "register.fxml", "Crear cuenta");
    }
}