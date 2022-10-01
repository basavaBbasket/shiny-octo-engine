package fandVAPITests;

import api.warehousecomposition.planogram_FC.AdminCookie;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.fandv.CustomerLogout;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class CustomerLogoutTest extends BaseTest {
    @DescriptionProvider(author = "nijaguna", description = "This testcase validates response schema for CustomerLogout api.",slug = "Validate CustomerLogout api")
    @Test(groups = {"fnv_automation" , "regression",})
    public void validateCustomerLogout() throws IOException {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> cookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);
        Properties prop=new Properties();
        prop.load(new FileInputStream("src//main//resources//fandvconfig.properties"));
        String csurftoken = prop.getProperty("csurftoken");String channel ="BB-FV-Android";
        String tracker = UUID.randomUUID().toString();
        CustomerLogout customerLogout = new CustomerLogout(cookie,csurftoken,channel,tracker);
        Response response = customerLogout.validateOTPRequestResponse(serverName,"schema//fandv//CustomerLogOut-200.json",report);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Customer Logout response: " + response.prettyPrint(),true);

    }
}
