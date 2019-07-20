package fxml.logger;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import services.utils.logs.LoggerFactory;

public class LabelStatusLogger implements LoggerFactory {

    private Label lbl_status;

    public LabelStatusLogger(Label lbl_status) {
        this.lbl_status = lbl_status;
    }

    @Override
    public void log(String msg, Color color) {
        Platform.runLater(() -> lbl_status.setText(msg));
    }

    @Override
    public void logInfo(String msg) {
        log(msg, Color.BLACK);
    }

    @Override
    public void logError(String msg) {
        log(msg, Color.BLACK);
    }

    @Override
    public void logEditor(String msg) {
        log(msg, Color.BLACK);
    }
}
