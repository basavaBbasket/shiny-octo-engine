package msvc.picking.external;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import msvc.wio.internal.Endpoints;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetPickingDetailsApi extends WebSettings implements Endpoints {

    IReport report;
    private RequestSpecification requestSpecification;

    public GetPickingDetailsApi( IReport report)
    {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * API: Get Picking Details
     * This method makes GET request to  API and validates schema
     * @param expectedResponseSchemaPath
     * @param Order_id
     * @return  response
     */

    public Response getPickingDetails(String expectedResponseSchemaPath, String Order_id)
    {
        String endpoint = String.format(msvc.picking.external.Endpoints.GET_PICKING_DETAILS,Order_id);


        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)

                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();


        report.log("Status code: " + response.getStatusCode() +"\n response \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                .extract().response();
        return response;
    }

    /**
     * This method makes GET request to API
     * @param  Order_id
     * @return Response
     */
    public  Response getPickingDetails(String Order_id)
    {
        String endpoint = String.format(msvc.picking.external.Endpoints.GET_PICKING_DETAILS , Order_id);

        Response response = RestAssured.given().spec(requestSpecification)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate Response: " + response.prettyPrint(),true);
        return response;


    }



}
