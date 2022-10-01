package msvc.listing_assembler;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ListingAssemblerApi extends WebSettings implements Endpoints {

    IReport report;
    private RequestSpecification requestSpecification;

    public ListingAssemblerApi(IReport report)
    {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);

    }

    /**
     * API: Listing Assembler--> Lists MS, SFL, DYF responses
     * This method makes GET request to  Listing Assembler API and validates the schema of response
     *  @param expectedResponseSchemaPath
     * @return   response
     */

     public Response getListingAssembler(String expectedResponseSchemaPath)
     {
         String endpoint = String.format(Endpoints.LISTING_ASSEMBLER);

         ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                 .get(msvcServerName + endpoint)
                 .then().log().all();
         Response response = validatableResponse.extract().response();


         report.log("Status code: " + response.getStatusCode() +"\n Listing Assembler \n" + response.prettyPrint(),true);
         report.log("Verifying response schema.",true);
         validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                 .extract().response();
         return response;


     }

    /**
     * This method makes make GET request to LISTING ASSEMBLER API
     * @return response
     */


    public Response getListingAssembler()
    {
        String endpoint = String.format(Endpoints.LISTING_ASSEMBLER);

        Response response = RestAssured.given().spec(requestSpecification)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate Response: " + response.prettyPrint(),true);

        return response;


    }




}
