package tms.api.OMS;

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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChangeSlots extends WebSettings implements Endpoints {

    IReport report;
    private String xCaller;
    private String xEntryContext;
    private String xEntryContextId;
    private String xOriginContext;
    private String xTenantId;
    private String xProject;
    private RequestSpecification requestSpecification;

    public ChangeSlots(IReport report, String xCaller, String xEntryContext, String xEntryContextId, String xOriginContext, String xTenantId, String xProject) {
        this.report = report;
        this.xCaller = xCaller;
        this.xEntryContextId=xEntryContextId;
        this.xEntryContext=xEntryContext;
        this.xOriginContext = xOriginContext;
        this.xTenantId = xTenantId;
        this.xProject = xProject;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    public Response changeSlot(Integer orderId, String slotDate , String slot_definition_id , String template_slot_id) throws IOException {

        String endpoint = String.format(tms.api.OMS.Endpoints.CHANGE_SLOTS,orderId);

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

        String requestBody = changeSlotBody( slotDate , slot_definition_id ,template_slot_id);

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
                //.post("http://hqasvc-alb.bigbasket.com"+ endpoint)
                .then().log().all()
                .extract().response();

        Assert.assertEquals(response.getStatusCode() /*actual value*/, 200 /*expected value*/);
        report.log("Status code: " + response.getStatusCode() +"\n Response: \n" + response.prettyPrint(),true);
        return response;
    }

    private static String changeSlotBody(String slotDate , String slot_definition_id , String template_slot_id)
    {
        JsonObject assign_slotsJson = new JsonObject()
                .put("slots", new JsonArray().add(new JsonObject()
                                .put("slot_date",slotDate)
                                .put("slot_definition_id",Integer.valueOf(slot_definition_id))
                                .put("template_slot_id",Integer.valueOf(template_slot_id))
                        ))
                .put("reason_id",428);//todo

        JsonObject jsonbody = new JsonObject()
                .put("assign_slots",assign_slotsJson)
                .put("operation","assign_slots");

        return jsonbody.toString();
    }

    public static Response doChangeSlot(IReport report, Map<String, String> headerData, Integer orderId, String slotDate , String slot_definition_id , String template_slot_id) throws IOException {
        ChangeSlots changeSlotsApi = new ChangeSlots(report,headerData.get("xCaller"),headerData.get("entryContext"),headerData.get("entryContextId"),headerData.get("originContext"),headerData.get("tenantId"),headerData.get("projectId"));
        Response response =   changeSlotsApi.changeSlot(orderId, slotDate,slot_definition_id,template_slot_id);
        return response;
    }


}
