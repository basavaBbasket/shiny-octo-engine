package msvc.planogram.external.warehouseID;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import api.warehousecomposition.planogram_FC.internal.Endpoints;
import org.testng.Assert;
import utility.dataset.JWT_TOKEN_DL;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class PickingAck extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;

    public PickingAck(IReport report) {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    public Response callPickingAckApi(int jobID, String warehouseID, List<HashMap<String, Object>> skuInfoList, boolean isSkipValidation) {

        String endpoint = String.format(DynamicLocationWarehouse.PICKING_ACK_RESOURCE, warehouseID, jobID);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Authorization", "Bearer " + JWT_TOKEN_DL.KOMAL.getValue());
        requestHeader.put("x-tracker", UUID.randomUUID().toString());
        String body = createPickingAckApiBody(skuInfoList);

        report.log("Calling PickingAck API: " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString() +
                "\n Body: " + body, true);

        Response response = RestAssured.given()
                .log()
                .all()
                .spec(requestSpecification)
                .headers(requestHeader)
                .body(body)
                .put(endpoint)
                .then()
                .log()
                .all()
                .extract().response();
        report.log("PickingACK API response : " + response.prettyPrint(), true);
        if (!isSkipValidation) {
            validateResponse(response);
        }
        return response;
    }

    private void validateResponse(Response response) {
        Assert.assertTrue(response.getStatusCode() == 200, response.asString() + "<br><br><br>");
    }


    private String createPickingAckApiBody(List<HashMap<String, Object>> skuInfoList) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder skuInfoArray = Json.createArrayBuilder();

        for (int i = 0; i < skuInfoList.size(); i++) {
            JsonObjectBuilder skuBinObject = Json.createObjectBuilder();
            skuBinObject.add("task_id", (int) skuInfoList.get(i).get("task_id"));
            skuBinObject.add("sku_id", (int) skuInfoList.get(i).get("sku_id"));
            skuBinObject.add("order_id", (int) skuInfoList.get(i).get("order_id"));
            skuBinObject.add("bin_location", (String) skuInfoList.get(i).get("bin_location"));
            skuBinObject.add("bin_id", (int) skuInfoList.get(i).get("bin_id"));
            skuBinObject.add("location_id", (int) skuInfoList.get(i).get("location_id"));
            skuBinObject.add("quantity_picked", (int) skuInfoList.get(i).get("quantity_picked"));
            skuBinObject.add("quantity_recommended", (int) skuInfoList.get(i).get("quantity_recommended"));
            skuBinObject.add("quantity_weight", (Double) skuInfoList.get(i).get("quantity_weight"));
            skuBinObject.add("batch_id", (int) skuInfoList.get(i).get("batch_id"));
            skuBinObject.add("picking_timestamp", (String) skuInfoList.get(i).get("picking_timestamp"));
            skuBinObject.add("underpick_reason_id", (int) skuInfoList.get(i).get("underpick_reason_id"));

            List<HashMap<String, Object>> crateInfoList = (List<HashMap<String, Object>>) skuInfoList.get(i).get("crates");
            JsonArrayBuilder crateInfoArray = Json.createArrayBuilder();
            for (int j = 0; j < crateInfoList.size(); j++) {
                JsonObjectBuilder crateObject = Json.createObjectBuilder();
                crateObject.add("type_id", (int) crateInfoList.get(j).get("type_id"));
                crateObject.add("label", (String) crateInfoList.get(j).get("label"));
                crateObject.add("quantity", (int) crateInfoList.get(j).get("quantity"));
                crateObject.add("weight", (Double) crateInfoList.get(j).get("weight"));
                crateObject.add("status", (String) crateInfoList.get(j).get("status"));
                crateObject.add("pick_timestamp", (String) crateInfoList.get(j).get("pick_timestamp"));
                crateInfoArray.add(crateObject);
            }

            skuBinObject.add("crates", crateInfoArray);
            skuInfoArray.add(skuBinObject);

        }
        jsonObjectBuilder.add("sku_info", skuInfoArray);
        JsonObject jsonObject = jsonObjectBuilder.build();
        return jsonObject.toString();
    }

    public static HashMap<String, Object> settingPickingAckSkuInfo(int taskID, int skuID, int orderID, String binLocation, int binID, int locationID
            , int qtyPicked, int qtyRecommended, Double qtyWeight, int batchID, String pickingTimeStamp, int underPickReasonID, List<HashMap<String, Object>> crateInfo) {

        HashMap<String, Object> skuInfo = new HashMap<>();
        skuInfo.put("task_id", taskID);
        skuInfo.put("sku_id", skuID);
        skuInfo.put("order_id", orderID);
        skuInfo.put("bin_location", binLocation);
        skuInfo.put("bin_id", binID);
        skuInfo.put("location_id", locationID);
        skuInfo.put("quantity_picked", qtyPicked);
        skuInfo.put("quantity_recommended", qtyRecommended);
        skuInfo.put("quantity_weight", qtyWeight);
        skuInfo.put("batch_id", batchID);
        skuInfo.put("picking_timestamp", pickingTimeStamp.replaceAll("\\.[0-9]+", "Z"));
        skuInfo.put("underpick_reason_id", underPickReasonID);
        skuInfo.put("crates", crateInfo);
        return skuInfo;
    }

    public static HashMap<String, Object> settingPickingAckSkuCrateInfo(int typeID, String label, int qty, Double weight, String status, String pickingTimeStamp) {
        HashMap<String, Object> crateInfo = new HashMap<>();
        crateInfo.put("type_id", typeID);
        crateInfo.put("label", label);
        crateInfo.put("quantity", qty);
        crateInfo.put("weight", weight);
        crateInfo.put("status", status);
        crateInfo.put("pick_timestamp", pickingTimeStamp.replaceAll("\\.[0-9]+", "Z"));
        return crateInfo;
    }
}
