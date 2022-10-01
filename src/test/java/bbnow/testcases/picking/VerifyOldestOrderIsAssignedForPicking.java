package bbnow.testcases.picking;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.PickingMethods;
import api.warehousecomposition.planogram_FC.internal.Helper;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static api.warehousecomposition.planogram_FC.internal.Helper.isDateTime1LessThanDateTime2;

public class VerifyOldestOrderIsAssignedForPicking extends BaseTest {
    @DescriptionProvider(slug = "verify oldest order is assigned for picking", description = "Test case checks \n " +
            " 1.) whether the oldest order is assigned for picking." +
            " 2.) Check whether the picking status and order status is in process", author = "vinay")
    @Test(groups = {"bbnow" , "regression", "dlphase2", "pickingFlow", "pickingFIFO"})
    public void verifyOldestOrderIsAssignedForPicking() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String entry_context= Config.bbnowConfig.getString("entry_context");
        String entry_context_id= Config.bbnowConfig.getString("entry_context_id");

        String clientUserSheetName = Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name");
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");
        int fcId = Integer.parseInt(Config.bbnowConfig.getString("bbnow_stores[1].fc_id"));
        String searchTerm = Config.bbnowConfig.getString("bbnow_stores[1].category_slug1");
        String searchType = Config.bbnowConfig.getString("bbnow_stores[1].search_type2");

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        Map<String, String> adminCookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);
        Map<String,String> memberCookie = MemberCookie.getMemberCookieForCustomAddress(cred[0],areaName,report);

        int skuID = EligibleSkuMethods.fetchSkuVisibleAndAvailableInPrimaryBin(memberCookie,adminCookie, fcId,entry_context,Integer.parseInt(entry_context_id),areaName,searchTerm,searchType,report);

        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID), 1);

        //to handle if there is no open order in the picking_request table
        int orderId1 = Integer.parseInt(OrderPlacement.placeBBNowOrder("bbnow" , 10 , cred[0], areaName, skuMap, true, false, report));
        int orderId2 = Integer.parseInt(OrderPlacement.placeBBNowOrder("bbnow" , 10 , cred[0], areaName, skuMap, true, false, report));

        int orderId = PickingMethods.invokeJobAssignmentApi(fcId, adminUserName, adminCookie, report);
        String whenToPickTimeOfCurAssignedOrd = Helper.fetchTheWhenToPickTimeOfGivenOrderFromPickingRequestTable(orderId, report);
        String oldestWhenToPickTimeOfOpenOrd = Helper.fetchTheWhenToPickTimeOfOldestOpenOrderFromPickingRequestTable(report);

        report.log("Comparing the when to pick time, to check whether oldest order is assigned", true);
        report.log("When to pick time of cur Assigned order: " + whenToPickTimeOfCurAssignedOrd, true);
        report.log("When to pick time of oldest open order: " + oldestWhenToPickTimeOfOpenOrd, true);
        Assert.assertTrue(isDateTime1LessThanDateTime2(whenToPickTimeOfCurAssignedOrd, oldestWhenToPickTimeOfOpenOrd), "order assignemnt is not following FIFO");
        report.log("Oldest open order is assigned for picking", true);

        Assert.assertTrue(Helper.fetchOrderStatus(orderId, report).equalsIgnoreCase("inprocess"),
                "Order status is not changed to inprocess post picking job creation");
        Assert.assertTrue(Helper.fetchPickingStatus(orderId, report).equalsIgnoreCase("inprocess"),
                "Picking status is not changed to inprocess post picking job creation");
        report.log("picking status and order status is changed to inprocess post picking job creation", true);
    }
}
