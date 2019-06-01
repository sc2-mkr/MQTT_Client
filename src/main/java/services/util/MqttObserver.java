package services.util;

import services.mqtt.MqttMessageExtended;

public interface MqttObserver {
    void update(MqttMessageExtended msg);
}
