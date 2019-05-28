package services.dataStorage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ReadFromDisk implements LoadData {

    BufferedReader br;

    public ReadFromDisk() {
    }

    public String read(String path) {

        String text = "";
        String currentLine;

        try {
            this.br = new BufferedReader(new FileReader(path));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            while ((currentLine = br.readLine()) != null) {
                text += currentLine + "\n";
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            br.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return text;
    }

}
