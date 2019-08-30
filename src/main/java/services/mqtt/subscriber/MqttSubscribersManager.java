package services.mqtt.subscriber;

import javafx.collections.*;
import org.eclipse.paho.client.mqttv3.*;
import services.mqtt.MqttManager;
import services.mqtt.messagges.MqttMessageExtended;
import services.utils.logs.Logger;
import services.utils.regex.MqttRegexUtil;

import java.text.MessageFormat;
import java.util.*;

public class MqttSubscribersManager implements MqttCallback {

    private MqttManager manager;

    // ArrayList with all arrived messages
    private ObservableList<MqttMessageExtended> messages = FXCollections.observableArrayList();
    // ArrayList with all subscribed topic
    private ObservableMap<String, String> topics = FXCollections.observableHashMap();

    // Current topic's messages showed
    // TRUST ME! I PERHAPS KNOW WHAT I DO!
    private ObservableList<String> currentTopic = FXCollections.observableArrayList(".*");

    public MqttSubscribersManager(MqttManager manager) {
        this.manager = manager;
        this.manager.getClient().setCallback(this);

        // Option for showing all messages
        topics.put("ALL MESSAGES", ".*");
    }

    public void addSubscription(String topic) {
        if (isTopicAlreadySubscribed(topic)) return;

        try {
            manager.getClient().subscribe(topic);
            topics.put(topic, MqttRegexUtil.getInstance().getRegexedTopic(topic));

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

        if (currentTopic.get(0).equals(topic)) {
            Optional<Map.Entry<String, String>> firstTopic = topics.entrySet().stream().findFirst();
            firstTopic.ifPresent(s -> currentTopic.set(0, s.getKey()));
        }

        Logger.getInstance().logInfo(MessageFormat.format("Unsubscribed from topic \"{0}\"", topic));
    }

    private boolean isTopicAlreadySubscribed(String topic) {
        for (Map.Entry<String, String> t : topics.entrySet()) {
            if (t.getKey().equals(topic)) {
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
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // TODO manage delivery complete
    }

    public void changeMessagesTopic(String topic) {
        currentTopic.set(0, topic);
    }

    public ObservableMap<String, String> getObservableTopicsMap() {
        return topics;
    }

    public ObservableList<String> getObservableCurrentTopic() {
        return currentTopic;
    }

    public ObservableList<MqttMessageExtended> getObservableMessagesList() {
        return messages;
    }
}
