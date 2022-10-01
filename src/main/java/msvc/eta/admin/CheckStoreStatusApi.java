package msvc.eta.admin;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CheckStoreStatusApi extends WebSettings implements Endpoints{

    IReport report;
    private String xCaller;
    private String xEntryContextId;
    private String xEntryContext;
    private RequestSpecification requestSpecification;
    private String bbDecodedUID;
    private String fc_id;

    public CheckStoreStatusApi( String xCaller, String xEntryContextId, String xEntryContext,String bbDecodedUID, String fc_id , IReport report) {
        this.fc_id = fc_id;
        this.report = report;
        this.xCaller = xCaller;
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;
        this.bbDecodedUID = bbDecodedUID;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request to Check the Store
     * sa ID is set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response getCheckStoreStatus(String expectedResponseSchemaPath) {
        String endpoint = String.format(Endpoints.CHECK_STORE_STATUS);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("bb-decoded-uid" , bbDecodedUID);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);


        report.log("Calling Get Store Status  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .queryParam("fc_id" , fc_id)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Get Store Status response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    public Response getCheckStoreStatus() {
        String endpoint = String.format(Endpoints.CHECK_STORE_STATUS);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("bb-decoded-uid" , bbDecodedUID);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);


        report.log("Calling Get Store Status  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .queryParam("fc_id" , fc_id)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Store Status  response: " + response.prettyPrint(), true);
        return  response;
    }


    public Response getCheckStoreStatusWithOutRequiredFeild() {
        String endpoint = String.format(Endpoints.CHECK_STORE_STATUS);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("bb-decoded-uid" , bbDecodedUID);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
       // requestHeader.put("X-Entry-Context",xEntryContext);


        report.log("Calling Get Store Status  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .queryParam("fc_id" , fc_id)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Store Status  response: " + response.prettyPrint(), true);
        return  response;
    }





}
