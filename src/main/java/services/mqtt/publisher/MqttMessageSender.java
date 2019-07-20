package services.mqtt.publisher;

import exceptions.InvalidQosException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

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
        //System.out.println("Message published"); // TODO logging
    }

    public void setClient(MqttClient client) {
        this.client = client;
    }
}