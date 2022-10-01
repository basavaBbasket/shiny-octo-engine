package dynamicLocation_rel1_bbnow_FC.picking;

import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.InventoryMethods;
import api.warehousecomposition.planogram_FC.PickingMethods;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import org.testng.Assert;
import org.testng.annotations.Test;
import utility.OrderUtils;

import java.util.HashMap;

public class PlaceBBnowOrderWith2SkuAndCompletePickingFlowModified extends BaseTest {
    @DescriptionProvider(slug = "Picking Flow Modified", description = "Test case creates the order with 2kus and completes the picking flow." +
            "\n Places order-> poll to check order appeared in picking platform -> JobAssignment APi, creates the picking job -> Order-Bag linking -> Picking Ack -> Picking Complete ", author = "vinay")
    @Test(groups = {"bbnow", "dlphase2", "pickingFlow"})
    public void placeBBnowOrderWith2SkuAndCompletePickingFlowModified() {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        String clientUserSheetName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_client_user_sheet_name");
        String areaName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_area_name");
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String pwd = adminCred[1];

        int skuID1 = EligibleSkuMethods.skuAvailableInPrimaryBin(adminUserName, pwd, fcId, report);
        int skuID2 = EligibleSkuMethods.skuAvailableInPrimaryBin(adminUserName, pwd, fcId, report);

        if (!InventoryMethods.isPrimaryBinMappedForSku(adminUserName, pwd, skuID2, fcId, report)) {
            Assert.fail("No sku Available in primary bin for skuId: "+skuID2);
        }
        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID1), 1);
        skuMap.put(String.valueOf(skuID2), 1);
        int orderId = Integer.parseInt(OrderPlacement.placeBBNowOrder(cred[0], areaName, skuMap, true, false, report));

        PickingMethods.pickingFlowModified(orderId, fcId, adminUserName, pwd, report);
        String orderStatus = OrderUtils.getCurrentOrderStatus(orderId, report);
        Assert.assertTrue(orderStatus.equalsIgnoreCase("packed"), "Order status is not updated to packed " +
                "\n Current Order Status is: " + orderStatus);
        report.log("Order status updated to packed ", true);
    }
}
