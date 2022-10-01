package TMS.configAndUtilities;

import TMS.configAndUtilities.Config;

import java.util.HashMap;
import java.util.Map;

public class CreateHeader {

    public static Map<String,String> createHeader()
    {
        Map<String, String> headerMap = new HashMap<>();

        headerMap.put("orderType" , "Normal");
        headerMap.put("entryContextId",Config.tmsConfig.getString("entry_context_id"));
        headerMap.put("entryContext" ,Config.tmsConfig.getString("entry_context"));
        headerMap.put("originContext" , Config.tmsConfig.getString("originContext"));
        headerMap.put("xCaller" ,"123");//todo
        headerMap.put("tenantId" , "2");//todo
        headerMap.put("projectId" ,"bb-tms");//todo
        headerMap.put("external_order_id", "21350312");//todo

        return headerMap;
    }
}
