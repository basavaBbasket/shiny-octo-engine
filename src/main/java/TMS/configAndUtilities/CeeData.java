package TMS.configAndUtilities;

import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CeeData {
    private static AtomicInteger OFFSET_COUNTER = new AtomicInteger(0);
    private static int TMS_UPPERBOUND_FOR_OFFSET ;

    public static Map<String, String> getCeeDetailsMap(String env)
    {
        Map<String, String> ceeData = new HashMap<>();
        String dbName = env+"-"+"HERTZ";//todo
        TMS_UPPERBOUND_FOR_OFFSET = TmsDBQueries.getUpperBoundForOffset(dbName,"select count(*) from cee");
        String query = "select username,password from cee limit 1 offset"+OFFSET_COUNTER.getAndIncrement()%TMS_UPPERBOUND_FOR_OFFSET+";";
        JsonObject jsonObject = new JsonObject(TmsDBQueries.getCeeData(dbName,query));

        ceeData.put("username", String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("username")));
        ceeData.put("password",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("password")));

        return ceeData;
    }
}
