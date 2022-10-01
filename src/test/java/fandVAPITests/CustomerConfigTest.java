package fandVAPITests;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.fandv.CustomerConfig;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class CustomerConfigTest extends BaseTest {
        @DescriptionProvider(author = "nijaguna", description = "This testcase validates response schema for CustomerConfig api.",slug = "Validate CustomerConfig api")
        @Test(groups = {"fnv_automation" , "regression",})
        public void validCustomerConfig() throws IOException {
            Properties prop=new Properties();
            prop.load(new FileInputStream("src//main//resources//fandvconfig.properties"));
            AutomationReport report = getInitializedReport(this.getClass(),false);
            String csurftoken = prop.getProperty("csurftoken");
            String channel =prop.getProperty("channel");
            String tracker = UUID.randomUUID().toString();
            CustomerConfig customerConfig = new CustomerConfig(csurftoken,channel,tracker);
            Response response = customerConfig.validateConfigDetails(serverName,"schema//fandv//customerConfig-200.json",report);
            report.log("Status code: " +  response.getStatusCode(),true);
            report.log("Customer Config response: " + response.prettyPrint(),true);

        }

}
