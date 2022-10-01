package tms.api.OMS;

import tms.configAndUtilities.Config;
import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.eta.admin.Endpoints;
import org.joda.time.DateTime;
import org.junit.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpdateOrderStatus extends WebSettings implements Endpoints {

    IReport report;
    private String xCaller;
    private String xEntryContext;
    private String xEntryContextId;
    private String xOriginContext;
    private String xTenantId;
    private String xProject;
    private RequestSpecification requestSpecification;

    public UpdateOrderStatus(IReport report, String xCaller, String xEntryContext, String xEntryContextId, String xOriginContext, String xTenantId, String xProject) {
        this.report = report;
        this.xCaller = xCaller;
        this.xEntryContextId=xEntryContextId;
        this.xEntryContext=xEntryContext;
        this.xOriginContext = xOriginContext;
        this.xTenantId = xTenantId;
        this.xProject = xProject;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);

    }

    public Response updateOrderStatus(Integer orderId, String status , String skuId, String external_product_id)
    {

        String endpoint = String.format(tms.api.OMS.Endpoints.UPDATE_ORDER_STATUS);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("X-Timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Origin-Context", xOriginContext);
        requestHeader.put("X-Tenant-Id", xTenantId);
        requestHeader.put("X-Project", xProject);
       // requestHeader.put("Host","hqa-svc.bigbasket.com");
        requestHeader.put("Content-Type", "application/json");

        String requestBody = createUpdateStatusBody(orderId, status,skuId, external_product_id);

        report.log("Changing order status to "+status,true);
        report.log("Order Status  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString()+
                "\n Headers: "+requestBody.toString(),true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .log().all()
                .when()
                .body(requestBody)
                .put(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        Assert.assertEquals(response.getStatusCode() /*actual value*/, 200 /*expected value*/);
        report.log("Status code: " + response.getStatusCode() +"\n Response: \n" + response.prettyPrint(),true);
        return response;
    }

    private String createUpdateStatusBody(Integer orderId, String status, String skuId, String external_product_id) {

        JsonObject skuKJSON = new JsonObject()
                .put("quantity",String.valueOf(Config.tmsConfig.getDouble("skuDetails.quantity")))
                .put("reason_id",Integer.parseInt(Config.tmsConfig.getString("skuDetails.reason_id")))
                .put("sku",skuId);

        JsonArray order = new JsonArray()
                .add(new JsonObject()
                        .put("id",orderId)
                        .put("status",status)
                        .put("timestamp",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(new DateTime(new Date()).toDate()))
                       .put("items",new JsonArray().add(skuKJSON))

                );
        JsonObject jsonbody = new JsonObject()
                .put("orders",order);
        return jsonbody.toString();
    }

    public static void updateOrderStatus(IReport report, Map<String,String> memberData,Map<String,String> skuData, Map<String,String> headerData, Integer orderId, String status ) {
        UpdateOrderStatus updateOrderStatus = new UpdateOrderStatus(report,headerData.get("xCaller"),headerData.get("entryContext"),headerData.get("entryContextId"),headerData.get("originContext"),headerData.get("tenantId"),headerData.get("projectId"));
        updateOrderStatus.updateOrderStatus(orderId,status, skuData.get("id"), skuData.get("external_sku_id"));
    }

    public static void updateOrderStatusToDelivered(IReport report, Map<String,String> memberData,Map<String,String> skuData, Map<String,String> headerData, Integer orderId ) {
        UpdateOrderStatus updateOrderStatus = new UpdateOrderStatus(report,headerData.get("xCaller"),headerData.get("entryContext"),headerData.get("entryContextId"),headerData.get("originContext"),headerData.get("tenantId"),headerData.get("projectId"));
        updateOrderStatus.updateOrderStatusToDeliver(orderId,"delivered", skuData.get("id"), skuData.get("external_sku_id"));
    }

    public Response updateOrderStatusToDeliver(Integer orderId, String status , String skuId, String external_product_id)
    {

        String endpoint = String.format(tms.api.OMS.Endpoints.UPDATE_ORDER_STATUS);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("X-Timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Origin-Context", xOriginContext);
        requestHeader.put("X-Tenant-Id", xTenantId);
        requestHeader.put("X-Project", xProject);
        //requestHeader.put("Host","hqa-svc.bigbasket.com");
        requestHeader.put("Content-Type", "application/json");

        String requestBody = createUpdateStatusToDeliverBody(orderId, status,skuId, external_product_id);

        report.log("Changing order status to delivered",true);
        report.log("Order Status api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString()+
                "\n Headers: "+requestBody.toString(),true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .log().all()
                .when()
                .body(requestBody)
                .put(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        Assert.assertEquals(response.getStatusCode() /*actual value*/, 200 /*expected value*/);
        report.log("Status code: " + response.getStatusCode() +"\n Response: \n" + response.prettyPrint(),true);
        return response;
    }

    private String createUpdateStatusToDeliverBody(Integer orderId, String status, String skuId, String external_product_id) {

        JsonArray order = new JsonArray()
                .add(new JsonObject()
                        .put("id",orderId)
                        .put("status",status)
                        .put("timestamp",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(new DateTime(new Date()).toDate()))

                );
        JsonObject jsonbody = new JsonObject()
                .put("orders",order);
        return jsonbody.toString();
    }

}
