package tms.api.OMS;

import tms.configAndUtilities.Config;
import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.eta.admin.Endpoints;
import org.joda.time.DateTime;
import org.junit.Assert;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConfirmOrderApi extends WebSettings implements Endpoints {

    IReport report;
    private String xCaller;
    private String xEntryContext;
    private String xEntryContextId;
    private String xOriginContext;
    private String xTenantId;
    private String xProject;
    private RequestSpecification requestSpecification;

    public ConfirmOrderApi(IReport report, String xCaller, String xEntryContext, String xEntryContextId, String xOriginContext, String xTenantId, String xProject) {
        this.report = report;
        this.xCaller = xCaller;
        this.xEntryContextId=xEntryContextId;
        this.xEntryContext=xEntryContext;
        this.xOriginContext = xOriginContext;
        this.xTenantId = xTenantId;
        this.xProject = xProject;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);

    }

    public Response confirmOrder(Integer reservationId,String orderType,int external_reference_id) throws IOException {

        String endpoint = String.format(tms.api.OMS.Endpoints.CONFIRM_ORDER);

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
       // requestHeader.put("Host","hqa-svc.bigbasket.com");
        requestHeader.put("Content-Type","application/json");

        String requestBody = createConfirmOrderBody(reservationId, orderType,external_reference_id);
        report.log("Calling Confirm order api",true);
        report.log("Request body for Confirm order api "+requestBody.toString(),true);

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

    private String createConfirmOrderBody(Integer reservationId , String orderType,int external_reference_id) {

        double price = Double.parseDouble(Config.tmsConfig.getString("skuDetails.sp"));
        double quantity = Double.parseDouble(Config.tmsConfig.getString("skuDetails.quantity"));
        double shipping_charge = Double.parseDouble(Config.tmsConfig.getString("skuDetails.shipment_charge"));
        Double total_payble = price* quantity + shipping_charge;

        JsonArray reservations = new JsonArray()
                .add(new JsonObject()
                        .put("reservation_id",reservationId)
                        .put("order_type",orderType)
                        .put("external_order_id",String.valueOf(external_reference_id))
                        .put("payment",new JsonObject()
                                .put("payment_method",Config.tmsConfig.getString("payment_method"))
                                .put("total_payable",total_payble.toString())
                        )
                );

        JsonObject jsonbody = new JsonObject()
                .put("reservations",reservations);

        return jsonbody.toString();
    }

    public static Integer fetchOrderId(IReport report, Map<String,String> memberData ,Map<String, String> headerData, Integer reservationId ,int external_reference_id) throws IOException {
        ConfirmOrderApi confirmOrderApi = new ConfirmOrderApi(report,headerData.get("xCaller"),headerData.get("entryContext"),headerData.get("entryContextId"),headerData.get("originContext"),headerData.get("tenantId"),headerData.get("projectId"));
        Response confirmOrderResponse =  confirmOrderApi.confirmOrder(reservationId,headerData.get("orderType"),external_reference_id);
        JsonPath jsonPath = confirmOrderResponse.jsonPath();
        return jsonPath.get("orders[0].id");
    }
    public static Response fetchOrderIdWithOutreservationid(IReport report, Map<String,String> memberData ,Map<String, String> headerData, Integer reservationId ,int external_reference_id) throws IOException {
        ConfirmOrderApi confirmOrderApi = new ConfirmOrderApi(report,headerData.get("xCaller"),headerData.get("entryContext"),headerData.get("entryContextId"),headerData.get("originContext"),headerData.get("tenantId"),headerData.get("projectId"));
        Response confirmOrderResponse =  confirmOrderApi.confirmOrderWithoutAssertion(reservationId,headerData.get("orderType"),external_reference_id);
        return confirmOrderResponse;
    }


    public Response confirmOrderWithoutAssertion(int reservationId,String orderType,int external_reference_id) throws IOException {

        String endpoint = String.format(tms.api.OMS.Endpoints.CONFIRM_ORDER);

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
       // requestHeader.put("Host","hqa-svc.bigbasket.com");
        requestHeader.put("Content-Type","application/json");

        String requestBody = createConfirmOrderBody(reservationId, orderType,external_reference_id);
        report.log("Do Confirm Order from confirm-order api",true);
        report.log("Calling Get Store Status  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString()+
                "\n Headers: "+requestBody.toString(),true);
        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .log().all()
                .when()
                .body(requestBody)
                .post(msvcServerName+ endpoint)
               // .post("http://hqasvc-alb.bigbasket.com"+ endpoint)

                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n Response: \n" + response.prettyPrint(),true);

        return response;


    }


}
