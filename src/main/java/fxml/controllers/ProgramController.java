package fxml.controllers;

import fxml.io.FileSaver;
import fxml.logger.ConsoleLogger;
import fxml.logger.LabelStatusLogger;
import fxml.logger.TextAreaLogsLogger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import services.mqtt.MqttManager;
import services.mqtt.messagges.MqttMessageExtended;
import services.utils.fxUtils.TextFlowUtils;
import services.utils.io.WriteOnFile;
import services.utils.logs.Logger;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.UUID;

public class ProgramController {

    // MQTT
    MqttManager manager;

    // BEGIN GUI
    // TOP
    @FXML
    private TextField tf_ipAddress;
    @FXML
    private TextField tf_port;
    @FXML
    private TextField tf_clientId;
    @FXML
    private Button btn_connect;
    @FXML
    private Button btn_disconnect;

    // CENTER

    // Subscription
    @FXML
    private TextField tf_subTopic;
    @FXML
    private ScrollPane scrollp_topics;
    @FXML
    private ScrollPane scrollp_messages;

    // Publish
    @FXML
    private TextField tf_topic;
    @FXML
    private TextField tf_payload;
    @FXML
    private RadioButton rb_qos0, rb_qos1, rb_qos2;
    private int currentQos = 0;
    @FXML
    private ToggleGroup qos;
    @FXML
    private CheckBox cb_retained;
    @FXML
    private CheckBox cb_loop;
    @FXML
    private Label lbl_interval;
    @FXML
    private TextField tf_interval;


    @FXML
    private TextFlow tflow_logs;


    // BOTTOM

    @FXML
    private Label lbl_status;
    // END GUI

    public ProgramController() {
    }

    public void initialize() {
        Logger.getInstance().addLogger(new LabelStatusLogger(lbl_status));
        Logger.getInstance().addLogger(new TextAreaLogsLogger(tflow_logs));
        Logger.getInstance().addLogger(new ConsoleLogger()); // TODO remove after tests
        scrollp_topics.setFitToWidth(true);
        scrollp_messages.setFitToWidth(true);

        rb_qos0.setUserData(0);
        rb_qos1.setUserData(1);
        rb_qos2.setUserData(2);
        qos.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (qos.getSelectedToggle() != null) {
                currentQos = (int) qos.getSelectedToggle().getUserData();
            }
        });

        cb_loop.selectedProperty().addListener((toggle) -> {
            lbl_interval.setDisable(!cb_loop.isSelected());
            tf_interval.setDisable(!cb_loop.isSelected());

        });
    }

    @FXML
    private void connectToBroker(MouseEvent event) {
        manager = new MqttManager(
                tf_ipAddress.getText(),
                tf_port.getText(),
                tf_clientId.getText(),
                scrollp_topics,
                scrollp_messages,
                btn_connect,
                btn_disconnect
        );
        manager.connect();
    }

    @FXML
    public void disconnect(MouseEvent event) {

    }

    @FXML
    private void generateUUID(MouseEvent event) {
        tf_clientId.setText(UUID.randomUUID().toString());
    }

    @FXML
    private void subscribe(MouseEvent event) {
        manager.subscribe(tf_subTopic.getText());
    }

    @FXML
    private void publish(MouseEvent event) {
        if (fieldsEmpty()) {
            // TODO show window dialog
        }
        else {
            MqttMessage msg = new MqttMessage(tf_payload.getText().getBytes());
            msg.setQos(currentQos);
            msg.setRetained(cb_retained.isSelected());
            MqttMessageExtended msgExtended = new MqttMessageExtended(tf_topic.getText(), msg);
            try {
                manager.publish(msgExtended, Integer.parseInt(tf_interval.getText()), cb_loop.isSelected());
            } catch (NumberFormatException e) {
                Logger.getInstance().logError(MessageFormat.format("MQTT publish: {0}", e.getMessage()));
            }
        }

    }

    private boolean fieldsEmpty() {
        return tf_topic.getText().isEmpty() ||
                tf_payload.getText().isEmpty() ||
                (tf_interval.getText().isEmpty() && cb_loop.isSelected());
    }

    @FXML
    private void exportLogs(MouseEvent event) {
        String text = TextFlowUtils.getInstance().getTextFromTextFlow(tflow_logs);
        File file = FileSaver.getInstance().getFile();

        if (file != null) {
            try {
                WriteOnFile.getInstance().write(file, text);
            } catch (IOException e) {
                Logger.getInstance().logError(MessageFormat.format(
                        "Logs export: {0}",
                        e.getMessage()));
                return;
            }
        }

        Logger.getInstance().logEditor(MessageFormat.format(
                "Logs saved successfully. Location: {0}",
                Objects.requireNonNull(file).getAbsolutePath())
        );
    }

    public void windowClosing() {
        if (manager != null) manager.getPubManager().stopAllPublishers();
        Platform.exit();
        System.exit(0);
    }
}
