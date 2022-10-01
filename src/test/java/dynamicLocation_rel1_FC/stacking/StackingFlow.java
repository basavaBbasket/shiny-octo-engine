package dynamicLocation_rel1_FC.stacking;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.StackingMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class StackingFlow extends BaseTest {
    @DescriptionProvider(slug = "Stacking Flow", description = "Test case completes the stacking flow." +
            "\nCreates Grn Stacking Job -> Stacking Ack -> Stacking Complete ", author = "vinay")
    @Test(groups = {"bbnow" , "regression", "dlphase2", "stackingFlow", "earlyrelease"})
    public void validateStackingFlow() {
        AutomationReport report = getInitializedReport(this.getClass(), false);

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



        StackingMethods.grnStackingFlow(fcId, skuID, adminUserName, stackingBody, adminCookie, report);
    }
}
