package fxml.gui.subscriber;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import services.mqtt.MqttMessageExtended;
import services.mqtt.MqttSubscriber;

public class MessageGUI {
    private static MessageGUI instance = new MessageGUI();

    public static MessageGUI getInstance() {
        return instance;
    }

    private MessageGUI() {

    }

    public Pane generateGUI(MqttMessageExtended sub) {
        Pane pane = new StackPane();
        pane.setBackground(new Background(
                new BackgroundFill(
                        Color.rgb(255, 231,0),
                        new CornerRadii(20.0),
                        Insets.EMPTY
                )
        ));
        Label lbl_topic = new Label(new String(sub.getPayload()));
        lbl_topic.setWrapText(true); // Remove for truncate topic if too long
        lbl_topic.setPadding(new Insets(0, 5, 0, 5));
        pane.getChildren().add(lbl_topic);

        return pane;
    }
}
