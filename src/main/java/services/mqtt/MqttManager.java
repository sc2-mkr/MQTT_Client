package services.mqtt;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import services.utils.logs.Logger;

import java.text.MessageFormat;

public class MqttManager {

    private String broker;
    private String port;
    private String clientId;
    private MemoryPersistence persistence = new MemoryPersistence();
    private MqttClient client;
    private MqttConnectionManager connManager;
    private MqttSubscribersManager subManager;
    private MqttPublishersManager pubManager;

    //GUI
    private ScrollPane scrollp_topics;
    private ScrollPane scrollp_messages;

    public MqttManager(String broker, String port, String clientId, ScrollPane scrollp_topics, ScrollPane scrollp_messages, Button btn_connect, Button btn_disconnect) {
        this.broker = broker;
        this.port = port;
        this.clientId = clientId;
        this.scrollp_topics = scrollp_topics;
        this.scrollp_messages = scrollp_messages;
    }

    public void connect() {
//        if (connManager.isConnected()) disconnect();
        try {
            String url = MessageFormat.format("tcp://{0}:{1}", broker, port);
            client = new MqttClient(url, clientId, persistence);
            connManager = new MqttConnectionManager(client);
            connManager.connect();
            subManager = new MqttSubscribersManager(this, scrollp_topics, scrollp_messages);
            pubManager = new MqttPublishersManager(client);
        } catch (MqttException e) {
            Logger.getInstance().logError(MessageFormat.format("MQTT connect: {0}", e.getMessage()));
        }
    }

    public void disconnect() {
        if (connManager.isConnected()) {
            try {
                connManager.disconnect();
            } catch (MqttException e) {
                Logger.getInstance().logError(MessageFormat.format("MQTT disconnect: {0}", e.getMessage()));
            }
        }
        System.out.println("Disconnected");
    }

    public MqttClient getClient() {
        return client;
    }

    public void subscribe(String topic) {
        subManager.addSubscription(topic);
    }

    public void publish(MqttMessageExtended msg, int intervall, boolean isLoop) {
        pubManager.addPublisher(msg, intervall, isLoop);
    }

    public MqttPublishersManager getPubManager() {
        return pubManager;
    }
}
