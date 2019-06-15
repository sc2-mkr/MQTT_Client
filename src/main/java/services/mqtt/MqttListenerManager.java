package services.mqtt;

import fxml.gui.ListenerGUI;
import org.eclipse.paho.client.mqttv3.MqttClient;
import services.utils.MqttObserver;

import java.util.ArrayList;

public class MqttListenerManager implements MqttObserver {

    private static MqttListenerManager instance = new MqttListenerManager();

    private static ArrayList<ListenerGUI> listeners = new ArrayList<>();
    private static MqttMessageReceiver msgReceiver;
//    private MqttClient client;

    private MqttListenerManager() {
    }

    public static MqttListenerManager getInstance() {
        return instance;
    }

    public ListenerGUI createNewListenerGui(MqttManager manager) {
        ListenerGUI listenerGui = new ListenerGUI(manager);
        listeners.add(listenerGui);
        return listenerGui;
    }

    public void setClient(MqttClient client) {
//        this.client = client;
        msgReceiver = new MqttMessageReceiver(client, this); // TODO move to appropriate position
    }

    @Override
    public void update(MqttMessageExtended msg) {
        for (ListenerGUI listener : listeners) {
            listener.updateContent(msg);
        }
    }
}
