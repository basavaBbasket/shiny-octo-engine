package tms.configAndUtilities;


import io.restassured.path.json.JsonPath;
import io.vertx.core.json.JsonObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;

import static com.bigbasket.automation.BaseSettings.serverName;
import static com.bigbasket.automation.utilities.AutomationUtilities.getEnvironmentFromServerName;
//
public class Config {

    public static JsonPath tmsConfig;

    static {
        JsonObject jsonObject = new JsonObject(loadJsonFile("/tms/tmsConfig.json"));
        tmsConfig = new JsonPath(jsonObject.getJsonObject(getEnvironmentFromServerName(serverName)).toString());
    }

    private static String loadJsonFile(String filePath) {
        String json = "null";
        try {
            json = IOUtils.toString(Config.class.getResourceAsStream(filePath), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }


}
