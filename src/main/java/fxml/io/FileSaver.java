package fxml.io;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class FileSaver {
    private static FileSaver instance = new FileSaver();

    public static FileSaver getInstance() {
        return instance;
    }

    private FileSaver() {
    }

    public File getFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Logs");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("TXT", "*.txt")
        );
        File file = fileChooser.showSaveDialog(new Stage());
        return file;
    }
}
