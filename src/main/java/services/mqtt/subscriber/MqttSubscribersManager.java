package services.mqtt.subscriber;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import services.mqtt.MqttManager;
import services.mqtt.messagges.MqttMessageExtended;
import services.utils.logs.Logger;

import java.text.MessageFormat;
import java.util.Optional;

public class MqttSubscribersManager implements MqttCallback {

    private MqttManager manager;

    // ArrayList with all arrived messages
    private ObservableList<MqttMessageExtended> messages = FXCollections.observableArrayList();
    // ArrayList with all subscribed topic
    private ObservableList<String> topics = FXCollections.observableArrayList();

    // Current topic's messages showed
    // TRUST ME! I PERHAPS KNOW WHAT I DO!
    private ObservableList<String> currentTopic = FXCollections.observableArrayList("ALL MESSAGES");

    public MqttSubscribersManager(MqttManager manager) {
        this.manager = manager;
        this.manager.getClient().setCallback(this);

        // Option for showing all messages
        topics.add("ALL MESSAGES");
    }

    public void addSubscription(String topic) {
        if (isTopicAlreadySubscribed(topic)) return;

        try {
            manager.getClient().subscribe(topic);
            topics.add(topic);

        } catch (MqttException e) {
            Logger.getInstance().logError(MessageFormat.format("MQTT subscription: {0}", e.getMessage()));
            return;
        }

        Logger.getInstance().logInfo(MessageFormat.format("Subscribed to topic \"{0}\"", topic));
    }

    public void removeTopic(String topic) {
        // TODO manage exception
        try {
            manager.getClient().unsubscribe(topic);
            topics.remove(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        if (currentTopic.equals(topic)) {
            Optional<String> firstTopic = topics.stream().findFirst();
            firstTopic.ifPresent(s -> currentTopic.set(0, s));
        }

        Logger.getInstance().logInfo(MessageFormat.format("Unsubscribed from topic \"{0}\"", topic));
    }

    private boolean isTopicAlreadySubscribed(String topic) {
        for (String t : topics) {
            if (t.equals(topic)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        // TODO manage connection lost
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        Logger.getInstance().logInfo(MessageFormat.format(
                "Message arrived.\nID: {0}, topic: {1}, payload: {2}, qos: {3}",
                mqttMessage.getId(),
                s,
                new String(mqttMessage.getPayload()),
                mqttMessage.getQos()));

        MqttMessageExtended msg = new MqttMessageExtended(s, mqttMessage);
        messages.add(msg);

//        if(currentTopic.equals(s)) Platform.runLater(() -> updateMessagesGui(s));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // TODO manage delivery complete
    }

    public void changeMessagesTopic(String topic) {
        currentTopic.set(0, topic);
    }

    public ObservableList<String> getObservableTopicsList() {
        return topics;
    }

    public ObservableList<String> getObservableCurrentTopic() {
        return currentTopic;
    }

    public ObservableList<MqttMessageExtended> getObservableMessagesList() {
        return messages;
    }
}
