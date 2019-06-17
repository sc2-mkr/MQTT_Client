package fxml.logger;

import configs.Configuration;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import services.utils.fxUtils.ColorUtils;
import services.utils.logs.LoggerFactory;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextAreaLogsLogger implements LoggerFactory {

    private TextFlow tflow_logs;

    public TextAreaLogsLogger(TextFlow tflow_logs) {
        this.tflow_logs = tflow_logs;
    }

    @Override
    public void log(String msg, Color color) {
        appendText(MessageFormat.format("\t\t\t{0}", msg), Color.BLACK);
    }

    @Override
    public void logInfo(String msg) {
        appendText(MessageFormat.format("[INFO]\t\t{0}", msg), Color.BLACK);
    }

    @Override
    public void logError(String msg) {
        appendText(MessageFormat.format("[ERROR]\t{0}", msg), Color.RED);
    }

    @Override
    public void logEditor(String msg) {
        appendText(MessageFormat.format("[EDITOR]\t{0}", msg), Color.BLUE);
    }

    private void appendText(String msg, Color color) {
        SimpleDateFormat formatter = new SimpleDateFormat(Configuration.getInstance().getValue("dateFormat"));
        Date date = new Date(System.currentTimeMillis());
        String msgExtended = MessageFormat.format("[{0}] {1}", formatter.format(date), msg);

        Text t = new Text(MessageFormat.format("{0}\n", msgExtended));
        t.setStyle(MessageFormat.format("-fx-fill: {0};", ColorUtils.getInstance().toHexCode(color)));

        tflow_logs.getChildren().add(t);
    }
}
