package services.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import services.utils.logs.Logger;

import java.text.MessageFormat;

public class MqttConnectionManager {

    private MqttClient client;

    // If the client is connected to the broker
    private boolean isConnected = false;

    public MqttConnectionManager(MqttClient client) {
        this.client = client;
    }

    public void connect() throws MqttException {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        Logger.getInstance().log(MessageFormat.format("Connecting to broker: {0} ...", client.getServerURI()));
        client.connect(connOpts);
        Logger.getInstance().log("Connected to broker.");
        isConnected = true;
    }

    public void disconnect() throws MqttException {
        client.disconnect();
        Logger.getInstance().log("Disconnecting from broker...");
        isConnected = false;
    }

    public boolean isConnected() {
        return isConnected;
    }
}
