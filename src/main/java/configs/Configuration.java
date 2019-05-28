package configs;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.HashMap;

public class Configuration {
    private final String JSON_PATH = "/configs/configs.json";

    private static Configuration instance = new Configuration();

    private HashMap<String, String> configs;
    public boolean isConfigAvailable = true;

    public static Configuration getInstance() {
        return instance;
    }

    private Configuration() {
        try {
            String json = getJSON();
            configs = new ObjectMapper().readValue(json, HashMap.class);
        } catch (IOException e) {
            System.err.println(MessageFormat.format("Configuration - IOException: {0}", e.getMessage()));
            isConfigAvailable = false;
        } catch (NullPointerException e) {
            System.err.println(MessageFormat.format("Configuration - NullPointerException: {0}", e.getMessage()));
            isConfigAvailable = false;
        }
    }

    private String getJSON() throws IOException, NullPointerException {
        File file = new File(getClass().getResource(JSON_PATH).getFile());

        return new String(Files.readAllBytes(file.toPath()));
    }

    public String getValue(String property) {
        return configs.get(property);
    }
}
