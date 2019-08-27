package fxml.io.logExporters;

import services.io.FileSaver;
import services.utils.io.WriteOnFile;
import services.utils.logs.Logger;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class PlainTextExporter implements ExporterFactory {

    @Override
    public void export() {
        String text = getText();

        // Get file
        File file = FileSaver.getInstance().getFile();

        if (file != null) {
            try {
                WriteOnFile.getInstance().write(file, text);

                Logger.getInstance().logClient(MessageFormat.format(
                        "Logs saved successfully. Location: {0}",
                        Objects.requireNonNull(file).getAbsolutePath())
                );
            } catch (IOException e) {
                Logger.getInstance().logError(MessageFormat.format(
                        "Logs export: {0}",
                        e.getMessage()));
                return;
            }
        }
    }

    private String getText() {
        // Get arrayList of messages to be concatenated
        ArrayList<String> msgs = new ArrayList<>();
        Logger.getInstance().getLogs().stream().forEach(log -> msgs.add(log.getMessage()));

        // Concat messages
        return msgs.stream().collect(Collectors.joining("\n"));
    }
}
