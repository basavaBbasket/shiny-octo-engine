package msvc.eta.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class FindSasApi extends WebSettings implements Endpoints{

    IReport report;
    private String xEntryContext;
    private String xEntryContextId;
    private String xTracker;
    private String xService;
    private String lng;
    private String lat;

    private RequestSpecification requestSpecification;

    public FindSasApi( String xEntry_context,String xEntry_context_id,String xService, String lng, String lat , IReport report) {
        this.report = report;
        this.xEntryContext=xEntry_context;
        this.xEntryContextId=xEntry_context_id;
        this.xService=xService;
        this.lat = lat;
        this.lng = lng;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }



    public Response findSAS(String expectedResponseSchemaPath) {
        String endpoint = String.format(Endpoints.FIND_SAS);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-TimeStamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("X-Service",xService);



        JSONObject jsonBody  = new JSONObject();
        jsonBody.put("lat",lat);
        jsonBody.put("lng",lng);


        report.log("Calling find-sas api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(jsonBody.toString())
                .post(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }



    public Response findSAS() {
        String endpoint = String.format(Endpoints.FIND_SAS);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-TimeStamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("X-Service",xService);



        JSONObject jsonBody  = new JSONObject();
        jsonBody.put("lat",lat);
        jsonBody.put("lng",lng);


        report.log("Calling find-sas api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(jsonBody.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("find sas response: " + response.prettyPrint(),true);
        return response;

    }

    public Response findSASWithIncompleteHeaders() {
        String endpoint = String.format(Endpoints.FIND_SAS);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-TimeStamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        //requestHeader.put("X-Service",xService);



        JSONObject jsonBody  = new JSONObject();
        jsonBody.put("lat",lat);
        jsonBody.put("lng",lng);


        report.log("Calling find-sas api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(jsonBody.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("find sas response: " + response.prettyPrint(),true);
        return response;

    }





}
