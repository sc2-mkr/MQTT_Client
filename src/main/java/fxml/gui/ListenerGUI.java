package fxml.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import org.eclipse.paho.client.mqttv3.MqttException;
import services.mqtt.MqttManager;
import services.mqtt.MqttMessageExtended;

public class ListenerGUI extends FlowPane {

    // GUI
    private Label lbl_topic = new Label("TOPIC");
    private TextField tf_topic = new TextField();
    private Label lbl_content = new Label("CONTENT");
    private TextField tf_content = new TextField();
    private Button btn_listen = new Button("LISTEN");

    private MqttManager manager;

    public ListenerGUI(MqttManager manager) {
        this.manager = manager;
        tf_content.setEditable(false);
        btn_listen.setOnAction(e -> startListening(tf_topic.getText()));
        getChildren().addAll(lbl_topic, tf_topic, lbl_content, tf_content, btn_listen);
    }


    // TODO manage exception
    private void startListening(String topic) {
        if (!tf_topic.getText().isEmpty())
            try {
                manager.getClient().subscribe(topic);
            } catch (MqttException e) {
                e.printStackTrace();
            }
    }

    public void updateContent(MqttMessageExtended msg) {
        if (!tf_topic.getText().isEmpty() && tf_topic.getText().equals(msg.getTopic()))
            tf_content.setText(new String(msg.getPayload()));
    }
}
