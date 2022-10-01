package tms.oms;

import TMS.api.OMS.UpdateActions;
import TMS.api.OMS.UpdateOrderStatus;
import TMS.api.OMS.UpdatePackageDetails;
import TMS.configAndUtilities.*;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.vertx.core.json.JsonObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class CheckOrderStatusToPacked extends BaseTest {
    @DescriptionProvider(slug = "TMS order creation and cheking order status as packed", description = "1. Get available slots for given location context and group of skus\n" +
            "            2. Reseve the slot by passing slot info  from get-slots\n" +
            "            3. Hit the confirm order Api and genrate order id\n" +
            "            4. update the package details and hit update actions\n" +
            "            5. Update the order status as packed and check order status in db as Packed \n" ,author = "tushar")
    @Test(groups = {"TMS","tms-order-creation"})
    public void checkOrderStatusToBePacked() throws IOException {

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
        UpdatePackageDetails.updatePackage(report,String.valueOf(orderId),headersData,memberData,skuData);
        UpdateActions.updateActions(report,String.valueOf(orderId),headersData,memberData,skuData);
        UpdateOrderStatus.updateOrderStatus(report,memberData,skuData,headersData,orderId,"packed");
        String checkOrderStatusDbQuery = "select status from order_detail where id = "+ orderId.toString() +";";
        JsonObject orderStatusJson = new JsonObject(TmsDBQueries.getOrderDetails(orderDbName,checkOrderStatusDbQuery));
        Assert.assertEquals("packed",orderStatusJson.getJsonArray("rows").getJsonObject(0).getString("status"),"Order has been cancelled successfully");
        report.log("Order has been packed successfully",true);
    }
}
