package services.mqtt;

import fxml.gui.TaskGUI;

import java.util.ArrayList;

public class MqttTaskManager {
    private static MqttTaskManager ourInstance = new MqttTaskManager();

    private static ArrayList<TaskGUI> tasks = new ArrayList<>();

    private MqttTaskManager() {
    }

    public static MqttTaskManager getInstance() {
        return ourInstance;
    }

    public TaskGUI createNewTaskGui(MqttManager manager) {
        TaskGUI taskGui = new TaskGUI(manager);
        tasks.add(taskGui);
        return taskGui;
    }

    public void stopAllTasks() {
        for (TaskGUI task : tasks) {
            task.stopTask();
        }
    }
}
