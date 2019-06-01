package services.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttMessageReceiver implements MqttCallback {

    private MqttListenerManager manager;
//    private MqttClient client;

    public MqttMessageReceiver(MqttClient client, MqttListenerManager manager) {
        this.manager = manager;
        client.setCallback(this);
    }

    // TODO manage connection lost
    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        MqttMessageExtended msg = new MqttMessageExtended(mqttMessage);
        msg.setTopic(s);
        manager.update(msg);
    }

    // TODO manage delivery complete
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
