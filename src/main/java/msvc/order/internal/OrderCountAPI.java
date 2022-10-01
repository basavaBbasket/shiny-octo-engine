package msvc.order.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import msvc.eta.internal.Endpoints;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class OrderCountAPI extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;
    private String xEntryContext;
    private String xEntryContextId;
    private String xService;
    private String xCaller;
    private Map<String,String> cookie;

    public OrderCountAPI(String xEntryContext, String xEntryContextId, String xService, String xCaller, Map<String, String> cookie, IReport report){
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
        this.xEntryContext = xEntryContext;
        this.xEntryContextId = xEntryContextId;
        this.xService = xService;
        this.xCaller = xCaller;
        this.cookie = cookie;
    }

    public Response GetOrderCountAPI(String sa_id, String slot_date, String dm_id, String[] order_statuses, String expectedResponseSchemaPath){
        String endpoint = msvc.order.internal.Endpoints.ORDERS;

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Cookie", cookie.toString());
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));

        report.log("Calling getOrderCounts api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader, true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .formParam("sa_id", sa_id)
                .formParam("slot_date", slot_date)
                .formParam("dm_id", dm_id)
                .formParam("order_statuses[]", Arrays.toString(order_statuses))
                .get(msvcServerName + endpoint)
                .then();

        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Live getOrderDetails response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

}
