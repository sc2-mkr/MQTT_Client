package fxml.gui.subscriber;

import configs.Configuration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import services.mqtt.messagges.MqttMessageExtended;

import java.text.SimpleDateFormat;

public class MessageGUI {
    private static MessageGUI instance = new MessageGUI();

    private MessageGUI() {}

    public static MessageGUI getInstance() {
        return instance;
    }

    public Pane generateGUI(MqttMessageExtended msg) {
        VBox pane = new VBox();
        pane.setSpacing(5);
        pane.setBackground(new Background(
                new BackgroundFill(
                        Color.rgb(255, 231, 0),
                        new CornerRadii(20.0),
                        Insets.EMPTY
                )
        ));

        Label lbl_topic = new Label(msg.getTopic());
        lbl_topic.setStyle("-fx-font-weight: bold");
        lbl_topic.setPadding(new Insets(0, 5, 0, 5));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lbl_date = new Label(new SimpleDateFormat(Configuration.getInstance().getValue("dateFormat"))
                .format(msg.getDate()));
        lbl_date.setPadding(new Insets(0, 5, 0, 5));

        HBox topPane = new HBox();
        topPane.getChildren().addAll(lbl_topic, spacer, lbl_date);
        topPane.setAlignment(Pos.BASELINE_RIGHT);

        Label lbl_payload = new Label(new String(msg.getPayload()));
//        lbl_payload.setWrapText(true); // Remove for truncate topic if too long
        lbl_payload.setPadding(new Insets(0, 5, 0, 5));
        pane.getChildren().addAll(topPane, lbl_payload);

        return pane;
    }
}
