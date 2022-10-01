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

public class Header {
    // Instance Variables
    Map<String, String> cookie;

    String channel;
    String tracker;
    // Constructor Declaration of Class
    public Header( Map<String,String> cookie,
                              String channel,
                              String tracker){
        this.cookie = cookie;
        this.channel = channel;
        this.tracker = tracker;
    }
    public Response Header(String serverName, String expectedResponseSchemaPath, AutomationReport report) {
        String endpoint = String.format(Endpoints.BB_Header);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Channel",channel);
        requestHeader.put("X-Tracker",tracker);
        report.log( serverName + endpoint, true);
        report.log("Calling Header api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);
        Response response = FnvAPI.getWithHeaderAndCookies(serverName+ endpoint,requestHeader,cookie,report,expectedResponseSchemaPath,"Header API");
        Assert.assertEquals(response.getStatusCode(),200,"Incorrect status code for Header API");
        return response;
    }

}
