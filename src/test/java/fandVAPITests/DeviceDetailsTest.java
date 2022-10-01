package fandVAPITests;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.fandv.DeviceDetails;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

public class DeviceDetailsTest extends BaseTest {
    @DescriptionProvider(author = "nijaguna", description = "This testcase validates response schema for Device Details api.",slug = "Validate Device Details api")
    @Test(groups = {"fnv_automation" , "regression",})
    public void validCustomerOTPRequest() throws IOException {
        Properties prop=new Properties();
        prop.load(new FileInputStream("src//main//resources//fandvconfig.properties"));
        AutomationReport report = getInitializedReport(this.getClass(), false);
         String csurftoken = prop.getProperty("csurftoken");
        String channel = prop.getProperty("channel");
        String tracker = UUID.randomUUID().toString();
        String deviceId = prop.getProperty("deviceDetailsDeviceName");
        DeviceDetails deviceDetails = new DeviceDetails(csurftoken, channel, tracker);
        Response response = deviceDetails.validateDeviceDetails(serverName,"schema//fandv//GetDeviceDetails-200.json", deviceId, report);
        report.log("Status code: " + response.getStatusCode(), true);
        report.log("Device Details response: " + response.prettyPrint(), true);
    }

}
