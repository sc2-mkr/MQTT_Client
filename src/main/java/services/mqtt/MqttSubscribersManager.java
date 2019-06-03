package services.mqtt;

import fxml.gui.subscriber.MessageGUI;
import fxml.gui.subscriber.SubscribeGUI;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import services.util.logs.LogSystem;

import java.text.MessageFormat;
import java.util.ArrayList;

public class MqttSubscribersManager implements MqttCallback {

    private MqttManager manager;

    private ArrayList<MqttSubscriber> subscribers = new ArrayList<>();
    private ScrollPane topicsPane;
    private ScrollPane messagesPane;

    private VBox p_topicContainer = new VBox();
    private VBox p_messagesContainer = new VBox();

    // Corrent topic's messages showed
    private String currentTopic = "";

    public MqttSubscribersManager(MqttManager manager, ScrollPane topicsPane, ScrollPane messagesPane) {
        this.manager = manager;
        this.manager.getClient().setCallback(this);
        this.topicsPane = topicsPane;
        this.messagesPane = messagesPane;

        // GUI
        p_topicContainer.setPadding(new Insets(5));
        p_topicContainer.setSpacing(5);

        p_messagesContainer.setPadding(new Insets(5));
        p_messagesContainer.setSpacing(5);
    }

    public void addSubscription(String topic) {
        if(isTopicAlreadySubscribed(topic)) return;

        MqttSubscriber sub = new MqttSubscriber(manager, topic);
        subscribers.add(sub);

        // TODO improve with IMqttMessageListener
        try {
            manager.getClient().subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }

//        updateGUI();
        Pane subGui = SubscribeGUI.getInstance().generateGUI(sub);
        subGui.setPadding(new Insets(5));
        subGui.setOnMouseClicked((event) -> changeMessagesTopic(sub));
        p_topicContainer.getChildren().add(subGui);
        topicsPane.setContent(p_topicContainer);

        LogSystem.getInstance().printLog(MessageFormat.format("Added subscription to topic \"{0}\"", topic));
    }

    private boolean isTopicAlreadySubscribed(String topic) {
        for(MqttSubscriber sub : subscribers) {
            if(sub.getTopic().equals(topic)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        for(MqttSubscriber sub : subscribers) {
            if(sub.getTopic().equals(s)) {
                sub.addMessage(new MqttMessageExtended(s, mqttMessage));
                if(currentTopic.equals(s)) {
//                    System.out.println("same topic - " + s);
                    Platform.runLater(() ->  changeMessagesTopic(sub));
                }
                break;
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    private void changeMessagesTopic(MqttSubscriber sub) {
        currentTopic = sub.getTopic();
        System.out.println("message changed: "+ currentTopic);
        ArrayList<MqttMessageExtended> messages = sub.getMessages();
        p_messagesContainer.getChildren().clear();
        for(MqttMessageExtended msg : messages) {
            Pane msgGui = MessageGUI.getInstance().generateGUI(msg);
            msgGui.setPadding(new Insets(5));
            p_messagesContainer.getChildren().add(msgGui);
        }
        messagesPane.setContent(p_messagesContainer);
    }
}
