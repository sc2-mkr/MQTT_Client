package services.utils.logs;

import java.util.Date;

public class Log {

    private String message;
    private Date date;
    private Logger.Type type;

    public Log(String message, Logger.Type type) {
        this.message = message;
        this.date = new Date();
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
