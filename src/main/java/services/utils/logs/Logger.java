package services.utils.logs;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Logger implements LoggerFactory {

    private static Logger instance = new Logger();

    private ArrayList<LoggerFactory> loggers = new ArrayList<>();

    private Logger() {
    }

    public static Logger getInstance() {
        return instance;
    }

    public void addLogger(LoggerFactory logger) {
        loggers.add(logger);
    }

    @Override
    public void log(String msg, Color color) {
        loggers.stream().forEach(logger -> logger.log(msg, color));
    }

    @Override
    public void logInfo(String msg) {
        loggers.stream().forEach(logger -> logger.logInfo(msg));
    }

    @Override
    public void logError(String msg) {
        loggers.stream().forEach(logger -> logger.logError(msg));
    }

    @Override
    public void logEditor(String msg) {
        loggers.stream().forEach(logger -> logger.logEditor(msg));
    }
}
