package services.utils.fxUtils;

import javafx.scene.control.Alert;

public class AlertUtil {
    private static AlertUtil instance = new AlertUtil();

    public static AlertUtil getInstance() {
        return instance;
    }

    private AlertUtil() {
    }

    public void showAlertAndWait(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showWarningAndWait(String title, String header, String content) {
        showAlertAndWait(Alert.AlertType.WARNING, title, header, content);
    }

    public void showErrorAndWait(String title, String header, String content) {
        showAlertAndWait(Alert.AlertType.ERROR, title, header, content);
    }
}
