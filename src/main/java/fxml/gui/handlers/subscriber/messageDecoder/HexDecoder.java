package fxml.gui.handlers.subscriber.messageDecoder;

import org.apache.commons.codec.binary.Hex;

public class HexDecoder implements Decoder {

    @Override
    public String decode(String text) {
        try {
            byte[] b = text.getBytes();
            return Hex.encodeHexString(b, false);
        } catch (NumberFormatException e) {
            return text;
        }
    }
}
