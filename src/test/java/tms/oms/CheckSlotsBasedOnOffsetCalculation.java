package tms.oms;

import tms.api.OMS.GetSlotsApi;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import tms.configAndUtilities.CreateHeader;
import tms.configAndUtilities.MemberData;
import tms.configAndUtilities.*;

import java.io.IOException;
import java.util.Map;

public class CheckSlotsBasedOnOffsetCalculation extends BaseTest{

        @DescriptionProvider(slug = "Verify that the cut off is calculated and returned back in the API response", description = "If the offset is given to TMS in the API then TMS should calculate the cut off and adjust that and then return the slots", author = "tushar")
        @Test(groups = {"TMS","OMS","OMS-slots"})
        public void checkSlotBasedOnOffsetCalculation() throws IOException {

            AutomationReport report = getInitializedReport(this.getClass(), false);
            String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
            Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
            Map<String ,String> headersData = CreateHeader.createHeader();
            Map<String,String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
            Integer[] offse_times = {0,1400};
            String[] day = {"Today","Tomorrow"};
            for(int i = 0; i<2 ; i++) {
                Response response = GetSlotsApi.getSlotsDatesWithParam(report, headersData, memberData, skuData, Config.tmsConfig.getString("lmd_fc_id"), offse_times[i], Integer.valueOf(Config.tmsConfig.getString("max_slot_days")),Integer.valueOf(Config.tmsConfig.getString("max_slot_count")));
                JsonPath jsonPath = response.jsonPath();
                String strpath = "orders[0].slots[0].day";
                Assert.assertEquals(day[i],jsonPath.getString(strpath),"checking with offset "+offse_times[i].toString());
                report.log("checking with offset: "+offse_times[i],true);
            }
        }
    }
