package tms.oms;

import TMS.configAndUtilities.CreateHeader;
import TMS.configAndUtilities.MemberData;
import TMS.configAndUtilities.OrderCreation;
import TMS.configAndUtilities.SkuData;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class CreatePickupOrder extends BaseTest {

    @DescriptionProvider(slug = "Verify that the Pickup order is getting placed with the original order id ", description = "If the offset is given to TMS in the API then TMS should calculate the cut off and adjust that and then return the slots", author = "Pranay")
    @Test(groups = {"test_oms","TMS","OMS","OMS-slots","oms2"})
    public void createPickupOrder() throws IOException {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String, String> headersData = CreateHeader.createHeader();
        Map<String, String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
        Random rand = new Random();
        int external_reference_id=(int)rand.nextInt(1000000000);
        int pickup=external_reference_id;

        Integer orderId = OrderCreation.placeTmsOrder(report, memberData, headersData, skuData,external_reference_id);
        headersData.replace("orderType","Pickup");
        external_reference_id=(int)rand.nextInt(1000000000);
       Response response= OrderCreation.placeTmsVariableOrder(report, memberData, headersData, skuData,pickup,external_reference_id);
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(jsonPath.get("orders[0].order_type"),"Pickup");



    }
}