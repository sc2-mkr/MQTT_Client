package services.translation;

import configs.Configuration;
import exceptions.ConfigFileNotFoundException;
import exceptions.TranslationNotFoundException;

import java.util.Locale;
import java.util.ResourceBundle;

public class Translation {
    private static Translation instance;

    static {
        try {
            instance = new Translation();
        } catch (ConfigFileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ResourceBundle messages;

    private Translation() throws ConfigFileNotFoundException {
        Locale location = new Locale(Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
        String bundlePath;
        if (!Configuration.getInstance().isConfigAvailable)
            throw new ConfigFileNotFoundException();

        bundlePath = Configuration.getInstance().getValue("bundlePath");
        messages = ResourceBundle.getBundle(bundlePath, location);
    }

    public static Translation getInstance() {
        return instance;
    }

    public String getString(String key) throws TranslationNotFoundException {
        if (messages.containsKey(key))
            return messages.getString(key);
        else
            throw new TranslationNotFoundException(key);
    }
}
