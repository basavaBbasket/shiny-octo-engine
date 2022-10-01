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

public class PriceDetails {
    Map<String, String> cookie;
    String csurftoken;
    String channel;
    String tracker;
    public PriceDetails( Map<String, String> cookie,
                        String csurftoken,
                        String channel,
                        String tracker){
        this.cookie = cookie;
        this.csurftoken = csurftoken;
        this.channel = channel;
        this.tracker = tracker;
    }
    public Response validatePriceDetailsResponse(String serverName,String expectedResponseSchemaPath, String mId, String saCityId, String saList,String skuIds, String visibility, String fcId,String pathId,String priority, String saId, AutomationReport report) {
        String endpoint = String.format(Endpoints.BB_Price_Details);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
                requestHeader.put("x-csurftoken",csurftoken);
                requestHeader.put("X-Channel",channel);
                requestHeader.put("X-Tracker",tracker);
                report.log( serverName + endpoint, true);
                report.log("Calling PriceDetails api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);
        JsonObject body= new JsonObject();
        body.put("m_id",mId).put("sa_city_id",saCityId).put("sa_list",saList).put("sku_ids",skuIds).put("visibility",visibility).put("fc_id",fcId).put("path_id",pathId).put("priority",priority).put("sa_id",saId);
        report.log("Request Body:" + body.toString(),true);
        Response response = FnvAPI.postWithHeaderAndCookies(serverName + endpoint,requestHeader,cookie,body.toString(),report,expectedResponseSchemaPath,"PriceDetails API");
        Assert.assertEquals(response.statusCode(),200,"Invalid StatusCode for PriceDetails API");
        return response;
    }
}
