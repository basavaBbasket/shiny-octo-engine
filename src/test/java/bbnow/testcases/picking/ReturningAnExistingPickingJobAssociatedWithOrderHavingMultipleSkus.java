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
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utility.OrderUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturningAnExistingPickingJobAssociatedWithOrderHavingMultipleSkus extends BaseTest {
    @DescriptionProvider(slug = "Returning existing Picking Job Associated with order having multiple skus", description = "" +
            "Places order-> poll to check order appeared in picking platform -> JobAssignment APi, creates the picking job -> Jobs apis \n" +
            "1. Place the order with 2skus \n" +
            "2. poll to check order appeared in picking platform \n" +
            "3. Call job assignment api, once the job is assigned. " +
            "Call the jobs api same picking job assigned should be returned \n" +
            "4. Check the order_status and picking_status in picking_request table should be inprocess \n" +
            "5. Check whether the entry is created in pick_ack, post job assignment \n" +
            "6. Check whether the duplicate entry is not created in pick ack table, when job assignment api is called 2nd time"+
            "7. Order status in order db should be inprocess \n" +
            "8. Check whether already used bag can be linked with new order again \n" +
            "9. Check whether single order can be mapped with multiple orders ", author = "tushar")
    @Test(groups = {"bbnow", "dlphase2", "pickingFlow"})
    public void placeBBnowOrderWith2SkuAndCompletePickingFlowModified() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String entry_context= Config.bbnowConfig.getString("entry_context");
        String entry_context_id= Config.bbnowConfig.getString("entry_context_id");

        String clientUserSheetName = Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name");
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");
        int fcId = Integer.parseInt(Config.bbnowConfig.getString("bbnow_stores[1].fc_id"));
       // String searchTerm = Config.bbnowConfig.getString("bbnow_stores[1].category_slug1");
        List<String> searchTerm = Config.bbnowConfig.getList("bbnow_stores[1].category_slug_list");
        String searchType = Config.bbnowConfig.getString("bbnow_stores[1].search_type2");

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);

        Map<String, String> adminCookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);
        Map<String,String> memberCookie = MemberCookie.getMemberCookieForCustomAddress(cred[0],areaName,report);

        System.out.println("Calling api to get skus");
        List<Integer> skuIDList = EligibleSkuMethods.fetchListOfSkuVisibleAndAvailableInPrimaryBin(memberCookie,adminCookie, fcId,entry_context,Integer.parseInt(entry_context_id),areaName,searchType,searchTerm,report);
        Assert.assertTrue(skuIDList.size()>=2,"Number of skus available is "+skuIDList.size()+", Min req skus 2 for flow");
        int skuID1 = skuIDList.get(0);
        int skuID2 = skuIDList.get(1);

        System.out.println("SkuId1: " + skuID1);
        System.out.println("SkuId2: " + skuID2);
        if (!InventoryMethods.isPrimaryBinMappedForSku(adminCookie, skuID2, fcId, report)) {
            Assert.fail("No sku Available in primary bin for skuId: " + skuID2);
        }

        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID1), 2);
        skuMap.put(String.valueOf(skuID2), 1);
        System.out.println("Sku map for picking: " + skuMap);
        int orderId = Integer.parseInt(OrderPlacement.placeBBNowOrder("bbnow" , 10 , cred[0], areaName, skuMap, true, false, report));

        Response jobAssignmentApiResponse = PickingMethods.callJobAssignmentApi(fcId, adminUserName, adminCookie, report);
        int assignedOrderId = jobAssignmentApiResponse.jsonPath().getInt("picking[0].sku_location_info[0].order_id");
        int jobId = jobAssignmentApiResponse.jsonPath().getInt("picking[0].job_id");

        Response jobAssignmentApiResponse2 = PickingMethods.callJobAssignmentApi(fcId, adminUserName, adminCookie, report);
        Assert.assertTrue(jobAssignmentApiResponse2.jsonPath().getInt("picking[0].job_id") == jobId, "Same job is not assigned");
        report.log("Existing picking job " + jobId + " is returned", true);

        JSONObject jsonObject = PickingMethods.fetchOrderDetailsFromPickingRequest(assignedOrderId, report);
        Assert.assertTrue(jsonObject.getJSONArray("rows").getJSONObject(0).getString("order_status").equalsIgnoreCase("inprocess"),
                "order_status in picking request table is not inprocess" +
                        "\nCurrent order status is : " + jsonObject.getJSONArray("rows").getJSONObject(0).getString("order_status"));
        report.log("order_status in picking request table is inprocess" +
                "\nCurrent order status is : " + jsonObject.getJSONArray("rows").getJSONObject(0).getString("order_status"), true);


        Assert.assertTrue(jsonObject.getJSONArray("rows").getJSONObject(0).getString("picking_status").equalsIgnoreCase("inprocess"),
                "picking_status in picking request table is not inprocess" +
                        "\nCurrent picking status is : " + jsonObject.getJSONArray("rows").getJSONObject(0).getString("picking_status"));
        report.log("picking_status in picking request table is inprocess" +
                "\nCurrent picking status is : " + jsonObject.getJSONArray("rows").getJSONObject(0).getString("picking_status"), true);

        jsonObject = PickingMethods.fetchDetailsFromPickAckTable(assignedOrderId, report);
        Assert.assertTrue(jsonObject.getJSONArray("rows").getJSONObject(0).getInt("job_id") == jobId, "" +
                "JobId " + jsonObject.getJSONArray("rows").getJSONObject(0).getInt("job_id") + " assigned for orderId " +
                "" + assignedOrderId + " is not matching in the pick_ack table");
        report.log("JobId " + jsonObject.getJSONArray("rows").getJSONObject(0).getInt("job_id") + " assigned for orderId " +
                "" + assignedOrderId + " is matching in the pick_ack table", true);

        Assert.assertTrue(jsonObject.getInt("numRows") == 1, "Duplicate entries got inserted in pick ack table " +
                ", on calling job assignment api twice");
        report.log("Duplicate entries not inserted in pick ack table , on calling job assignment api twice", true);

        String orderStatus = OrderUtils.getCurrentOrderStatus(assignedOrderId, report);
        Assert.assertTrue(orderStatus.equalsIgnoreCase("in_process"), "Order status is not updated to in_process " +
                "\n Current Order Status is: " + orderStatus);
        report.log("Order status updated to in_process, post job assignment ", true);

        jsonObject = PickingMethods.fetchContainerLabelInUse(report,fcId,adminCookie);
        String containerLabel = jsonObject.getJSONArray("rows").getJSONObject(0)
                .getString("container_label");
        report.log("Container label in use is : " + containerLabel, true);

        Response orderBagLinkingResponse = PickingMethods.orderBagLinking(fcId, assignedOrderId, containerLabel, adminCookie, true, report);
        Assert.assertTrue(orderBagLinkingResponse.path("errors[0].display_msg").toString().contains("The bag is already in use."), "" +
                "Container Label is not in use, select the bag which is already in use");
        report.log("The bag is already in use. Please use a different one message is displayed as expected", true);

        //Linking single order with multiple bag
        String bagLabel1 = String.valueOf(Instant.now().toEpochMilli());
        PickingMethods.orderBagLinking(fcId, assignedOrderId, bagLabel1, adminCookie, false, report);
        Assert.assertTrue(PickingMethods.checkWhetherContainerLabelIsInUse(bagLabel1, report), "" +
                "Even after order " + assignedOrderId + " bag " + bagLabel1 + " linking bag label status is still not in-use");
        report.log("Post order " + assignedOrderId + " -bag(" + bagLabel1 + ") linking,  label bag label status is in-use", true);

        String bagLabel2 = String.valueOf(Instant.now().toEpochMilli());
        PickingMethods.orderBagLinking(fcId, assignedOrderId, bagLabel2, adminCookie, false, report);
        Assert.assertTrue(PickingMethods.checkWhetherContainerLabelIsInUse(bagLabel2, report), "" +
                "Even after order " + assignedOrderId + " bag " + bagLabel2 + " linking bag label status is still not in-use");
        report.log("Post order " + assignedOrderId + " -bag(" + bagLabel2 + ") linking,  label bag label status is in-use", true);
        report.log("Linked single order " + assignedOrderId + " with multiple bag " + bagLabel1 + "," + bagLabel2, true);


        Assert.assertTrue(PickingMethods.checkWhetherEntryExistsInOrderContainerTable(assignedOrderId, bagLabel1, report),
                "Order " + assignedOrderId + " Container label " + bagLabel1 + " entry is not present in order-container table");
        report.log("Order " + assignedOrderId + " Container label " + bagLabel1 + " entry is present in order-container table", true);

        Assert.assertTrue(PickingMethods.checkWhetherEntryExistsInOrderContainerTable(assignedOrderId, bagLabel2, report),
                "Order " + assignedOrderId + " Container label " + bagLabel2 + " entry is not present in order-container table");
        report.log("Order " + assignedOrderId + " Container label " + bagLabel2 + " entry is present in order-container table", true);

    }

}
