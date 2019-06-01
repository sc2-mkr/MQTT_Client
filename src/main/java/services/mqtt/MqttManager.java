package services.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.text.MessageFormat;

public class MqttManager {

    private String broker;
    private String port;
    private String clientId;
    private MemoryPersistence persistence = new MemoryPersistence();
    private MqttClient client;

    private boolean isConnected = false;

    public MqttManager(String broker, String port, String clientId) {
        this.broker = broker;
        this.port = port;
        this.clientId = clientId;
    }

    // TODO make MqttConnectionManager class
    public boolean connect() {
        if (isConnected) disconnect();
        try {
            String url = MessageFormat.format("tcp://{0}:{1}", broker, port);
            client = new MqttClient(url, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);
            System.out.println("Connected");
            isConnected = true;
            MqttListenerManager.getInstance().setClient(client); // TODO move to appropriate position
            return true;
        } catch (MqttException e) {
            System.err.println(MessageFormat.format("MQTT connect: {0}", e.getMessage()));
            isConnected = false;
            return false;
        }
    }

    public void disconnect() {
        if (isConnected) {
            try {
                client.disconnect();
            } catch (MqttException e) {
                System.err.println(MessageFormat.format("MQTT disconnect: {0}", e.getMessage()));
            }
        }
        System.out.println("Disconnected");
    }

    public MqttClient getClient() {
        return client;
    }
}
