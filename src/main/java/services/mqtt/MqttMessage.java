package services.mqtt;

public class MqttMessage {

    private String topic;
    private String message;
    private int qos;

    public MqttMessage(String topic, String message, int qos) {
        this.topic = topic;
        this.message = message;
        this.qos = qos;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }
}
