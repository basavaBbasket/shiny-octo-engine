package msvc.order.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;

public class OrderStatusAPI extends WebSettings implements Endpoints{

    IReport report;
    private String xCaller;
    private String xEntryContextId;
    private String xEntryContext;
    private String xservice;
    private RequestSpecification requestSpecification;

    public OrderStatusAPI( String xEntryContext,String xEntryContextId,String xservice,String xCaller,  IReport report) {
        this.report = report;
        this.xservice=xservice;
        this.xCaller = xCaller;
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    public Response changeOrderStatus(String status, String orderID, List<Integer> sku) {
        String endpoint = String.format(Endpoints.ORDER);

        Map<String, Object> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Service",xservice);
        requestHeader.put("X-Timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("Content-Type","application/json");



        JsonArray skuArr = new JsonArray();

        for(int i= 0 ; i< sku.size();i++)
        {
            JsonObject objec = new JsonObject().put("sku",sku.get(i).toString()).put("quantity","2").put("reason_id",2);
            skuArr.add(objec);
        }

        JsonArray arr = new JsonArray().add(new JsonObject().put("id",orderID).put("status",status).put("timestamp","2020-09-09 23:59:71").put("items",skuArr));
        JsonObject body = new JsonObject().put("orders",arr);


        Response response  = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .put(msvcServerName + endpoint)
                .then()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n  Change Order status response: \n" + response.prettyPrint(),true);
        return response;


    }


}
