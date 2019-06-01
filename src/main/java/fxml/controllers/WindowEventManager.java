package fxml.controllers;

import services.mqtt.MqttTaskManager;

public class WindowEventManager {
    private static WindowEventManager instance = new WindowEventManager();

    private WindowEventManager() {
    }

    public static WindowEventManager getInstance() {
        return instance;
    }

    public static void windowClosing() {
        stopAllTasks();
    }

    private static void stopAllTasks() {
        MqttTaskManager.getInstance().stopAllTasks();
        System.exit(0);
    }
}
