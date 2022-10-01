package bbnow.testcases.picking;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.InventoryMethods;
import api.warehousecomposition.planogram_FC.PickingMethods;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utility.OrderUtils;

import java.util.HashMap;
import java.util.Map;

public class PlaceBBnowOrderWith2SkuAndCompletePickingFlowModified extends BaseTest {
    @DescriptionProvider(slug = "Picking Flow with multiple sku and multiple qty", description = "" +
            "Places order-> poll to check order appeared in picking platform -> JobAssignment APi, creates the picking job -> Order-Bag linking -> Picking Ack -> Picking Complete \n" +
            "1. Place the order with 2skus \n" +
            "2. Check whether the two entries are created in picking_item_request table \n" +
            "3. Check whether the ordered_qty matches with entry in picking_item_request table \n" +
            "4. Complete the picking flow.", author = "vinay")
    @Test(groups = {"bbnow", "dlphase2", "pickingFlow","hello"})
    public void placeBBnowOrderWith2SkuAndCompletePickingFlowModified() {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        String clientUserSheetName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_client_user_sheet_name");
        String areaName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_area_name");
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> cookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        System.out.println("Calling api to get sku1");
        int skuID1 = EligibleSkuMethods.skuAvailableInPrimaryBin(cookie, fcId, report);
        System.out.println("Calling api to get sku2");
        int skuID2 = EligibleSkuMethods.skuAvailableInPrimaryBin(cookie, fcId, report);

        System.out.println("SkuId1: " + skuID1);
        System.out.println("SkuId2: " + skuID2);
        if (!InventoryMethods.isPrimaryBinMappedForSku(cookie, skuID2, fcId, report)) {
            Assert.fail("No sku Available in primary bin for skuId: "+skuID2);
        }
        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID1), 2);
        skuMap.put(String.valueOf(skuID2), 1);
        System.out.println("Sku map for picking: " + skuMap);
        int orderId = Integer.parseInt(OrderPlacement.placeBBNowOrder("bbnow" , 10 , cred[0], areaName, skuMap, true, false, report));

        PickingMethods.pickingFlowModified(orderId, fcId, adminUserName, cookie, report);
        String orderStatus = OrderUtils.getCurrentOrderStatus(orderId, report);
        Assert.assertTrue(orderStatus.equalsIgnoreCase("packed"), "Order status is not updated to packed " +
                "\n Current Order Status is: " + orderStatus);
        report.log("Order status updated to packed ", true);

        JSONObject jsonObject = PickingMethods.fetchOrderDetailsFromPickingItemRequest(orderId,report);
        Assert.assertTrue(jsonObject.getInt("numRows") == 2,"Two entries are not created in" +
                "picking item request table\n" +
                "Current entries: "+jsonObject.getInt("numRows"));
        report.log("Two entries are created in picking_item_request table",true);

        for(int i=0;i<jsonObject.getInt("numRows");i++){
            if(skuID1==jsonObject.getJSONArray("rows").getJSONObject(i).getInt("sku_id")){
                double ordQty = jsonObject.getJSONArray("rows").getJSONObject(i).getDouble("ordered_quantity");
                Assert.assertTrue(ordQty==2,"ordered_quantity is not 2 in picking_item_request table \n" +
                        "Current qty is : "+ordQty);
                report.log("ordered_quantity is 2 in picking_item_request table for skuId "+skuID1+" and orderId "+orderId,true);
            }else {
                double ordQty = jsonObject.getJSONArray("rows").getJSONObject(i).getDouble("ordered_quantity");
                Assert.assertTrue(ordQty==1,"ordered_quantity is not 2 in picking_item_request table \n" +
                        "Current qty is : "+ordQty);
                report.log("ordered_quantity is 1 in picking_item_request table for skuId "+skuID2+" and orderId "+orderId,true);
            }
        }

    }

}
