package services.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttMessageExtended extends MqttMessage {

    private String topic;

    public MqttMessageExtended() {
    }

    public MqttMessageExtended(byte[] payload) {
        super(payload);
    }

    public MqttMessageExtended(String topic, byte[] payload, int qos) {
        super(payload);
        super.setQos(qos);
        this.topic = topic;
    }

    public MqttMessageExtended(MqttMessage msg) {
        super(msg.getPayload());
        super.setQos(msg.getQos());
        super.setId(msg.getId());
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
