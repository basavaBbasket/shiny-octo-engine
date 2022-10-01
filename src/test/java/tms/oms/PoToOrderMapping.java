package tms.oms;

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

public class PoToOrderMapping extends BaseTest {
    @DescriptionProvider(slug = "One PO should become one TMS Order", description = "1. Get available slots for given location context and group of skus\n" +
            "            2. Reseve the slot by passing slot info  from get-slots\n" +
            "            3. Hit the confirm order Api and genrate order id\n" +
            "            4. fetch Po id from order details table \n" +
            "            5. Check unique record exist in table for given PO ID \n" ,author = "tushar")
    @Test(groups = {"TMS","OMS"})
    public void checkReasonIdInDbAfterCancellation() throws IOException {

        AutomationReport report = getInitializedReport(this.getClass(), false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String, String> headersData = CreateHeader.createHeader();
        Map<String, String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
        Random rand = new Random();
        int external_reference_id=(int)rand.nextInt(1000000000);
        Integer orderId = OrderCreation.placeTmsOrder(report, memberData, headersData, skuData,external_reference_id);
        String checkOrderDetailsDbQuery = "select * from order_detail where id = " + orderId.toString() + ";";
        String orderDbName = env.toUpperCase() + "-" + "ORDER-CROMA";
        JsonObject orderDetailsJson = new JsonObject(TmsDBQueries.getOrderDetails(orderDbName, checkOrderDetailsDbQuery));
        Integer po_id = orderDetailsJson.getJsonArray("rows").getJsonObject(0).getInteger("po_id");
        report.log("PO ID:"+po_id,true);
        String checkTmsOrderInDbUsingPO = "select * from order_detail where po_id="+po_id.toString()+";";
        JsonObject orderDetailsUsingPOJson = new JsonObject(TmsDBQueries.getOrderDetails(orderDbName,checkTmsOrderInDbUsingPO));
        Assert.assertEquals(true, orderDetailsUsingPOJson.getInteger("numRows") == 1,"");
        report.log("There is one PO for Tms order",true);
    }

}
