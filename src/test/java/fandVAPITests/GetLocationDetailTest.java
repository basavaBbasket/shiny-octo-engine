package fandVAPITests;

import api.warehousecomposition.planogram_FC.AdminCookie;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.fandv.GetLocationDetail;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class GetLocationDetailTest extends BaseTest {
        @DescriptionProvider(author = "nijaguna", description = "This testcase validates response schema for GetLocationDetail api.",slug = "Validate GetLocationDetail api")
        @Test(groups = {"fnv_automation" , "regression",})
        public void validGetLocationDetail() throws IOException {
            AutomationReport report = getInitializedReport(this.getClass(),false);
            String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
            String adminUserName = adminCred[0];
            String adminPassword = adminCred[1];
            Map<String, String> cookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);
            Properties prop=new Properties();
            prop.load(new FileInputStream("src//main//resources//fandvconfig.properties"));
            String csurftoken = prop.getProperty("csurftoken");
            String channel =prop.getProperty("channel");
            String tracker = UUID.randomUUID().toString();
            Map<String,String> queryParams = new HashMap<>();
            queryParams.put("referrer",prop.getProperty("referrer"));
            queryParams.put("lat",prop.getProperty("lat"));
            queryParams.put("lng",prop.getProperty("lng"));
            GetLocationDetail getLocationDetail = new GetLocationDetail(cookie,csurftoken,channel,tracker);
            Response response = getLocationDetail.validateLocationDetail(serverName,queryParams,"schema//fandv//getLocationDetail-200.json",report);
            report.log("Status code: " +  response.getStatusCode(),true);
            report.log("Customer Config response: " + response.prettyPrint(),true);

        }

}
