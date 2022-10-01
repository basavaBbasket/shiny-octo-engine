package bbnow.testcases.eta.order_eta;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.DeliveryBinMethods;
import api.warehousecomposition.planogram_FC.PickingMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.order.internal.OrderCountAPI;
import msvc.order.internal.OrderStatusAPI;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetOrderEtaStatus extends BaseTest {


    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for Get order Eta api.",slug = "Validate  order Eta api")
    @Test(groups = { "bbnow" , "regression","dl2","eta","bbnow-schema-validation","dl2-schema-validation"})
    public void getOrderEtaTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        report.log("credn" + creds[0],true);
        String bb_decoded_mid;//todo

        JsonObject jsonObject = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select id from member_member where mobile_no="+creds[0] +";"));
        if (jsonObject.getInteger("numRows") == 0)
            bb_decoded_mid = null;
        else
            bb_decoded_mid =  String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("id"));


        String xcaller="bb-now";
        String entrycontextid="10";
        String entrycontext="bbnow";
        String xService = "123";       // can be any valid string
        String xCaller = "hertz-svc";  // can be any valid string
        String[] cred = AutomationUtilities.getUniqueAdminUser(serverName,"admin-superuser-mfa");
        String adminUser = cred[0];
        String adminPassword = cred[1];
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));
        Map<String , String> cookie = AdminCookie.getMemberCookie(adminUser,adminPassword , report);

        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[1].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[1].search_term2");
        String[] searchTerms = {searchTerm1,searchTerm2};
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");
        String orderId = OrderPlacement.placeBBNowOrder("bbnow" , 10 , creds[0], areaName, 2, "ps",searchTerms, true, false, report);
        report.log("order Id:" + orderId ,true);

        OrderCountAPI orderCountAPI =  new OrderCountAPI(entrycontext,entrycontextid,xService,xCaller,cookie,report);
        Response response = orderCountAPI.getOrderDetailsApi(orderId);
        JsonArray arr = new JsonArray(response.body().asString());
        JsonObject jsonObj = arr.getJsonObject(0);
        List<Integer> sku = new ArrayList<>();
        JsonArray itemDetailsJson = jsonObj.getJsonArray("item_details");
        for(int i = 0; i< itemDetailsJson.size() ; i++)
        {
            JsonObject itemJson = itemDetailsJson.getJsonObject(i);
            sku.add(itemJson.getInteger("sku_id"));
        }
        report.log("sku id: "+sku,true);



        msvc.eta.internal.GetOrderEta getOrderEta =new msvc.eta.internal.GetOrderEta(orderId,xcaller,entrycontext,entrycontextid,bb_decoded_mid,report);
        PickingMethods.pickingFlowModified(Integer.parseInt(orderId), fcId, adminUser, cookie, report);
        DeliveryBinMethods.perfomOrderBinMapping(fcId,Integer.parseInt(orderId),"binning",cookie,report);

        OrderStatusAPI orderStatusAPI = new OrderStatusAPI("bbnow","10","monolith","monolith",report);
        orderStatusAPI.changeOrderStatus("ready_to_ship",orderId,sku);



        Response response1 = getOrderEta.getOrderEta("schema//eta//internal//get-order-eta-200.json");
        report.log("Status code: " +  response1.getStatusCode(),true);
        report.log("Get Order Eta response: " + response1.prettyPrint(),true);
       // AutomationUtilities.executeMicroserviceDatabaseQuery("UAT-HULK","show databases;");
    }
}
