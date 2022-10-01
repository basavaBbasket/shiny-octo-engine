package tms.api.OMS;

import tms.configAndUtilities.Config;
import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;
import org.junit.Assert;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UpdateActions extends WebSettings implements Endpoints{
    IReport report;
    private String xCaller;
    private String xEntryContext;
    private String xEntryContextId;
    private String xOriginContext;
    private String xTenantId;
    private String xProject;
    private RequestSpecification requestSpecification;


    public UpdateActions(IReport report, String xCaller, String xEntryContext, String xEntryContextId, String xOriginContext, String xTenantId, String xProject) {
        this.report = report;
        this.xCaller = xCaller;
        this.xEntryContextId=xEntryContextId;
        this.xEntryContext=xEntryContext;
        this.xOriginContext = xOriginContext;
        this.xTenantId = xTenantId;
        this.xProject = xProject;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);

    }

    public Response updateActions(String orderId, Map<String, String> skuData , Map<String, String> headerData) throws IOException {


        String endpoint = String.format(Endpoints.UPDATE_ACTIONS);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Origin-Context",xOriginContext);
        requestHeader.put("X-Tenant-Id",xTenantId);
        requestHeader.put("X-Project",xProject);
        //requestHeader.put("Host","hqa-svc.bigbasket.com");
        requestHeader.put("Content-Type","application/json");

        String requestBody = createBody(orderId,skuData , headerData);

        report.log("Updating actions details",true);
        report.log("Calling Update Actions api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString()+
                "\n Headers: "+requestBody.toString(),true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .log().all()
                .when()
                .body(requestBody)
                .post(msvcServerName+ endpoint)
                .then().log().all()
                .extract().response();

        Assert.assertEquals(response.getStatusCode() /*actual value*/, 200 /*expected value*/);
        report.log("Status code: " + response.getStatusCode() +"\n Response: \n" + response.prettyPrint(),true);

        return response;

    }

    private String createBody(String orderId, Map<String, String> skuData ,Map<String, String> headerData ) {

        JsonArray actions_details = new JsonArray()
                .add(new JsonObject()
                        .put("action_code",Config.tmsConfig.getInt("actions.action_code") ) //todo
                        .put("external_product_id",skuData.get("external_sku_id"))
                        .put("action_desc",Config.tmsConfig.getList("actions.action_desc"))
                        .put("value",Config.tmsConfig.getList("actions.value")));

        JsonObject orderJson = new JsonObject()
                .put("external_order_id",headerData.get("external_order_id"))
                .put("order_id",orderId)
                .put("actions",actions_details);



        JsonArray orderArrJson = new JsonArray()
                .add(orderJson);
        JsonObject jsonObject = new JsonObject().put("orders",orderArrJson);
        return jsonObject.toString();

    }


    public static void updateActions(IReport report, String orderId , Map<String, String> headerData,Map<String, String> memberData,Map<String, String> skuData) throws IOException
    {
        UpdateActions updateAction = new UpdateActions(report,headerData.get("xCaller"),headerData.get("entryContext"),headerData.get("entryContextId"),headerData.get("originContext"),headerData.get("tenantId"),headerData.get("projectId"));
        Response response = updateAction.updateActions(orderId, skuData , headerData);

    }


}
