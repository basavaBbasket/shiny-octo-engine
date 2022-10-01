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

public class GetLMDStoreMetrics extends WebSettings implements Endpoints {

    IReport report;

    private String sa_id;
    private String dm_id;
    private String xEntryContext;
    private String xEntryContextId;
    private String xCaller;
    private RequestSpecification requestSpecification;
    private String otor;


    public GetLMDStoreMetrics( String sa_id, String dm_id,String xEntryContext,String xEntryContextId,String xCaller, IReport report) {
        this.report = report;

        this.sa_id=sa_id;
        this.dm_id=dm_id;
        this.xEntryContext=xEntryContext;
        this.xEntryContextId=xEntryContextId;
        this.xCaller=xCaller;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request to Get LMD Store Metrics api.
     * sa ID and DM id is set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response getLMDStoreMetrics(String expectedResponseSchemaPath,String otor) {
        String endpoint = String.format(Endpoints.LMD_STORE_METRICS_OTOR,sa_id,dm_id,otor);
        Map<String, String> requestHeader = new HashMap<>();

        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-TimeStamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("Content-Type","application/json");

        report.log("Calling Get LMD Store Metrics  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Get LMD Store Metrics response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    /**
     * This method makes GET request to Get LMD Store Metrics api.
     * sa ID and DM id is set during object creation.
     * @return
     */
    public Response getLMDStoreMetrics(String otor) {
        String endpoint = String.format(Endpoints.LMD_STORE_METRICS_OTOR,sa_id,dm_id,otor);
        Map<String, String> requestHeader = new HashMap<>();

        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-TimeStamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("Content-Type","application/json");

        report.log("Calling Get LMD Store Metrics  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response= RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Get LMD Store Metrics response: " + response.prettyPrint(),true);
        return response;
    }


    /**
     * This makes GET request Lmd store Metrics Otor is set during object creation.
     * validates the response
     * @param expectedResponseSchemaPath
     * @param otor
     * @return
     */
    public Response getLMDStoreMetricsOtor(String expectedResponseSchemaPath,String otor) {
        this.otor=otor;
        String endpoint = String.format(Endpoints.LMD_STORE_METRICS_OTOR,sa_id,dm_id,otor);
        Map<String, String> requestHeader = new HashMap<>();

        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-TimeStamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("Content-Type","application/json");

        report.log("Calling Get LMD Store Metrics using otor api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Get LMD Store Metrics response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    /**
     * This makes GET request Lmd store Metrics Otor is set during object creation.
     * @param otor
     * @return
     */
    public Response getLMDStoreMetricsOtor(String otor) {
        this.otor=otor;
        String endpoint = String.format(Endpoints.LMD_STORE_METRICS_OTOR,sa_id,dm_id,otor);
        Map<String, String> requestHeader = new HashMap<>();

        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-TimeStamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("Content-Type","application/json");

        report.log("Calling Get LMD Store Metrics  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response= RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Get LMD Store Metrics response: " + response.prettyPrint(),true);
        return response;
    }


}
