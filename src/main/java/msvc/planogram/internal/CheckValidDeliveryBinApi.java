package msvc.planogram.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import msvc.wio.internal.Endpoints;

import java.time.Instant;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CheckValidDeliveryBinApi extends WebSettings implements Endpoints {

    IReport report;
    private RequestSpecification requestSpecification;
    private  String binloc;

    public CheckValidDeliveryBinApi(String binloc , IReport report)
    {
        this.binloc = binloc;
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**API(20): Check if delivery bin is valid
     * This method makes GET request and does schema validation
     * @param expectedResponseSchemaPath schema
     * @param  fc_id
     * @response Response
     */

    public Response getCheckValidDeliveryBinApi(String expectedResponseSchemaPath,String fc_id)
    {
        String endpoint = String.format(msvc.planogram.internal.Endpoints.CHECK_VALID_DELIVERY_BIN,fc_id);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .urlEncodingEnabled(false)
                .queryParam("binLoc",binloc)
                .header("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()))
                .header("X-Tracker", UUID.randomUUID().toString())
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n Fc List Response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                .extract().response();
        return response;

    }


    /**
     * This method makes GET request
     * @param fc_id fc_id
     * @response Response
     */

    public Response getCheckValidDeliveryBinApi(String fc_id)
    {
        String endpoint = String.format(msvc.planogram.internal.Endpoints.CHECK_VALID_DELIVERY_BIN, fc_id);

        Response response = RestAssured.given().spec(requestSpecification)
                .queryParam("binloc",binloc)
                .header("X-Tracker", UUID.randomUUID().toString())
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate Response: " + response.prettyPrint(),true);
        return response;

    }






}
