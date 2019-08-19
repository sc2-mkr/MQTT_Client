package fxml.gui.handlers;

import fxml.gui.subscriber.TopicInfoGUI;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import services.mqtt.subscriber.MqttSubscribersManager;
import services.utils.logs.Logger;

import java.text.MessageFormat;

public class GUITopicsSubscriberHandler implements GUIHandler {

    private ScrollPane topicsPane;

    private MqttSubscribersManager manager;

    private VBox vbox_topicContainer = new VBox();

    private ObservableList<String> topics;

    public GUITopicsSubscriberHandler(ScrollPane topicsPane, MqttSubscribersManager manager) {
        this.topicsPane = topicsPane;
        this.manager = manager;
        JavaFxObservable.emitOnChanged(manager.getObservableTopicsList())
                .subscribe(
                        list -> {
                            topics = list;
                            Platform.runLater(this::updateGUI);
                        },
                        error -> Logger.getInstance().logError(MessageFormat.format("GUI Subscriber Topics Handler: {0}", error.getMessage())),
                        () -> {}
                );
        setStaticGUISettings();
    }

    private void setStaticGUISettings() {
        vbox_topicContainer.setPadding(new Insets(5));
        vbox_topicContainer.setSpacing(5);
    }

    /**
     * Method to be called for updating the graphics
     */
    @Override
    public void updateGUI() {
        vbox_topicContainer.getChildren().clear();
        for (String topic : topics) {
            Pane subGui = TopicInfoGUI.getInstance().generateGUI(manager, topic, !topic.equals("ALL MESSAGES"));
            subGui.setPadding(new Insets(5));
            subGui.setOnMouseClicked((event) -> manager.changeMessagesTopic(topic));
            vbox_topicContainer.getChildren().add(subGui);
        }
        topicsPane.setContent(vbox_topicContainer);
    }
}
