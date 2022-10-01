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

public class ProductDetails {
    Map<String, String> cookie;
    String csurftoken;
    String channel;
    String tracker;
    public ProductDetails(Map<String, String>  cookie,
                          String csurftoken,
                          String channel,
                          String tracker){
        this.cookie = cookie;
        this.csurftoken = csurftoken;
        this.channel = channel;
        this.tracker = tracker;
    }
    public Response ProductDetails(String serverName, String expectedResponseSchemaPath, AutomationReport report) {
        String endpoint = String.format(Endpoints.BB_Product_Details);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("x-csurftoken",csurftoken);
        requestHeader.put("X-Channel",channel);
        requestHeader.put("X-Tracker",tracker);
        requestHeader.put("User-Agent","BB Android/v1.1.2-debug/os 11");
        requestHeader.put("X-Entry-Context-Id","8");
        requestHeader.put("X-Entry-Context","pb-fnv");
        requestHeader.put("_bb_bb2.0","1");
        Response response = FnvAPI.getWithHeaderAndCookies(serverName+ endpoint,requestHeader,cookie,report,expectedResponseSchemaPath,"Product Details");
        Assert.assertEquals(response.statusCode(),200,"Invalid StatusCode for ProductDetails API");
        return response;
    }

}
