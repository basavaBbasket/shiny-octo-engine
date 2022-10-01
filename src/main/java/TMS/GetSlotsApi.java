package TMS;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.eta.admin.Endpoints;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;


import javax.json.Json;
import javax.json.JsonObjectBuilder;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    public Response getAvailableSlots(String order_type) throws IOException {


        String endpoint = String.format(TMS.Endpoints.GET_SLOTS);

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

        String requestBody = createGetSlotsBody(order_type);

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

    private static String createGetSlotsBody(String order_type)
    {

        Map<String , Object> skuMap = Config.tmsConfig.get("skuDetails");
        JSONObject skuKJSON = new JSONObject(skuMap);

        JsonArray order = new JsonArray()
                .add(new JsonObject()
                        .put("order_type",order_type)
                        .put("lmd_fc_id",Integer.parseInt(Config.tmsConfig.getString("lmd_fc_id")))
                        .put("max_slot_count",Integer.parseInt(Config.tmsConfig.getString("max_slot_count")))
                        .put("max_slot_days",Integer.parseInt(Config.tmsConfig.getString("max_slot_days")))
                        .put("items",new JsonArray().add(skuKJSON)));

        JsonObject jsonbody = new JsonObject()
                .put("location",new JsonObject().put("pincode",Integer.parseInt(Config.tmsConfig.get("address.pincode"))))
                .put("orders",order);

        return jsonbody.toString();

    }




}
