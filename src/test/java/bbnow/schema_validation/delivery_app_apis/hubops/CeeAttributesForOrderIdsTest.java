package bbnow.schema_validation.delivery_app_apis.hubops;


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
import msvc.delivery_app_apis.hubops.CeeAttributesForOrderIds;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CeeAttributesForOrderIdsTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for CeeAttributesForOrderIds api.",slug = "Validate Cee Attributes For OrderIds api")
    @Test(groups = {"bbnow" , "regression","dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation","unstable","testheader"})
    public void ceeAttributesForOrderIdsApiTest(){
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
      //  String cookie2 = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";//todo
        String xcaller="fe";//todo
        String entrycontextid= Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");
        ArrayList Orderids=new ArrayList();
        String orderId = OrderPlacement.placeBBNowOrder("bbnow", 10, cred[0], areaName, skuMap, true, false, report);

        Orderids.add(Integer.valueOf(orderId));//todo
        CeeAttributesForOrderIds ceeAttributesForOrderIds=new CeeAttributesForOrderIds(xcaller,entrycontextid,entrycontext,cookie.toString(),Orderids,report);

        Response response = ceeAttributesForOrderIds.getCeeAttributesForOrderIds("schema//delivery_app//hubops//cee-attributes-for-order-ids-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Cee Attributes For OrderIds  response: " + response.prettyPrint(),true);
    }

}
