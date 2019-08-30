package fxml;

import configs.Configuration;
import fxml.controllers.ProgramController;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Program extends Application {

    private Parent rootNode;
    private ProgramController controller;

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
            controller = fxmlLoader.getController();
        } catch (NullPointerException e) {
            System.err.println("Program - NullPointerException: " + e.getMessage()); // TODO use logger
        } catch (IOException e) {
            System.err.println("Program - IOException: " + e.getMessage()); // TODO use logger
        }
    }

    @Override
    public void start(Stage primaryStage) {
        // Manage window closure
        primaryStage.setOnCloseRequest(e -> controller.windowClosing());

        primaryStage.setScene(new Scene(rootNode));
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/favicon/web_hi_res_512.png")));
        primaryStage.setTitle("MQTT Client");
        primaryStage.setMinHeight(new Double(Configuration.getInstance().getValue("minWindowHeight")));
        primaryStage.setMinWidth(new Double(Configuration.getInstance().getValue("minWindowWidth")));
        primaryStage.show();
    }

    @Override
    public void stop() {

    }
}
