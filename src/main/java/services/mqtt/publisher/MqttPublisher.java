package services.mqtt.publisher;

import services.mqtt.messagges.MqttMessageExtended;

import java.util.Timer;
import java.util.TimerTask;

public class MqttPublisher implements Runnable {

    private MqttMessageExtended msg;
    private int interval;
    private boolean isLoop;

    private Timer timer = new Timer();
    private TimerTask task;

    public MqttPublisher(MqttMessageExtended msg, int interval, boolean isLoop) {
        this.msg = msg;
        this.interval = interval;
        this.isLoop = isLoop;

        task = new MqttTask(msg);
    }

    @Override
    public void run() {
        if (isLoop) timer.schedule(task, 0, interval);
        else timer.schedule(task, 0);
    }

    public void stop() {
        timer.cancel();
    }

    public MqttMessageExtended getMsg() {
        return msg;
    }
}

