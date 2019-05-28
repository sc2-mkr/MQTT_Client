package services.mqtt;

import exceptions.InvalidQosException;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.Timer;
import java.util.TimerTask;

public class MqttTask implements Runnable {

    MqttMessage msg;

    private int interval;

    private Timer timer = new Timer();

    private MqttMessageSender msgSender;

    private Task task;

    // Flag that indicate if the content in run method should be performed
//    private volatile boolean running = true;

    public MqttTask(MqttMessage msg, MqttClient client, int interval) {
        this.msg = msg;
        this.interval = interval;
        msgSender = new MqttMessageSender(client);
        task = new Task(msg, msgSender);
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
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