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

import java.io.FileInputStream;
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

    public Response doReserveSlots(String orderType , ArrayList<ArrayList<String>> slotDateList) throws IOException {


        String endpoint = String.format(TMS.Endpoints.RESERVER_SLOTS);

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
        requestHeader.put("Content-Type","application/json");

        String requestBody = createReserveSlotBody(orderType, slotDateList.get(0).get(1) ,slotDateList.get(1).get(1) ,slotDateList.get(2).get(1));

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

    private String createReserveSlotBody(String order_type , String slotDate , String slot_definition_id , String template_slot_id ) {

        JSONObject memberDetails = new JSONObject(Config.tmsConfig.get("member"));
        JSONObject addressDetails = new JSONObject(Config.tmsConfig.get("address"));
        JSONObject dminfoDetails = new JSONObject(Config.tmsConfig.get("default_dm_info"));
        JSONObject skuKJSON = new JSONObject(Config.tmsConfig.get("skuDetails"));

        JsonArray order = new JsonArray()
                .add(new JsonObject()
                        .put("order_type",order_type)
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
                .put("location",new JsonObject().put("pincode",Integer.parseInt(Config.tmsConfig.get("address.pincode"))))
                .put("member",memberDetails)
                .put("address",addressDetails)
                .put("orders",order);



        return jsonbody.toString();


    }


}
