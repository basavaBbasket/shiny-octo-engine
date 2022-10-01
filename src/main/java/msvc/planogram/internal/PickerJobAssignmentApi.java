package msvc.planogram.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import msvc.wio.internal.Endpoints;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class PickerJobAssignmentApi extends WebSettings implements Endpoints {

    IReport report;
    private RequestSpecification requestSpecification;

    public PickerJobAssignmentApi(IReport report)
    {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**API(28): Picker JOB Assignment
     * This method makes POST request and does schema validation
     * @param expectedResponseSchemaPath
     * @response Response
     */

    public Response getPickerJobAssignment(String expectedResponseSchemaPath)
    {
        String endpoint = String.format(msvc.planogram.internal.Endpoints.PICKER_JOB_ASSIGNMENT);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .post(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();


        report.log("Status code: " + response.getStatusCode() +"\n Picker job assignment \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                .extract().response();
        return response;

    }


    /**
     * This method makes POST request to API
     * @return Response
     */

    public Response getPickerJobAssignment()
    {
        String endpoint = String.format(msvc.planogram.internal.Endpoints.PICKER_JOB_ASSIGNMENT);

        Response response = RestAssured.given().spec(requestSpecification)
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate Response: " + response.prettyPrint(),true);
        return response;


    }


}
