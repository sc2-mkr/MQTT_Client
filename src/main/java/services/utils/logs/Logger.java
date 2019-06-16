package services.utils.logs;

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
    public void log(String msg) {
        loggers.stream().forEach(logger -> logger.log(msg));
    }

    @Override
    public void logError(String msg) {
        loggers.stream().forEach(logger -> logger.logError(msg));
    }
}
