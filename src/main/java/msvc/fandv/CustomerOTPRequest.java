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
import java.util.UUID;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CustomerOTPRequest {

    String channel;

    public CustomerOTPRequest(
            String channel){
        this.channel = channel;
    }
    public Response validateOTPRequestResponse(String serverName, String identifier, AutomationReport report) {
        String endpoint = String.format(Endpoints.BB_Customer_OTP_Request);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Channel",channel);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        report.log("Calling Customer OTP Request api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);
        JsonObject body= new JsonObject();
        body.put("identifier",identifier);
        Response response= FnvAPI.postWithHeader(serverName+ endpoint,requestHeader,body.toString(),report,"Customer OTP request");
        Assert.assertEquals(response.statusCode(),200,"Invalid StatusCode for Customer OTP API");
        return response;
    }

}
