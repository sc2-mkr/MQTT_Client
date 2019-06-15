package fxml.logger;

import javafx.scene.control.Label;
import services.utils.logs.LoggerFactory;

public class LabelStatusLogger implements LoggerFactory {

    private Label lbl_status;

    public LabelStatusLogger(Label lbl_status) {
        this.lbl_status = lbl_status;
    }

    @Override
    public void log(String msg) {
        lbl_status.setText(msg);
    }

    @Override
    public void logError(String msg) {
        log(msg);
    }
}
