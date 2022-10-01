package tms.dapi;

import tms.api.DAPI.DapiUtils;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;
import tms.configAndUtilities.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class DapiTest extends BaseTest {
    @DescriptionProvider(slug = "TMS Delivery flow Till check in", description = "Genrate OMS order and do delivery till checkin flow", author = "Pranay")

    @Test(groups = {"TMS", "order_status", "OMS", "test_oms11"})
    public void verifyOrderIsDelivered() throws IOException, InterruptedException, ParseException {

        AutomationReport report = getInitializedReport(this.getClass(), false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String, String> headersData = CreateHeader.createHeader();
        Map<String, String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
        Random rand = new Random();
        int external_reference_id=(int)rand.nextInt(1000000000);
        Integer orderId = OrderCreation.placeTmsOrder(report, memberData, headersData, skuData,external_reference_id);


        TmsDBQueries tmsDBQueries=new TmsDBQueries();
        ArrayList latlong=tmsDBQueries.getLatLongOrderDetails(String.valueOf(orderId),report);
        DapiUtils dapiUtils=new DapiUtils();
        int ceeid=tmsDBQueries.getCeeId(10,2,Config.tmsConfig.getString("dapi_password"),report);

        tmsDBQueries.logOutCee(ceeid,report);
       Response respons= dapiUtils.dapiTillCheckin(report,"2","bb-stable",ceeid,"123",String.valueOf(rand.nextInt(1000000000)),"6.0.0","",String.valueOf(latlong.get(0)),String.valueOf(latlong.get(1)),tmsDBQueries.getQrCode(Config.tmsConfig.getString("lmd_fc_id")));
        report.log("response"+respons.prettyPrint(),true);

    }
}
