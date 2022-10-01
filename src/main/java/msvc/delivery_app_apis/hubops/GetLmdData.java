package msvc.delivery_app_apis.hubops;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetLmdData extends WebSettings implements Endpoints {

    IReport report;
    private String cookie;
    private String order_id;
    private String xEntryContext;
    private String xEntryContextId;
    private String xCaller;

    private RequestSpecification requestSpecification;


    public GetLmdData( String order_id, String cookie,String xEntryContext,String xEntryContextId,String xCaller, IReport report) {
        this.report = report;
        this.cookie = cookie;
        this.order_id=order_id;
        this.xEntryContext=xEntryContext;
        this.xEntryContextId=xEntryContextId;
        this.xCaller=xCaller;

        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * this function makes a get call to LMD data api and validates the response
     * order_id is set during object creation
     * @return
     */
    public Response getLmdData(String expectedResponseSchemaPath) {
        String endpoint = String.format(Endpoints.LMD_DATA,order_id);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-TimeStamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("Content-Type","application/json");


        report.log("Calling getLmdData  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .formParam("include","eta_at_rts,total_distance,cee_location")
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n getLmdData response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;

    }

    /**
     * this function makes a get call to LMD data api
     * @return
     */
    public Response getLmdData() {
        String endpoint = String.format(Endpoints.LMD_DATA,order_id);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-TimeStamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("Content-Type","application/json");


        report.log("Calling getLmdData  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response  = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .formParam("include","eta_at_rts,total_distance,cee_location")
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n getLmdData response: \n" + response.prettyPrint(),true);
        return response;

    }

}
