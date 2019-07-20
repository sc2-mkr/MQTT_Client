package services.utils.fxUtils;

import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.text.MessageFormat;

public class TextFlowUtils {
    private static TextFlowUtils ourInstance = new TextFlowUtils();

    private TextFlowUtils() {
    }

    public static TextFlowUtils getInstance() {
        return ourInstance;
    }

    public String getTextFromTextFlow(TextFlow textflow) {
        String text = "";

        // Impossible to use stream because text must be final
        for (Node row : textflow.getChildren()) {
            text = MessageFormat.format("{0}{1}", text, ((Text) row).getText());
        }

        return text;
    }
}
