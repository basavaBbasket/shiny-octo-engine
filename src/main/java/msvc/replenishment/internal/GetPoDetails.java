package msvc.replenishment.internal;

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

public class GetPoDetails extends WebSettings implements Endpoints {

    IReport report;

    private String po_id;
    private String xEntryContext;
    private String xEntryContextId;
    private String xCaller;

    private RequestSpecification requestSpecification;


    public GetPoDetails( String po_id, String xEntryContext,String xEntryContextId,String xCaller, IReport report) {
        this.report = report;
        this.po_id=po_id;
        this.xEntryContext=xEntryContext;
        this.xEntryContextId=xEntryContextId;
        this.xCaller=xCaller;

        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * this function makes a get call to the get po details api
     *  po_id is set during object creation
     *  validates the response
     * @return
     */
    public Response getPoDetails(String expectedResponseSchemaPath) {
        String endpoint = String.format(Endpoints.PO_DETAILS,po_id);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("accept","application/json");


        report.log("Calling getPoDetails  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n getPoDetails response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;

    }


    /**
     * this function makes a get call to the get po details api
     * po_id is set during object creation
     * @return
     */
    public Response getPoDetails() {

        String endpoint = String.format(Endpoints.PO_DETAILS,po_id);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("accept","application/json");


        report.log("Calling getPoDetails  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Get getPoDetails response: " + response.prettyPrint(),true);

        return response;

    }

    /**
     * this function makes a get call to the get po details api
     *  po_id is set during object creation
     *  validates the response
     * @return
     */
    public Response getPoDetails(String expectedResponseSchemaPath,boolean is_sku_info_required) {
        String endpoint = String.format(Endpoints.PO_DETAILS,po_id);
        Map<String, String> requestHeader = new HashMap<>();
         requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());




        report.log("Calling getPoDetails  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .formParam("is_sku_info_required",is_sku_info_required)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n getPoDetails response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;

    }



    /**
     * this function makes a get call to the get po details api
     *  po_id is set during object creation
     * @return
     */
    public Response getPoDetails(boolean is_sku_info_required) {
        String endpointparam=String.format("%s?is_sku_info_required=%s",po_id,is_sku_info_required);
        String endpoint = String.format(Endpoints.PO_DETAILS,endpointparam);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("Content-Type","application/json");


        report.log("Calling getPoDetails  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Get getPoDetails response: " + response.prettyPrint(),true);

        return response;

    }
}
