package services.utils.fxUtils;

import javafx.scene.paint.Color;

public class ColorUtils {
    private static ColorUtils instance = new ColorUtils();

    private ColorUtils() {
    }

    public static ColorUtils getInstance() {
        return instance;
    }

    public String toHexCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}
