package msvc.wio.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import msvc.wio.internal.Endpoints;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class FcReceivingMarkStatusCompleteApi extends WebSettings implements Endpoints {

    IReport report;
    private String xEntryContext;
    private String xService;
    private String xEcID;
    private String contentType;
    private String xCaller;
    private String xUserID;
    private String UserName;

    private RequestSpecification requestSpecification;

    public FcReceivingMarkStatusCompleteApi(String xEcID,String xEntryContext,String xService,String contentType,String xCaller,String xUserID,String UserName,IReport report ){
        this.xEcID = xEcID;
        this.xEntryContext  =xEntryContext;
        this.xService = xService;
        this.contentType = contentType;
        this.xCaller = xCaller;
        this.xUserID = xUserID;
        this.UserName = UserName;
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }


    /**
     * API: FC Receiving Mark Status Complete --> Marks Complete
     * This method makes POST request to FC Receiving Mark Status Complete API and validates Schema
     * @param expectedResponseSchemaPath
     * @return  response
     */

   public Response postFcReceivingMarkStatusComplete(String expectedResponseSchemaPath)
   {
       String endpoint = String.format(Endpoints.FC_RECEIVING_MARK_STATUS_COMPLETE);

       // Setting Header
       Map<String, String> requestHeader = new HashMap<>();
       requestHeader.put("X-Tracker", UUID.randomUUID().toString());
       requestHeader.put("X-Caller", xCaller);
       requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));
       requestHeader.put("X-Entry-Context-Id", xEcID);
       requestHeader.put("X-Entry-Context", xEntryContext);
       requestHeader.put("X-Service",xService);
       requestHeader.put("Content-Type",contentType);
       requestHeader.put("UserId",xUserID);
       requestHeader.put("UserName" , UserName);


       // Setting Body
       JSONObject jsonUserId1 = new JSONObject();
       jsonUserId1.put("id" , 2);

       JSONObject jsonUserId2 = new JSONObject();
       jsonUserId2.put("id" , 3);

       JSONArray requestBody = new JSONArray();
       requestBody.put(jsonUserId1);
       requestBody.put(jsonUserId2);



       // making POST request and validating schema
       ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
               .headers(requestHeader)
               .body(requestBody.toString())
               .post(msvcServerName + endpoint)
               .then();
       Response response = validatableResponse.extract().response();

       report.log("Status code: " + response.getStatusCode() +"\n Fc Mark Status Complete: \n" + response.prettyPrint(),true);
       report.log("Verifying response schema.",true);
       validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
               .extract().response();
       return response;


   }


    /**
     * API: FC Receiving Mark Status Complete --> Marks Complete
     * This method makes POST request to FC Receiving Mark Status Complete
     * @return response
     */



    public Response postFcReceivingMarkStatusComplete()
   {


       String endpoint = String.format(Endpoints.FC_RECEIVING_MARK_STATUS_COMPLETE);

       // Setting Header
       Map<String, String> requestHeader = new HashMap<>();

       requestHeader.put("Content-Type",contentType);

       // Setting Body
       JSONObject jsonUserId1 = new JSONObject();
       jsonUserId1.put("id" , "2");

       JSONObject jsonUserId2 = new JSONObject();
       jsonUserId1.put("id" , "3");

       JSONArray requestBody = new JSONArray();
       requestBody.put(jsonUserId1);
       requestBody.put(jsonUserId2);


       report.log("Calling FC Mark Status API " +
               "\n Endpoint:" + endpoint +
               "\n Headers: " + requestHeader.toString(), true);



       Response response = RestAssured.given().spec(requestSpecification)
               .headers(requestHeader)
               .body(requestBody.toString())
               .post(msvcServerName + endpoint)
               .then().log().all()
               .extract().response();

       report.log("Validate FC Status Response: " + response.prettyPrint(),true);
       return response;

   }



}
