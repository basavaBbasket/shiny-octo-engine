package TMS.configAndUtilities;

import com.bigbasket.automation.utilities.AutomationUtilities;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.bigbasket.automation.BaseSettings.serverName;

public class SkuData {
    private static AtomicInteger OFFSET_COUNTER = new AtomicInteger(0);
    private static int TMS_UPPERBOUND_FOR_OFFSET = 18;

    public static Map<String, String> getSkuDetailsMap(String env)
    {
        Map<String, String> skuData = new HashMap<>();
        String query = "select external_sku_id, id, length, breadth, height, fragile, stackable  from product_productdescription limit 1 offset "+OFFSET_COUNTER.getAndIncrement()%TMS_UPPERBOUND_FOR_OFFSET+";";
        String dbName = env+"-"+"SKU-CROMA";
        JsonObject jsonObject = new JsonObject(TmsDBQueries.getSkuData(dbName,query));

        skuData.put("external_sku_id", String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("external_sku_id")));
        skuData.put("id",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("id")));
        skuData.put("length",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getDouble("length")));
        skuData.put("breadth",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getDouble("breadth")));
        skuData.put("height",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getDouble("height")));
        skuData.put("fragile" , String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("fragile")));
        skuData.put("stackable",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("stackable")));

        return skuData;
    }
}
