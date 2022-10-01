package tms.dapi;

import TMS.api.DAPI.DapiAppForTMS;
import TMS.configAndUtilities.*;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class DapiTest extends BaseTest {
    @DescriptionProvider(slug = "TMS Delivery flow", description = "Genrate OMS order and do delivery", author = "tushar")

    @Test(groups = {"TMS", "order_status", "OMS", "test_oms1"})
    public void verifyOrderIsDelivered() throws IOException {

        AutomationReport report = getInitializedReport(this.getClass(), false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> ceeData = CeeData.getCeeDetailsMap(env.toUpperCase());
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String, String> headersData = CreateHeader.createHeader();
        Map<String, String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
        Random rand = new Random();
        int external_reference_id=(int)rand.nextInt(1000000000);
        Integer orderId = OrderCreation.placeTmsOrder(report, memberData, headersData, skuData,external_reference_id);

        //DoDelivery.doDelivery(report,ceeData,headersData);

    }
}
