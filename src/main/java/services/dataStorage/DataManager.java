package services.dataStorage;

public interface DataManager {

    void saveData(String data, String path);

    String readData(String path);

}
