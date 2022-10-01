package fandVAPITests;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.fandv.CustomerQRLogin;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class CustomerQRLoginTest extends BaseTest {
    @DescriptionProvider(author = "nijaguna", description = "This testcase logins Customer through QR API",slug = "Customer QR Login api")
    @Test(groups = {"fnv_automation" , "regression",})
    public void CustomerLoginTest() throws IOException {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        Properties prop=new Properties();
        prop.load(new FileInputStream("src//main//resources//fandvconfig.properties"));
        String csurftoken = prop.getProperty("csurftoken");
        String channel =prop.getProperty("channel");
        String tracker = UUID.randomUUID().toString();
        String challengeId= prop.getProperty("challengeId");
        String deviceId= prop.getProperty("deviceId");
        String deviceName= prop.getProperty("deviceName");
        CustomerQRLogin customerQRLogin = new CustomerQRLogin(csurftoken,channel,tracker);
        Response response = customerQRLogin.validateLoginResponse(serverName,"schema//fandv//QRLogin-200.json",challengeId,deviceId,deviceName,report);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Customer QR Login response: " + response.prettyPrint(),true);

    }
}
