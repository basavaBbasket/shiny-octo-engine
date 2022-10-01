package tms.oms;

import tms.api.OMS.GetSlotsApi;
import tms.configAndUtilities.Config;
import tms.configAndUtilities.CreateHeader;
import tms.configAndUtilities.MemberData;
import tms.configAndUtilities.SkuData;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;


public class VerifyCorrectNoOfSLotsReturned extends BaseTest {

    @DescriptionProvider(slug = "VerifyCorrectNoOfSlotsReturned", description = "Verify that the get slots api is  returning Correct No Of Slots", author = "Pranay")

    @Test(groups = {"TMS","OMS","OMS-slots"})
    public void verifyCorrectNoOfSlotsReturned() throws IOException, ParseException {

        AutomationReport report = getInitializedReport(this.getClass(),false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String ,String> headersData = CreateHeader.createHeader();
        Map<String,String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());

        int expectedslots=10;
        report.log("Calling get slots api with"+expectedslots+" no of slots",true);
        Response response =  GetSlotsApi.getSlotsDatesWithParam(report, headersData, memberData, skuData, Config.tmsConfig.getString("lmd_fc_id"), 10, Integer.valueOf(Config.tmsConfig.getString("max_slot_days")),expectedslots);

        Assert.assertEquals(response.getStatusCode(),200,"the api is not returning 500 after passing no lmd fc id");
       int slots= GetSlotsApi.getSlotsFromResponse(response,report);
        report.log("Get Slots api returned"+slots+" no of slots",true);
       Assert.assertEquals(slots,expectedslots,"the  slots returnted from getslots are not matching with the slots requested");




    }

}
