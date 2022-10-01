package bbnow.schema_validation.delivery_app_apis.hubops;


import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
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
    @Test(groups = {"bbnow" , "regression","dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation","revisit","testheader"})
    public void ceeAttributesForOrderIdsApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        ArrayList Orderids=new ArrayList();

        String xcaller="fe";//todo

        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        report.log("Starting  order placement.", true);
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



        int skuID = EligibleSkuMethods.fetchSkuVisibleAndAvailableInPrimaryBin(memberCookie,adminCookie,
                fcId,entrycontext,Integer.parseInt(entrycontextid),areaName,searchTerm,searchType,report);

        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID), 1);

        String orderId = OrderPlacement.placeBBNowOrder("bbnow", 10, cred[0], areaName, skuMap, true, false, report);

        Orderids.add(Integer.valueOf(orderId));//todo
        CeeAttributesForOrderIds ceeAttributesForOrderIds=new CeeAttributesForOrderIds(xcaller,entrycontextid,entrycontext,adminCookie.toString(),Orderids,report);

        Response response = ceeAttributesForOrderIds.getCeeAttributesForOrderIds("schema//delivery_app//hubops//cee-attributes-for-order-ids-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Cee Attributes For OrderIds  response: " + response.prettyPrint(),true);
    }

}
