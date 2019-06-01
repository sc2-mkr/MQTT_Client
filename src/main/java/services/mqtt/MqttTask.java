package services.mqtt;

import exceptions.InvalidQosException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Timer;
import java.util.TimerTask;

public class MqttTask implements Runnable {

    MqttMessageExtended msg;

    private int interval;

    private Timer timer = new Timer();

    private MqttMessageSender msgSender;

    private Task task;

    // Flag that indicate if the content in run method should be performed
//    private volatile boolean running = true;

    public MqttTask(MqttMessageExtended msg, MqttClient client, int interval) {
        this.msg = msg;
        this.interval = interval;
        msgSender = new MqttMessageSender(client);
        task = new Task(msg, msgSender);
    }

    @Override
    public void run() {
        timer.schedule(task, 0, interval);
    }

    public void stop() {
        timer.cancel();
    }
}

// Task executed every interval
class Task extends TimerTask {

    private MqttMessageExtended msg;

    private MqttMessageSender msgSender;

    public Task(MqttMessageExtended msg, MqttMessageSender msgSender) {
        this.msg = msg;
        this.msgSender = msgSender;
    }

    @Override
    public void run() {
        // TODO manage exception
        try {
            msgSender.sendMessage(msg.getTopic(), new String(msg.getPayload()), msg.getQos());
        } catch (InvalidQosException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}