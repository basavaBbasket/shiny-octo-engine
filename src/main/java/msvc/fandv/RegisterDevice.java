package msvc.fandv;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.Assert;
import utility.api.FnvAPI;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RegisterDevice {

    String channel;
    String tracker;
    public RegisterDevice(
            String channel,
            String tracker){

        this.channel = channel;
        this.tracker = tracker;
    }
    public Response validateRegisterDevice(String serverName,String expectedResponseSchemaPath, String imei, AutomationReport report) {
        String endpoint = String.format(Endpoints.BB_Register_Device);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Channel",channel);
        requestHeader.put("X-Tracker",tracker);
        Response response = FnvAPI.postWithHeader(serverName + endpoint, requestHeader,"\"imei\":" + imei,report,expectedResponseSchemaPath,"Validate Register Device");
        Assert.assertEquals(response.statusCode(),200,"Invalid StatusCode for Register Device API");
        return response;
    }

}
