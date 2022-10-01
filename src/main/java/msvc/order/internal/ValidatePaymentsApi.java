package msvc.order.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
//import com.google.api.services.sheets.v4.model.Response;
import com.google.gson.JsonObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ValidatePaymentsApi extends WebSettings implements Endpoints{
    IReport report;
    private String xEntryContext;
    private String xEntryContextId;
    private String ContentType;
    //private String TimeStamp;
    private String xTracker;
    private String Service;
    private String bbDecodedMid;
    private String xCaller;
    private RequestSpecification requestSpecification;


    public ValidatePaymentsApi(  String xEntryContext ,String xEntryContextId, String ContentType,String xTracker ,String Service,String bbDecodedMid, String xCaller, IReport report ){
        this.xEntryContext = xEntryContext;
        this.xEntryContextId = xEntryContextId;
        this.ContentType = ContentType;
        //this.TimeStamp = TimeStamp;
        this.xTracker = xTracker;
        this.Service = Service;
        this.bbDecodedMid = bbDecodedMid;
        this.xCaller = xCaller;
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }


    public Response postValidatePaymentsData(String expectedResponseSchemaPath)
    {
        String endpoint = String.format(Endpoints.PAYMENT_STATUS);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Entry-Context" , xEntryContext);
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("Content-Type",ContentType);
        requestHeader.put("X-TimeStamp",  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Tracker",xTracker);
        requestHeader.put("X-Service" , Service);
        requestHeader.put("bb-decoded-mid" , bbDecodedMid);
        requestHeader.put("X-Caller",xCaller);

        JSONObject requestBody = new JSONObject();
        ArrayList<Integer> l = new ArrayList<Integer>();
        l.add(1000231173);
        requestBody.put("orders",l);
        requestBody.put("txn_id" , "f25b657fa9f8aebd1be9");
        requestBody.put("txn_type" , "checkout");
        requestBody.put("operation_type" , "validate");

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(requestBody.toString())
                .post(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n Live order tracking response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                .extract().response();
        return response;


    }


    public Response postValidatePaymentsData()
    {
        String endpoint = String.format(Endpoints.PAYMENT_STATUS);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Entry-Context" , xEntryContextId);
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("Content-Type",ContentType);
        requestHeader.put("X-TimeStamp",  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Tracker",xTracker);
        requestHeader.put("X-Service" , Service);
        requestHeader.put("bb-decoded-mid" , bbDecodedMid);
        requestHeader.put("X-Caller",xCaller);


        JSONObject requestBody = new JSONObject();
        ArrayList<Integer> l = new ArrayList<Integer>();
        requestBody.put("orders",l);
        requestBody.put("txn_id" , "f25b657fa9f8aebd1be9");
        requestBody.put("txn_type" , "checkout");
        requestBody.put("operation_type" , "validate");




        report.log("Calling validate payments api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);



        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(requestBody.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate Payments  response: " + response.prettyPrint(),true);
        return response;
    }

}
