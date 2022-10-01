package bbnow.schema_validation.planogram.external;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.StackingMethods;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import com.bigbasket.automation.Config;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.planogram.external.fcID.StackingAlternateBinAPI;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StackingAlternateBin extends BaseTest {
    @DescriptionProvider(author = "Shruti", description = "This testcase validates response schema for Stacking Alternate Bin api.",slug = "Validate Stacking Alternate Bin api")
    @Test(groups = {"bbnow" })
    public void StackingAlternateBinApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        int totalQty = Integer.parseInt(Settings.dlConfig.getProperty("total_quantity"));
        int numOfContainers = Integer.parseInt(Settings.dlConfig.getProperty("num_of_containers"));
        int looseQty = Integer.parseInt(Settings.dlConfig.getProperty("loose_quantity"));
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));

        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        report.log("Starting  order placement.", true);
        String entrycontext= Config.bbnowConfig.getString("entry_context");
        String entrycontextid=Config.bbnowConfig.getString("entry_context_id");
        String clientUserSheetName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_client_user_sheet_name");
        String areaName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_area_name");
        //int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));
        String searchTerm = Config.bbnowConfig.getString("bbnow_stores[1].category_slug1");
        String searchType = Config.bbnowConfig.getString("bbnow_stores[1].search_type2");

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> adminCookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        Map<String,String> memberCookie = MemberCookie.getMemberCookieForCustomAddress(cred[0],areaName,report);

        HashMap<String, Object> stackingBody = new HashMap<>();
        stackingBody.put("total_quantity", totalQty);
        stackingBody.put("num_of_containers", numOfContainers);
        stackingBody.put("loose_quantity", looseQty);

        int skuID = EligibleSkuMethods.fetchSkuVisibleAndAvailableInPrimaryBin(memberCookie,adminCookie,
                fcId,entrycontext,Integer.parseInt(entrycontextid),areaName,searchTerm,searchType,report);


        List<Integer> binIdList= StackingMethods.doStackingOnAlternateBinAndReturnAlternateBinIds(fcId, skuID, adminUserName, stackingBody, adminCookie, report);
        boolean flag=false;
        report.log(binIdList.toString(),true);
        for(int i =1; i<=binIdList.size()-1; i++)
        {
            if(binIdList.get(i)!=binIdList.get(0)) {
                report.log("alternate bin suggested: "+binIdList.get(i), true);
                flag = true;
                break;
            }
        }

        if(!flag)
        {
            Assert.fail("Same bin recommended in create job call is not recommended in alternate bin");
        }


    }
}