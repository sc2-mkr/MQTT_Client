package fxml.gui.subscriber;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import services.mqtt.MqttSubscriber;

public class SubscribeGUI {

    private static SubscribeGUI instance = new SubscribeGUI();

    public static SubscribeGUI getInstance() {
        return instance;
    }

    private SubscribeGUI() {}

    public Pane generateGUI(MqttSubscriber sub) {
        Pane pane = new StackPane();
        pane.setBackground(new Background(
                new BackgroundFill(
                        Color.rgb(0, 255,230),
                        new CornerRadii(20.0),
                        Insets.EMPTY
                )
        ));
        Label lbl_topic = new Label(sub.getTopic());
        lbl_topic.setWrapText(true); // Remove for truncate topic if too long
        lbl_topic.setPadding(new Insets(0, 5, 0, 5));
        pane.getChildren().add(lbl_topic);

        return pane;
    }
}
