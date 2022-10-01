package bbnow.testcases.deliveybin;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.DeliveryBinMethods;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.PickingMethods;
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


public class OrderBinMapping extends BaseTest {
    @DescriptionProvider(slug = "place order-->Picking Flow Modified-->Order Bin Mapping", description = "1. Place BBnow order \n" +
            "            2. Check whether the order entry is created in picking_request and picking_item_request table \n" +
            "            3. Complete the picking flow. \n" +
            "            4.Perfrom order bin mapping " +
            "            5. Validate the mapping like reason id and is occupied fields  and check the entry in the FC_SKU_STOCK table ", author = "Pranay")
    @Test(groups = {"bbnow", "dlphase2", "pickingFlow","deliverybin"})
    public void orderBinMapping()  {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        String clientUserSheetName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_client_user_sheet_name");
        String areaName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_area_name");
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> cookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        int skuID = EligibleSkuMethods.skuAvailableInPrimaryBin(cookie, fcId, report);

        String[] cred = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID), 1);


        int orderId = Integer.parseInt(OrderPlacement.placeBBNowOrder("bbnow" , 10,cred[0], areaName, skuMap, true, false, report));
        PickingMethods.pickingFlowModified(orderId, fcId, adminUserName, cookie, report);
        DeliveryBinMethods.perfomOrderBinMapping(fcId,orderId,"binning",cookie,report);
        System.out.println("binning completed");

        Assert.assertTrue(DeliveryBinMethods.isOrderBinMapped(fcId,orderId,report));
        Map<String,Object> orderbinmapping = DeliveryBinMethods.getOrderBinMappingDetails(fcId,orderId,report);
        String[] createddate=String.valueOf(orderbinmapping.get("created_on")).split("T");
        Assert.assertTrue(DeliveryBinMethods.entryInFc_Sku_Stock(orderbinmapping.get("bin_id"),createddate[0],report));




    }
}
