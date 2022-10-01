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

public class CategoryTree {
    Map<String, String> cookie;
    String csurfToken;
    String channel;
    String tracker;
    public CategoryTree(Map<String, String>  cookie,
                        String csurfToken,
                        String channel,
                        String tracker){
        this.cookie = cookie;
        this.csurfToken = csurfToken;
        this.channel = channel;
        this.tracker = tracker;
    }
    public Response CategoryTree(String serverName, String expectedResponseSchemaPath, AutomationReport report) {
        String endpoint = String.format(Endpoints.BB_Category_Tree);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("x-csurftoken",csurfToken);
        requestHeader.put("X-Channel",channel);
        requestHeader.put("X-Tracker",tracker);
        Response response = FnvAPI.getWithHeaderAndCookies(serverName+ endpoint,requestHeader,cookie,report,expectedResponseSchemaPath,"Set Current Address");
        Assert.assertEquals(response.statusCode(),200,"Invalid StatusCode for CategoryTree API");
        return response;
    }

}
