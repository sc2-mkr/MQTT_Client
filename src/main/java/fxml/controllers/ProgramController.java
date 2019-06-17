package fxml.controllers;

import fxml.io.FileSaver;
import fxml.logger.ConsoleLogger;
import fxml.logger.LabelStatusLogger;
import fxml.logger.TextAreaLogsLogger;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import services.mqtt.MqttManager;
import services.mqtt.MqttMessageExtended;
import services.utils.io.WriteOnFile;
import services.utils.logs.Logger;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

public class ProgramController {

    // MQTT
    MqttManager manager;

    // BEGIN GUI
    // TOP
    @FXML
    private Label lbl_ipAddress;
    @FXML
    private TextField tf_ipAddress;
    @FXML
    private Label lbl_port;
    @FXML
    private TextField tf_port;
    @FXML
    private Label lbl_clientId;
    @FXML
    private TextField tf_clientId;
    @FXML
    private Button btn_generateUUID;
    @FXML
    private Button btn_connect;
    @FXML
    private Button btn_disconnect;

    // CENTER
//    private FlowPane p_tasksPane = new FlowPane();
//    @FXML
//    private Button btn_addTask;
//    @FXML
//    private ScrollPane scrollp_tasksContainer;
//    private FlowPane p_listenersContainer = new FlowPane();


    // Subscription
    @FXML
    private TextField tf_subTopic;
    //    @FXML
//    private Button btn_subscibe;
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
    private CheckBox cb_retained;
    @FXML
    private CheckBox cb_loop;
    @FXML
    private TextField tf_intervall;


    @FXML
    private TextFlow tflow_logs;


    // BOTTOM
    @FXML
    private Label lbl_statusFixedText;
    // END GUI

    @FXML
    private Label lbl_status;


    public ProgramController() {
    }

    public void initialize() {
        Logger.getInstance().addLogger(new LabelStatusLogger(lbl_status));
        Logger.getInstance().addLogger(new TextAreaLogsLogger(tflow_logs));
        Logger.getInstance().addLogger(new ConsoleLogger()); // TODO remove after tests
        scrollp_topics.setFitToWidth(true);
        scrollp_messages.setFitToWidth(true);
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

//    @FXML
//    private void addTask(MouseEvent event) {
//        TaskGUI task = MqttTaskManager.getInstance().createNewTaskGui(manager);
//        p_tasksPane.getChildren().add(task);
//        scrollp_tasksContainer.setContent(p_tasksPane);
//    }

    @FXML
    private void subscribe(MouseEvent event) {
        manager.subscribe(tf_subTopic.getText());
    }

    @FXML
    private void publish(MouseEvent event) {
        MqttMessage msg = new MqttMessage();
        MqttMessageExtended msgExtended = new MqttMessageExtended();
        try {
            manager.publish(msgExtended, Integer.parseInt(tf_intervall.getText()));
        } catch (NumberFormatException e) {
            Logger.getInstance().logError(MessageFormat.format("MQTT publish: {0}", e.getMessage()));
        }
    }

    @FXML
    private void exportLogs(MouseEvent event) {
        String text = "";

        // Impossible to use stream because text must be final
        for(Node row : tflow_logs.getChildren()) {
            text = MessageFormat.format("{0}{1}", text, ((Text) row).getText());
        }

        File file = FileSaver.getInstance().getFile();

        if (file != null) {
            try {
                WriteOnFile.getInstance().write(file, text);
            } catch (IOException e) {
                Logger.getInstance().logError(MessageFormat.format("Logs saver: {0}", e.getMessage()));
                return;
            }
        }

        Logger.getInstance().logEditor(MessageFormat.format("Logs saved successfully. Location: {0}", file.getAbsolutePath()));
    }
}
