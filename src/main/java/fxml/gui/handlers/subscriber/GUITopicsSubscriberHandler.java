package fxml.gui.handlers.subscriber;

import fxml.gui.handlers.GUIHandler;
import fxml.gui.subscriber.TopicInfoGUI;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import services.mqtt.messagges.MqttMessageExtended;
import services.mqtt.subscriber.MqttSubscribersManager;
import services.utils.logs.Logger;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GUITopicsSubscriberHandler implements GUIHandler {

    private ScrollPane topicsPane;

    private MqttSubscribersManager manager;

    private VBox vbox_topicContainer = new VBox();

    private ObservableList<String> topics;

    public GUITopicsSubscriberHandler(ScrollPane topicsPane, MqttSubscribersManager manager) {
        this.topicsPane = topicsPane;
        this.manager = manager;

        startTopicsObservable();
        startMessagesObservable();

        setStaticGUISettings();
    }

    private void startTopicsObservable() {
        JavaFxObservable.emitOnChanged(manager.getObservableTopicsList())
                .subscribe(
                        list -> {
                            topics = list;
                            Platform.runLater(this::updateGUI);
                        },
                        error -> Logger.getInstance().logError(MessageFormat.format("GUI Subscriber Topics Handler (Topics): {0}", error.getMessage())),
                        () -> {}
                );
    }

    private void startMessagesObservable() {
        JavaFxObservable.emitOnChanged(manager.getObservableMessagesList())
                .subscribe(
                        list -> {
                            Platform.runLater(this::updateGUI);
                        },
                        error -> Logger.getInstance().logError(MessageFormat.format("GUI Subscriber Topics Handler (Messages): {0}", error.getMessage())),
                        () -> {}
                );
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
            Pane subGui = TopicInfoGUI.getInstance().generateGUI(
                    manager,
                    topic,
                    countMessagesOfTopic(topic),
                    !topic.equals("ALL MESSAGES"));
            subGui.setPadding(new Insets(5));
            subGui.setOnMouseClicked((event) -> manager.changeMessagesTopic(topic));
            vbox_topicContainer.getChildren().add(subGui);
        }
        topicsPane.setContent(vbox_topicContainer);
    }

    private int countMessagesOfTopic(String topic) {
        if (topic.equals("") || topic.isEmpty()) return 0;
        else if (topic.equals("ALL MESSAGES")) {
            return manager.getObservableMessagesList().size();
        } else {
            // Condition for filtering
            Predicate<MqttMessageExtended> byTopic = msg -> msg.getTopic().equals(topic);

            return manager.getObservableMessagesList().stream()
                    .filter(byTopic)
                    .collect(Collectors.toCollection(ArrayList::new)).size();

        }
    }
}
