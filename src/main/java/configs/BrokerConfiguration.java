package configs;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import services.utils.fxUtils.AlertUtil;
import services.utils.logs.Logger;

import java.io.*;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Optional;

public class BrokerConfiguration {
    private static BrokerConfiguration instance = new BrokerConfiguration();

    private final String JSON_PATH = System.getenv("APPDATA") +
                    Configuration.getInstance().getValue("appDataPath") +
                    "\\config\\";
    public boolean isConfigAvailable = true;
    private ObservableList<ConnectionProfile> profiles;

    public static BrokerConfiguration getInstance() {
        return instance;
    }

    private BrokerConfiguration() {
        checkPathExist();
        try {
            String json = getJSON();
            ArrayList<ConnectionProfile> p = new ObjectMapper().readValue(json, new TypeReference<ArrayList<ConnectionProfile>>() {});
            profiles = FXCollections.observableArrayList(p);
        } catch (Exception e) {
            Logger.getInstance().logError(MessageFormat.format("BrokerConfiguration (Constructor) - Exception: {0}", e.getMessage()));
            isConfigAvailable = false;
        }
    }

    private void checkPathExist() {
        File path = new File(JSON_PATH);
        if(!path.exists()) {
            path.mkdirs();
            try {
                createJSONInAppData();
            } catch (IOException e) {
                System.err.println("IO " + e);
            } catch (NullPointerException e) {
                System.err.println("Null " + e);
            }
        }
    }

    private void createJSONInAppData() throws IOException, NullPointerException {
        FileWriter fileWriter = new FileWriter(JSON_PATH + "\\connectionProfiles.json");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        String json = getJSONInResources();
        printWriter.print(json);
        printWriter.close();
    }

    private String getJSONInResources() throws IOException, NullPointerException {
        String filePath = getClass().getResource("/configs/connectionProfiles.json").getFile();
        File file = new File(filePath);
        return new String(Files.readAllBytes(file.toPath()));
    }

    private String getJSON() throws IOException, NullPointerException {
        String filePath = JSON_PATH + "\\connectionProfiles.json";
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        String st;
        String content = "";
        while ((st = br.readLine()) != null)
            content = MessageFormat.format("{0}\n{1}", content, st);

        return content;
    }

    public ObservableList<ConnectionProfile> getProfiles() {
        return profiles;
    }

    public Optional<ConnectionProfile> getProfileByName(String name) {
        return profiles.stream()
                .filter(profile -> profile.getName().equals(name))
                .findFirst();
    }

    public void addConnectionProfile(
            String name,
            String address,
            String port,
            String clientId,
            boolean isErasable) {
        if(profileAlreadyExist(name))
            AlertUtil.getInstance().showErrorAndWait(
                    "Connection Profile",
                    "",
                    MessageFormat.format("Profile with name \"{0}\" already exist.", name));
        else {
            ConnectionProfile profile = new ConnectionProfile(name, address, port, clientId, isErasable);
            profiles.add(profile);
            saveProfiles();
        }
    }

    private boolean profileAlreadyExist(String name) {
        if(getProfileByName(name).isPresent()) return true;
        else return false;
    }

    public void saveProfiles() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            FileOutputStream outputStream = new FileOutputStream(JSON_PATH + "\\connectionProfiles.json");
            mapper.writeValue(outputStream, new ArrayList<>(profiles));
        } catch (IOException e) {
            Logger.getInstance().logError(MessageFormat.format("Broker Configuration (Save profile): {0}", e.getMessage()));
        }
    }
}
