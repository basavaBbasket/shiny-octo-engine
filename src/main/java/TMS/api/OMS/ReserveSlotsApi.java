package TMS.api.OMS;

import TMS.configAndUtilities.Config;
import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.reports.ReportDetails;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.eta.admin.Endpoints;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import org.junit.Assert;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReserveSlotsApi extends WebSettings implements Endpoints {

    IReport report;
        private String xCaller;
        private String xEntryContext;
        private String xEntryContextId;
        private String xOriginContext;
        private String xTenantId;
        private String xProject;
        private RequestSpecification requestSpecification;

    public ReserveSlotsApi(IReport report, String xCaller, String xEntryContext, String xEntryContextId, String xOriginContext, String xTenantId, String xProject) {
        this.report = report;
        this.xCaller = xCaller;
        this.xEntryContextId=xEntryContextId;
        this.xEntryContext=xEntryContext;
        this.xOriginContext = xOriginContext;
        this.xTenantId = xTenantId;
        this.xProject = xProject;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);

    }

    public Response doReserveSlots(String orderType , ArrayList<ArrayList<String>> slotDateList, Map<String,String > memberData, String external_porduct_id,int external_reference_id) throws IOException {

        String endpoint = String.format(TMS.api.OMS.Endpoints.RESERVER_SLOTS);

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

        String requestBody = createReserveSlotBody(orderType, slotDateList.get(0).get(0) ,slotDateList.get(1).get(0) ,slotDateList.get(2).get(0), memberData, external_porduct_id,external_reference_id);
        report.log("calling Reserve slots api",true);
        report.log("Request body for reserve slots "+requestBody.toString(),true);
        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .log().all()
                .when()
                .body(requestBody)
                .post(msvcServerName+ endpoint)
               // .post("http://hqasvc-alb.bigbasket.com"+ endpoint)

                .then().log().all()
                .extract().response();

        Assert.assertEquals(response.getStatusCode() /*actual value*/, 200 /*expected value*/);
        report.log("Status code: " + response.getStatusCode() +"\n Response: \n" + response.prettyPrint(),true);

        return response;
    }

    private String createReserveSlotBody(String order_type , String slotDate , String slot_definition_id , String template_slot_id, Map<String,String> memberData,String external_product_id,int external_reference_id) {

        JsonObject memberDetails = new JsonObject()
                .put("external_member_id",memberData.get("external_member_id"))
                .put("first_name",memberData.get("first_name"))
                .put("last_name",memberData.get("last_name"))
                .put("email",memberData.get("email"))
                .put("mobile_no",memberData.get("mobile_no"));

        JsonObject addressDetails = new JsonObject()
                .put("house_no",memberData.get("house_no"))
                .put("apartment_name",memberData.get("apartment_name"))
                .put("area",memberData.get("area"))
                .put("pincode", Integer.parseInt(memberData.get("pincode")) )
                .put("address1",memberData.get("address1"))
                .put("landmark",memberData.get("landmark"))
                .put("city",memberData.get("city"));
               // .put("address_nickname","");

        JSONObject dminfoDetails = new JSONObject(Config.tmsConfig.get("default_dm_info"));

        JsonObject skuKJSON = new JsonObject()
                .put("offset_in_minutes",Config.tmsConfig.getInt("skuDetails.offset_in_minutes"))
                .put("mrp",Double.parseDouble(Config.tmsConfig.getString("skuDetails.mrp")))
                .put("sp",Double.parseDouble(Config.tmsConfig.getString("skuDetails.sp")))
                .put("quantity",Double.parseDouble(Config.tmsConfig.getString("skuDetails.sp")))
                .put("shipment_charge",Double.parseDouble(Config.tmsConfig.getString("skuDetails.sp")))
                .put("reason_id",Config.tmsConfig.getString("skuDetails.reason_id"))
                .put("external_product_id",external_product_id);


        JsonArray order = new JsonArray()
                .add(new JsonObject()
                        .put("order_type",order_type)
                        .put("external_reference_id",String.valueOf(external_reference_id))
                        .put("default_dm_info",dminfoDetails)
                        .put("lmd_fc_id",Integer.parseInt(Config.tmsConfig.getString("lmd_fc_id")))
                        .put("max_slot_count",Integer.parseInt(Config.tmsConfig.getString("max_slot_count")))
                        .put("max_slot_days",Integer.parseInt(Config.tmsConfig.getString("max_slot_days")))
                        .put("items",new JsonArray().add(skuKJSON))
                        .put("slots", new JsonObject()
                                .put("slot_date",slotDate)
                                .put("slot_definition_id",Integer.parseInt(slot_definition_id))
                                .put("template_slot_id",Integer.parseInt(template_slot_id))
                            )
                        .put("shipment_charge",Config.tmsConfig.getString("skuDetails.shipment_charge"))
                    );

        JsonObject jsonbody = new JsonObject()
                .put("location",new JsonObject().put("pincode",Integer.parseInt(memberData.get("pincode"))))
                .put("member",memberDetails)
                .put("address",addressDetails)
                .put("orders",order);

        return jsonbody.toString();

    }


    public Response doReturnReserveSlots(String orderType , ArrayList<ArrayList<String>> slotDateList, Map<String,String > memberData, String external_porduct_id, int Original_order_id,int external_reference_id) throws IOException {

        String endpoint = String.format(TMS.api.OMS.Endpoints.RESERVER_SLOTS);

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

        String requestBody = createReturnReserveSlotBody(orderType, slotDateList.get(0).get(0) ,slotDateList.get(1).get(0) ,slotDateList.get(2).get(0), memberData, external_porduct_id,Original_order_id,external_reference_id);
        report.log("calling get slots api",true);
        report.log("Request body for Get slots "+requestBody.toString(),true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .log().all()
                .when()
                .body(requestBody)
                .post(msvcServerName+ endpoint)
//                .post("http://hqasvc-alb.bigbasket.com"+ endpoint)

                .then().log().all()
                .extract().response();

        Assert.assertEquals(response.getStatusCode() /*actual value*/, 200 /*expected value*/);
        report.log("Status code: " + response.getStatusCode() +"\n Response: \n" + response.prettyPrint(),true);

        return response;
    }

    private String createReturnReserveSlotBody(String order_type , String slotDate , String slot_definition_id , String template_slot_id, Map<String,String> memberData,String external_product_id,int Original_order_id,int external_reference_id) {

        JsonObject memberDetails = new JsonObject()
                .put("external_member_id",memberData.get("external_member_id"))
                .put("first_name",memberData.get("first_name"))
                .put("last_name",memberData.get("last_name"))
                .put("email",memberData.get("email"))
                .put("mobile_no",memberData.get("mobile_no"));

        JsonObject addressDetails = new JsonObject()
                .put("house_no",memberData.get("house_no"))
                .put("apartment_name",memberData.get("apartment_name"))
                .put("area",memberData.get("area"))
                .put("pincode", Integer.parseInt(memberData.get("pincode")) )
                .put("address1",memberData.get("address1"))
                .put("landmark",memberData.get("landmark"))
                .put("city",memberData.get("city"));
               // .put("address_nickname","");

        JSONObject dminfoDetails = new JSONObject(Config.tmsConfig.get("default_dm_info"));

        JsonObject skuKJSON = new JsonObject()
                .put("offset_in_minutes",Config.tmsConfig.getInt("skuDetails.offset_in_minutes"))
                .put("mrp",Double.parseDouble(Config.tmsConfig.getString("skuDetails.mrp")))
                .put("sp",Double.parseDouble(Config.tmsConfig.getString("skuDetails.sp")))
                .put("quantity",Double.parseDouble(Config.tmsConfig.getString("skuDetails.quantity")))
                .put("shipment_charge",Double.parseDouble(Config.tmsConfig.getString("skuDetails.sp")))
                .put("reason_id",Config.tmsConfig.getString("skuDetails.reason_id"))
                .put("external_product_id",external_product_id);


        JsonArray order = new JsonArray()
                .add(new JsonObject()
                        .put("order_type",order_type)
                        .put("external_reference_id",String.valueOf(external_reference_id))
                        .put("default_dm_info",dminfoDetails)
                        .put("lmd_fc_id",Integer.parseInt(Config.tmsConfig.getString("lmd_fc_id")))
                        .put("max_slot_count",Integer.parseInt(Config.tmsConfig.getString("max_slot_count")))
                        .put("max_slot_days",Integer.parseInt(Config.tmsConfig.getString("max_slot_days")))
                        .put("items",new JsonArray().add(skuKJSON))
                        .put("slots", new JsonObject()
                                .put("slot_date",slotDate)
                                .put("slot_definition_id",Integer.parseInt(slot_definition_id))
                                .put("template_slot_id",Integer.parseInt(template_slot_id))
                            )
                        .put("shipment_charge",Config.tmsConfig.getString("skuDetails.shipment_charge"))
                        .put("original_external_order_id",String.valueOf(Original_order_id))
                );

        JsonObject jsonbody = new JsonObject()
                .put("location",new JsonObject().put("pincode",Integer.parseInt(memberData.get("pincode"))))
                .put("member",memberDetails)
                .put("address",addressDetails)
                .put("orders",order);

        return jsonbody.toString();

    }

    public static Integer getReservationId(IReport report , Map<String,String> memberData ,Map<String, String> headerData,Map<String, String> skuData, ArrayList<ArrayList<String>> slotDatesList,int external_reference_id) throws IOException {
        ReserveSlotsApi reserveSlotsApi = new ReserveSlotsApi(report,headerData.get("xCaller"),headerData.get("entryContext"),headerData.get("entryContextId"),headerData.get("originContext"),headerData.get("tenantId"),headerData.get("projectId"));
        Response reserveSlotResponse =  reserveSlotsApi.doReserveSlots(headerData.get("orderType") , slotDatesList, memberData,skuData.get("external_sku_id"),external_reference_id);
        JsonPath jsonPath = reserveSlotResponse.jsonPath();
        return jsonPath.get("reservations[0].reservation_id");
    }


    public static Integer getVariableReservationId(IReport report , Map<String,String> memberData ,Map<String, String> headerData,Map<String, String> skuData, ArrayList<ArrayList<String>> slotDatesList,int Original_order_id,int external_reference_id) throws IOException {
        ReserveSlotsApi reserveSlotsApi = new ReserveSlotsApi(report,headerData.get("xCaller"),headerData.get("entryContext"),headerData.get("entryContextId"),headerData.get("originContext"),headerData.get("tenantId"),headerData.get("projectId"));
        Response reserveSlotResponse =  reserveSlotsApi.doReturnReserveSlots(headerData.get("orderType") , slotDatesList, memberData,skuData.get("external_sku_id"),Original_order_id,external_reference_id);
        JsonPath jsonPath = reserveSlotResponse.jsonPath();
        return jsonPath.get("reservations[0].reservation_id");
    }



}
