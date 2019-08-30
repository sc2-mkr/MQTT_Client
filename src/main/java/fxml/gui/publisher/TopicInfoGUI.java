package fxml.gui.publisher;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import services.mqtt.publisher.MqttPublisher;

public class TopicInfoGUI {

    private static TopicInfoGUI instance = new TopicInfoGUI();

    private TopicInfoGUI() {
    }

    public static TopicInfoGUI getInstance() {
        return instance;
    }

//    public Pane generateGUI(MqttSubscribersManager manager, MqttSubscriber sub) {
    public Pane generateGUI(MqttPublisher pub) {
        HBox pane = new HBox();
        pane.setBackground(new Background(
                new BackgroundFill(
                        Color.rgb(0, 255, 230),
                        new CornerRadii(20.0),
                        Insets.EMPTY
                )
        ));
        Label lbl_topic = new Label(pub.getMsg().getTopic());
        lbl_topic.setWrapText(true); // Remove for truncate topic if too long
        lbl_topic.setPadding(new Insets(0, 5, 0, 5));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btn_delete = new Button();
        btn_delete.setMinSize(20, 20);
        btn_delete.setMaxSize(20, 20);

        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(
                        getClass().getResource("/images/icons/cross.png").toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        btn_delete.setBackground(background);
//        btn_delete.setOnAction((event) -> manager.removeTopic(sub));

        pane.getChildren().addAll(lbl_topic, spacer, btn_delete);

        pane.setPadding(new Insets(5));

        return pane;
    }
}
