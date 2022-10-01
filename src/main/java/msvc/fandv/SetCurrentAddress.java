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

public class SetCurrentAddress {
    Map<String, String>  cookie;
    String channel;
    String tracker;
    public SetCurrentAddress(
            Map<String, String>  cookie,
            String channel,
            String tracker){
        this.cookie = cookie;
        this.channel = channel;
        this.tracker = tracker;
    }
    public Response validateSetCurrentAddress(String serverName, String homeAddress, AutomationReport report) {
        String endpoint = String.format(Endpoints.BB_Set_Current_Address);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Channel",channel);
        requestHeader.put("X-Tracker",tracker);
        Response response = FnvAPI.postWithHeaderAndCookies(serverName+ endpoint,requestHeader,cookie,"homeAddress" + homeAddress,report,"Set Current Address");
        Assert.assertEquals(response.statusCode(),200,"Invalid StatusCode for Set Current Address API");
        return response;
    }

}
