package TMS.api.OMS;

import TMS.configAndUtilities.Config;
import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class UpdatePackageDetails extends WebSettings implements Endpoints{

    IReport report;
    private String xCaller;
    private String xEntryContext;
    private String xEntryContextId;
    private String xOriginContext;
    private String xTenantId;
    private String xProject;
    private RequestSpecification requestSpecification;


    public UpdatePackageDetails(IReport report, String xCaller, String xEntryContext, String xEntryContextId, String xOriginContext, String xTenantId, String xProject) {
        this.report = report;
        this.xCaller = xCaller;
        this.xEntryContextId=xEntryContextId;
        this.xEntryContext=xEntryContext;
        this.xOriginContext = xOriginContext;
        this.xTenantId = xTenantId;
        this.xProject = xProject;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);

    }

    public Response updatePackageDetails(String orderId,Map<String, String> skuData , Map<String, String> headerData) throws IOException {


        String endpoint = String.format(Endpoints.UPDATE_PACKAGE);

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
        report.log("Updating package details in db",true);
        report.log("Update Package Details API " +
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

       // Assert.assertEquals(response.getStatusCode() /*actual value*/, 200 /*expected value*/);
        report.log("Status code: " + response.getStatusCode() +"\n Response: \n" + response.prettyPrint(),true);

        return response;

    }

    private String createBody(String orderId, Map<String, String> skuData ,Map<String, String> headerData ) {

        boolean fragile = (Integer.parseInt(skuData.get("fragile"))==1)?true:false;
        boolean stackable = (Integer.parseInt(skuData.get("stackable"))==1)?true:false;

        JsonArray package_details = new JsonArray()
                .add(new JsonObject()
                        .put("id",Config.tmsConfig.getString("skuDetails.id")) //todo
                        .put("label",Config.tmsConfig.getString("skuDetails.label")) //todo
                        .put("length",Double.parseDouble(skuData.get("length")))
                        .put("breadth",Double.parseDouble(skuData.get("length")))
                        .put("height",Double.parseDouble(skuData.get("length")))
                        .put("sku_info" ,new JsonArray().add(new JsonObject()
                                .put("sku",Integer.parseInt(skuData.get("id")))
                                .put("external_product_id",skuData.get("external_sku_id"))
                                .put("quantity",Config.tmsConfig.getString("skuDetails.quantity"))))
                        .put("fragile",fragile)
                        .put("stackable",stackable)
                        .put("upright",true));//todo

        JsonObject orderJson = new JsonObject()
                .put("external_order_id",headerData.get("external_order_id"))
                .put("order_id",orderId)
                .put("package",package_details);



        JsonArray orderArrJson = new JsonArray()
                .add(orderJson);
        JsonObject jsonBody = new JsonObject().put("orders",orderArrJson);
        return jsonBody.toString();

    }


    public static Response updatePackage(IReport report, String orderId , Map<String, String> headerData,Map<String, String> memberData,Map<String, String> skuData) throws IOException
    {
        UpdatePackageDetails updatePackageDetails = new UpdatePackageDetails(report,headerData.get("xCaller"),headerData.get("entryContext"),headerData.get("entryContextId"),headerData.get("originContext"),headerData.get("tenantId"),headerData.get("projectId"));
        Response response = updatePackageDetails.updatePackageDetails(orderId, skuData , headerData);
        return response;

    }


}
