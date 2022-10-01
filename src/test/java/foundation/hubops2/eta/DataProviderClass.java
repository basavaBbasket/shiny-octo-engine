package foundation.hubops2.eta;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.utilities.AutomationUtilities;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.io.IOUtils;
import org.testng.annotations.DataProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DataProviderClass extends WebSettings {

    @DataProvider(parallel = true, name = "slots-eta-data-provider")
    public Object[] dataProviderMethod() throws IOException {
        //Input stream to load file.
        InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("foundation//hubops2//eta//findSlots.json");
        //Creating JsonObject from string.
        String jsonStr = IOUtils.toString(inputStream, "UTF-8");
        JsonArray jsonArray = new JsonObject(jsonStr).getJsonArray(AutomationUtilities.getEnvironmentFromServerName(serverName));
        Object[] obj = new Object[jsonArray.size()];
        for (int i = 0; i < jsonArray.size(); i++) {
            Map<Object, Object> dataMap = new HashMap<>();
            dataMap.put("name", jsonArray.getJsonObject(i).getString("name"));
            dataMap.put("sa_id", jsonArray.getJsonObject(i).getInteger("sa_id"));
            dataMap.put("dm_id", jsonArray.getJsonObject(i).getInteger("dm_id"));
            dataMap.put("src_fc", jsonArray.getJsonObject(i).getInteger("src_fc"));
            dataMap.put("dest_fc", jsonArray.getJsonObject(i).getInteger("dest_fc"));
            dataMap.put("contact_hub_id", jsonArray.getJsonObject(i).getInteger("contact_hub_id"));
            obj[i] = dataMap;
        }
        return obj;
    }

}
