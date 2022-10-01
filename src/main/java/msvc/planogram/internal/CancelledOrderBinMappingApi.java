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

public class CancelledOrderBinMappingApi extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;

    public CancelledOrderBinMappingApi(IReport report)
    {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**API(20): Empty Bin Post to clear the bins API
     * This method makes POST request and does schema validation
     * @param expectedResponseSchemaPath schema
     * @param fc_id fc_id
     * @response Response
     */

    public Response postCancelledOrderBinMappingApi(String expectedResponseSchemaPath , String fc_id)
    {
        String endpoint = String.format(msvc.planogram.internal.Endpoints.ORDER_BIN_MAPPING, fc_id);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .queryParam("orderID","1000701621")
                .header("X-Tracker", UUID.randomUUID().toString())
                .header("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()))
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();


        report.log("Status code: " + response.getStatusCode() +"\n Cancelled Order \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                .extract().response();
        return response;

    }

    /**
     * This method makes POST request
     * @param fc_id fc_id
     * @return Response
     */

    public Response postCancelledOrderBinMappingApi(String fc_id)
    {
        String endpoint = String.format(msvc.planogram.internal.Endpoints.ORDER_BIN_MAPPING , fc_id);

        Response response = RestAssured.given().spec(requestSpecification)
                .header("X-Tracker", UUID.randomUUID().toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate Response: " + response.prettyPrint(),true);
        return response;


    }



}
