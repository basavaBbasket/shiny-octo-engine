package tms.oms;

import tms.configAndUtilities.CreateHeader;
import tms.configAndUtilities.MemberData;
import tms.configAndUtilities.OrderCreation;
import tms.configAndUtilities.SkuData;
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

public class CreateExchangeOrder extends BaseTest {

    @DescriptionProvider(slug = "Verify that the exchange order is getting placed with the original order id ", description = "Test case to verify the exchange order creation flow ", author = "Pranay")
    @Test(groups = {"test_oms","TMS","OMS","OMS-slots"})
    public void  CreateExchangeOrder() throws IOException {

    AutomationReport report = getInitializedReport(this.getClass(), false);
    String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
    Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
    Map<String, String> headersData = CreateHeader.createHeader();
    Map<String, String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
        Random rand = new Random();
    int external_reference_id=(int)rand.nextInt(1000000000);
        int pickup=external_reference_id;
    Integer orderId = OrderCreation.placeTmsOrder(report, memberData, headersData, skuData,external_reference_id);
        headersData.replace("orderType","Exchange");
        Response response=  OrderCreation.placeTmsVariableOrder(report, memberData, headersData, skuData,pickup,external_reference_id);
        JsonPath jsonPath = response.jsonPath();
        Assert.assertEquals(jsonPath.get("orders[0].order_type"),"Replacement");

}}
