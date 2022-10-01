package msvc.planogram.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class BusyPickerCountApi extends WebSettings implements msvc.wio.internal.Endpoints {
    IReport report;

    private RequestSpecification requestSpecification;

    public BusyPickerCountApi(IReport report)
    {

        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * API: Gets the count of busy pikcer doing some job
     * This method makes GET request to  Picker api and validates the schema of response
     *  @param expectedResponseSchemaPath schema
     * @param  fc_id
     * @return   response
     */

    public Response getCountOfBusyPickerApi(String expectedResponseSchemaPath, String fc_id)
    {

        String endpoint = String.format(Endpoints.COUNT_OF_BUSY_PICKERS , fc_id);





        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .header("X-Tracker", UUID.randomUUID().toString())
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();


        report.log("Status code: " + response.getStatusCode() +"\n Reponse  \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                .extract().response();
        return response;


    }

    /**
     * This method makes GET request to Picker api
     * @param fc_id fc_id
     * @return response
     */


    public Response getCountOfBusyPickerApi(String fc_id)
    {
        String endpoint = String.format(msvc.planogram.internal.Endpoints.ASSIGNED_PICKER, fc_id);



        Response response = RestAssured.given().spec(requestSpecification)
                .header("X-Tracker", UUID.randomUUID().toString())
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate Response: " + response.prettyPrint(),true);
        return response;

    }

}

