package exceptions;

public class ConfigFileNotFoundException extends ExceptionImpl {

    public ConfigFileNotFoundException() {
    }

    @Override
    public String toString() {
        return "Configuration file not found";
    }
}
