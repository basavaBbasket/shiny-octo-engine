package msvc.eta.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.joda.time.DateTime;

import io.restassured.response.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ThresoldMessage extends WebSettings implements Endpoints{

    IReport report;
    private String xCaller;
    private String xEntryContextId;
    private String xEntryContext;
    private String sa_id;
    private String threshold;
    private String order_timeStamp;
    private RequestSpecification requestSpecification;

    public ThresoldMessage( String xcaller,String xEntryContext,String xEntryContextId,String sa_id,String threshold,String order_timeStamp, IReport report) {
        this.report = report;
        this.xCaller = xcaller;
        this.xEntryContextId=xEntryContextId;
        this.xEntryContext=xEntryContext;
        this.sa_id = sa_id;
        this.order_timeStamp = order_timeStamp;
        this.threshold = threshold;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    public Response GetMessage(String expectedResponseSchemaPath)
    {
        String endpoint = String.format(Endpoints.THRESHOLD);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);


        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .queryParam("sa_id",sa_id)
                .queryParam("threshold_name",threshold)
                .queryParam("order_open_status_time",order_timeStamp)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();


        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;

    }


    public Response GetMessage()
    {
        String endpoint = String.format(Endpoints.THRESHOLD);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);

        report.log(
                "Endpoint: " + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)

                .queryParam("sa_id",sa_id)
                .queryParam("threshold_name",threshold)
                .queryParam("order_open_status_time",order_timeStamp)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Response:  " + response.prettyPrint(),true);
        return response;



    }

    public Response GetMessageWithIncompleteHeaders()
    {
        String endpoint = String.format(Endpoints.THRESHOLD);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        //requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);

        report.log(
                "Endpoint: " + endpoint +
                        "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)

                .queryParam("sa_id",sa_id)
                .queryParam("threshold_name",threshold)
                .queryParam("order_open_status_time",order_timeStamp)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Response:  " + response.prettyPrint(),true);
        return response;



    }


}
