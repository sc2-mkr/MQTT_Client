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

public class PlainTextExporterFactory implements ExporterFactory {

    @Override
    public void export() {
        // Get arrayList of messages to be concatenated
        ArrayList<String> msgs = new ArrayList<>();
        Logger.getInstance().getLogs().stream().forEach(log -> msgs.add(log.getMessage()));

        // Concat messages
        String text = msgs.stream().collect(Collectors.joining("\n"));

        File file = FileSaver.getInstance().getFile();

        if (file != null) {
            try {
                WriteOnFile.getInstance().write(file, text);
            } catch (IOException e) {
                Logger.getInstance().logError(MessageFormat.format(
                        "Logs export: {0}",
                        e.getMessage()));
                return;
            }
        }

        Logger.getInstance().logClient(MessageFormat.format(
                "Logs saved successfully. Location: {0}",
                Objects.requireNonNull(file).getAbsolutePath())
        );
    }
}
