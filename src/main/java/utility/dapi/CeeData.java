package utility.dapi;

import com.bigbasket.automation.utilities.AutomationUtilities;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class CeeData {

    public static Map<String, String> getCeeDetailsMap(String env,int sa_id)
    {
        Map<String, String> ceeData = new HashMap<>();
        String dbName = env+"-"+"HERTZ";
        String query = "select id,username,password from cee where sa_id="+sa_id+ " limit 1"+";";
        JsonObject jsonObject = new JsonObject(AutomationUtilities.executeMicroserviceDatabaseQuery(dbName, query));

        ceeData.put("id", String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("id")));
        ceeData.put("username", String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("username")));
        ceeData.put("password",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("password")));

        return ceeData;
    }

    public static void doForceLogOut(String env, int id)
    {
        String dbName = env+"-"+"HERTZ";
        String query = "update cee set is_logged_in=0 where id="+id+";";
        AutomationUtilities.executeMicroserviceDatabaseQuery(dbName, query);
    }


}
