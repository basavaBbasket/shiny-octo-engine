package msvc.delivery_app_apis.hubops.dapi_v2.route;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class CollectOrder extends WebSettings implements Endpoints {
    IReport report;
    private String cookie;
    private String route_id;
    private String xRefreshToken;
    private String Authorization;
    private RequestSpecification requestSpecification;


    public CollectOrder( String route_id, String cookie,String xRefreshToken,String authorization, IReport report) {
        this.report = report;
        this.cookie = cookie;
        this.route_id=route_id;
        this.xRefreshToken=xRefreshToken;
        this.Authorization=authorization;

        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * this function will make patch call to collect order api
     * route id is set during object creation
     * validates the api response it should be 204
     * @param collected_status
     * @return
     */
    public Response collectOrder(boolean collected_status) {
        String endpoint = String.format(Endpoints.COLLECT_ORDER,route_id);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Refresh-Token",xRefreshToken);
        requestHeader.put("Authorization",Authorization);


        report.log("Calling collectOrder  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        JsonObject body= new JsonObject();
        body.put("collected",collected_status);
        Response  response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .patch(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        Assert.assertEquals(response.getStatusCode(),204,"Response status is not matching");
        report.log("Status code: " + response.getStatusCode() +"\n VerifyLogin response: \n" + response.prettyPrint(),true);

        return response;
    }
}
