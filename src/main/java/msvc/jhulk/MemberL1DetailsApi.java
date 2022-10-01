package msvc.jhulk;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import msvc.wio.internal.Endpoints;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class MemberL1DetailsApi extends WebSettings implements Endpoints {

    IReport report;
    private RequestSpecification requestSpecification;
    private String EntryContext;
    private String EntryContextId;
    private String xCaller;

    public MemberL1DetailsApi(String EntryContext, String EntryContextId, String xCaller, IReport report)
    {
        this.EntryContext  = EntryContext;
        this.EntryContextId = EntryContextId;
        this.xCaller = xCaller;
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request to  API and validates schema
     * @param  expectedResponseSchemaPath
     * @param memberId
     * @return  response
     */
    public Response getMemberL1Details(String expectedResponseSchemaPath , int memberId)
    {
        String endpoint = String.format(msvc.jhulk.Endpoints.MEMBER_L1_DETAILS, memberId);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Entry-Context-Id" , EntryContextId);
        requestHeader.put("X-Entry-Context",EntryContext);

        report.log("Calling get member l1 details api. Endpoint: "+endpoint,true);
        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
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
     * @param memberId
     * @return Response
     */
    public  Response getMemberL1Details(String memberId)
    {
        String endpoint = String.format(msvc.jhulk.Endpoints.MEMBER_L1_DETAILS, memberId);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Entry-Context-Id" , EntryContextId);
        requestHeader.put("X-Entry-Context",EntryContext);

        report.log("Calling get member l1 details api. Endpoint: "+endpoint,true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Get member l1 api response: <br>" + response.prettyPrint(),true);
        return response;
    }



}
