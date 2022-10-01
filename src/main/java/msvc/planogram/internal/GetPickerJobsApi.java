package msvc.planogram.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import msvc.wio.internal.Endpoints;

import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetPickerJobsApi extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;
    private  String user ;
    private String type;
    private String status;

    public GetPickerJobsApi(String user , String type , String status , IReport report)
    {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
        this.type = type;
        this.user = user;
        this.status = status;
    }

    /**API(30): Get Picker Jobs
     * This method makes GET request and does schema validation
     * @param expectedResponseSchemaPath schema
     * @param fc_id fc_id
     * @response Response
     */

    public Response getPickerJob(String expectedResponseSchemaPath, String fc_id)
    {
        String endpoint = String.format(msvc.planogram.internal.Endpoints.GET_PICKER_JOB , fc_id);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .queryParam("user" , user)
                .queryParam("type",type)
                .queryParam("status" , status)
                .header("X-Tracker", UUID.randomUUID().toString())
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();


        report.log("Status code: " + response.getStatusCode() +"\n Picking \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                .extract().response();
        return response;

    }

    /**
     * This method makes GET request to API
     * @param fc_id fc_id
     * @return Response
     */

    public Response getPickerJob(String fc_id)
    {
        String endpoint = String.format(msvc.planogram.internal.Endpoints.GET_PICKER_JOB, fc_id);

        Response response = RestAssured.given().spec(requestSpecification)
                .queryParam("user" , user)
                .queryParam("type",type)
                .queryParam("status" , status)
                .header("X-Tracker", UUID.randomUUID().toString())
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate Response: " + response.prettyPrint(),true);
        return response;


    }



}
