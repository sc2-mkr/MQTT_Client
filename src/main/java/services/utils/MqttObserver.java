package services.utils;

import services.mqtt.MqttMessageExtended;

public interface MqttObserver {
    void update(MqttMessageExtended msg);
}
