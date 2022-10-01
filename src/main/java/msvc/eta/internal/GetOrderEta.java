package msvc.eta.internal;

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

public class GetOrderEta extends WebSettings implements Endpoints {



    IReport report;
    private String xCaller;
    private String xEntryContextId;
    private String xEntryContext;
    private  String bb_decoded_mid;
    private String order_id;
    private RequestSpecification requestSpecification;

    public GetOrderEta( String order_id,String xcaller,String xEntryContext,String xEntryContextId,String bb_decoded_mid, IReport report) {
        this.report = report;
        this.xCaller = xcaller;
        this.order_id=order_id;
        this.xEntryContextId=xEntryContextId;
        this.xEntryContext=xEntryContext;
        this.bb_decoded_mid=bb_decoded_mid;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request to Get order Eta api.
     * Order ID is set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response getOrderEta(String expectedResponseSchemaPath) {
        String endpoint = String.format(Endpoints.GET_ORDER_ETA);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("bb-decoded-mid",bb_decoded_mid);


        report.log("Calling Get Order ETA api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .queryParam("order_id" , order_id)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Get Order Eta response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    /**
     * This method makes GET request to order eta api.
     * Order ID is set during object creation.
     *
     * @return response
     */
    public Response getOrderEta() {
        String endpoint = String.format(Endpoints.GET_ORDER_ETA);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("bb-decoded-mid",bb_decoded_mid);

        report.log("Calling Get Order ETA api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .queryParam("order_id",order_id)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("order eta api response: " + response.prettyPrint(),true);
        return response;
    }


    public Response getOrderEtaWithInvalidHeaders() {
        String endpoint = String.format(Endpoints.GET_ORDER_ETA);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
      //  requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("bb-decoded-mid",bb_decoded_mid);

        report.log("Calling Get Order ETA api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .queryParam("order_id",order_id)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("order eta api response: " + response.prettyPrint(),true);
        return response;
    }



}
