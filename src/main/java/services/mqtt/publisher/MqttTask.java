package services.mqtt.publisher;

import org.eclipse.paho.client.mqttv3.MqttException;
import regexExpressions.ExpressionsAnalyzer;
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
            ExpressionsAnalyzer ea = new ExpressionsAnalyzer();
            String interpretedPayload = ea.compute(msg.getPayloadString());
            MqttMessageSender.getInstance().sendMessage(msg.getTopic(), interpretedPayload, msg.getQos());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}