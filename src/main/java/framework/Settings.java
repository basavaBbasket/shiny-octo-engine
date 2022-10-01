package framework;

import com.bigbasket.automation.WebSettings;

import java.io.IOException;
import java.util.Properties;

public class Settings extends WebSettings {
    public static Properties config;
    public static Properties dlConfig;

    static {
        try {
            config = new Properties();
            config.load(Settings.class.getResourceAsStream("/config.properties"));

            dlConfig = new Properties();
            dlConfig.load(Settings.class.getResourceAsStream("/dynamicLocation/dl.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
