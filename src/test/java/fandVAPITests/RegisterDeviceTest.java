package fandVAPITests;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.fandv.RegisterDevice;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class RegisterDeviceTest extends BaseTest {
        @DescriptionProvider(author = "nijaguna", description = "This testcase validates response schema for RegisterDevice api.",slug = "Validate RegisterDevice api")
        @Test(groups = {"fnv_automation" , "regression",})
        public void validRegisterDevice() throws IOException {
            Properties prop=new Properties();
            prop.load(new FileInputStream("src//main//resources//fandvconfig.properties"));
            AutomationReport report = getInitializedReport(this.getClass(),false);
            String csurftoken = prop.getProperty("csurftoken");
            String channel =prop.getProperty("channel");
            String tracker = UUID.randomUUID().toString();
            String imei = prop.getProperty("imei");
            RegisterDevice registerDevice = new RegisterDevice(csurftoken,channel,tracker);
            Response response = registerDevice.validateRegisterDevice(serverName,"schema//fandv//registerDevice-200.json",imei,report);
            report.log("Status code: " +  response.getStatusCode(),true);
            report.log("Customer Config response: " + response.prettyPrint(),true);

        }

}
