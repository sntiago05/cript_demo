package cripto.controller;

import cripto.dominio.CriptoEnPortfolio;
import cripto.model.Symbol;
import cripto.model.User;
import cripto.repository.UserRepository;
import cripto.util.JpaUtil;
import cripto.util.SessionManager;
import cripto.util.WindowManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.UsuarioService;


import java.util.Map;

public class DashBoardController {
    @FXML
    private TableView<CriptoEnPortfolio> tablaPortfolio;
    @FXML
    private TableColumn<CriptoEnPortfolio, String> colSimbolo;
    @FXML
    private TableColumn<CriptoEnPortfolio, Double> colCantidad;

    private User usuarioActual;

    @FXML
    private TextField txtAgregarSaldo;
    @FXML
    private Label lblSaldo;
    UserRepository userRepository = new UserRepository();

    public void initialize() {
        colSimbolo.setCellValueFactory(cellData -> cellData.getValue().symbolProperty());
        colCantidad.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asObject());
        usuarioActual = userRepository.findById(SessionManager.getCurrentUser().getId());
        lblSaldo.setText(usuarioActual.getBalance().toString());
    }

    @FXML
    private void agregarSaldo() {
        String saldo = txtAgregarSaldo.getText();
        if (!saldo.isBlank()) {
            try {
                double monto = Double.parseDouble(saldo);
                usuarioActual.setBalance(usuarioActual.getBalance() + monto);
                userRepository.save(usuarioActual);
                lblSaldo.setText(String.format("%.2f", usuarioActual.getBalance()));
                txtAgregarSaldo.clear();
            } catch (NumberFormatException e) {
                System.err.println("Ingrese un valor numérico válido.");
            }
        }
    }

    @FXML
    private void volver() {
        WindowManager.changeWindow((Stage) tablaPortfolio.getScene().getWindow(), "main.fxml", "mercado");
    }

    public void cargarPortfolio(User user) {
        this.usuarioActual = user;
        ObservableList<CriptoEnPortfolio> datos = FXCollections.observableArrayList();

        for (Map.Entry<Symbol, Double> entry : user.getPortfolio().entrySet()) {
            datos.add(new CriptoEnPortfolio(entry.getKey().getName(), entry.getValue()));
        }
        tablaPortfolio.setItems(datos);
    }
}
