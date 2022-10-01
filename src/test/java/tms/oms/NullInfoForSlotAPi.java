package tms.oms;

import TMS.api.OMS.GetSlotsApi;
import TMS.configAndUtilities.Config;
import TMS.configAndUtilities.CreateHeader;
import TMS.configAndUtilities.MemberData;
import TMS.configAndUtilities.SkuData;
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

public class NullInfoForSlotAPi extends BaseTest {

    @DescriptionProvider(slug = "Getslotswithoutlmdfcid", description = "Verify that the get slots api is not returning anydata without proper details", author = "Pranay")

    @Test(groups = {"TMS","OMS","OMS-slots"})
    public void getslotswithoutlmdfcid() throws IOException, ParseException {

        AutomationReport report = getInitializedReport(this.getClass(),false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String ,String> headersData = CreateHeader.createHeader();
        Map<String,String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
        report.log("Calling get slots api without LMD FC Id in the api body",true);
        Response response =  GetSlotsApi.getSlotsDatesWithParam(report, headersData, memberData, skuData, "", 10, Integer.valueOf(Config.tmsConfig.getString("max_slot_days")),Integer.valueOf(Config.tmsConfig.getString("max_slot_count")));

        Assert.assertEquals(response.getStatusCode(),500,"the Get Slots api is not returning 500 after passing no lmd fc id");


    }

    @DescriptionProvider(slug = "Getslotswithoutpincode", description = "Verify that the get slots api is not returning anydata without proper details", author = "Pranay")

    @Test(groups = {"TMS","OMS","OMS-slots"})
    public  void getslotswithoutpincode() throws IOException, ParseException {

        AutomationReport report = getInitializedReport(this.getClass(), false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String ,String> headersData = CreateHeader.createHeader();
        Map<String,String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
        report.log("Calling get slots api without pincode in the api body",true);

        memberData.replace("pincode","12");
        Response response =  GetSlotsApi.getSlotsDatesWithParam(report, headersData, memberData, skuData, Config.tmsConfig.getString("lmd_fc_id"), 10, Integer.valueOf(Config.tmsConfig.getString("max_slot_days")),Integer.valueOf(Config.tmsConfig.getString("max_slot_count")));
        Assert.assertEquals(response.getStatusCode(),500,"the Get Slots api is not returning 500 after passing no pincode");




    }

    @DescriptionProvider(slug = "Getslotswithoutexternalskuid", description = "Verify that the get slots api is not returning anydata without proper details", author = "Pranay")
    @Test(groups = {"TMS","OMS","OMS-slots"})
    public void getslotswithoutexternalskuid() throws IOException, ParseException {

        AutomationReport report = getInitializedReport(this.getClass(), false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String ,String> headersData = CreateHeader.createHeader();
        Map<String,String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
        skuData.replace("external_sku_id","");
        report.log("Calling get slots api without external sku id in the api body",true);

        Response response =  GetSlotsApi.getSlotsDatesWithParam(report, headersData, memberData, skuData, Config.tmsConfig.getString("lmd_fc_id"), 10, Integer.valueOf(Config.tmsConfig.getString("max_slot_days")),Integer.valueOf(Config.tmsConfig.getString("max_slot_count")));
        Assert.assertEquals(response.getStatusCode(),500,"the Get Slots api is not returning 500 after passing no external sku id");




    }
}
