package bbnow.testcases.hertz;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.order.external.GetOrderDetailsApi;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class hertzDataBaseChanges extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "Check DB changes across multiple tables- sa_dm,sa_dm_ecg,sa_dm_slots,global_dm_config,route_order", slug = "Verify Tables in hertz db ")
    @Test(groups = {"bbnow" , "regression","bbnow-status"})

    public void verifyTables(){
        AutomationReport report = getInitializedReport(this.getClass(), false);

        /**
        String xService = "123";//can be valid string
        String xCaller = "123";//can be valid string
        String xEcId = Config.bbnowConfig.getString("entry_context_id");;//todo
        String entryContext = Config.bbnowConfig.getString("entry_context");//todo
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));


        //placing order
        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[1].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[1].search_term2");
        String[] searchTerms = {searchTerm1,searchTerm2};
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");

        String orderId = OrderPlacement.placeBBNowOrder("bbnow" , 10 , creds[0], areaName, 1, searchTerms, false, false, report);
        report.log("Order Id: " + orderId,true);





        GetOrderDetailsApi getOrderDetailsApi = new GetOrderDetailsApi(xService,xCaller,xEcId,entryContext,orderId,report);
        Response response = getOrderDetailsApi.getOrderDetails();
        JsonArray arr = new JsonArray(response.body().asString());
        int sa_id = arr.getJsonObject(0).getJsonObject("details").getInteger("sa_id");
        int dm_id = arr.getJsonObject(0).getJsonObject("details").getInteger("dm_id");
        report.log("sa_id:"+sa_id,true);
        report.log("dm_id:"+dm_id,true);**/

        ArrayList<String > myElementToSearch = new ArrayList<>();

        String query1  = "describe global_dm_config";
        JSONObject globalDmConfig=new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getHertzDb(), query1,report));
        JSONArray globalDmConfigJsonArray = globalDmConfig.getJSONArray("results");
        myElementToSearch.add("dispatch_time_offset_for_the_slots");
        Assert.assertEquals(true,findElement(globalDmConfigJsonArray, myElementToSearch));
        report.log("global_dm_config table contains dispatch_time_offset_for_the_now_slots ",true);
        myElementToSearch.clear();
       // report.log("l"+ myElementToSearch,true);

        String query2  = "describe sa_dm_config";
        JSONObject saDmConfig=new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getHertzDb(), query2,report));
        JSONArray saDmConfigJsonArray = saDmConfig.getJSONArray("results");
        myElementToSearch.add("sa_id");
        myElementToSearch.add("dm_id");
        myElementToSearch.add("dispatch_time_offset_for_the_slots");
        myElementToSearch.add("dispatch_time_offset_for_the_now_slots");
        myElementToSearch.add("enable_delivery_photos");
        myElementToSearch.add("enable_customer_to_cee_calls");
        myElementToSearch.add("allow_assignment_from_ui");
        myElementToSearch.add("location_sync_interval");
        myElementToSearch.add("trip_location_sync_interval");
        myElementToSearch.add("bin_scan_delta");
        myElementToSearch.add("allow_qr_scan_checkin");
        Assert.assertEquals(true,findElement(saDmConfigJsonArray, myElementToSearch));
        report.log("sa_dm_config : ",true);
        myElementToSearch.clear();


        String query3  = "describe global_satype_config";
        JSONObject globalSaTypeConfig=new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getHertzDb(), query3,report));
        JSONArray globalSaTypeConfigJsonArray = globalSaTypeConfig.getJSONArray("results");
        myElementToSearch.add("sa_type");
        myElementToSearch.add("enable_eq");
        myElementToSearch.add("enable_sync_cee_device_payload");
        Assert.assertEquals(true,findElement(globalSaTypeConfigJsonArray, myElementToSearch));
        report.log("global_satype_config : ",true);
        myElementToSearch.clear();

        String query4 = "describe sa_dm_ecg_config";
        JSONObject saDmECG=new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getHertzDb(), query4,report));
        JSONArray saDmECGArray = saDmECG.getJSONArray("results");
        myElementToSearch.add("sa_id");
        myElementToSearch.add("dm_id");
        myElementToSearch.add("ec_group_id");
        myElementToSearch.add("dispatch_time_offset_for_the_slots");
        myElementToSearch.add("dispatch_time_offset_for_the_now_slots");
        Assert.assertEquals(true,findElement(saDmECGArray, myElementToSearch));
        report.log("sa_dm_ecg_config: ",true);
        myElementToSearch.clear();

        String query5 = "describe sa_dm_slot_config";
        JSONObject saDmSlotConfig=new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getHertzDb(), query5,report));
        JSONArray saDmSlotConfigArray = saDmSlotConfig.getJSONArray("results");
        myElementToSearch.add("sa_id");
        myElementToSearch.add("dm_id");
        myElementToSearch.add("slot_definition_id");
        myElementToSearch.add("slot_type_id");
        Assert.assertEquals(true,findElement(saDmSlotConfigArray, myElementToSearch));
        report.log("sa_dm_slot_config: ",true);
        myElementToSearch.clear();



        String orderId = "1000764714";
        String order_query = "select * from route_order where order_id="+orderId;
        JSONObject orderRoute=new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getHertzDb(), order_query,report));
        boolean orderRouteEntry = (int)orderRoute.get("numRows")>0?true:false;
        Assert.assertEquals(true,orderRouteEntry);



















    }

    public boolean findElement(JSONArray jsonArray , ArrayList<String> myElementToSearch)
    {

        List<Boolean> ans = new ArrayList<Boolean>();
        for(String element: myElementToSearch) {
            boolean found = false;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray ls = (JSONArray) jsonArray.get(i);
                if(ls.get(0).equals(element))
                {
                    found = true;
                }

            }

          ans.add(found);
        }

        System.out.println(ans);



        return ans.stream().allMatch(b->b);
    }

}
