package fxml;

import configs.Configuration;
import fxml.controllers.WindowEventManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Program extends Application {

    private Parent rootNode;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void init() {
        if (Platform.isFxApplicationThread()) loadFxml();
        else Platform.runLater(() -> loadFxml());
    }

    public void loadFxml() {
        try {
            FXMLLoader fxmlLoader;
            fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Program.fxml"));
            rootNode = fxmlLoader.load();
        } catch (NullPointerException e) {
            System.err.println("Program - NullPointerException: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Program - IOException: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Manage window closure
        primaryStage.setOnCloseRequest(e -> WindowEventManager.windowClosing());

        primaryStage.setScene(new Scene(rootNode));
        //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/icon.png")));
        primaryStage.setTitle("MQTT Client");
        primaryStage.setMinHeight(new Double(Configuration.getInstance().getValue("minWindowHeight")));
        primaryStage.setMinWidth(new Double(Configuration.getInstance().getValue("minWindowWidth")));
        primaryStage.show();
    }
}
