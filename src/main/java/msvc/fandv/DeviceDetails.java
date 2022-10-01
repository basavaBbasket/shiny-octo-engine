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


public class DeviceDetails {
    String channel;
    String tracker;
    public DeviceDetails( String channel,
                          String tracker){
        this.channel = channel;
        this.tracker = tracker;
    }
    public Response validateDeviceDetails(String serverName, String expectedResponseSchemaPath, String deviceId, AutomationReport report) {
        String endpoint = String.format(Endpoints.BB_Device_Details);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Channel",channel);
        requestHeader.put("X-Tracker",tracker);
        Response response= FnvAPI.getWithHeaders(serverName + endpoint.replace("replaceDeviceId",deviceId),requestHeader,report,expectedResponseSchemaPath,"Device Details");
        Assert.assertEquals(response.getStatusCode(),200,"Incorrect status code for Device Details API");
        return response;
    }
}
