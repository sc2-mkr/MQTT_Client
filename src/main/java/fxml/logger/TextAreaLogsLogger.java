package fxml.logger;

import configs.Configuration;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import services.utils.fxUtils.ColorUtils;
import services.utils.logs.LoggerFactory;

import java.text.*;
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
    public void logClient(String msg) {
        appendText(MessageFormat.format("[CLIENT]\t{0}", msg), Color.BLUE);
    }

    private void appendText(String msg, Color color) {
        String msgExtended = MessageFormat.format("[{0}] {1}", getDateFormatted(), msg);

        Text t = getTextFormatted(msgExtended, color);

        Platform.runLater(() -> tflow_logs.getChildren().add(t));
    }

    private Text getTextFormatted(String msg, Color color) {
        msg = formatText(msg);
        Text t = new Text(MessageFormat.format("{0}\n", msg));
        t.setStyle(MessageFormat.format("-fx-fill: {0};", ColorUtils.getInstance().toHexCode(color)));
        return t;
    }

    private String formatText(String text) {
        String msg = text.replace("\n", "\n\t\t\t\t\t\t\t");
        return msg;
    }

    private String getDateFormatted() {
        SimpleDateFormat formatter = new SimpleDateFormat(Configuration.getInstance().getValue("dateFormat"));
        Date date = new Date(System.currentTimeMillis());
        return formatter.format(date);
    }
}
