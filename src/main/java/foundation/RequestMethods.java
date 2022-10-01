package foundation;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 *  Its a general class to have the request methods that would be used as part of bb2.0 foundation.
 *  Overload predefined methods for various types of request variants.
 */
public class RequestMethods extends WebSettings {

    Logger logger = Logger.getLogger(RequestMethods.class);
    private RequestSpecBuilder requestSpecBuilder;
    private RequestSpecification requestSpecification;
    private AutomationReport report;

    /**
     * Its a constructor for setting basic headers like x-timestamp,x-tracker,x-caller etc.
     * These headers are common across multiple requests of bb2.0 foundation
     * @param report to setup the custom build reporter in the class
     * @param baseUri to setup the request base url for restassured
     * @param apiEndPoint the endpoint to which api has to be called.
     */
    public RequestMethods(AutomationReport report, String baseUri, String apiEndPoint) {
        System.out.println("***********Request Specification*************");
        this.report = report;
        requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setBaseUri(baseUri);
        requestSpecBuilder.setBasePath(apiEndPoint);
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String todayDateTime = format2.format(new DateTime(new Date()).toDate());
        UUID uuid= UUID.randomUUID();
        requestSpecBuilder.addHeader("X-Timestamp", todayDateTime);
        requestSpecBuilder.addHeader("X-Tracker", uuid.toString());
        requestSpecBuilder.addHeader("X-Caller", "AUTOMATION");
        requestSpecification = requestSpecBuilder.build();
        System.out.println("*********requestSpecBuilder*********\n"+requestSpecBuilder.toString());
    }

    /**
     * The method is to make post request using restassured with raw string as body and header map
     * @param headerMap A map of the prameters to be passed in the header of the request
     * @param body A string containing the json formatted body for the request
     * @return The response we get from the restassured with restassured return object
     */
    public Response postRequestAPIResponse(Map<String, String> headerMap, String body) {
        report.info("Making Post request to API with Body:: <pre>"+ prettyPrintJson(body));
        Response response = RestAssured.given()
                .spec(requestSpecification)
                .log()
                .all()
                .contentType(ContentType.JSON)
                .headers(headerMap)
                .body(body)
                .when()
                .post()
                .then()
                .extract().response();
        logger.info("Status Code : "+response.statusLine());
        report.info("response: <pre>" + response.prettyPrint());
        return response;
    }

    /**
     * The method is to make get request with just header parameters using restassured
     * @param headerMap A map of the parameters to be passed as header
     * @return The response recived from the restassured after making the API call
     */
    public Response getRequestAPIResponse(Map<String, String> headerMap) {
        report.info("Making get request to API ");
        Response response = RestAssured.given()
                .spec(requestSpecification)
                .log()
                .all()
                .contentType(ContentType.JSON)
                .headers(headerMap)
                .when()
                .get()
                .then()
                .extract().response();
        logger.info("Status Code : "+response.statusLine());
        report.info("response: <pre>" + response.prettyPrint());
        return response;
    }

    /**
     * The method is to make get request with header parameters and query Params using restassured
     * @param headerMap A map of the parameters to be passed as header
     * @return The response recived from the restassured after making the API call
     */
    public Response getRequestAPIResponse(Map<String, String> headerMap,Map<String,String> qParam) {
        report.info("Making get request to API ");
        Response response = RestAssured.given()
                .spec(requestSpecification)
                .log()
                .all()
                .contentType(ContentType.JSON).formParams(qParam)
                .headers(headerMap)
                .when()
                .get()
                .then()
                .extract().response();
        logger.info("Status Code : "+response.statusLine());
        report.info("response: <pre>" + response.prettyPrint());
        return response;
    }

    public Response getRequestAPIResponse(Map<String, String> headerMap, Map<String,String> qParam, Cookies cookies) {
        report.info("Making get request to API ");
        Response response = RestAssured.given()
                .spec(requestSpecification)
                .log()
                .all()
                .contentType(ContentType.JSON).formParams(qParam)
                .headers(headerMap).cookies(cookies)
                .when()
                .get()
                .then()
                .extract().response();
        logger.info("Status Code : "+response.statusLine());
        report.info("response: <pre>" + response.prettyPrint());
        return response;
    }

    private String prettyPrintJsonObject(String jsonObject){
        return new JsonObject(jsonObject).encodePrettily();
    }

    private String prettyPrintJson(String json){
        try {
            return new JsonObject(json).encodePrettily();
        } catch (DecodeException e){
            return new JsonArray(json).encodePrettily();
        }
    }

    private String prettyPrintJsonArray(String jsonArray){
        return new JsonArray(jsonArray).encodePrettily();
    }
}
