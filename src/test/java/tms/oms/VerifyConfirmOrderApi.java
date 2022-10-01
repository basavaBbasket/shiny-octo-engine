package tms.oms;

import TMS.configAndUtilities.*;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.Random;

public class VerifyConfirmOrderApi extends BaseTest {

    @DescriptionProvider(slug = "VerifyCorrectNoOfSlotsReturned", description = "Verify that the confirm order api is throwing error without valid reservation id", author = "Pranay")

    @Test(groups = {"TMS","OMS","OMS-slots"})
    public void verifyConfirmOrderApi() throws IOException {

    AutomationReport report = getInitializedReport(this.getClass(), false);
    String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
    Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
    Map<String ,String> headersData = CreateHeader.createHeader();
    Map<String,String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
    report.log("Hitting Create order api with no wrong reservation id",true);
        Random rand = new Random();
        int external_reference_id=(int)rand.nextInt(1000000000);
    Response response=OrderCreation.placeTmsOrderWithOutReservation(report , memberData,headersData,skuData,0,external_reference_id);
        Assert.assertEquals(response.getStatusCode(),500,"the Confirm order is Placing order without reservation id");
    }

    @DescriptionProvider(slug = "VerifyCorrectNoOfSlotsReturned", description = "Verify that the confirm order api is throwing error without valid reservation id", author = "Pranay")

    @Test(groups = {"TMS","OMS","OMS-slots"})
    public void verifyConfirmOrderApiwithRandomreservationid() throws IOException {

        AutomationReport report = getInitializedReport(this.getClass(), false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String ,String> headersData = CreateHeader.createHeader();
        Map<String,String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());

        Random rand = new Random();

        int randomreservationid=(int)rand.nextInt(1000000000);
        int external_reference_id=(int)rand.nextInt(1000000000);
        report.log("Hitting Create order api with random reservation id" + Integer.valueOf(randomreservationid),true);

        Response response=OrderCreation.placeTmsOrderWithOutReservation(report , memberData,headersData,skuData,randomreservationid,external_reference_id);
        Assert.assertEquals(response.getStatusCode(),500,"the Confirm order is Placing order without reservation id");
    }

    @DescriptionProvider(slug = "VerifyCorrectNoOfSlotsReturned", description = "Verify that the confirm order api is throwing error without valid reservation id", author = "Pranay")

    @Test(groups = {"TMS","OMS","OMS-slots"})
    public void verifyConfirmOrderApiwithExpiredReservationid() throws IOException {

        AutomationReport report = getInitializedReport(this.getClass(), false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String ,String> headersData = CreateHeader.createHeader();
        Map<String,String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
        Random rand = new Random();

        int external_reference_id=(int)rand.nextInt(1000000000);
        String orderDbName = env.toUpperCase()+ "-"+"ORDER-CROMA";
        String checkOrderStatusDbQuery="select id from potential_order where status=\"abandoned\" limit 1";
        JsonObject abandonedorderid = new JsonObject(TmsDBQueries.getOrderDetails(orderDbName,checkOrderStatusDbQuery));
        Integer abandonedreservationid= abandonedorderid.getJsonArray("rows").getJsonObject(0).getInteger("id");
        Response response=OrderCreation.placeTmsOrderWithOutReservation(report , memberData,headersData,skuData,abandonedreservationid,external_reference_id);
        Assert.assertEquals(response.getStatusCode(),500,"the Confirm order is Placing order without reservation id");
    }



}