package fxml.controllers;

import exceptions.TranslationNotFoundException;
import fxml.gui.ListenerGUI;
import fxml.gui.TaskGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import services.mqtt.MqttListenerManager;
import services.mqtt.MqttManager;
import services.mqtt.MqttTaskManager;
import services.translation.Translation;

import java.util.UUID;

public class ProgramController {

    // MQTT
    MqttManager mqttManager;

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

    // CENTER
    private FlowPane p_tasksPane = new FlowPane();
    @FXML
    private Button btn_addTask;
    @FXML
    private ScrollPane scrollp_tasksContainer;
    private FlowPane p_listenersContainer = new FlowPane();
    @FXML
    private Button btn_addListener;
    @FXML
    private ScrollPane scrollp_listenersContainer;

    // BOTTOM
    @FXML
    private Label lbl_statusFixedText;
    // END GUI

    @FXML
    private Label lbl_status;


    public ProgramController() {
        Platform.runLater(() -> {
            translateGUIElements();
        });
    }

    private void translateGUIElements() {

        try {
            // TOP
            lbl_ipAddress.setText(Translation.getInstance().getString("lbl_ipAddress"));
            lbl_port.setText(Translation.getInstance().getString("lbl_port"));
            lbl_clientId.setText(Translation.getInstance().getString("lbl_clientId"));
            btn_generateUUID.setText(Translation.getInstance().getString("btn_generateUUID"));
            btn_connect.setText(Translation.getInstance().getString("btn_connect"));

            // CENTER
            btn_addTask.setText(Translation.getInstance().getString("btn_addTask"));
            btn_addListener.setText(Translation.getInstance().getString("btn_addListener"));

            // BOTTOM
            lbl_statusFixedText.setText(Translation.getInstance().getString("lbl_statusFixedText"));
        } catch (TranslationNotFoundException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void connectToBroker(MouseEvent event) {
        mqttManager = new MqttManager(tf_ipAddress.getText(), tf_port.getText(), tf_clientId.getText());
        mqttManager.connect();
    }

    @FXML
    private void generateUUID(MouseEvent event) {
        tf_clientId.setText(UUID.randomUUID().toString());
    }

    @FXML
    private void addTask(MouseEvent event) {
        TaskGUI task = MqttTaskManager.getInstance().createNewTaskGui(mqttManager);
        p_tasksPane.getChildren().add(task);
        scrollp_tasksContainer.setContent(p_tasksPane);
    }

    @FXML
    private void addListener(MouseEvent event) {
        ListenerGUI listener = MqttListenerManager.getInstance().createNewListenerGui(mqttManager);
        p_listenersContainer.getChildren().add(listener);
        scrollp_listenersContainer.setContent(p_listenersContainer);
    }
}
