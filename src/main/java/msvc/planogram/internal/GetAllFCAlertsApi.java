package msvc.planogram.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import msvc.listing_assembler.Endpoints;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetAllFCAlertsApi extends WebSettings implements Endpoints {

    IReport report;
    private RequestSpecification requestSpecification;


    public GetAllFCAlertsApi(IReport report)
    {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * API: Alert API--> used to get all alerts created for FC
     * This method makes GET request to  Alet API and validates the schema of response
     *  @param expectedResponseSchemaPath
     * @return   response
     */

    public Response getAllFcAlerts(String expectedResponseSchemaPath)
    {
        String endpoint = String.format(msvc.planogram.internal.Endpoints.GET_ALL_FC_ALERTS);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();


        report.log("Status code: " + response.getStatusCode() +"\n FC alerts \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                .extract().response();
        return response;


    }

    /**
     * This method makes GET request to Alert api
     * @return
     */


    public Response getAllFcAlerts()
    {
        String endpoint = String.format(msvc.planogram.internal.Endpoints.GET_ALL_FC_ALERTS);

        Response response = RestAssured.given().spec(requestSpecification)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate Response: " + response.prettyPrint(),true);
        return response;

    }




}
