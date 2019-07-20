package services.utils;

import services.mqtt.messagges.MqttMessageExtended;

public interface MqttObserver {
    void update(MqttMessageExtended msg);
}
