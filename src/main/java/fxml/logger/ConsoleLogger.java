package fxml.logger;

import javafx.scene.paint.Color;
import services.utils.logs.LoggerFactory;

public class ConsoleLogger implements LoggerFactory {

    public ConsoleLogger() {
    }

    @Override
    public void log(String msg, Color color) {
        System.out.println(msg);
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
    public void logClient(String msg) {
        log(msg, Color.BLACK);
    }
}
