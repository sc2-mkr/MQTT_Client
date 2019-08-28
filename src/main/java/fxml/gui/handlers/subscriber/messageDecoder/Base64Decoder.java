package fxml.gui.handlers.subscriber.messageDecoder;

import java.util.Base64;

public class Base64Decoder implements Decoder {

    @Override
    public String decode(String text) {
        try {
            return new String(Base64.getDecoder().decode(text));
        } catch (IllegalArgumentException e) {
            return text;
        }
    }
}
