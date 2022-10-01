package tms.oms;

import tms.api.OMS.UpdateOrderStatus;
import tms.api.OMS.UpdatePackageDetails;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import tms.configAndUtilities.*;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class UpdatePackageDetailsAfterOrderIsPacked extends BaseTest {

    @DescriptionProvider(slug = "Don't allow package update if the order is already in packed status.", description = "1. Get available slots for given location context and group of skus\n" +
            "            2. Reseve the slot by passing slot info  from get-slots\n" +
            "            3. Hit the confirm order Api and genrate order id\n" +
            "            4. update the package details and hit update actions\n" +
            "            5. Update the order status as packed and check order status in db as Packed \n" ,author = "tushar")
    @Test(groups = {"TMS","OMS"})
    public void doUpdatePackageaDetailsAfterOrderIsPacked() throws IOException {

        AutomationReport report = getInitializedReport(this.getClass(), false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String ,String> headersData = CreateHeader.createHeader();
        Map<String,String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
        Random rand = new Random();
        int external_reference_id=(int)rand.nextInt(1000000000);
        Integer orderId = OrderCreation.placeTmsOrder(report , memberData,headersData,skuData,external_reference_id);
        String checkOrderDetailsDbQuery = "select * from order_detail where id = "+ orderId.toString() +";";
        String orderDbName = env.toUpperCase()+ "-"+"ORDER-CROMA";
        JsonObject orderDetailsJson = new JsonObject(TmsDBQueries.getOrderDetails(orderDbName,checkOrderDetailsDbQuery));
        Assert.assertEquals(true, orderDetailsJson.getInteger("numRows") == 1,"");
        report.log("OrderId is in DB",true);
        UpdateOrderStatus.updateOrderStatus(report,memberData,skuData,headersData,orderId,"packed");
        String checkOrderStatusDbQuery = "select status from order_detail where id = "+ orderId.toString() +";";
        JsonObject orderStatusJson = new JsonObject(TmsDBQueries.getOrderDetails(orderDbName,checkOrderStatusDbQuery));
        Assert.assertEquals("packed",orderStatusJson.getJsonArray("rows").getJsonObject(0).getString("status"),"Order has been cancelled successfully");
        report.log("Order has been packed successfully",true);
        report.log("Updating Package details after order has been packed",true);
        Response response = UpdatePackageDetails.updatePackage(report,String.valueOf(orderId),headersData,memberData,skuData);
        JsonPath jsonPath = response.jsonPath();
        String strpath = "errors[0].display_msg";
        String responseMsg = jsonPath.getString(strpath);
        boolean unableToUpdatePackage = responseMsg.contains("order is not in open or inprocess state, can not update package");
        Assert.assertEquals(true,unableToUpdatePackage);
        report.log("Unable to do update package after order status has been changed to packed",true);

    }
}
