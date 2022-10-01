package msvc.wio.external;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import msvc.order.internal.Endpoints;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UpdateReturnReceivingApi extends WebSettings implements Endpoints {

    IReport report;
    private String contentType;
    private String xTracker;
    private String UserId;
    private String xCaller;
    private String bbAuth;
    private RequestSpecification requestSpecification;

    public UpdateReturnReceivingApi(String  bbAuth, String xTracker, String xCaller, String UserId, String contentType, IReport report) {
        this.bbAuth = bbAuth;
        this.xTracker = xTracker;
        this.xCaller = xCaller;
        this.report = report;
        this.contentType = contentType;
        this.UserId = UserId;
        this.requestSpecification = getSimpleRequestSpecification(serverName, this.report);
    }


    /**
     * API: Update Return Receiving
     * This method makes PUT request to API and validates the schema
     *
     * @param expectedResponseSchemaPath
     * @return Response
     */

    public Response putUpdateReturnReceiving(String expectedResponseSchemaPath) {
        String endpoint = String.format(msvc.wio.external.Endpoints.UPDATE_RETURN_RECEIVING);
        Map<String, String> requestHeader = new HashMap<>();

        requestHeader.put("Content-Type" , contentType);
        requestHeader.put("X-TimeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));

        requestHeader.put("X-Tracker", xTracker);
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("UserId", UserId);
        requestHeader.put("cookie" , bbAuth);


        JSONObject requestBody = new JSONObject();

        requestBody.put("id", 10);
        requestBody.put("mode", "container_receiving");

        JSONObject dataJson1 = new JSONObject();
        dataJson1.put("container_label", "2");
        dataJson1.put("status", "missing");

        JSONObject dataJson2 = new JSONObject();
        dataJson2.put("container_label", "4");
        dataJson2.put("status", "missing");

        JSONArray dataArray = new JSONArray();
        dataArray.put(dataJson1);
        dataArray.put(dataJson2);


        requestBody.put("data", dataArray);


        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(requestBody.toString())
                .put(serverName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n  response: \n" + response.prettyPrint(), true);
        report.log("Verifying response schema.", true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                .extract().response();
        return response;

    }

    /**
     * This method make PUT request to api
     * @return Response
     */


    public Response putUpdateReturnReceiving() {
        String endpoint = String.format(msvc.wio.external.Endpoints.UPDATE_RETURN_RECEIVING);
        Map<String, String> requestHeader = new HashMap<>();

        requestHeader.put("Content-Type" , contentType);
        requestHeader.put("X-TimeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));

        requestHeader.put("X-Tracker", xTracker);
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("UserId", UserId);
        requestHeader.put("cookie" , bbAuth);
        JSONObject requestBody = new JSONObject();

        requestBody.put("id", "10");
        requestBody.put("mode", "container_receiving");

        JSONObject dataJson1 = new JSONObject();
        dataJson1.put("container_label", "2");
        dataJson1.put("status", "missing");

        JSONObject dataJson2 = new JSONObject();
        dataJson2.put("container_label", "4");
        dataJson2.put("status", "missing");

        JSONArray dataArray = new JSONArray();
        dataArray.put(dataJson1);
        dataArray.put(dataJson2);


        requestBody.put("data", dataArray);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(requestBody)
                .put(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log(" response: " + response.prettyPrint(), true);
        return response;


    }
}
