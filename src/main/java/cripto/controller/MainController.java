package cripto.controller;

import cripto.model.Cripto;
import cripto.model.Symbol;
import cripto.repository.UserRepository;
import cripto.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    @FXML
    private ListView<Cripto> criptoList;

    @FXML
    private Label usernameTxt;

    @FXML
    private Label balanceTxt;

    private UserRepository user = new UserRepository();


    @FXML
    public void initialize() {
        usernameTxt.setText(SessionManager.getCurrentUser().getName());
        double balance = user.findById(SessionManager.getCurrentUser().getId()).getBalance();
        balanceTxt.setText(String.valueOf(balance));
        criptoList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Cripto selectedCripto = criptoList.getSelectionModel().getSelectedItem();
                if (selectedCripto != null) {

                    abrirGrafico(selectedCripto, usernameTxt);
                }
            }
        });

        List<Cripto> criptos = new ArrayList<>();
        for (Symbol symbol : Symbol.values()) {

            Cripto cripto = new Cripto();
            cripto.setSymbol(symbol);
            cripto.setCurrentPrice(symbol.getInitialPrice());
            criptos.add(cripto);
        }
        criptoList.setItems(FXCollections.observableList(criptos));


        criptoList.setCellFactory(param -> new ListCell<Cripto>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(Cripto cripto, boolean empty) {
                super.updateItem(cripto, empty);

                if (empty || cripto == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(cripto.getSymbol().getName());
                    String imagePath = "/imgs/criptos/" + cripto.getSymbol().getName().toLowerCase() + ".png";
                    InputStream imageStream = getClass().getResourceAsStream(imagePath);
                    Image image = new Image(imageStream);
                    imageView.setImage(image);
                    imageView.setFitWidth(24);
                    imageView.setFitHeight(24);
                    setGraphic(imageView);
                }
            }
        });
    }

    @FXML
    private void abrirUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cripto/dashboard.fxml"));
            Parent root = loader.load();

            DashBoardController controller = loader.getController();
            controller.cargarPortfolio(user.findById(SessionManager.getCurrentUser().getId()));

            Stage stage = (Stage) usernameTxt.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Pantalla Principal");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void abrirGrafico(Cripto cripto, Node nodoOrigen) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cripto/grafico_view.fxml"));
            Parent root = loader.load();

            GraficoController controller = loader.getController();
            controller.setCripto(cripto);

            Stage nuevaVentana = new Stage();
            nuevaVentana.setTitle("Gráfico de " + cripto.getSymbol().getName());
            nuevaVentana.setScene(new Scene(root));
            nuevaVentana.show();

            Stage ventanaActual = (Stage) nodoOrigen.getScene().getWindow();
            ventanaActual.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
