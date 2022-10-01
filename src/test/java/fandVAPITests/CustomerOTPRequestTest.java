package fandVAPITests;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.fandv.CustomerOTPRequest;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class CustomerOTPRequestTest extends BaseTest {
    @DescriptionProvider(author = "nijaguna", description = "This testcase validates CustomerOTPRequest response API.",slug = "Validate CustomerOTPRequest api")
    @Test(groups = {"fnv_automation" , "regression",})
    public void validCustomerOTPRequest() throws IOException {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        Properties prop=new Properties();
        prop.load(new FileInputStream("src//main//resources//fandvconfig.properties"));
        String csurftoken = prop.getProperty("csurftoken");
        String channel =prop.getProperty("channel");
        String tracker = UUID.randomUUID().toString();
        String identifier= prop.getProperty("identifier");
        CustomerOTPRequest customerOTPRequest=new CustomerOTPRequest(channel,tracker);
        Response response = customerOTPRequest.validateOTPRequestResponse(serverName,"schema//fandv//CustomerOTPRequest-200.json",report);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Customer QR Login response: " + response.prettyPrint(),true);

    }

}


