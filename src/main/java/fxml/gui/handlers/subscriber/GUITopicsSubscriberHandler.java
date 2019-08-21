package fxml.gui.handlers.subscriber;

import fxml.gui.handlers.GUIHandler;
import fxml.gui.subscriber.TopicInfoGUI;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import services.mqtt.messagges.MqttMessageExtended;
import services.mqtt.subscriber.MqttSubscribersManager;
import services.utils.logs.Logger;
import services.utils.regex.RegexUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GUITopicsSubscriberHandler implements GUIHandler {

    private ScrollPane topicsPane;

    private MqttSubscribersManager manager;

    private VBox vbox_topicContainer = new VBox();

    private Map<String, String> topics;

    public GUITopicsSubscriberHandler(ScrollPane topicsPane, MqttSubscribersManager manager) {
        this.topicsPane = topicsPane;
        this.manager = manager;

        startTopicsObservable();
        startMessagesObservable();

        setStaticGUISettings();
    }

    private void startTopicsObservable() {
        JavaFxObservable.emitOnChanged(manager.getObservableTopicsMap())
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
        for (Map.Entry<String, String> topic : topics.entrySet()) {
            Pane subGui = TopicInfoGUI.getInstance().generateGUI(
                    manager,
                    topic.getKey(),
                    countMessagesOfTopic(topic.getValue()),
                    !topic.getKey().equals("ALL MESSAGES"));
            subGui.setPadding(new Insets(5));
            subGui.setOnMouseClicked((event) -> manager.changeMessagesTopic(topic.getValue()));
            vbox_topicContainer.getChildren().add(subGui);
        }
        topicsPane.setContent(vbox_topicContainer);
    }

    private int countMessagesOfTopic(String topic) {
        if (topic.equals("") || topic.isEmpty()) return 0;
        else {
            // Condition for filtering
            Predicate<MqttMessageExtended> byTopic = msg -> RegexUtil.getInstance().match(topic ,msg.getTopic());

            return manager.getObservableMessagesList().stream()
                    .filter(byTopic)
                    .collect(Collectors.toCollection(ArrayList::new)).size();
        }
    }
}
