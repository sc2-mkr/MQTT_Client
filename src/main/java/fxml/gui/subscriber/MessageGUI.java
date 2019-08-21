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

    public Pane generateGUI(MqttMessageExtended sub) {
        VBox pane = new VBox();
        pane.setSpacing(5);
        pane.setBackground(new Background(
                new BackgroundFill(
                        Color.rgb(255, 231, 0),
                        new CornerRadii(20.0),
                        Insets.EMPTY
                )
        ));

        Label lbl_date = new Label(new SimpleDateFormat(Configuration.getInstance().getValue("dateFormat"))
                .format(sub.getDate()));
        lbl_date.setPadding(new Insets(0, 5, 0, 5));

        HBox dateBox = new HBox();
        dateBox.getChildren().add(lbl_date);
        dateBox.setAlignment(Pos.BASELINE_RIGHT);

        Label lbl_payload = new Label(new String(sub.getPayload()));
//        lbl_payload.setWrapText(true); // Remove for truncate topic if too long
        lbl_payload.setPadding(new Insets(0, 5, 0, 5));
        pane.getChildren().addAll(dateBox, lbl_payload);

        return pane;
    }
}
