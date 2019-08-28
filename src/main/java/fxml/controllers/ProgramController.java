package fxml.controllers;

import fxml.gui.handlers.ExportersHandler;
import fxml.gui.handlers.subscriber.GUIMessagesSubscriberHandler;
import fxml.gui.handlers.connection.GUIProfilesHandler;
import fxml.gui.handlers.GUIPublishersHandler;
import fxml.gui.handlers.subscriber.GUITopicsSubscriberHandler;
import services.io.FileSaver;
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
import services.utils.fxUtils.AlertUtil;
import services.utils.fxUtils.TextFlowUtils;
import services.utils.io.WriteOnFile;
import services.utils.logs.Logger;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

public class ProgramController {

    // MQTT
    MqttManager manager;

    // GUI
    // TOP
    @FXML
    private ComboBox<String> combo_profiles;
    @FXML
    private Button btn_loadProfiles;
    @FXML
    private TextField tf_profileName;
    @FXML
    private Button btn_saveProfile;



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


    @FXML
    private Label lbl_messageTopic;
    @FXML
    private Label lbl_messageData;
    @FXML
    private Label lbl_messageQos;
    @FXML
    private TextArea txta_messagePayload;
    @FXML
    private ComboBox combo_messageFormat;




    // Publish
    // Create publisher
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

    // Publishers list
    @FXML
    private ScrollPane scrollp_pubsContainer;


    @FXML
    private TextFlow tflow_logs;

    // Logs
    @FXML
    private ComboBox combo_exporters;
    @FXML
    private Button btn_exportLogs;


    // BOTTOM

    @FXML
    private Label lbl_status;

    // Hsndlers
    private ExportersHandler exportersHandler;

    // GUI Handlers
    private GUIPublishersHandler publishersHandler;
    private GUIMessagesSubscriberHandler messagesSubscriberHandler;
    private GUITopicsSubscriberHandler topicsSubscriberHandler;
    private GUIProfilesHandler connectionProfilesHandler;

    public ProgramController() {
    }

    public void initialize() {
        // TODO create method for loggers initialization
        Logger.getInstance().addLogger(new LabelStatusLogger(lbl_status));
        Logger.getInstance().addLogger(new TextAreaLogsLogger(tflow_logs));
        Logger.getInstance().addLogger(new ConsoleLogger()); // TODO remove after tests
        scrollp_topics.setFitToWidth(true);
        scrollp_messages.setFitToWidth(true);

        // TODO create method for radio buttons initialization
        rb_qos0.setUserData(0);
        rb_qos1.setUserData(1);
        rb_qos2.setUserData(2);
        qos.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (qos.getSelectedToggle() != null) {
                currentQos = (int) qos.getSelectedToggle().getUserData();
            }
        });

        // TODO create method for checkbox initialization
        cb_loop.selectedProperty().addListener((toggle) -> {
            lbl_interval.setDisable(!cb_loop.isSelected());
            tf_interval.setDisable(!cb_loop.isSelected());
        });

        connectionProfilesHandler = new GUIProfilesHandler(
                combo_profiles,
                btn_loadProfiles,
                tf_ipAddress,
                tf_port,
                tf_clientId,
                tf_profileName,
                btn_saveProfile);

        exportersHandler = new ExportersHandler(combo_exporters, btn_exportLogs);
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
        publishersHandler = new GUIPublishersHandler(scrollp_pubsContainer, manager.getPubManager().getPublishers());
        topicsSubscriberHandler = new GUITopicsSubscriberHandler(scrollp_topics, manager.getSubManager());
        messagesSubscriberHandler = new GUIMessagesSubscriberHandler(
                scrollp_messages,
                lbl_messageTopic,
                lbl_messageData,
                lbl_messageQos,
                txta_messagePayload,
                combo_messageFormat,
                manager.getSubManager());
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
        if(tf_subTopic.getText().isEmpty())
            AlertUtil.getInstance().showErrorAndWait(
                    "Subscription",
                    "",
                    "Invalid topic");
        else
            manager.subscribe(tf_subTopic.getText());
    }

    @FXML
    private void publish(MouseEvent event) {
        if (fieldsEmpty()) {
            // TODO show window dialog
        } else {
            MqttMessage msg = new MqttMessage(tf_payload.getText().getBytes()); // TODO USe Regex for special function
            msg.setQos(currentQos);
            msg.setRetained(cb_retained.isSelected());
            MqttMessageExtended msgExtended = new MqttMessageExtended(tf_topic.getText(), msg);
            try {
                manager.publish(msgExtended, Integer.parseInt(tf_interval.getText()), cb_loop.isSelected());
            } catch (NumberFormatException e) {
                Logger.getInstance().logError(MessageFormat.format("MQTT publish: {0}", e.getMessage())); // TODO manage exception
            }
        }
    }

    private boolean fieldsEmpty() {
        return tf_topic.getText().isEmpty() ||
                tf_payload.getText().isEmpty() ||
                (tf_interval.getText().isEmpty() && cb_loop.isSelected());
    }

    public void windowClosing() {
        if (manager != null) manager.getPubManager().stopAllPublishers();
        Platform.exit();
        System.exit(0);
    }
}
