package exceptions;

import java.text.MessageFormat;

public class TranslationNotFoundException extends ExceptionImpl {

    String key;

    public TranslationNotFoundException(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Translation not found for key \"{0}\"", key);
    }
}
