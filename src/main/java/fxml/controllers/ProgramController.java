package fxml.controllers;

import exceptions.TranslationNotFoundException;
import fxml.gui.TaskGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import services.mqtt.MqttManager;
import services.mqtt.MqttTaskManager;
import services.translation.Translation;

import java.util.UUID;

public class ProgramController {

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


    // BOTTOM
    @FXML
    private Label lbl_statusFixedText;
    @FXML
    private Label lbl_status;
    // END GUI

    // MQTT
    MqttManager mqttManager;


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
        TaskGUI taskGui = MqttTaskManager.getInstance().createNewTaskGui(mqttManager);
        p_tasksPane.getChildren().add(taskGui);
        scrollp_tasksContainer.setContent(p_tasksPane);
    }
}
