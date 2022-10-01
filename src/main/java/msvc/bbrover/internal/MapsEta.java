package msvc.bbrover.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class MapsEta extends WebSettings implements Endpoints {


    IReport report;

    private String sourcelat;
    private String sourcelong;
    private String destlat;
    private String destlong;
    private String profile;
    private  String provider;
    private String requiredAnnotations;

    private RequestSpecification requestSpecification;


    public MapsEta(  IReport report) {
        this.report = report;


        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes Post request to maps eta api.
     * Api to provide Road Distance based on Lat/lon pairs
     * source lat, lond, destination lat long  is set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response getDistanceDuration(String expectedResponseSchemaPath,String sourcelat,String sourcelong, String destlat, String destlong,  String profile,  String provider,  ArrayList requiredAnnotations) {
        String endpoint = String.format(Endpoints.BB_ROVER_ETA);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");

        JsonObject source=new JsonObject();
        JsonObject destinaiton=new JsonObject();
        source.put("lat",Double.valueOf(sourcelat)).put("lng",Double.valueOf(sourcelong));
        destinaiton.put("lat",Double.valueOf(destlat)).put("lng",Double.valueOf(destlong));
        JsonArray sourcearray=new JsonArray();
        sourcearray.add(source);
        JsonArray destarray=new JsonArray();
        destarray.add(destinaiton);
        report.log("Calling maps eta  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        JsonObject body= new JsonObject();
        body.put("sources",sourcearray).put("destinations",destarray).put("profile",profile).put("provider",provider);
        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Get maps eta response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }


    /**
     * This method makes Post request to maps eta api.
     * Api to provide Road Distance based on Lat/lon pairs
     * source lat, lond, destination lat long  is set during object creation.
     *
     * @return response
     */
    public Response getDistanceDuration(String sourcelat,String sourcelong, String destlat, String destlong,  String profile,  String provider,  ArrayList requiredAnnotations) {
        String endpoint = String.format(Endpoints.BB_ROVER_ETA);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");

        JsonObject source=new JsonObject();
        JsonObject destinaiton=new JsonObject();
        source.put("lat",Double.valueOf(sourcelat)).put("lng",Double.valueOf(sourcelong));
        destinaiton.put("lat",Double.valueOf(destlat)).put("lng",Double.valueOf(destlong));
        JsonArray sourcearray=new JsonArray();
        sourcearray.add(source);
        JsonArray destarray=new JsonArray();
        destarray.add(destinaiton);
        report.log("Calling maps eta  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        JsonObject body= new JsonObject();
        body.put("sources",sourcearray).put("destinations",destarray).put("profile",profile).put("provider",provider);
        Response response  = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n maps eta response: \n" + response.prettyPrint(),true);
        return response;

    }
}
