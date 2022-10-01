package bbnow.schema_validation.eta.internal;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import io.restassured.response.Response;
import msvc.eta.internal.GetOrderEta;
import msvc.order.internal.OrderStatusAPI;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class GetOrderEtaTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for Get order Eta api.",slug = "Validate Get order Eta api")
    @Test(groups = {"bbnow" , "regression","dl2","eta","bbnow-schema-validation","dl2-schema-validation","unstable","testheader"},enabled = false)
    public void getOrderEtaTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String clientUserSheetName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_client_user_sheet_name");
        String areaName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_area_name");
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> cookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        String[] cred = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        HashMap<String, Integer> skuMap = new HashMap<>();
        int skuID = EligibleSkuMethods.skuAvailableInPrimaryBin(cookie, fcId, report);

        skuMap.put(String.valueOf(skuID), 1);
        String bb_decoded_mid="2";
        String xcaller="bb-now";
        String entrycontextid=Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");



       String orderId = OrderPlacement.placeBBNowOrder("bbnow", 10, cred[0], areaName, skuMap, true, false, report);
        OrderStatusAPI orderStatusAPI = new OrderStatusAPI(entrycontext,String.valueOf(entrycontextid),"monolith","monolith",report);
        //orderStatusAPI.changeOrderStatus("ready_to_ship", String.valueOf(orderId),skuList);


        GetOrderEta getOrderEta =new GetOrderEta("1001184639",xcaller,entrycontext,entrycontextid,bb_decoded_mid,report);

        Response response = getOrderEta.getOrderEta("schema//eta//internal//get-order-eta-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Get Order Eta response: " + response.prettyPrint(),true);
    }
}
