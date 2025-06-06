package cripto.util;

import javafx.scene.control.Alert;

public class AlertManager {
    private static Alert alert;

    private AlertManager() {

    }

    public static void showErrorAlert(String mensaje) {
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
     public static void showSuccesAlert(String mensaje) {
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
