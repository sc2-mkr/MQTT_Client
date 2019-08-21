package services.utils.regex;

public class MqttRegexUtil {
    private static MqttRegexUtil instance = new MqttRegexUtil();

    public static MqttRegexUtil getInstance() {
        return instance;
    }

    private MqttRegexUtil() {
    }

    public String getRegexedTopic(String topic) {
        return topic.replace("+", "[^/]+").replace("#", ".*");
    }
}
