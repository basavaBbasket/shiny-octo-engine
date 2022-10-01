package msvc.wio.external;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import msvc.order.internal.Endpoints;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetReturningCeeListApi extends WebSettings implements Endpoints {

    IReport report;
    private String xCaller;
    private String xTracker;
    private String UserId;
    private String bbauth;
    private RequestSpecification requestSpecification;

    public GetReturningCeeListApi(  String xTracker , String xCaller, String UserId,String bbauth, IReport report ){
        this.xTracker = xTracker;
        this.xCaller = xCaller;
        this.report = report;
        this.UserId =UserId;
        this.bbauth = bbauth;
        this.requestSpecification = getSimpleRequestSpecification(serverName, this.report);
    }

    /**API: Get Returing CEE List
     * This method makes GET request to API and validates the schema
     * @param expectedResponseSchemaPath schema
     * @param  fc_id fc_id
     * @return Response
     */

    public Response getReturnCeeList(String expectedResponseSchemaPath, String fc_id)
    {
        String endpoint = String.format(msvc.wio.external.Endpoints.GET_RETURNING_CEE_LIST , fc_id);
        Map<String, String> requestHeader = new HashMap<>();
        //requestHeader.put("cookie", cookie.toString().replace(",",";").replaceAll("\\s",""));
        requestHeader.put("cookie" , bbauth);
        requestHeader.put("X-Tracker",xTracker);
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("UserId", UserId);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(serverName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n  response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                .extract().response();
        return response;

    }

    /**
     * This method makes GET request to api
     * @param  fc_id fc_id
     * @return Response
     */

    public Response getReturnCeeList(String fc_id)
    {


        String endpoint = String.format(msvc.wio.external.Endpoints.GET_RETURNING_CEE_LIST, fc_id);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie" , bbauth);
        requestHeader.put("X-Tracker",xTracker);
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("UserId", UserId);

        report.log("Calling  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);



        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)

                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log(" response: " + response.prettyPrint(),true);
        return response;


    }


}
