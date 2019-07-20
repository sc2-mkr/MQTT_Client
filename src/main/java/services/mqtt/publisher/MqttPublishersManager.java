package services.mqtt.publisher;

import org.eclipse.paho.client.mqttv3.MqttClient;
import services.mqtt.messagges.MqttMessageExtended;
import services.utils.logs.Logger;

import java.text.MessageFormat;
import java.util.ArrayList;

public class MqttPublishersManager {

    private ArrayList<MqttPublisher> publishers = new ArrayList<>();

    public MqttPublishersManager(MqttClient client) {
        MqttMessageSender.getInstance().setClient(client);
    }

    public void addPublisher(MqttMessageExtended msg, int interval, boolean isLoop) {
        MqttPublisher pub = new MqttPublisher(msg, interval, isLoop);
        publishers.add(pub);
        Thread t = new Thread(pub);
        printLog(msg, interval, isLoop);
        t.start();
    }

    private void printLog(MqttMessageExtended mqttMsg, int interval, boolean isLoop) {
        String msg = MessageFormat.format("Added publisher: Topic: {0}, Payload: {1}", mqttMsg.getTopic(), mqttMsg.getPayloadString());
        Logger.getInstance().logInfo(msg);
    }

    public void stopAllPublishers() {
        publishers.stream().forEach((pub) -> pub.stop());
    }
}
