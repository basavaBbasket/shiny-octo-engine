package msvc.delivery_app_apis.hubops;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CeeAttributesForOrderIds extends WebSettings implements Endpoints {

    IReport report;
    private String cookie;
    private String xChannel;
    private String xCaller;
    private String xEntryContextId;
    private String xEntryContext;
    private RequestSpecification requestSpecification;
    private ArrayList Orderids;

    public CeeAttributesForOrderIds( String xCaller, String xEntryContextId, String xEntryContext,String cookie,ArrayList Orderids, IReport report) {
        this.report = report;
        this.cookie = cookie;
        this.xCaller = xCaller;
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;
        this.Orderids=Orderids;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes Post request to getCeeAttributesForOrderIds api.
     * Order IDs is set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response getCeeAttributesForOrderIds(String expectedResponseSchemaPath) {
        String endpoint = String.format(Endpoints.CEE_ATTRIBUTES_FOR_ORDER_IDS);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-TimeStamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("Content-Type","application/json");


        report.log("Calling Cee Attributes For Order Ids  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        JsonObject body= new JsonObject();
        body.put("order_ids",Orderids);
        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Get Cee Attributes For OrderIds response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    /**
     * This method makes Post request to getCeeAttributesForOrderIds api.
     *       Order IDs is set during object creation.
     * @return
     */
    public Response getCeeAttributesForOrderIds() {
        String endpoint = String.format(Endpoints.CEE_ATTRIBUTES_FOR_ORDER_IDS);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-TimeStamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("Content-Type","application/json");


        report.log("Calling Cee Attributes For Order Ids  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        JsonObject body= new JsonObject();
        body.put("order_ids",Orderids);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Cee Attributes For OrderIds response: " + response.prettyPrint(),true);
        return response;
    }

}
