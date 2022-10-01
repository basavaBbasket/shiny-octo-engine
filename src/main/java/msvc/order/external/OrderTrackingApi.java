package msvc.order.external;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.config.RestAssuredConfig.config;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class OrderTrackingApi extends WebSettings implements Endpoints {
    IReport report;
    private String xOriginContext;
    private String xAuthToken;
    private String xCaller;
    private String xEntryContextId;
    private String xEntryContext;
    private String orderId;
    private RequestSpecification requestSpecification;

    public OrderTrackingApi(String xOriginContext, String xAuthToken, String xCaller, String xEntryContextId, String xEntryContext, String orderId, IReport report) {
        this.report = report;
        this.xOriginContext = xOriginContext;
        this.xAuthToken = xAuthToken;
        this.xCaller = xCaller;
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;
        this.orderId = orderId;

        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request to order tracking api.
     * Order ID is set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response getLiveOrderTrackingData(String expectedResponseSchemaPath) {
        String endpoint = String.format(Endpoints.TRACK_ORDER, orderId);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Origin-Context", xOriginContext);
        requestHeader.put("X-Authtoken", xAuthToken);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);

        report.log("Calling live order tracking api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Live order tracking response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    /**
     * This method makes GET request to order tracking api.
     * Order ID is set during object creation.
     *
     * @return response
     */
    public Response getLiveOrderTrackingData() {
        String endpoint = String.format(Endpoints.TRACK_ORDER, orderId);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Origin-Context", xOriginContext);
        requestHeader.put("X-Authtoken", xAuthToken);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);

        report.log("Calling live order tracking api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Live order tracking response: " + response.prettyPrint(),true);
        return response;
    }
}
