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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class FcReceivingMarkStatusCompleteApi extends WebSettings implements Endpoints {

    IReport report;
    private String xEntryContext;
    private String xEntryContextId;
    private String xTracker;
    private String Service;
    private String ContentType;
    private String xCaller;
    private String UserId;
    private String UserName;
    private RequestSpecification requestSpecification;

    public FcReceivingMarkStatusCompleteApi(String xEntryContext , String xEntryContextId, String xTracker , String Service, String ContentType, String xCaller, String UserId, String UserName, IReport report ){
        this.xEntryContext = xEntryContext;
        this.xEntryContextId = xEntryContextId;
        this.xTracker = xTracker;
        this.Service = Service;
        this.ContentType = ContentType;
        this.xCaller = xCaller;
        this.UserId = UserId;
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

       requestHeader.put("X-TimeStamp",  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
               .format(new DateTime(new Date()).toDate()));
       requestHeader.put("X-Entry-Context" , xEntryContextId);
       requestHeader.put("X-Entry-Context-Id",xEntryContextId);
       requestHeader.put("X-Tracker",xTracker);
       requestHeader.put("X-Service" , Service);
       requestHeader.put("Content-Type",ContentType);
       requestHeader.put("X-Caller",xCaller);
       requestHeader.put("UserId" , UserId);
       requestHeader.put("UserName" , UserName);

       // Setting Body
       JSONObject jsonUserId = new JSONObject();
       jsonUserId.put("id" , 2);

       JSONArray requestBody = new JSONArray();
       requestBody.put(jsonUserId);



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

       requestHeader.put("X-TimeStamp",  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
               .format(new DateTime(new Date()).toDate()));
       requestHeader.put("X-Entry-Context" , xEntryContextId);
       requestHeader.put("X-Entry-Context-Id",xEntryContextId);
       requestHeader.put("X-Tracker",xTracker);
       requestHeader.put("X-Service" , Service);
       requestHeader.put("Content-Type",ContentType);
       requestHeader.put("X-Caller",xCaller);
       requestHeader.put("UserId" , UserId);
       requestHeader.put("UserName" , UserName);

       // Setting Body
       JSONObject jsonUserId = new JSONObject();
       jsonUserId.put("id" , 2);

       JSONArray requestBody = new JSONArray();
       requestBody.put(jsonUserId);

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
