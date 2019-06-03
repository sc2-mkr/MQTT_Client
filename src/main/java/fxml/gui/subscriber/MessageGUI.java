package fxml.gui.subscriber;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import services.mqtt.MqttMessageExtended;
import services.mqtt.MqttSubscriber;

import java.text.SimpleDateFormat;

public class MessageGUI {
    private static MessageGUI instance = new MessageGUI();

    public static MessageGUI getInstance() {
        return instance;
    }

    private MessageGUI() {

    }

    public Pane generateGUI(MqttMessageExtended sub) {
        VBox pane = new VBox();
        pane.setBackground(new Background(
                new BackgroundFill(
                        Color.rgb(255, 231,0),
                        new CornerRadii(20.0),
                        Insets.EMPTY
                )
        ));

        Label lbl_date = new Label(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(sub.getDate()));
        lbl_date.setPadding(new Insets(5, 5, 5, 5));

        Label lbl_payload = new Label(new String(sub.getPayload()));
        lbl_payload.setWrapText(true); // Remove for truncate topic if too long
        lbl_payload.setPadding(new Insets(0, 5, 5, 5));
        pane.getChildren().addAll(lbl_date, lbl_payload);

        return pane;
    }
}
