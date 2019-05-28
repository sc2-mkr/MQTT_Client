package fxml.gui;

import exceptions.TranslationNotFoundException;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import services.mqtt.MqttManager;
import services.mqtt.MqttMessage;
import services.mqtt.MqttTask;
import services.translation.Translation;

public class TaskGUI extends FlowPane {

    // GUI
    private Label lbl_topic = new Label("TOPIC");
    private TextField tf_topic = new TextField();
    private Label lbl_msg = new Label("MESSAGE");
    private TextField tf_msg = new TextField();
    private Label lbl_qos = new Label("QOS");
    private TextField tf_qos = new TextField();
    private Label lbl_interval = new Label("INTERVAL");
    private TextField tf_interval = new TextField();
    private Button btn_runTask = new Button("RUN");

    private MqttManager mqttManager;
    private MqttTask mqttTask;
    private Thread taskThread;

    /**
     * Creates a horizontal FlowPane layout with hgap/vgap = 0.
     */
    public TaskGUI(MqttManager mqttManager) {
        translateGUI();
        this.mqttManager = mqttManager;
        //btn_runTask.setOnAction(new );
        btn_runTask.setOnAction(e -> runTask());
        getChildren().addAll(lbl_topic, tf_topic, lbl_msg, tf_msg, lbl_qos, tf_qos, lbl_interval, tf_interval, btn_runTask);
    }

    private void translateGUI() {
        try {
            lbl_topic.setText(Translation.getInstance().getString("lbl_topic"));
            lbl_msg.setText(Translation.getInstance().getString("lbl_msg"));
            lbl_qos.setText(Translation.getInstance().getString("lbl_qos"));
            lbl_interval.setText(Translation.getInstance().getString("lbl_interval"));
            btn_runTask.setText(Translation.getInstance().getString("btn_runTask"));
        } catch (TranslationNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void runTask() {

        if (taskThread != null) {
            mqttTask.stop();
        }

        MqttMessage msg = new MqttMessage(
                tf_topic.getText(),
                tf_msg.getText(),
                Integer.parseInt(tf_qos.getText()));

        mqttTask = new MqttTask(
                msg,
                mqttManager.getClient(),
                Integer.parseInt(tf_interval.getText()));

        taskThread = new Thread(mqttTask);
        taskThread.run();
    }

    public void stopTask() {
        if (taskThread != null) {
            mqttTask.stop();
        }
    }
}

/*
class RunTask extends ActionEvent {

}
*/
