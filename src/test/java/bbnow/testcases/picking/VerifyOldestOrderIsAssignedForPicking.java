package bbnow.testcases.picking;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.PickingMethods;
import api.warehousecomposition.planogram_FC.internal.Helper;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
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

        String clientUserSheetName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_client_user_sheet_name");
        String areaName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_area_name");
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> cookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        int skuID = EligibleSkuMethods.skuAvailableInPrimaryBin(cookie, fcId, report);

        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID), 1);

        //to handle if there is no open order in the picking_request table
        int orderId1 = Integer.parseInt(OrderPlacement.placeBBNowOrder("bbnow" , 10 , cred[0], areaName, skuMap, true, false, report));
        int orderId2 = Integer.parseInt(OrderPlacement.placeBBNowOrder("bbnow" , 10 , cred[0], areaName, skuMap, true, false, report));

        int orderId = PickingMethods.invokeJobAssignmentApi(fcId, adminUserName, cookie, report);
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
