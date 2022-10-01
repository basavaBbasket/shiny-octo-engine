package TMS.configAndUtilities;

import com.bigbasket.automation.utilities.AutomationUtilities;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.bigbasket.automation.BaseSettings.serverName;

public class MemberData {

    private static AtomicInteger OFFSET_COUNTER = new AtomicInteger(0);
    private static int TMS_UPPERBOUND_FOR_OFFSET = 18;

    public static Map<String, String> getMemberDetailsMap(String env)
    {
        Map<String, String> memberData = new HashMap<>();
        String query = "select ref_id as external_member_id, city, first_name, last_name, email, mobile_no, SUBSTRING_INDEX(SUBSTRING_INDEX(address1, ' ', 1), ' ', -1) as house_no, SUBSTRING_INDEX(SUBSTRING_INDEX(address1, ' ', 2), ' ', -1) as apartment_name, area, zipcode as pincode, SUBSTRING_INDEX(SUBSTRING_INDEX(address1, ' ', 3), ' ', -1) as address1, landmark from member_member where zipcode='401101' limit 1 offset "+OFFSET_COUNTER.getAndIncrement()%TMS_UPPERBOUND_FOR_OFFSET+";";
        String dbName = env+"-"+"HULK-CROMA";
        JsonObject jsonObject = new JsonObject(TmsDBQueries.getMemberData(dbName,query));

        memberData.put("external_member_id", String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("external_member_id")));
        //memberData.put("external_member_id", "12345678");
        memberData.put("first_name",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("first_name")));
        memberData.put("last_name",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("last_name")));
        memberData.put("email",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("email")));
        memberData.put("mobile_no",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("mobile_no")));
        //memberData.put("mobile_no","8910030597");
        memberData.put("city" , String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("city")));
        memberData.put("house_no",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("house_no")));
        memberData.put("apartment_name",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("apartment_name")));
        memberData.put("area",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("area")));
        memberData.put("address1",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("address1")));
        memberData.put("landmark",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getString("landmark")));
        memberData.put("pincode",String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("pincode")));
        //memberData.put("pincode","401101");

        return memberData;
    }
}
