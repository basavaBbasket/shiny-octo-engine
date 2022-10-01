package bbnow.testcases.picking;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.InventoryMethods;
import api.warehousecomposition.planogram_FC.PickingMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utility.OrderUtils;

import java.util.HashMap;
import java.util.List;
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

        System.out.println("Calling api to get skus");
        List<Integer> skuIDList = EligibleSkuMethods.fetchListOfSkuVisibleAndAvailableInPrimaryBin(memberCookie,adminCookie, fcId,entry_context,Integer.parseInt(entry_context_id),areaName,searchType,searchTerm,report);
        Assert.assertTrue(skuIDList.size()>2,"Number of skus available is "+skuIDList.size()+", Min req skus 2 for flow");
        int skuID1 = skuIDList.get(0);
        int skuID2 = skuIDList.get(1);

        System.out.println("SkuId1: " + skuID1);
        System.out.println("SkuId2: " + skuID2);
        if (!InventoryMethods.isPrimaryBinMappedForSku(adminCookie, skuID2, fcId, report)) {
            Assert.fail("No sku Available in primary bin for skuId: "+skuID2);
        }
        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID1), 2);
        skuMap.put(String.valueOf(skuID2), 1);
        System.out.println("Sku map for picking: " + skuMap);
        int orderId = Integer.parseInt(OrderPlacement.placeBBNowOrder("bbnow" , 10 , cred[0], areaName, skuMap, true, false, report));

        PickingMethods.pickingFlowModified(orderId, fcId, adminUserName, adminCookie, report);
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
