package fxml.gui.subscriber;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import services.mqtt.subscriber.MqttSubscribersManager;

public class TopicInfoGUI {

    private static TopicInfoGUI instance = new TopicInfoGUI();

    private TopicInfoGUI() {
    }

    public static TopicInfoGUI getInstance() {
        return instance;
    }

    public Pane generateGUI(MqttSubscribersManager manager, String topic, int numMessages, boolean isErasable) {
        VBox pane = new VBox();
        pane.setAlignment(Pos.CENTER_RIGHT);

        HBox topPane = new HBox();

        pane.setBackground(new Background(
                new BackgroundFill(
                        Color.rgb(0, 255, 230),
                        new CornerRadii(20.0),
                        Insets.EMPTY
                )
        ));
        Label lbl_topic = new Label(topic);
        lbl_topic.setWrapText(true); // Remove for truncate topic if too long
        lbl_topic.setPadding(new Insets(0, 5, 0, 5));

        // Spacer between topic's name and messages counter
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label lbl_counter = new Label(Integer.toString(numMessages));

        topPane.getChildren().addAll(lbl_topic, spacer, lbl_counter);
        pane.getChildren().add(topPane);

        if (isErasable) {
            BackgroundImage backgroundImage = new BackgroundImage(
                    new Image(
                            getClass().getResource("/images/icons/cross.png").toExternalForm()),
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    BackgroundSize.DEFAULT);
            Background background = new Background(backgroundImage);



            Button btn_delete = new Button();
            btn_delete.setMinSize(20, 20);
            btn_delete.setMaxSize(20, 20);

            btn_delete.setBackground(background);
            btn_delete.setOnAction((event) -> manager.removeTopic(topic));

            pane.getChildren().add(btn_delete);
        }


        return pane;
    }
}
