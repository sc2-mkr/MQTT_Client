package exceptions;

import java.text.MessageFormat;

public class InvalidQosException extends ExceptionImpl {

    int invalidQos;

    public InvalidQosException(int invalidQos) {
        this.invalidQos = invalidQos;
    }

    @Override
    public String toString() {
        return MessageFormat.format("The QOS code {0} is not valid", invalidQos);
    }
}
