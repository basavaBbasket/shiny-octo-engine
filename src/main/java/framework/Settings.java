package framework;

import com.bigbasket.automation.WebSettings;

import java.io.IOException;
import java.util.Properties;

public class Settings extends WebSettings {
    public static Properties config;

    static {
        try {
            config = new Properties();
            config.load(Settings.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
