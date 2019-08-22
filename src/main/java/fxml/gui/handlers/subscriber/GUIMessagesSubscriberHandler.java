package fxml.gui.handlers.subscriber;

import configs.Configuration;
import fxml.gui.handlers.GUIHandler;
import fxml.gui.subscriber.MessageGUI;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import services.mqtt.messagges.MqttMessageExtended;
import services.mqtt.subscriber.MqttSubscribersManager;
import services.utils.logs.Logger;
import services.utils.regex.RegexUtil;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GUIMessagesSubscriberHandler implements GUIHandler {

    private ScrollPane messagesPane;
    private Label lbl_messageTopic;
    private Label lbl_messageData;
    private Label lbl_messageQos;
    private TextArea txta_messagePayload;

    private VBox vbox_messagesContainer = new VBox();

    private ArrayList<MqttMessageExtended> messages;
    private String currentTopic = "ALL MESSAGES";

    private MqttSubscribersManager manager;

    public GUIMessagesSubscriberHandler(
            ScrollPane messagesPane,
            Label lbl_messageTopic,
            Label lbl_messageData,
            Label lbl_messageQos,
            TextArea txta_messagePayload,
            MqttSubscribersManager manager) {
        this.messagesPane = messagesPane;
        this.lbl_messageQos = lbl_messageQos;
        this.lbl_messageTopic = lbl_messageTopic;
        this.lbl_messageData = lbl_messageData;
        this.txta_messagePayload = txta_messagePayload;
        this.manager = manager;

        startMessagesObserver();
        startCurrentTopicObserver();

        setStaticGUISettings();
    }

    private void startMessagesObserver() {
        JavaFxObservable.emitOnChanged(manager.getObservableMessagesList())
                .subscribe(
                        list -> {
                            messages = new ArrayList<>(list);
                            Platform.runLater(this::updateGUI);
                        },
                        error -> Logger.getInstance().logError(MessageFormat.format("GUI Subscriber Messages Handler (Messages): {0}", error.getMessage())),
                        () -> {}
                );
    }

    private void startCurrentTopicObserver() {
        JavaFxObservable.emitOnChanged(manager.getObservableCurrentTopic())
                .subscribe(
                        topic -> {
                            currentTopic = topic.get(0);
                            Platform.runLater(this::updateGUI);
                        },
                        error -> Logger.getInstance().logError(MessageFormat.format("GUI Subscriber Messages Handler (Topic): {0}", error.getMessage())),
                        () -> {}
                );
    }

    private void setStaticGUISettings() {
        vbox_messagesContainer.setPadding(new Insets(5));
        vbox_messagesContainer.setSpacing(5);
    }

    /**
     * Method to be called for updating the graphics
     */
    @Override
    public void updateGUI() {
        vbox_messagesContainer.getChildren().clear();

        ArrayList<MqttMessageExtended> msgs = getMessagesFromCurrentTopic();

        // Show last messasges on top
        for (int i = msgs.size() - 1; i >= 0; i--) {
            Pane msgGui = MessageGUI.getInstance().generateGUI(msgs.get(i));
            msgGui.setPadding(new Insets(5));
            int finalI = i;
            msgGui.setOnMouseClicked(event -> changeInspectedMessage(msgs.get(finalI)));
            vbox_messagesContainer.getChildren().add(msgGui);
        }
        messagesPane.setContent(vbox_messagesContainer);
    }

    private ArrayList<MqttMessageExtended> getMessagesFromCurrentTopic() {
        if (currentTopic.equals("") || currentTopic.isEmpty()) return new ArrayList<>();
        else {
            // Condition for filtering
            Predicate<MqttMessageExtended> byTopic = msg -> RegexUtil.getInstance().match(currentTopic ,msg.getTopic());

            return messages.stream()
                    .filter(byTopic)
                    .collect(Collectors.toCollection(ArrayList::new));

        }
    }

    private void changeInspectedMessage(MqttMessageExtended msg) {
        lbl_messageTopic.setText(msg.getTopic());
        lbl_messageData.setText(new SimpleDateFormat(Configuration.getInstance().getValue("dateFormat"))
                .format(msg.getDate()));
        lbl_messageQos.setText(MessageFormat.format("QOS {0}", msg.getQos()));
        txta_messagePayload.setText(new String(msg.getPayload()));
    }
}
