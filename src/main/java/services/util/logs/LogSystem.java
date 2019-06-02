package services.util.logs;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogSystem {

    private static LogSystem instance = new LogSystem();

    private Label lbl_status;
    private TextArea ta_logs;

    public static LogSystem getInstance() {
        return instance;
    }

    private LogSystem() {}

    public void printLog(String msg) {
        lbl_status.setText(msg);
        SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String msgExtended = MessageFormat.format("[{0}] {1}", formatter.format(date), msg);
        ta_logs.setText(MessageFormat.format("{0}\n{1}", ta_logs.getText(), msgExtended));
    }

    public Label getLbl_status() {
        return lbl_status;
    }

    public void setLbl_status(Label lbl_status) {
        this.lbl_status = lbl_status;
    }

    public TextArea getTa_logs() {
        return ta_logs;
    }

    public void setTa_logs(TextArea ta_logs) {
        this.ta_logs = ta_logs;
    }
}
