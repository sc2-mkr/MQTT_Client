package services.utils.logs;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Logger {

    private static Logger instance = new Logger();

    private ArrayList<LoggerFactory> loggers = new ArrayList<>();

    private ArrayList<Log> logs = new ArrayList<>();

    public enum Type {
        INFO,
        ERROR,
        CLIENT
    }

    private Logger() {
    }

    public static Logger getInstance() {
        return instance;
    }

    public void addLogger(LoggerFactory logger) {
        loggers.add(logger);
    }

    public void log(String msg, Color color, Type type) {
        logs.add(new Log(msg, type));
        loggers.stream().forEach(logger -> logger.log(msg, color));
    }

    public void logInfo(String msg) {
        logs.add(new Log(msg, Type.INFO));
        loggers.stream().forEach(logger -> logger.logInfo(msg));
    }

    public void logError(String msg) {
        logs.add(new Log(msg, Type.ERROR));
        loggers.stream().forEach(logger -> logger.logError(msg));
    }

    public void logClient(String msg) {
        logs.add(new Log(msg, Type.CLIENT));
        loggers.stream().forEach(logger -> logger.logClient(msg));
    }

    public ArrayList<LoggerFactory> getLoggers() {
        return loggers;
    }

    public ArrayList<Log> getLogs() {
        return logs;
    }
}
