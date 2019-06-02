package services.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import services.util.logs.LogSystem;

import java.text.MessageFormat;

public class MqttConnectionManager {

    private MqttClient client;

    public MqttConnectionManager(MqttClient client) {
        this.client = client;
    }

    public boolean connect() throws MqttException {
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        System.out.println(MessageFormat.format("Connecting to broker: {0}", client.getServerURI()));
        LogSystem.getInstance().printLog(MessageFormat.format("Connecting to broker: {0}", client.getServerURI()));
        client.connect(connOpts);
        LogSystem.getInstance().printLog("Connected");
        return true;
    }

    public void disconnect() throws MqttException {
        client.disconnect();
        System.out.println("Disconnected"); // TODO use log class
    }
}
