package services.mqtt.publisher;

import exceptions.InvalidQosException;
import org.eclipse.paho.client.mqttv3.MqttException;
import services.mqtt.messagges.MqttMessageExtended;

import java.util.TimerTask;

class MqttTask extends TimerTask {

    private MqttMessageExtended msg;

    public MqttTask(MqttMessageExtended msg) {
        this.msg = msg;
    }

    @Override
    public void run() {
        // TODO manage exception and logging
        try {
            MqttMessageSender.getInstance().sendMessage(msg.getTopic(), new String(msg.getPayload()), msg.getQos());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}