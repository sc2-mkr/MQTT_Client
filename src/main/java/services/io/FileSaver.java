package services.io;

import javafx.stage.*;

import java.io.File;

public class FileSaver {
    private static FileSaver instance = new FileSaver();

    private FileSaver() {
    }

    public static FileSaver getInstance() {
        return instance;
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
