package fxml.gui.handlers.subscriber;

import configs.Configuration;
import fxml.gui.handlers.GUIHandler;
import fxml.gui.handlers.subscriber.messageDecoder.*;
import fxml.gui.subscriber.MessageGUI;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import services.mqtt.messagges.MqttMessageExtended;
import services.mqtt.subscriber.MqttSubscribersManager;
import services.utils.logs.Logger;
import services.utils.regex.RegexUtil;

import java.text.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GUIMessagesSubscriberHandler implements GUIHandler {

    private ScrollPane messagesPane;
    private Label lbl_messageTopic;
    private Label lbl_messageData;
    private Label lbl_messageQos;
    private TextArea txta_messagePayload;
    private ComboBox combo_messageFormat;

    private VBox vbox_messagesContainer = new VBox();

    private ArrayList<MqttMessageExtended> messages;
    private String currentTopic = "ALL MESSAGES";
    private MqttMessageExtended currentMessageInspected = new MqttMessageExtended();
    private Decoder currentDecoder = new PlainTextDecoder();

    private Map<String, Decoder> decodingMethod = new HashMap<>();

    private MqttSubscribersManager manager;

    public GUIMessagesSubscriberHandler(
            ScrollPane messagesPane,
            Label lbl_messageTopic,
            Label lbl_messageData,
            Label lbl_messageQos,
            TextArea txta_messagePayload,
            ComboBox combo_messageFormat,
            MqttSubscribersManager manager) {
        this.messagesPane = messagesPane;
        this.lbl_messageQos = lbl_messageQos;
        this.lbl_messageTopic = lbl_messageTopic;
        this.lbl_messageData = lbl_messageData;
        this.txta_messagePayload = txta_messagePayload;
        this.combo_messageFormat = combo_messageFormat;
        this.manager = manager;

        startMessagesObserver();
        startCurrentTopicObserver();

        addDecodingMethod();

        setCombo_messageFormatContent();
        setCombo_messageFormatAction();

        setStaticGUISettings();
    }

    // Add decodingMethod method here
    private void addDecodingMethod() {
        decodingMethod.put("Hex Decoder", new HexDecoder());
        decodingMethod.put("Base64 Decoder", new Base64Decoder());
        decodingMethod.put("Plain Text Decoder", new PlainTextDecoder());
    }

    private void setCombo_messageFormatContent() {
        ArrayList<String> decode = new ArrayList<>();
        decodingMethod.forEach((key, value) -> decode.add(key));
        combo_messageFormat.setItems(FXCollections.observableArrayList(decode));
        combo_messageFormat.getSelectionModel().select("Plain Text Decoder");
    }

    private void setCombo_messageFormatAction() {
        combo_messageFormat.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) changeMessageEncoding((String)newValue);
        });
    }

    private void changeMessageEncoding(String decoding) {
        currentDecoder = decodingMethod.get(decoding);
        txta_messagePayload.setText(decodingMethod.get(decoding).decode(currentMessageInspected.getPayloadString()));
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
        currentMessageInspected = msg;
        lbl_messageTopic.setText(msg.getTopic());
        lbl_messageData.setText(new SimpleDateFormat(Configuration.getInstance().getValue("dateFormat"))
                .format(msg.getDate()));
        lbl_messageQos.setText(MessageFormat.format("QOS {0}", msg.getQos()));
        txta_messagePayload.setText(currentDecoder.decode(msg.getPayloadString()));
    }
}
