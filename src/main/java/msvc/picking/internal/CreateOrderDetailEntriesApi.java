package msvc.picking.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import msvc.wio.internal.Endpoints;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CreateOrderDetailEntriesApi extends WebSettings implements Endpoints {

    IReport report;
    private RequestSpecification requestSpecification;

    public CreateOrderDetailEntriesApi( IReport report)
    {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * API: Create order detail entries in picking service
     * This method makes POST request to  API and validates schema
     * @param  expectedResponseSchemaPath
     * @return  response
     */

    public Response postCreateOrderDetailEntriesApi(String expectedResponseSchemaPath)
    {
        String endpoint = String.format(msvc.picking.internal.Endpoints.CREATE_ORDER_DETAIL_ENTRIES);


        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)

                .post(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();


        report.log("Status code: " + response.getStatusCode() +"\n response \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                .extract().response();
        return response;
    }


    /**
     * This method makes POST request to API
     * @return Response
     */
    public  Response postCreateOrderDetailEntriesApi()
    {
        String endpoint = String.format(msvc.picking.internal.Endpoints.CREATE_ORDER_DETAIL_ENTRIES);

        Response response = RestAssured.given().spec(requestSpecification)
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate Response: " + response.prettyPrint(),true);
        return response;


    }

}
