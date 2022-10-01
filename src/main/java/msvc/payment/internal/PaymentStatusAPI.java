package msvc.payment.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;
import msvc.order.internal.Endpoints;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class PaymentStatusAPI extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;

    private final String xSourceSlug;
    private final String xSourceId;
    private final String xAuthToken;
    private final String xAuthorization;
    private final String xEntryContext;
    private final String xEntryContextId;
    private final String bbDecodedMid;


    public PaymentStatusAPI(String xSourceSlug, String xSourceId, String xAuthToken, String xAuthorization, String xEntryContext, String xEntryContextId, String bbDecodedMid, IReport report ){
        this.xSourceSlug = xSourceSlug;
        this.xSourceId = xSourceId;
        this.xAuthToken = xAuthToken;
        this.xAuthorization = xAuthorization;
        this.xEntryContext = xEntryContext;
        this.xEntryContextId = xEntryContextId;
        this.bbDecodedMid = bbDecodedMid;
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    public Response GetPaymentStatusAPI(String bb_txn_id, String expectedResponseSchemaPath) {
        String endpoint = msvc.payment.internal.Endpoints.VALIDATE_PAYMENT_SVC;

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-SourceSlug", xSourceSlug);
        requestHeader.put("X-SourceId", xSourceId);
        requestHeader.put("X-AuthToken", xAuthToken);
        requestHeader.put("Authorization", xAuthorization);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));
        requestHeader.put("bb-decoded-mid", bbDecodedMid);

        report.log("Calling validate payment service api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader, true);

        JsonObject body= new JsonObject();
        body.put("bb_txn_id", bb_txn_id);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body)
                .post(msvcServerName + endpoint)
                .then().log().all();;

        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Payment Status API response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }
}
