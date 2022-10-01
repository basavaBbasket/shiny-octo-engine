package msvc.order.external;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;


import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class CreateOrder extends WebSettings implements Endpoints {



    IReport report;
    private HashMap commonheaders;
    private String xCaller;
    private String xservice;
    private String cookie;
    private RequestSpecification requestSpecification;


    public CreateOrder(  HashMap CommonHeaders,String cookie,String xCaller,String xservice, IReport report ){

        this.commonheaders=CommonHeaders;
        this.xCaller=xCaller;
        this.cookie=cookie;
        this.xservice=xservice;
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }


    public Response createOrder(String po_id)
    {
        String endpoint = String.format(Endpoints.CREATE_ORDER);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Service",xservice);
        requestHeader.put("Cookie", cookie);
        requestHeader.put("X-TimeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));

        JsonObject body=new JsonObject();
        body.put("po",new JsonObject().put("id",Integer.valueOf(po_id))).put("payment",new JsonObject().put("payment_method","NB_HDFC").put("bb_txn_id","a0f70076751ebedcb50c").put("payment_method_type","NB_HDFC").put("wallet",false));

        Response response = RestAssured.given().spec(requestSpecification)
                .urlEncodingEnabled(false)
                .headers(requestHeader)
                .headers(commonheaders)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n create potential order response: \n" + response.prettyPrint(),true);

        return response;


    }


    public int getOrderId(Response response)
    {
        JSONObject respObj = new JSONObject(response.asString());
        JSONArray ordersArray = respObj.getJSONArray("orders");
        JSONObject ordersObj = ordersArray.getJSONObject(0);
        int orderId = ordersObj.getInt("id");
        return orderId;
    }

    public double getOrderPrice(Response response)
    {
        JSONObject respObj = new JSONObject(response.asString());
        JSONArray ordersArray = respObj.getJSONArray("orders");
        JSONObject ordersObj = ordersArray.getJSONObject(0);
        double orderId = ordersObj.getDouble("sub_total");
        return orderId;
    }

}
