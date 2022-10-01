package msvc.order.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class OrderListApi extends WebSettings implements Endpoints {
    IReport report;
    private String xCaller;
    private String xEntryContextId;
    private String xEntryContext;
    private String xservice;
    private int bb_decoded_mid;
    private RequestSpecification requestSpecification;

    public OrderListApi( String xservice,String xCaller, String xEntryContextId, String xEntryContext,int bb_decoded_mid,  IReport report) {
        this.report = report;
        this.xservice=xservice;
        this.xCaller = xCaller;
        this.bb_decoded_mid=bb_decoded_mid;
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request to order list api.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response getorderList(String expectedResponseSchemaPath, ArrayList<Integer> eclist) {
        String endpoint = String.format(Endpoints.ORDER_LIST);
        Map<String, Object> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Service",xservice);
        requestHeader.put("bb-decoded-mid",bb_decoded_mid);
        requestHeader.put("X-Timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("Content-Type","application/json");

        report.log("Calling getorderList api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        JSONObject body=new JSONObject();
        body.put("member_id",bb_decoded_mid).put("page",1).put("context","active").put("group_by","po").put("modifications",true).put("old_system_orders",false).put("ec_ids",eclist);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body( body.toString())
                .post(msvcServerName + endpoint)
                .then();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n  getorderList response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    /**
     * this method makes GET request to order list api.
     * @return
     */
    public Response getorderList(ArrayList eclist) {
        String endpoint = String.format(Endpoints.ORDER_LIST);
        Map<String, Object> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Service",xservice);
        requestHeader.put("bb-decoded-mid",bb_decoded_mid);
        requestHeader.put("X-Timestamp", String.valueOf(Instant.now().toEpochMilli()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("Content-Type","application/json");

        report.log("Calling getorderList api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);
        JSONObject body=new JSONObject();
        body.put("member_id",bb_decoded_mid).put("page",1).put("context","active").put("group_by","po").put("modifications",true).put("old_system_orders",false).put("ec_ids",eclist);

        Response response  = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then()
                .extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n  getorderList response: \n" + response.prettyPrint(),true);
        return response;
    }

}
