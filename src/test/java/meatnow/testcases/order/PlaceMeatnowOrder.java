package meatnow.testcases.order;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class PlaceMeatnowOrder extends BaseTest {
    @DescriptionProvider(slug = "place meatnow order", description = "Place meatnow order  ", author = "vinay")
    @Test(groups = {"meatNowOrder"})
    public void placeBBnowOrderAndCompletePickingFlowModified() {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        int entrycontextid = Integer.parseInt(Config.meatnowConfig.getString("entry_context_id"));
        String entrycontext = Config.meatnowConfig.getString("entry_context");
        String clientUserSheetName = Config.meatnowConfig.getString("bbnow_stores[1].member_sheet_name");
        String areaName = Config.meatnowConfig.getString("bbnow_stores[1].area");
        int fcId = Integer.parseInt(Config.meatnowConfig.getString("bbnow_stores[1].fc_id"));
        String searchTerm = Config.meatnowConfig.getString("bbnow_stores[1].category_slug1");
        String searchType = Config.meatnowConfig.getString("bbnow_stores[1].search_type2");

        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        Map<String, String> memberCookie = MemberCookie.getMemberCookieForCustomAddress(cred[0], areaName, report);

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> adminCookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        int skuID = EligibleSkuMethods.fetchSkuVisibleAndAvailableInPrimaryBin(memberCookie, adminCookie,
                fcId, entrycontext, entrycontextid, areaName, searchTerm, searchType, report);


        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID), 2);
        int orderId = Integer.parseInt(OrderPlacement.placeBBNowOrder(entrycontext, entrycontextid, cred[0], areaName, skuMap, true, false, report));
        report.log("Meat Now Order ID : " + orderId, true);
    }
}
