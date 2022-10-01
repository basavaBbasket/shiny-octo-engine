package msvc.planogram.external.fcID;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import msvc.wio.internal.Endpoints;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class TaskLevelPickingApi extends WebSettings implements Endpoints {

    IReport report;
    private RequestSpecification requestSpecification;
    private String bbDecodedUid;
    private String contentType;
    private String bbauth;
    private String xTracker;


    public TaskLevelPickingApi(String xTracker,String bbDecodedUid,String contentType ,String bbauth, IReport report)
    {
        this.xTracker = xTracker;
        this.bbDecodedUid = bbDecodedUid;
        this.contentType = contentType;
        this.report = report;
        this.bbauth = bbauth;
        this.requestSpecification = getSimpleRequestSpecification(serverName, this.report);
    }

    /**
     * API: Task level Picking
     * This method makes PUT request to  API and validates status code
     * @return  response
     */


    public Response putTaskLevelPickingApi()
    {
        String endpoint = String.format(api.warehousecomposition.planogram_FC.internal.Endpoints.WareHouseCompositionFCPlanogram.TASK_LEVEL_PICKING);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("bb-decoded-uid", bbDecodedUid);
        requestHeader.put("content-type",contentType);
        requestHeader.put("cookie" , bbauth);
        requestHeader.put("X-TimeStamp",  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("x-tracker",xTracker);

        JSONObject requestBody = new JSONObject();

        JSONObject internalJson = new JSONObject();

        internalJson.put("task_id","362");
        internalJson.put("sku_id","10000103");
        internalJson.put("order_id","1");
        internalJson.put("bin_location","C-03-B-2");
        internalJson.put("bin_id","1053");
        internalJson.put("location_id","1");
        internalJson.put("quantity_picked","2");
        internalJson.put("quantity_recommended","2");
        internalJson.put("quantity_weight","0");
        internalJson.put("batch_id","8");
        internalJson.put("picking_timestamp","2021-11-18T11:55:43.511Z");
        internalJson.put("underpick_reason_id","0");

        JSONObject cratesJson = new JSONObject();
        cratesJson.put("type_id" , "0");
        cratesJson.put("label","BB-02");
        cratesJson.put("quantity", "2");
        cratesJson.put("weight" , "0");
        cratesJson.put("status","open");
        cratesJson.put("pick_timestamp" , "2020-11-09T11:14:42.072Z");

        JSONArray cratesArJson = new JSONArray();
        cratesArJson.put(cratesJson);
        internalJson.put("crates", cratesArJson);

        JSONArray skuInfoJson = new JSONArray();

        skuInfoJson.put(internalJson);

        requestBody.put("sku_info" , skuInfoJson);


        report.log("Calling api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);



        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(requestBody.toString())
                .put(serverName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate   response: " + response.prettyPrint(),true);
        return response;

    }






}
