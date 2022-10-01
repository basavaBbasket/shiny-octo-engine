package bbnow.testcases.eta.order_eta;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.DeliveryBinMethods;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.PickingMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
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
import org.testng.reporters.jq.INavigatorPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetOrderEtaStatus extends BaseTest {


    @DescriptionProvider(slug = "validate response schema for Eta api.", description = "1. Fetch member id from member_member table \n" +
            "            2. Place BBNow Order\n" +
            "            3. Perform picking and then do binning. \n" +
            "            4. Bring Order status to Ready To Ship. \n" +
            "            5. Call GetOrderEta and do schema validation ", author = "tushar")

    @Test(groups = { "check_bbnow_test","bbnow" , "regression","dl2","eta","bbnow-schema-validation","dl2-schema-validation"})
    public void getOrderEtaTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        String bb_decoded_mid;
        JsonObject jsonObject = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select id from member_member where mobile_no="+creds[0] +";"));
        if (jsonObject.getInteger("numRows") == 0)
            bb_decoded_mid = null;
        else
            bb_decoded_mid =  String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("id"));

        String xService = "123";       // can be any valid string
        String xCaller = "hertz-svc";  // can be any valid string
        String entrycontext= Config.bbnowConfig.getString("entry_context");
        String entrycontextid=Config.bbnowConfig.getString("entry_context_id");
        String clientUserSheetName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_client_user_sheet_name");
        String areaName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_area_name");
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));
        String searchTerm = Config.bbnowConfig.getString("bbnow_stores[1].category_slug1");
        String searchType = Config.bbnowConfig.getString("bbnow_stores[1].search_type2");

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> adminCookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        Map<String,String> memberCookie = MemberCookie.getMemberCookieForCustomAddress(cred[0],areaName,report);

        int skuID =  EligibleSkuMethods.fetchSkuVisibleAndAvailableInPrimaryBin(memberCookie,adminCookie,
                fcId,entrycontext,Integer.parseInt(entrycontextid),areaName,searchTerm,searchType,report);

        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID), 2);
        String orderId = OrderPlacement.placeBBNowOrder("bbnow" , 10,cred[0], areaName, skuMap, true, false, report);

        PickingMethods.pickingFlowModified(Integer.parseInt(orderId), fcId, adminUserName, adminCookie, report);
        DeliveryBinMethods.perfomOrderBinMapping(fcId,Integer.parseInt(orderId),"binning",adminCookie,report);

        OrderStatusAPI orderStatusAPI = new OrderStatusAPI("bbnow","10","monolith","monolith",report);
        ArrayList<Integer> skuList = new ArrayList<>();
        skuList.add(skuID);
        orderStatusAPI.changeOrderStatus("ready_to_ship",orderId,skuList);

        msvc.eta.internal.GetOrderEta getOrderEta =new msvc.eta.internal.GetOrderEta(orderId,xCaller,entrycontext,entrycontextid,bb_decoded_mid,report);
        Response response1 = getOrderEta.getOrderEta("schema//eta//internal//get-order-eta-200.json");
        report.log("Status code: " +  response1.getStatusCode(),true);
        report.log("Get Order Eta response: " + response1.prettyPrint(),true);
    }
}
