package services.utils.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class WriteOnFile {
    private static WriteOnFile instance = new WriteOnFile();

    public static WriteOnFile getInstance() {
        return instance;
    }

    private WriteOnFile() {
    }

    public void write(File file, String text, boolean append) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, append));
        writer.write(text);

        writer.close();
    }

    public void write(File file, String text) throws IOException {
        write(file, text, false);
    }
}
