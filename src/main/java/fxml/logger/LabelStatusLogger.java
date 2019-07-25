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
        Platform.runLater(() -> lbl_status.setText(formatText(msg)));
    }

    @Override
    public void logInfo(String msg) {
        log(msg, null);
    }

    @Override
    public void logError(String msg) {
        log(msg, null);
    }

    @Override
    public void logClient(String msg) {
        log(msg, null);
    }

    private String formatText(String text) {
        String msg = text;
        if(msg.contains("\n")) msg = msg.substring(0, msg.indexOf("\n"));
        return msg;
    }
}
