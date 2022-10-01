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

public class GetLocationDetail {
    Map<String, String>  cookie;
    String channel;
    String tracker;
    public GetLocationDetail( Map<String, String>  cookie,
                          String channel,
                          String tracker){
        this.cookie = cookie;
        this.channel = channel;
        this.tracker = tracker;
    }
    public Response validateLocationDetail(String serverName,Map<String,String> queryParams,String expectedResponseSchemaPath, AutomationReport report) {
        String endpoint = String.format(Endpoints.BB_Get_Location_Detail);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Channel",channel);
        requestHeader.put("X-Tracker",tracker);
        Response response = FnvAPI.getWithHeaderCookiesAndQueryParms(serverName+ endpoint,requestHeader,cookie,queryParams,report,expectedResponseSchemaPath,"Location Details");
        Assert.assertEquals(response.getStatusCode(),200,"Incorrect status code for Get Location Details API");
        return response;
    }

}
