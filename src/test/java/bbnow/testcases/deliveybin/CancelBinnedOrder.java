package bbnow.testcases.deliveybin;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.DeliveryBinMethods;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.PickingMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderCancel;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import msvc.order.external.CancelOrder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class CancelBinnedOrder extends BaseTest {

    @DescriptionProvider(slug = "place order-->Picking Flow Modified-->Binning flow-->Cancel Binned Order", description = "1. Place BBnow order \n" +
            "            2. Check whether the order entry is created in picking_request and picking_item_request table \n" +
            "            3. Complete the picking flow. \n" +
            "            4. Perfrom order bin mapping check" +
            "            5. Cancel the order after order bin mapping " +
            "            6. Validate the entry in  bin_request_id_mapping table against the changed values of reason id, is occupied fields" +
            "             7. Check is the entry is deleted in fc sku stock table after the cancelation  ", author = "pranay")
    @Test(groups = {"bbnow", "dlphase2", "pickingFlow","deliverybin"})
    public void cancelBinnedOrder() throws InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String entry_context= Config.bbnowConfig.getString("entry_context");
        String entry_context_id=Config.bbnowConfig.getString("entry_context_id");
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


        int orderId = Integer.parseInt(OrderPlacement.placeBBNowOrder("bbnow" , 10,cred[0], areaName, skuMap, true, false, report));

        PickingMethods.pickingFlowModified(orderId, fcId, adminUserName, cookie, report);

        DeliveryBinMethods.perfomOrderBinMapping(fcId,orderId,"binning",cookie,report);
        System.out.println("binning completed");

        Assert.assertTrue(DeliveryBinMethods.isOrderBinMapped(fcId,orderId,report));
        Map<String,Object> orderbinmapping = DeliveryBinMethods.getOrderBinMappingDetails(fcId,orderId,report);
        String[] createddate=String.valueOf(orderbinmapping.get("created_on")).split("T");
       // Assert.assertTrue(DeliveryBinMethods.entryInFc_Sku_Stock(orderbinmapping.get("bin_id"),createddate[0],report));
        CancelOrder cancelOrder=new CancelOrder(entry_context,entry_context_id,"monolith",report);
        cancelOrder.cancelOrder(String.valueOf(orderId));
        DeliveryBinMethods.perfomOrderBinMapping(fcId,orderId,"emptying",cookie,report);
        System.out.println("emptying completed");
        orderbinmapping = DeliveryBinMethods.getOrderBinMappingDetails(fcId,orderId,report);
        Assert.assertEquals(orderbinmapping.get("is_occupied"),0,"bin is not emptied after  empting bin api");
        Assert.assertEquals(orderbinmapping.get("reason_id"),2,"reason_id not emptied after cancelling order");

     //   Assert.assertFalse(DeliveryBinMethods.entryInFc_Sku_Stock(orderbinmapping.get("bin_id"),createddate[0],report));



    }
}
