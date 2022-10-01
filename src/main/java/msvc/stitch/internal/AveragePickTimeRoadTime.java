package msvc.stitch.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AveragePickTimeRoadTime extends WebSettings implements Endpoints {


    IReport report;
    private RequestSpecification requestSpecification;



    public AveragePickTimeRoadTime(  IReport report) {
        this.report = report;

        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request to Get AveragePickTimeRoadTime api.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response getAveragePickTimeRoadTime(String expectedResponseSchemaPath) {
        String endpoint = String.format(Endpoints.AVG_PICK_ROAD_TIME);



        report.log("Calling Get Average Pick Time Road Time  api. " +
                "\n Endpoint:" + endpoint , true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n  Get Average Pick Time Road Time response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    public Response getAveragePickTimeRoadTime() {
        String endpoint = String.format(Endpoints.AVG_PICK_ROAD_TIME);


        report.log("Calling Get Average Pick Time Road Time  api. " +
                "\n Endpoint:" + endpoint, true);

        Response response= RestAssured.given().spec(requestSpecification)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n  Get Average Pick Time Road Time response: \n" + response.prettyPrint(),true);
        return response;
    }
}
