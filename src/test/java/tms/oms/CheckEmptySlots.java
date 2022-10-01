package tms.oms;

import TMS.api.OMS.GetSlotsApi;
import TMS.api.OMS.UpdateActions;
import TMS.api.OMS.UpdateOrderStatus;
import TMS.api.OMS.UpdatePackageDetails;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class CheckEmptySlots extends BaseTest {

    @DescriptionProvider(slug = "Check empty slot", description = "Test will empty the slots for the next day by updating the slot instance table will validate the response from get slots api and will add the slots back to the day", author = "Pranay")

    @Test(groups = {"TMS","tms-order-creation","tms1"},enabled = false)
    public void checkOrderStatusToBePacked() throws IOException {

        AutomationReport report = getInitializedReport(this.getClass(), false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String ,String> headersData = CreateHeader.createHeader();
        Map<String,String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
        String mavericksDbName = env.toUpperCase()+ "-"+"MAVERICKS-CROMA";


        SimpleDateFormat dtf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        Date dateObj = calendar.getTime();
        String formattedDate = dtf.format(dateObj);
        report.log("Updating the slots capacity to 0",true);
        System.out.println("date date"+formattedDate);
        String emptyslots="update slot_instance set capacity=0  where slot_start_date='"+formattedDate+"';";
        Response orderDetailsJson = (TmsDBQueries.updateSlotInstanceDetails(mavericksDbName,emptyslots));
        report.log("Getting the slots ",true);

        Response response =  GetSlotsApi.getSlotsDatesWithParam(report, headersData, memberData, skuData, Config.tmsConfig.getString("lmd_fc_id"), 10, Integer.valueOf(Config.tmsConfig.getString("max_slot_days")),Integer.valueOf(Config.tmsConfig.getString("max_slot_count")));
        report.log("response"+response.toString(),true);
        Assert.assertEquals(GetSlotsApi.getSlotslengthFromResponse(response,report),0,"Slots are getting even after making them 0");
        report.log("Updating the slots capacity to 1000000",true);
        String updateslots="update slot_instance set capacity=1000000  where slot_start_date='"+formattedDate+"';";
         orderDetailsJson = TmsDBQueries.updateSlotInstanceDetails(mavericksDbName,updateslots);
         response =  GetSlotsApi.getSlotsDatesWithParam(report, headersData, memberData, skuData, Config.tmsConfig.getString("lmd_fc_id"), 10, Integer.valueOf(Config.tmsConfig.getString("max_slot_days")),Integer.valueOf(Config.tmsConfig.getString("max_slot_count")));
        Assert.assertNotEquals(GetSlotsApi.getSlotslengthFromResponse(response,report),0,"Slots are getting even after making them 0");

        report.log("response"+response.toString(),true);

    }
}


