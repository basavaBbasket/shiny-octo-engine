package bbnow.testcases.member;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.utilities.AutomationUtilities;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataProviderClass extends WebSettings {

    @DataProvider(name = "get-membersource-membertype-combinations")
    public Object[] dataProviderMethod() {

        //Combination of sourceid & member type {"SourceName","SourceId","MemberType","MemberTypeId"}
        Object[][] combinations = {
                {"BigBasket", 1, "Normal", 1},
                {"BigBasket", 1, "Institutional", 2},
                {"BigBasket", 1, "Kirana", 5},
                {"BigBasket", 1, "Mandi", 16},
                {"Fresho", 11, "Normal", 1},
                {"Dunzo", 6, "Normal", 1},
                {"PB-FNV", 8, "Normal", 1}
        };
        Object[] obj = new Object[combinations.length];
        for (int i = 0; i < combinations.length; i++) {
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("src", combinations[i][0]);
            dataMap.put("src_id", combinations[1][1]);
            dataMap.put("member_type", combinations[i][2]);
            dataMap.put("member_type_id", combinations[i][3]);
            dataMap.put("member_id", getMemberId(combinations[1][1], combinations[i][3]));
            obj[i] = dataMap;
        }
        return obj;
    }

    private String getMemberId(Object srcId, Object memTypeId) {
        JsonObject jsonObject = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select id from member_member where source_id=" + srcId + " and member_type_id=" + memTypeId + " order by id desc limit 1;"));
        if (jsonObject.getInteger("numRows") == 0)
            return "null";
        else
            return String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("id"));
    }

    public static ArrayList<Integer> getEcIdList(int sourceId, int memberTypeId){
        JsonObject jsonObject = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select ec_id from member_member_ec_group where source_id="+sourceId+" and member_type_id="+memberTypeId+";"));
        if (jsonObject.getInteger("numRows")>0) {
            ArrayList<Integer> ecIdList = new ArrayList<>();
            JsonArray rows = jsonObject.getJsonArray("rows");
            for (int i = 0; i < rows.size(); i++) {
                ecIdList.add(rows.getJsonObject(i).getInteger("ec_id"));
            }
            return ecIdList;
        } else {
            return new ArrayList<>();
        }
    }

}
