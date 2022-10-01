package msvc.fandv;

import com.bigbasket.automation.reports.AutomationReport;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import org.testng.Assert;
import utility.api.FnvAPI;
import java.util.HashMap;
import java.util.Map;

public class CustomerOTPVerification {
    String channel;

    public CustomerOTPVerification(
            String channel){
        this.channel = channel;
    }

    public Response validateCustomerOTPVerification(String serverName,String identifier,String otp,String sourceid, AutomationReport report) {
        String endpoint = String.format(Endpoints.BB_Customer_OTP_Verification);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Channel",channel);
        report.log("Calling  Customer OTP Verification api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);
        JsonObject body= new JsonObject();
        body.put("mobile_no",identifier);
        body.put("mobile_no_otp",otp);
        body.put("source_id",sourceid);
        Response response= FnvAPI.postWithHeader(serverName+ endpoint,requestHeader,body.toString(),report,"Customer OTP");
        Assert.assertEquals(response.getStatusCode(),200,"Incorrect status code for customer OTP validation");
        return response;
    }

}
