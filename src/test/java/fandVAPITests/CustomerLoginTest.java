package fandVAPITests;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.fandv.CustomerOTPRequest;
import msvc.fandv.CustomerOTPVerification;
import msvc.fandv.GetCustomerOTP;
import org.json.JSONObject;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;
import static com.bigbasket.automation.BaseSettings.serverName;
public class CustomerLoginTest extends BaseTest {
    @DescriptionProvider(author = "nijaguna", description = "This testcase validates the Customer Login",slug = "Validate Customer Login")
    @Test(groups = {"validatecustomerlogin_fnv" , "regression","fnv_automation",})
    public void customerLoginValidation() throws IOException {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        Properties prop=new Properties();
        prop.load(Files.newInputStream(Paths.get("src//main//resources//fandvconfig.properties")));
        String channel = prop.getProperty("channel");
        String token = prop.getProperty("Authorization");
        String sourceId = prop.getProperty("sourceId");
        String identifier = AutomationUtilities.getRandomMobileNo();
        CustomerOTPRequest customerOTPRequest=new CustomerOTPRequest(channel);
        report.log("After selection of Language, Enter the Mobile number", true);
        Response customerOTPRequestResponse=customerOTPRequest.validateOTPRequestResponse(serverName,identifier,report);
        report.log("Customer OTP requested", true);
        GetCustomerOTP getCustomerOTP=new GetCustomerOTP();
        Response getCustomerOTPResponse=getCustomerOTP.validateGetOTPResponse(identifier,token,report);
        report.log("Entering the OTP to login", true);
        String otpCode=new JSONObject(getCustomerOTPResponse.getBody().asString()).getString("code");
        CustomerOTPVerification customerOTPVerification=new CustomerOTPVerification(channel);
        customerOTPVerification.validateCustomerOTPVerification(serverName,identifier,otpCode,sourceId,report );
    }

}
