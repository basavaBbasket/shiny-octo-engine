package tms.api.DAPI;

import api.warehousecomposition.planogram_FC.internal.Cookie;
import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.testng.Assert;
import utility.dapi.DeliveryApp;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.RestAssuredConfig.config;

public class DapiAppForTMS extends WebSettings implements Endpoints {
    public static Logger logger = Logger.getLogger(DeliveryApp.class);
    public Cookie cookie;
    public Response response;

    private Map<String, String> commonHeader;
    private IReport report;
    private RequestSpecification monolithSpec;
    private RequestSpecification msvcSpec;

    public DapiAppForTMS(AutomationReport report, String tenantID, String projectId) {
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
        commonHeader.put("X-Tenant-Id", tenantID);
        commonHeader.put("X-Project",projectId);

    }

    public DapiAppForTMS login(int cee_id , String password, String deviceId, String app_version, String registrationId) throws IOException, InterruptedException {

        String endpoint = String.format(Endpoints.VERIFY_LOGIN);
        report.log("Calling Login "+
                "Endpoint: "+ endpoint, true);

        JsonObject body= new JsonObject();
        body.put("cee_id",cee_id)
                .put("password",password)
                .put("device_id",deviceId)
                .put("app_version",app_version)
                .put("registration_id",registrationId);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body.toString())
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        this.response=response;
        validateApiResponse(response);
        commonHeader.put("Authorization","Bearer "+response.path("access_token", new String[0]));
        commonHeader.put("X-Refresh-Token",response.path("refresh_token", new String[0]));
        this.cookie = new Cookie(response.cookies());

        Response getStoresResponse = getStores("has_route_assigned,stores,pool_status");
        report.log("getstores response"+getStoresResponse.prettyPrint(),true);
        int sa_id = getStoresResponse.path("sa_id");
        int dm_id = getStoresResponse.path("dm_id");
         selectStore(sa_id,dm_id);
        //checkIn("19.310900350184177","72.85341024398802","1:2022-07-13");
        //currentRoute();
        return this;
    }

    public Response getStores(String query_value) {
        String endpoint = String.format(Endpoints.CEE_CURRENT);
        report.log("Get Store " +
                "Endpoint: " + endpoint, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .queryParam("include",query_value)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();
        validateApiResponse(response);

        this.cookie = new Cookie(response.cookies());
        return response;
    }

    public Response selectStore(int sa_id , int dm_id){

        String endpoint = String.format(Endpoints.CEE_CURRENT);
        report.log("Select Store"+
                "Endpoint: "+ endpoint, true);

        JsonObject body= new JsonObject();
        body.put("sa_id",sa_id)
                .put("dm_id",dm_id);
        report.log("Store sa_id and dm_id: "+body.toString(),true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .header("X-Tracker",UUID.randomUUID().toString())
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body.toString())
                .patch(endpoint)
                .then()
                .log().all()
                .extract().response();
        this.response=response;
        validateApiResponse(response);
        return response;
    }

    public Response checkIn(String lat, String lon, String qrCode){

        String endpoint = String.format(Endpoints.CEE_CHECK_IN);
        report.log("Do CheckIn"+
                "Endpoint: "+ endpoint, true);

        JsonObject body= new JsonObject();
        body.put("latitude",lat)
                .put("longitude",lon)
                .put("qr_code",qrCode);
        report.log("Store lat and log "+body.toString(),true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body.toString())
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        this.response=response;
        validateApiResponse(response);
        return response;
    }

    public Response currentRoute(){

        String endpoint = String.format(Endpoints.CURRENT_ROUTE);
        report.log("Current Route"+
                "Endpoint: "+ endpoint, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .put(endpoint)
                .then()
                .log().all()
                .extract().response();
        this.response=response;
        validateApiResponse(response);
        return response;
    }




    /**
     * Method to update the trip status, this method make a post call to trip status api, with route_id and status
     * @param route_id
     * @param status
     * @return
     */
    public Response updateTripStatus(int route_id, String status){

        String endpoint = String.format(Endpoints.TRIP_STATUS);
        report.log("Calling Login "+
                "Endpoint: "+ endpoint, true);

        JsonObject body= new JsonObject();
        body.put("route_id",route_id)
                .put("status",status);
        report.log("Updating Trip Status body"+body.toString(),true);
        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body.toString())
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        this.response=response;
        validateApiResponse(response);
        return response;
    }


    public Response travelStart(int order_id, String latitude, String longitude, Array linked_orders,Array undeliveredOrders){

        String endpoint = String.format(Endpoints.TRAVEL_START);
        report.log("Calling Login "+
                "Endpoint: "+ endpoint, true);

        JsonObject body= new JsonObject();
        body.put("orderId",order_id)
                .put("latitude",latitude)
                .put("longitude",longitude)
                .put("linked_orders",linked_orders)
                .put("undeliveredOrders",undeliveredOrders);

        report.log("Updating TRAVEL START body"+body.toString(),true);
        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body.toString())
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        this.response=response;
        validateApiResponse(response);
        return response;
    }

//
    public Response reachedCustomer(int order_id, String latitude, String longitude, Array linked_orders,boolean  is_early_delivery, boolean was_early_delivery_warning_shown, String slot_start_time){

        String endpoint = String.format(Endpoints.REACHED_CUSTOMER);
        report.log("Calling Login "+
                "Endpoint: "+ endpoint, true);

        JsonObject body= new JsonObject();
        body.put("orderId",order_id)
                .put("linked_orders",linked_orders)
                .put("latitude",latitude)
                .put("longitude",longitude)
                .put("device_time_stamp",new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss'")
                        .format(new DateTime(new Date()).toDate()))
                .put("is_early_delivery",is_early_delivery)
                .put("was_early_delivery_warning_shown",was_early_delivery_warning_shown)
                .put("slot_start_time",slot_start_time)
                .put("server_time_stamp",new SimpleDateFormat("dd/MM/yyyy' 'HH:mm:ss'")
                        .format(new DateTime(new Date()).toDate()));



        report.log("Updating TRAVEL START body"+body.toString(),true);
        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body.toString())
                .post(endpoint)
                .then()
                .log().all()
                .extract().response();
        this.response=response;
        validateApiResponse(response);
        return response;
    }

    public Response verification(boolean consider_skip_as_failed, String slot_date, String slot_definition_id, String type,String action_code,String action_level,String entity_id, String labels,String trigger, String values){

        String endpoint = String.format(Endpoints.VERIFICATION);
        report.log("Calling Login "+
                "Endpoint: "+ endpoint, true);

        String arrlabels[]=new String[1];
        String arrvalues[]=new String[1];

        arrlabels[0]=labels;
        arrvalues[0]=values;
        JsonObject payload= new JsonObject();
        payload.put("action_code",action_code)
                .put("action_level",action_level)
                .put("entity_id",entity_id)
                .put("lables",arrlabels)
                .put("trigger",trigger)
                .put("values",arrvalues);
        
        JsonObject body= new JsonObject();
        body.put("consider_skip_as_failed",consider_skip_as_failed)
                .put("slot_date",slot_date)
                .put("slot_definition_id",slot_definition_id)
                .put("type",type)
                .put("payload",payload);



        report.log("Updating TRAVEL START body"+body.toString(),true);
        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body.toString())
                .patch(endpoint)
                .then()
                .log().all()
                .extract().response();
        this.response=response;
        validateApiResponse(response);
        return response;
    }




    public Response checkOrderStatus(String action, int order_id, String slot_date, int slot_definition_id,JsonArray returns){

        String endpoint = String.format(Endpoints.ORDER_STATUS);
        report.log("Calling Login "+
                "Endpoint: "+ endpoint, true);

        JsonArray orders=new JsonArray();
        orders.add(new JsonObject().put("order_id",order_id).put("slot_date",slot_date).put("slot_definition_id",slot_definition_id).put("returns",new JsonArray().add(returns)));
        JsonObject body= new JsonObject();
        body.put("action",action)
                .put("orders",orders);


        report.log("Updating TRAVEL START body"+body.toString(),true);
        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body.toString())
                .patch(endpoint)
                .then()
                .log().all()
                .extract().response();
        this.response=response;
        validateApiResponse(response);
        return response;
    }

   
    public Response postPaymentReturns(String calculated_time, String  customer_comments, String delivery_status,String latitude, String  longitude,int manually_off_data,int data_network_issue,int order_id,int slot_id,String slot_date, String payment_method, String eze_tap_reason, JsonObject returns, JsonArray linked_orders, int cash_collected, int coupon_collected, String reference_number){

        String endpoint = String.format(Endpoints.POST_PAYMENT_RETURNS);
        report.log("Calling Login "+
                "Endpoint: "+ endpoint, true);

        JsonObject body= new JsonObject();
        body.put("calculated_time",calculated_time)
                .put("customer_comments",customer_comments)
                .put("delivery_status",delivery_status)
                .put("latitude",latitude)
                .put("longitude",longitude)
                .put("manually_off_data",manually_off_data)
                .put("data_network_issue",data_network_issue)
                .put("order_id",order_id)
                .put("slot_id",slot_id)
                .put("slot_date",slot_date)
                .put("payment_method",payment_method)
                .put("eze_tap_reason",eze_tap_reason)
                .put("returns",returns)
                .put("linked_orders",linked_orders)
                .put("payment_info",new JsonObject().put("cash_collected",cash_collected).put("coupon_collected",coupon_collected).put("reference_number",reference_number));




        report.log("Updating TRAVEL START body"+body.toString(),true);
        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body.toString())
                .patch(endpoint)
                .then()
                .log().all()
                .extract().response();
        this.response=response;
        validateApiResponse(response);
        return response;
    }

    private void validateApiResponse(Response response) {
        int statusCode = response.statusCode();
        Assert.assertEquals(statusCode, 200, "Incorrect status code. Response: " + response.asString());
    }


}
