package msvc.fandv;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.vertx.core.json.JsonObject;
import org.testng.Assert;
import utility.api.FnvAPI;

import java.util.HashMap;
import java.util.Map;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetCustomerOTP {

    public Response validateGetOTPResponse(String identifier,String token, AutomationReport report) {
        String endpoint = String.format(Endpoints.BB_Get_Customer_OTP_Request).replace("replace_Identifier",identifier) ;
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Authorization",token);
        report.log("Fetching the Requested Customer OTP", true);
        report.log("Calling Get Customer OTP api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);
        Response response = FnvAPI.getWithHeaders(endpoint,requestHeader,report,"Get Customer OTP");
        Assert.assertEquals(response.getStatusCode(),200,"Incorrect status code for Get Customer OTP API");
        return response;
    }

}
