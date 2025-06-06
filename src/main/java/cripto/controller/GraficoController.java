package cripto.controller;

import cripto.model.Cripto;
import cripto.model.User;
import cripto.repository.TransactionalRepository;
import cripto.repository.UserRepository;
import cripto.util.AlertManager;
import cripto.util.JpaUtil;
import cripto.util.SessionManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.CriptoService;

import java.io.IOException;
import java.util.Random;

public class GraficoController {

    @FXML
    private LineChart<Number, Number> priceChart;

    @FXML
    private NumberAxis axisX;

    @FXML
    private Button btnComprar, btnVender;

    @FXML
    private Label lblActual, lblTotal;

    @FXML
    private TextField textCantidad;

    private Cripto cripto;
    private User user;

    private int day = 1;
    private XYChart.Series<Number, Number> series;
    private Timeline timeline;
    private Random random = new Random();
    private CriptoService criptoService;


    @FXML
    private void initialize() {
        this.user = SessionManager.getCurrentUser();
        System.out.println(this.user.getId());

    }

    public void setCripto(Cripto cripto) {
        this.cripto = cripto;
        this.criptoService = new CriptoService(JpaUtil.getEntityManager());
        lblActual.setText("$" + this.cripto.getCurrentPrice());
        configurarEjes();
        iniciarSimulacion();

    }

    @FXML
    private void irAtras() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cripto/main.fxml"));
            Parent root = loader.load();


            Stage stage = (Stage) lblActual.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Pantalla Principal");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void actualizarLabel() {
        String texto = textCantidad.getText();

        if (texto == null || texto.isEmpty()) {
            lblTotal.setText("$0.00");
            return;
        }

        try {
            double cantidad = Double.parseDouble(texto);
            double total = cripto.getCurrentPrice() * cantidad;
            lblTotal.setText(String.format("$%.2f", total));
        } catch (NumberFormatException e) {
            lblTotal.setText("Valor inválido");
        }
    }


    @FXML
    private void comprar() {
        try {
            double cantidad = Double.parseDouble(textCantidad.getText());
            double precioActual = cripto.getCurrentPrice();

            criptoService.buyCripto(user.getId(), cripto.getSymbol(), cantidad, precioActual);
            lblTotal.setText(String.format("%.2f USD", cantidad * precioActual));
            AlertManager.showSuccesAlert(String.format("Compra realizada: %.4f %s por %.2f USD",
                    cantidad, cripto.getSymbol().getName(), cantidad * precioActual));
        } catch (NumberFormatException e) {
            lblTotal.setText("Cantidad inválida");
            e.printStackTrace();
        } catch (Exception e) {
            lblTotal.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void vender() {
        try {
            double cantidad = Double.parseDouble(textCantidad.getText());
            double precioActual = cripto.getCurrentPrice();

            criptoService.sellCripto(user.getId(), cripto.getSymbol(), cantidad, precioActual);
            AlertManager.showSuccesAlert(String.format("Venta realizada: %.4f %s por %.2f USD",
                    cantidad, cripto.getSymbol().getName(), cantidad * precioActual));
        } catch (NumberFormatException e) {
            lblTotal.setText("Cantidad inválida");
        } catch (Exception e) {
            AlertManager.showErrorAlert(e.getMessage());
        }
    }

    private void configurarEjes() {
        priceChart.getXAxis().setLabel("Tiempo (días)");
        priceChart.getYAxis().setLabel("Precio (USD)");
        axisX.setAutoRanging(false);
        axisX.setLowerBound(0);
        axisX.setUpperBound(10);
    }

    private void iniciarSimulacion() {
        series = new XYChart.Series<>();
        series.setName(cripto.getSymbol().getName());
        priceChart.getData().add(series);

        double initialPrice = cripto.getCurrentPrice();
        series.getData().add(new XYChart.Data<>(day, initialPrice));
        cripto.getHistoryprice().add(initialPrice);

        timeline = new Timeline(new KeyFrame(Duration.seconds(15), event -> agregarNuevoPunto()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void agregarNuevoPunto() {
        double price = cripto.getCurrentPrice();
        double variation = (random.nextDouble() - 0.5) * 0.4;
        double newPrice = price + price * variation;

        cripto.setCurrentPrice(newPrice);
        lblActual.setText("$" + newPrice);
        cripto.getHistoryprice().add(newPrice);
        series.getData().add(new XYChart.Data<>(day, newPrice));

        if (series.getData().size() > 50) {
            series.getData().removeFirst();
        }

        if (day > 10) {
            axisX.setLowerBound(day - 10);
            axisX.setUpperBound(day);
        }
        day++;
    }
}
