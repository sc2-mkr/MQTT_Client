package fxml.gui.handlers;

import fxml.gui.publisher.TopicInfoGUI;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import services.mqtt.publisher.MqttPublisher;
import services.utils.logs.Logger;

import java.text.MessageFormat;

public class GUIPublishersHandler implements GUIHandler {
    private ScrollPane scrollp_pubsContainer;

    // Pane where the main pane layout is created
    private VBox tempPane = new VBox();

    // https://github.com/ReactiveX/RxJavaFX/blob/2.x/src/test/java/io/reactivex/rxjavafx/sources/JavaFxObservableTest.java
    private ObservableList<MqttPublisher> pubs;

    public GUIPublishersHandler(ScrollPane scrollp_pubsContainer, ObservableList<MqttPublisher> publishers) {
        this.scrollp_pubsContainer = scrollp_pubsContainer;
        JavaFxObservable.emitOnChanged(publishers)
                .subscribe(
                        list -> {
                            pubs = list;
                            Platform.runLater(this::updateGUI);
                        },
                        error -> Logger.getInstance().logError(MessageFormat.format("MQTT publisher: {0}", error.getMessage())),
                        () -> {});
    }

    public void setStaticGUISettings() {
        tempPane.setPadding(new Insets(5));
        tempPane.setSpacing(5);
    }

    @Override
    public void updateGUI() {
        setStaticGUISettings();

        tempPane.getChildren().clear();
        pubs.forEach(pub -> tempPane.getChildren().add(TopicInfoGUI.getInstance().generateGUI(pub)));
        scrollp_pubsContainer.setContent(tempPane);
    }
}
