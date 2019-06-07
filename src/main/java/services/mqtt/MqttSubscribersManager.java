package services.mqtt;

import fxml.gui.subscriber.MessageGUI;
import fxml.gui.subscriber.TopicInfoGUI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import services.util.logs.LogSystem;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Optional;

public class MqttSubscribersManager implements MqttCallback {

    private MqttManager manager;

    private ArrayList<MqttSubscriber> subscribers = new ArrayList<>();
    private ScrollPane topicsPane;
    private ScrollPane messagesPane;

    private VBox p_topicContainer = new VBox();
    private VBox p_messagesContainer = new VBox();

    // Corrent topic's messages showed
    private String currentTopic = "";

    public MqttSubscribersManager(MqttManager manager, ScrollPane topicsPane, ScrollPane messagesPane) {
        this.manager = manager;
        this.manager.getClient().setCallback(this);
        this.topicsPane = topicsPane;
        this.messagesPane = messagesPane;

        // GUI
        p_topicContainer.setPadding(new Insets(5));
        p_topicContainer.setSpacing(5);

        p_messagesContainer.setPadding(new Insets(5));
        p_messagesContainer.setSpacing(5);
    }

    public void addSubscription(String topic) {
        if (isTopicAlreadySubscribed(topic)) return;

        MqttSubscriber sub = new MqttSubscriber(manager, topic);
        subscribers.add(sub);

        // TODO improve with IMqttMessageListener
        try {
            manager.getClient().subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        Platform.runLater(() -> updateGui());
        LogSystem.getInstance().printLog(MessageFormat.format("Subscribed to topic \"{0}\"", topic));
    }

    private Optional<MqttSubscriber> findSubscriberToTopic(String topic) {
        return subscribers.stream()
                .filter(sub -> sub.getTopic().equals(topic))
                .findFirst();
    }

    public void removeSubscription(MqttSubscriber subscriber) {
        // TODO manage exception
        try {
            manager.getClient().unsubscribe(subscriber.getTopic());
            subscribers.remove(subscriber);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        String topic = subscriber.getTopic();

        if (currentTopic.equals(topic)) {
            Optional<MqttSubscriber> first = subscribers.stream().findFirst();
            if (first.isPresent()) {
                Platform.runLater(() -> changeMessagesTopic(first.get()));
                currentTopic = first.get().getTopic();
            } else Platform.runLater(() -> cleanMessages());
        }

        Platform.runLater(() -> updateGui());
        LogSystem.getInstance().printLog(MessageFormat.format("Unsubscribed to topic \"{0}\"", topic));
    }

    private void updateGui() {
        p_topicContainer.getChildren().clear();
        for (MqttSubscriber sub : subscribers) {
            Pane subGui = TopicInfoGUI.getInstance().generateGUI(this, sub);
            subGui.setPadding(new Insets(5));
            subGui.setOnMouseClicked((event) -> changeMessagesTopic(sub));
            p_topicContainer.getChildren().add(subGui);
        }
        topicsPane.setContent(p_topicContainer);
    }

    private boolean isTopicAlreadySubscribed(String topic) {
        for (MqttSubscriber sub : subscribers) {
            if (sub.getTopic().equals(topic)) {
                return true;
            }
        }
        return false;
    }

    private void cleanMessages() {
        messagesPane.setContent(new Pane());
    }

    @Override
    public void connectionLost(Throwable throwable) {
        // TODO manage connection lost
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        for (MqttSubscriber sub : subscribers) {
            if (sub.getTopic().equals(s)) {
                sub.addMessage(new MqttMessageExtended(s, mqttMessage));
                if (currentTopic.equals(s)) {
                    Platform.runLater(() -> changeMessagesTopic(sub));
                }
                break;
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // TODO manage delivery complete
    }

    private void changeMessagesTopic(MqttSubscriber sub) {
        currentTopic = sub.getTopic();
        ArrayList<MqttMessageExtended> messages = sub.getMessages();
        p_messagesContainer.getChildren().clear();
        for (int i = messages.size() - 1; i >= 0; i--) {
            Pane msgGui = MessageGUI.getInstance().generateGUI(messages.get(i));
            msgGui.setPadding(new Insets(5));
            p_messagesContainer.getChildren().add(msgGui);
        }
        messagesPane.setContent(p_messagesContainer);
    }
}
