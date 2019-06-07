package fxml.controllers;

import fxml.gui.TaskGUI;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import services.mqtt.MqttManager;
import services.mqtt.MqttTaskManager;
import services.util.logs.LogSystem;

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
    private FlowPane p_tasksPane = new FlowPane();
    @FXML
    private Button btn_addTask;
    @FXML
    private ScrollPane scrollp_tasksContainer;
    private FlowPane p_listenersContainer = new FlowPane();


    // Subscription
    @FXML
    private TextField tf_subTopic;
//    @FXML
//    private Button btn_subscibe;
    @FXML
    private ScrollPane scrollp_topics;
    @FXML
    private ScrollPane scrollp_messages;




    @FXML
    private TextArea ta_logs;





    // BOTTOM
    @FXML
    private Label lbl_statusFixedText;
    // END GUI

    @FXML
    private Label lbl_status;


    public ProgramController() {}

    public void initialize(){
        LogSystem.getInstance().setLbl_status(lbl_status);
        LogSystem.getInstance().setTa_logs(ta_logs);
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

    @FXML
    private void addTask(MouseEvent event) {
        TaskGUI task = MqttTaskManager.getInstance().createNewTaskGui(manager);
        p_tasksPane.getChildren().add(task);
        scrollp_tasksContainer.setContent(p_tasksPane);
    }

    @FXML
    private void subscribe(MouseEvent event) {
//        ListenerGUI listener = MqttListenerManager.getInstance().createNewListenerGui(manager);
//        p_listenersContainer.getChildren().add(listener);
//        scrollp_listenersContainer.setContent(p_listenersContainer);
        manager.subscribe(tf_subTopic.getText());
    }
}
