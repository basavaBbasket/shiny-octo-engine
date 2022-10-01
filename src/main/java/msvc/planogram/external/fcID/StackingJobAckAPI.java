package msvc.planogram.external.fcID;

import api.warehousecomposition.planogram_FC.internal.Endpoints;
import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class StackingJobAckAPI extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;
    private final String xCaller;

    public StackingJobAckAPI(String xCaller, IReport report) {
        this.xCaller = xCaller;
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    public Response PutStackingJobAckAPI(JsonObject body, Map cookie) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.GRN_STACKING_CREATE_JOB,1);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie.toString());
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));
        requestHeader.put("X-Caller", xCaller);

        report.log("Calling validate payment service api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader, true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body)
                .put(msvcServerName + endpoint)
                .then().log().all();
        ;

        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() + "\n Payment Status API response: \n" + response.prettyPrint(), true);
        report.log("Verifying status 200.", true);
        validatableResponse.assertThat().statusCode(200).log().all()
                .extract().response();
        return response;
    }
}
