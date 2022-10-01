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

public class CancelledOrderBinListPiApi extends WebSettings implements Endpoints {

    IReport report;
    private RequestSpecification requestSpecification;

    public CancelledOrderBinListPiApi(IReport report)
    {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**API(19): Cancelled Order Bin List PI
     * This method makes GET request and does schema validation
     * @param expectedResponseSchemaPath
     * @response Response
     */


    public Response getCancelledOrderBinList(String expectedResponseSchemaPath)
    {
        String endpoint = String.format(msvc.planogram.internal.Endpoints.CANCELLED_ORDER_BIN_LIST);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
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
     * This method makes GET request to API
     * @return Response
     */

    public Response getCancelledOrderBinList()
    {
        String endpoint = String.format(msvc.planogram.internal.Endpoints.CANCELLED_ORDER_BIN_LIST);

        Response response = RestAssured.given().spec(requestSpecification)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate Response: " + response.prettyPrint(),true);
        return response;

    }


}
