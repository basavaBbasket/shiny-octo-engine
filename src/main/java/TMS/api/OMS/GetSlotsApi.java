package TMS.api.OMS;

import TMS.configAndUtilities.Config;
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

public class GetSlotsApi extends WebSettings implements Endpoints {

    IReport report;
    private String xCaller;
    private String xEntryContext;
    private String xEntryContextId;
    private String xOriginContext;
    private String xTenantId;
    private String xProject;
    private RequestSpecification requestSpecification;

    public GetSlotsApi(IReport report, String xCaller, String xEntryContext, String xEntryContextId, String xOriginContext, String xTenantId, String xProject) {
        this.report = report;
        this.xCaller = xCaller;
        this.xEntryContextId=xEntryContextId;
        this.xEntryContext=xEntryContext;
        this.xOriginContext = xOriginContext;
        this.xTenantId = xTenantId;
        this.xProject = xProject;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    public Response getAvailableSlots(String order_type,Integer pincode, String external_product_id,int external_reference_id) throws IOException {

        String endpoint = String.format(TMS.api.OMS.Endpoints.GET_SLOTS);

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

        String requestBody = createGetSlotsBody(order_type,pincode,external_product_id,external_reference_id);
        report.log("Fetch avialable slots from get-slots api",true);
        report.log("Get slots API:" +
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

    private static String createGetSlotsBody(String order_type,Integer pincode, String external_product_id,int external_reference_id)
    {

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
                        .put("lmd_fc_id",Integer.parseInt(Config.tmsConfig.getString("lmd_fc_id")))
                        .put("max_slot_count",Integer.parseInt(Config.tmsConfig.getString("max_slot_count")))
                        .put("max_slot_days",Integer.parseInt(Config.tmsConfig.getString("max_slot_days")))
                        .put("items",new JsonArray().add(skuKJSON)));

        JsonObject jsonbody = new JsonObject()
                .put("location",new JsonObject().put("pincode",pincode))
                .put("orders",order);

        return jsonbody.toString();
    }

    public static ArrayList<ArrayList<String>> getSlotsDates(IReport report, Map<String, String> headerData,Map<String, String> memberData,Map<String, String> skuData,int external_reference_id) throws IOException {

        GetSlotsApi getSlotsApi = new GetSlotsApi(report,headerData.get("xCaller"),headerData.get("entryContext"),headerData.get("entryContextId"),headerData.get("originContext"),headerData.get("tenantId"),headerData.get("projectId"));
        Response response = getSlotsApi.getAvailableSlots(headerData.get("orderType") ,Integer.parseInt(memberData.get("pincode")),skuData.get("external_sku_id"),external_reference_id);

        ArrayList<String> slotDates = new ArrayList<>();
        ArrayList<String> slot_definition_id = new ArrayList<>();
        ArrayList<String> template_slot_id = new ArrayList<>();
        JsonPath jsonPath = response.jsonPath();
        for(int i = 0; i< Integer.parseInt(Config.tmsConfig.getString("max_slot_count")) ; i++) {
            String strPath = "orders[0].slots["+i+"].slot_start_date";
            String slot_def_path = "orders[0].slots["+i+"].slots[0].slot_definition_id";
            String template_def_path= "orders[0].slots["+i+"].slots[0].template_slot_id";
            slotDates.add(jsonPath.getString(strPath));
            slot_definition_id.add(jsonPath.getString(slot_def_path));
            template_slot_id.add(jsonPath.getString(template_def_path));
        }


        ArrayList<ArrayList<String>> slots_details = new ArrayList<>();
        slots_details.add(slotDates);
        slots_details.add(slot_definition_id);
        slots_details.add(template_slot_id);

        return slots_details;
    }

    public Response getAvailableSlotsWithParam(String external_product_id,String order_type,Integer pincode, String lmd_fc_id, Integer offset, Integer max_slot_day ,Integer max_slot_count) throws IOException {

        String endpoint = String.format(TMS.api.OMS.Endpoints.GET_SLOTS);

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
        requestHeader.put("Host","hqa-svc.bigbasket.com");
        requestHeader.put("Content-Type","application/json");

        JsonObject skuKJSON = new JsonObject()
                .put("offset_in_minutes",offset)
                .put("mrp",Double.parseDouble(Config.tmsConfig.getString("skuDetails.mrp")))
                .put("sp",Double.parseDouble(Config.tmsConfig.getString("skuDetails.sp")))
                .put("quantity",Double.parseDouble(Config.tmsConfig.getString("skuDetails.quantity")))
                .put("shipment_charge",Double.parseDouble(Config.tmsConfig.getString("skuDetails.sp")))
                .put("reason_id",Config.tmsConfig.getString("skuDetails.reason_id"))
                .put("external_product_id",external_product_id);


        JsonArray order = new JsonArray()
                .add(new JsonObject()
                        .put("order_type",order_type)
                        .put("lmd_fc_id",lmd_fc_id)
                        .put("max_slot_count",max_slot_count)
                        .put("max_slot_days",max_slot_day)
                        .put("items",new JsonArray().add(skuKJSON)));

        JsonObject jsonbody = new JsonObject()
                .put("location",new JsonObject().put("pincode",pincode))
                .put("orders",order);

        report.log("calling get slots api",true);
        report.log("Request body for Get slots "+jsonbody.toString(),true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .log().all()
                .when()
                .body(jsonbody.toString())
                .post(msvcServerName+ endpoint)
                //.post("http://hqasvc-alb.bigbasket.com"+ endpoint)

                .then().log().all()
                .extract().response();

       // Assert.assertEquals(response.getStatusCode() /*actual value*/, 200 /*expected value*/);
        report.log("Status code: " + response.getStatusCode() +"\n Response: \n" + response.prettyPrint(),true);

        return response;
    }

    public static Response getSlotsDatesWithParam(IReport report, Map<String, String> headerData,Map<String, String> memberData,Map<String, String> skuData, String lmd_fc_id, Integer offset, Integer max_slot_day,Integer max_slot_count) throws IOException {

        GetSlotsApi getSlotsApi = new GetSlotsApi(report,headerData.get("xCaller"),headerData.get("entryContext"),headerData.get("entryContextId"),headerData.get("originContext"),headerData.get("tenantId"),headerData.get("projectId"));
        Response response =   getSlotsApi.getAvailableSlotsWithParam( skuData.get("external_sku_id"), headerData.get("orderType"),Integer.parseInt(memberData.get("pincode")),lmd_fc_id,  offset, max_slot_day,max_slot_count);
        return response;
    }



    public static int getSlotsFromResponse(Response response,IReport report){
        ArrayList slots= new ArrayList<>();
        JsonPath jsonPath = response.jsonPath();
        slots=jsonPath.get ("orders[0].slots");
        int slotscount=0;
          int len=slots.toArray().length;
          for(int i=0 ;i<len;i++){
              HashMap slotsin= (HashMap) slots.get(i);
              ArrayList slotsinmap= (ArrayList) slotsin.get("slots");
              slotscount=slotscount+slotsinmap.toArray().length;
          }
          return slotscount;

    }
    public static int getSlotslengthFromResponse(Response response,IReport report) {
        ArrayList slots = new ArrayList<>();
        JsonPath jsonPath = response.jsonPath();
        slots = jsonPath.get("orders[0].slots");
        int len=slots.toArray().length;
        return len;
    }
}
