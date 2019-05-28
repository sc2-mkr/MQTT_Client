package services.dataStorage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteOnDisk implements SaveData {

    public WriteOnDisk() {
    }

    public void write(String path, String text) {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(path));

            String[] lines = text.split("\n");

            for (int i = 0; i < lines.length; i++) {
                printWriter.println(lines[i]);
            }
            printWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
