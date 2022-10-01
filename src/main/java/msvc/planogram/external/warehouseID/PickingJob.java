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

public class PickingJob extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;

    public PickingJob(IReport report) {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes post request to grn stacking job api.
     * picking job will be created with the provided parameters.
     * @param warehouseID
     * @param pickingSkuInfoList
     * @param skipValidation
     * @return
     */
    public Response callPickingJobApi(String warehouseID, List<HashMap<String, Object>> pickingSkuInfoList, boolean skipValidation) {
        String endpoint = String.format(DynamicLocationWarehouse.PICKING_JOB_RESOURCE, warehouseID);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Authorization", "Bearer " + JWT_TOKEN_DL.KOMAL.getValue());
        requestHeader.put("x-tracker", UUID.randomUUID().toString());
        String body = getPickingJobBody(pickingSkuInfoList);

        report.log("Calling PickingJob API: " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString() +
                "\n Body: " + body, true);

        Response response = RestAssured.given()
                .log()
                .all()
                .spec(requestSpecification)
                .headers(requestHeader)
                .body(body)
                .post(endpoint)
                .then()
                .log()
                .all()
                .extract().response();
        report.log("PickingJob API response : " + response.prettyPrint(), true);
        if (!skipValidation) {
            validateResponse(response);
        }
        return response;
    }

    private void validateResponse(Response response) {
        Assert.assertTrue(response.getStatusCode() == 200, response.asString() + "<br><br><br>");
    }

    private String getPickingJobBody(List<HashMap<String, Object>> skuInfoList) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        JsonArrayBuilder skuInfoArray = Json.createArrayBuilder();
        for (int i = 0; i < skuInfoList.size(); i++) {
            JsonObjectBuilder looseQtyObj1 = Json.createObjectBuilder();
            looseQtyObj1.add("sku_id", ((int) skuInfoList.get(i).get("sku_id")));
            looseQtyObj1.add("order_id", ((int) skuInfoList.get(i).get("order_id")));
            looseQtyObj1.add("quantity_requested", (int) skuInfoList.get(i).get("quantity_requested"));
            skuInfoArray.add(looseQtyObj1);
        }

        jsonObjectBuilder.add("sku_info", skuInfoArray);
        JsonObject jsonObject = jsonObjectBuilder.build();
        System.out.println("Body: " + jsonObject);
        return jsonObject.toString();
    }


    public static HashMap<String, Object> settingPickingJobInfo(int skuID, int orderID, int qtyRequested) {
        HashMap<String, Object> skuInfo = new HashMap<>();
        skuInfo.put("sku_id", skuID);
        skuInfo.put("order_id", orderID);
        skuInfo.put("quantity_requested", qtyRequested);
        return skuInfo;
    }

}
