package fxml.gui.handlers.subscriber.messageDecoder;

public class PlainTextDecoder implements Decoder {

    @Override
    public String decode(String text) {
        return text;
    }
}
