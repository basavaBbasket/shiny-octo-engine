package msvc.jhulk;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import msvc.wio.internal.Endpoints;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CurrentDeliveryApi extends WebSettings implements Endpoints {
    IReport report;
    private String EntryContext;
    private String EntryContextId;
    private String xCaller;
    private String xService;
    private RequestSpecification requestSpecification;

    public CurrentDeliveryApi(String EntryContext, String EntryContextId, String xCaller, String xService, IReport report) {
        this.EntryContext = EntryContext;
        this.EntryContextId = EntryContextId;
        this.xService = xService;
        this.xCaller = xCaller;
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request to  API and validates schema
     *
     * @param expectedResponseSchemaPath
     * @return response
     */
    public Response getCurrentDelivery(String expectedResponseSchemaPath, String memberId) {
        String endpoint = String.format(msvc.jhulk.Endpoints.CURRENT_DELIVERY);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Entry-Context-Id", EntryContextId);
        requestHeader.put("X-Entry-Context", EntryContext);
        requestHeader.put("bb-decoded-mid", memberId);
        requestHeader.put("X-Service", xService);
        requestHeader.put("Content-Type", "application/json");

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();


        report.log("Status code: " + response.getStatusCode() + "\n response \n" + response.prettyPrint(), true);
        report.log("Verifying response schema.", true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                .extract().response();
        return response;
    }

    /**
     * This method makes GET request to API
     *
     * @return Response
     */
    public Response getCurrentDelivery(String memberId) {
        String endpoint = String.format(msvc.jhulk.Endpoints.CURRENT_DELIVERY);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Entry-Context-Id", EntryContextId);
        requestHeader.put("X-Entry-Context", EntryContext);
        requestHeader.put("bb-decoded-mid", memberId);
        requestHeader.put("X-Service", xService);

        report.log("Calling get current delivery api. Endpoint: " + endpoint, true);
        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate Response: " + response.prettyPrint(), true);
        return response;


    }

}
