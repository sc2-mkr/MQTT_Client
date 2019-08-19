package services.mqtt.subscriber;

import services.mqtt.MqttManager;
import services.mqtt.messagges.MqttMessageExtended;

import java.util.ArrayList;

@Deprecated
public class MqttSubscriber {

    private MqttManager manager;
    private String topic;

    private ArrayList<MqttMessageExtended> messages = new ArrayList<>();

    public MqttSubscriber(MqttManager manager, String topic) {
        this.manager = manager;
        this.topic = topic;
    }

//    public boolean isTopicOfThisSubsciber(String topic) {
//        return this.topic.equals(topic);
//    }

    public void addMessage(MqttMessageExtended msg) {
        messages.add(msg);
    }

    public String getTopic() {
        return topic;
    }

    public ArrayList<MqttMessageExtended> getMessages() {
        return messages;
    }
}
