package services.mqtt.publisher;

import javafx.collections.*;
import org.eclipse.paho.client.mqttv3.MqttClient;
import services.mqtt.messagges.MqttMessageExtended;
import services.utils.logs.Logger;

import java.text.MessageFormat;

public class MqttPublishersManager {

    private ObservableList<MqttPublisher> publishers = FXCollections.observableArrayList();

//    private ArrayList<MqttPublisher> publishers = new ArrayList<>();

    public MqttPublishersManager(MqttClient client) {
        MqttMessageSender.getInstance().setClient(client);
    }

    public void addPublisher(MqttMessageExtended msg, int interval, boolean isLoop) {
        MqttPublisher pub = new MqttPublisher(msg, interval, isLoop);
        publishers.add(pub);
        Thread t = new Thread(pub);
        t.start();
        printLog(msg);
    }

    private void printLog(MqttMessageExtended mqttMsg) {
        String msg = MessageFormat.format("Added publisher.\nTopic: {0}", mqttMsg.getTopic());
        Logger.getInstance().logInfo(msg);
    }

    public void stopAllPublishers() {
        publishers.stream().forEach(pub -> pub.stop());
    }

    public ObservableList<MqttPublisher> getPublishers() {
        return publishers;
    }
}
