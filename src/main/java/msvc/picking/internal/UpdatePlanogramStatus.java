package msvc.picking.internal;

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
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UpdatePlanogramStatus extends WebSettings implements Endpoints {

    IReport report;

    private Map cookie;
    private String bb_decoded_uid;
    private RequestSpecification requestSpecification;

    public UpdatePlanogramStatus(String bb_decoded_uid,Map cookie, IReport report) {
        this.report = report;
        this.cookie=cookie;
        this.bb_decoded_uid=bb_decoded_uid;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request update Planogram Status To Complete api.
     * fc ID and job id are set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response updatePlanogramStatusToComplete(String expectedResponseSchemaPath,String fcid, String jobid) {
        String endpoint = String.format(Endpoints.PICKING_JOB_COMPLETE,fcid,jobid);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("bb-decoded-uid", bb_decoded_uid);
        requestHeader.put("content-type", "application/json");
        requestHeader.put("cookie",cookie.toString());
        requestHeader.put("cache-control", "no-cache");
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("postman-token", "2389156a-f65c-c63c-5ff9-63256b65e096");
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));

        report.log("Calling  update Planogram Status To Complete api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .post(msvcServerName + endpoint)
                .then();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n update Planogram Status To Complete response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    /**
     * This funtion call update Planogram Status To Complete
     * fc and job id are set during object creation
     * @param fcid
     * @param jobid
     * @return
     */
    public Response updatePlanogramStatusToComplete(String fcid, String jobid) {
        String endpoint = String.format(Endpoints.PICKING_JOB_COMPLETE,fcid,jobid);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("bb-decoded-uid", bb_decoded_uid);
        requestHeader.put("content-type", "application/json");
        requestHeader.put("cookie",cookie.toString());
        requestHeader.put("cache-control", "no-cache");
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("postman-token", "2389156a-f65c-c63c-5ff9-63256b65e096");
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));

        report.log("Calling  update Planogram Status To Complete api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .post(msvcServerName + endpoint)
                .then()
                .extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n update Planogram Status To Complete response: \n" + response.prettyPrint(),true);
        return response;
    }
}
