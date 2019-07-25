package services.mqtt.publisher;

import exceptions.InvalidQosException;
import javafx.scene.paint.Color;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import services.utils.logs.Logger;

import java.text.MessageFormat;

public class MqttMessageSender {

    private static MqttMessageSender instance = new MqttMessageSender();

    private MqttClient client;

    public MqttMessageSender() {
    }

    public static MqttMessageSender getInstance() {
        return instance;
    }

    synchronized public void sendMessage(String topic, String content, int qos) throws MqttException {
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        client.publish(topic, message);
        Logger.getInstance().logInfo(MessageFormat.format(
                "Message sended.\nTopic: {0}, content: {1}, qos: {2}",
                topic,
                content,
                qos));
    }

    public void setClient(MqttClient client) {
        this.client = client;
    }
}