package services.utils.logs;

import javafx.scene.paint.Color;

public interface LoggerFactory {
    void log(String msg, Color color);

    void logInfo(String msg);

    void logError(String msg);

    void logClient(String msg);
}
