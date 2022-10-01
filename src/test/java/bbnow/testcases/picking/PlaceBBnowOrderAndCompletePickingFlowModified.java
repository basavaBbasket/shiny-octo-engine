package bbnow.testcases.picking;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.PickingMethods;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utility.OrderUtils;

import java.util.HashMap;
import java.util.Map;

public class PlaceBBnowOrderAndCompletePickingFlowModified extends BaseTest {
    @DescriptionProvider(slug = "place order-->Picking Flow Modified-->order_container_info", description = "1. Place BBnow order \n" +
            "            2. Check whether the order entry is created in picking_request and picking_item_request table \n" +
            "            3. Complete the picking flow. \n" +
            "            4. Check whether order_conatiner_info api is returning the bag associated with order, container info and sku item in bag ", author = "vinay")
    @Test(groups = {"bbnow", "dlphase2", "pickingFlow"})
    public void placeBBnowOrderAndCompletePickingFlowModified() {
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
        skuMap.put(String.valueOf(skuID), 2);
        int orderId = Integer.parseInt(OrderPlacement.placeBBNowOrder("bbnow" , 10 , cred[0], areaName, skuMap, true, false, report));

        PickingMethods.pickingFlowModified(orderId, fcId, adminUserName, cookie, report);
        String orderStatus = OrderUtils.getCurrentOrderStatus(orderId, report);
        Assert.assertTrue(orderStatus.equalsIgnoreCase("packed"), "Order status is not updated to packed " +
                "\n Current Order Status is: " + orderStatus);
        report.log("Order status updated to packed ", true);

        Response response = PickingMethods.orderContainerInfo(orderId, cookie, report);

        JSONObject jsonObject = PickingMethods.fetchContainerLabelLinkedForGivenOrder(orderId, report);
        String containerLabelLinked = jsonObject.getJSONArray("rows").getJSONObject(0).getString("container_label");
        String containerLabelInfoReturned = response.path("container_info[0].container_label").toString();
        Assert.assertTrue(containerLabelLinked.equalsIgnoreCase(containerLabelInfoReturned), "container Label returned in order container info is not the same linked in order_container table \n" +
                "container label returned in order_container_info api call is : " + containerLabelInfoReturned + "\n" +
                "Conatiner label mapped in order_container table  : " + containerLabelLinked);
        report.log("order container info api returned the container Label linked to order in the order_container table", true);

        int skuId = Integer.parseInt(response.path("container_info[0].sku_items[0].sku_id").toString());
        Assert.assertTrue(skuId == skuID, "skuId used in order placement is not the same returned in order_container_info api \n" +
                "Sku returned in order_container_info api call is : " + skuId + "\n" +
                "Order placed with the sku : " + skuID);
        report.log("order container info api returned the sku item present in the bag", true);
    }
}
