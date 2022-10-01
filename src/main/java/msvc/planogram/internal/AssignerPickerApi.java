package msvc.planogram.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import msvc.wio.internal.Endpoints;

import java.util.HashMap;
import java.util.Map;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AssignerPickerApi extends WebSettings implements Endpoints {
    IReport report;
    private String xTracker;
    private RequestSpecification requestSpecification;

    public AssignerPickerApi(String xTracker , IReport report)
    {
        this.xTracker = xTracker;
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * API: Alert: Assigned Picker api
     * This method makes GET request to  Assigned Picker api and validates the schema of response
     *  @param expectedResponseSchemaPath
     * @return   response
     */

    public Response getAssignedPickerApi(String expectedResponseSchemaPath)
    {

        String endpoint = String.format(msvc.planogram.internal.Endpoints.ASSIGNED_PICKER);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker",xTracker);


        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();


        report.log("Status code: " + response.getStatusCode() +"\n Assigned Picker \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                .extract().response();
        return response;


    }

    /**
     * This method makes GET request to Picker api
     * @return response
     */


    public Response getAssignedPickerApi()
    {
        String endpoint = String.format(msvc.planogram.internal.Endpoints.ASSIGNED_PICKER);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker",xTracker);


        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate Response: " + response.prettyPrint(),true);
        return response;

    }

}
