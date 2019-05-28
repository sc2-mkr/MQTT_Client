package services.mqtt;

import exceptions.InvalidQosException;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.TimerTask;

public class Task extends TimerTask {

    private MqttMessage msg;

    private MqttMessageSender msgSender;

    public Task(MqttMessage msg, MqttMessageSender msgSender) {
        this.msg = msg;
        this.msgSender = msgSender;
    }

    @Override
    public void run() {
        // TODO manage exception
        try {
            msgSender.sendMessage(msg.getTopic(), msg.getMessage(), msg.getQos());
        } catch (InvalidQosException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
