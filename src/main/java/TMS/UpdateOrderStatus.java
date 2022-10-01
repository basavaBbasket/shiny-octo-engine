package TMS;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.eta.admin.Endpoints;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

    public Response updateOrderStatus(Integer orderId, String status , String external_order_id)
    {

        String endpoint = String.format(TMS.Endpoints.UPDATE_ORDER_STATUS);

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
        requestHeader.put("Content-Type", "application/json");

        String requestBody = createUpdateStatusBody(orderId, status,external_order_id);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .log().all()
                .when()
                .body(requestBody)
                .put(msvcServerName+ endpoint)
                .then().log().all()
                .extract().response();

        Assert.assertEquals(response.getStatusCode() /*actual value*/, 200 /*expected value*/);
        report.log("Status code: " + response.getStatusCode() +"\n Response: \n" + response.prettyPrint(),true);

        return response;



    }

    private String createUpdateStatusBody(Integer orderId, String status, String external_order_id) {

        JSONObject skuKJSON = new JSONObject(Config.tmsConfig.get("skuDetails"));
        JsonArray order = new JsonArray()
                .add(new JsonObject()
                        .put("id",orderId)
                        .put("status",status)
                        .put("timestamp",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                .format(new DateTime(new Date()).toDate()))
                        .put("external_order_id",external_order_id)
                        .put("items",new JsonArray().add(skuKJSON))

                );
        JsonObject jsonbody = new JsonObject()
                .put("orders",order);

        return jsonbody.toString();

    }

}
