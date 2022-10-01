package utility.dapi;

import admin.pages.HomePage;
import admin.pages.Login;
import api.warehousecomposition.planogram_FC.internal.Cookie;
import api.warehousecomposition.planogram_FC.internal.Endpoints;
import api.warehousecomposition.planogram_FC.internal.IQApp;
import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.MapiEndpoints;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.RestAssuredConfig.config;

public class DeliveryApp extends WebSettings implements Endpoints {

    public static Logger logger = Logger.getLogger(DeliveryApp.class);
    public Cookie cookie;
    private Map<String, String> commonHeader;
    private IReport report;
    private RequestSpecification monolithSpec;
    private RequestSpecification msvcSpec;

    public DeliveryApp(AutomationReport report) {
        RestAssured.config = config().logConfig(LogConfig.logConfig().enablePrettyPrinting(true));

        this.monolithSpec = new RequestSpecBuilder()
                .setBaseUri(serverName)
                .setConfig(config().logConfig(LogConfig.logConfig().defaultStream(report.getRestAssuredLogger()))
                        .encoderConfig(encoderConfig()
                                .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .build().log().all();
        this.msvcSpec = new RequestSpecBuilder()
                .setBaseUri(msvcServerName)
                .setConfig(config().encoderConfig(encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .build().log().all();
        this.report = report;
        this.cookie = new Cookie(new HashMap<>());
        this.commonHeader = new HashMap<>();
        commonHeader.put("Content-Type","application/json");
    }

    public DeliveryApp checkCeeeId(String ceeId){

        String endpoint = String.format(utility.dapi.Endpoints.CHECK_CEE_ID, ceeId);
        report.log("Calling check bb2 cee id api. "+
                "Endpoint: "+ endpoint, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .cookies(this.cookie.allCookies)
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();

        this.cookie = new Cookie(response.cookies());
        validateApiResponse(response);
        return this;
    }

    public DeliveryApp login(int cee_id , String password, String qr_code ) throws IOException, InterruptedException {

        String endpoint = String.format(utility.dapi.Endpoints.VERIFY_LOGIN);
        report.log("Calling verify-login api. "+
                "Endpoint: "+ endpoint, true);

        Properties prop=new Properties();
        prop.load(new FileInputStream("src//main//resources//dapi//deviceConfig.properties"));

        JsonObject body= new JsonObject();
        body.put("cee_id",cee_id)
                .put("password",password)
                .put("device_id",prop.getProperty("device_id"))
                .put("app_version",prop.getProperty("app_version"))
                .put("registration_id",prop.getProperty("registration_id"));

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .body(body.toString())
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        validateApiResponse(response);

        this.cookie = new Cookie(response.cookies());
        commonHeader.put("Authorization","Bearer "+response.path("access_token"));
        commonHeader.put("X-Refresh-Token",response.path("refresh_token"));
        commonHeader.put("deviceId",prop.getProperty("device_id"));

        updateGcm(prop.getProperty("registration_id"));
        Response routeAssignedResponse = checkRouteAssigned("has_route_assigned%2Cstores");
        int sa_id = routeAssignedResponse.path("sa_id");
        int dm_id = routeAssignedResponse.path("dm_id");

        checkCeeCurrent(dm_id,sa_id);
        checkAuth(prop.getProperty("app_version"),cee_id);
        postGetConfig();
        ceeCheckIn(prop.getProperty("latitude"),prop.getProperty("longitude"));
        checkRouteAssigned("has_route_assigned%2Cpool_status");
        ceeLocationData(prop.getProperty("latitude"),prop.getProperty("longitude"));
        Response route_assignment = routeAssignment(qr_code); //todo
        int routeId = route_assignment.path("route_id");
        collectOrder(routeId); // collecting order
        getOrder();
        ceeTripStatus(routeId,"start") ;// starting trip journey
        Thread.sleep(5000);

        ceeTripStatus(routeId,"end");



        return this;
    }

    public void updateGcm(String registration_id) throws IOException {

        String endpoint = String.format(utility.dapi.Endpoints.UPDATE_GCM);
        report.log("Calling update-gcm api. "+
                "Endpoint: "+ endpoint, true);

        JsonObject b= new JsonObject();
        b.put("registration_id",registration_id);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .header("X-Tracker",UUID.randomUUID().toString())
                .cookies(this.cookie.allCookies)
                .body(b.toString())
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        validateApiResponse(response);

        this.cookie = new Cookie(response.cookies());
    }

    public Response checkRouteAssigned(String query_value) throws IOException {

        String endpoint = String.format(utility.dapi.Endpoints.CEE_CURRENT);
        report.log("Check Route Assigned. "+
                "Endpoint: "+ endpoint, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .queryParam("include",query_value)
                .headers(commonHeader)
                .header("X-Tracker",UUID.randomUUID().toString())
                .cookies(this.cookie.allCookies)
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();
        validateApiResponse(response);

        this.cookie = new Cookie(response.cookies());
        return response;
    }

    public Response checkCeeCurrent(int dm_id, int sa_id)
    {
        String endpoint = String.format(utility.dapi.Endpoints.CEE_CURRENT);
        report.log("Check Cee Current FC. "+
                "Endpoint: "+ endpoint, true);

        JsonObject b= new JsonObject();
        b.put("dm_id",dm_id);
        b.put("sa_id",sa_id);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .header("X-Tracker",UUID.randomUUID().toString())
                .cookies(this.cookie.allCookies)
                .body(b.toString())
                .when()
                .patch(endpoint)
                .then()
                .log().all()
                .extract().response();
        validateApiResponse(response);

        this.cookie = new Cookie(response.cookies());
        return response;
    }

    public void checkAuth(String app_version, int cee_id)
    {
        String endpoint = String.format(utility.dapi.Endpoints.CHECK_AUTH);
        report.log("Calling check-auth api. "+
                "Endpoint: "+ endpoint, true);

        JsonObject b= new JsonObject();
        b.put("app_version",app_version);
        b.put("cee_id",cee_id);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .header("X-Tracker",UUID.randomUUID().toString())
                .cookies(this.cookie.allCookies)
                .body(b.toString())
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        validateApiResponse(response);

        this.cookie = new Cookie(response.cookies());

    }

    public void postGetConfig()
    {
        String endpoint = String.format(utility.dapi.Endpoints.GET_CONFIG);
        report.log("Calling get config api. "+
                "Endpoint: "+ endpoint, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .header("X-Tracker",UUID.randomUUID().toString())
                .cookies(this.cookie.allCookies)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        validateApiResponse(response);

        this.cookie = new Cookie(response.cookies());

    }

    public void ceeCheckIn(String lat, String lng)
    {
        String endpoint = String.format(utility.dapi.Endpoints.CEE_CHECK_IN);
        report.log("Calling post cee check-in api. "+
                "Endpoint: "+ endpoint, true);

        JsonObject b= new JsonObject();
        b.put("latitude",lat);
        b.put("longitude",lng);
        b.put("qr_code","");

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .header("X-Tracker",UUID.randomUUID().toString())
                .cookies(this.cookie.allCookies)
                .body(b.toString())
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        validateApiResponse(response);

        this.cookie = new Cookie(response.cookies());

    }

    public void ceeLocationData(String lat, String lng)
    {
        String endpoint = String.format(utility.dapi.Endpoints.CEE_LOCATION_DATA);
        report.log("Checking cee location. "+
                "Endpoint: "+ endpoint, true);


        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Instant instant = timestamp.toInstant();

        JsonObject location= new JsonObject()
        .put("accuracy",3.9)
        .put("gpsEnabled",true)
        .put("lat",Double.parseDouble(lat))
        .put("lng",Double.parseDouble(lng))
        .put("provider","fused")
        .put("speed",0.6888213) // valid string
        .put("timestamp",instant.toEpochMilli())
        .put("valid",false);

        JsonObject location1 = new JsonObject().put("location",location);
        JsonObject payload = new JsonObject().put("payload",location1);


        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .header("X-Tracker",UUID.randomUUID().toString())
                .cookies(this.cookie.allCookies)
                .body(payload.toString())
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        //validateApiResponse(response);

        this.cookie = new Cookie(response.cookies());

    }

    public Response routeAssignment(String qr_code)
    {
        String endpoint = String.format(utility.dapi.Endpoints.ROUTE_ASSIGNMENT);
        report.log("Route Assignment, calling route assignment api. "+
                "Endpoint: "+ endpoint, true);

        JsonObject b= new JsonObject();
        b.put("qr_code",qr_code);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .header("X-Tracker",UUID.randomUUID().toString())
                .cookies(this.cookie.allCookies)
                .body(b.toString())
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        validateApiResponse(response);

        this.cookie = new Cookie(response.cookies());
        return response;
    }

    public void collectOrder(int route_id)
    {
        String endpoint = String.format(utility.dapi.Endpoints.COLLECT_ORDERS,route_id);
        report.log("Collect Order "+
                "Endpoint: "+ endpoint, true);

        JsonObject b= new JsonObject();
        b.put("collected",true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .header("X-Tracker",UUID.randomUUID().toString())
                .cookies(this.cookie.allCookies)
                .body(b.toString())
                .when()
                .patch(endpoint)
                .then()
                .log().all()
                .extract().response();
       // validateApiResponse(response);

        this.cookie = new Cookie(response.cookies());

    }

    public void getOrder()
    {
        String endpoint = String.format(utility.dapi.Endpoints.GET_ORDERS);
        report.log("Calling get-orders api "+
                "Endpoint: "+ endpoint, true);



        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .header("X-Tracker",UUID.randomUUID().toString())
                .cookies(this.cookie.allCookies)
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        // validateApiResponse(response);

        this.cookie = new Cookie(response.cookies());

    }

    //TRAVEL START


    public void ceeTripStatus(int route_id,String status)
    {
        String endpoint = String.format(utility.dapi.Endpoints.CEE_TRIP_STATUS);
        report.log("Starting trip "+
                "Endpoint: "+ endpoint, true);

        JsonObject b= new JsonObject();
        b.put("route_id",route_id);
        b.put("status",status);


        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .header("X-Tracker",UUID.randomUUID().toString())
                .cookies(this.cookie.allCookies)
                .body(b.toString())
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        // validateApiResponse(response);

        this.cookie = new Cookie(response.cookies());

    }

















    private void validateApiResponse(Response response) {
        int statusCode = response.statusCode();
        Assert.assertEquals(statusCode, 200, "Incorrect status code. Response: " + response.asString());
    }



}

