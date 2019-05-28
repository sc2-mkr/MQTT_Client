package services.mqtt;

import exceptions.InvalidQosException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttMessageSender {

    private MqttClient client;

    public MqttMessageSender(MqttClient client) {
        this.client = client;
    }

    public void sendMessage(String topic, String content, int qos) throws InvalidQosException, MqttException {
        if ((qos < 0) || (qos > 2)) throw new InvalidQosException(qos);
        //System.out.println("Publishing message: " + content);
        MqttMessage message = new MqttMessage(content.getBytes());
        message.setQos(qos);
        client.publish(topic, message);
        //System.out.println("Message published");
    }
}
