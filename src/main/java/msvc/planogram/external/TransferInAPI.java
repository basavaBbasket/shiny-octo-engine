package msvc.planogram.external;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import utility.dataset.JWT_TOKEN_DL;
import utility.dataset.TransferInType;
import utility.database.DynamicLocationDBQueries;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import msvc.planogram.TransferInGeneralMethods;
import org.json.JSONObject;
import org.testng.Assert;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class TransferInAPI extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;

    public TransferInAPI(IReport report) {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes post request to transferin api.
     * this will be used to make inward of stocks to warehouse
     * @param wareHouseID
     * @param type
     * @param rtvId
     * @param looseQtySKUList
     * @param containerSKUList
     * @param transferInGeneralMethods
     * @param isSkipValidation
     * @return
     */
    public String getTransferInAPIResponse(String wareHouseID, TransferInType type, String rtvId, List<HashMap<String, Object>> looseQtySKUList,
                                           List<HashMap<String, Object>> containerSKUList, TransferInGeneralMethods transferInGeneralMethods, boolean isSkipValidation) {
        String endpoint = String.format(DynamicLocationWarehouse.TRANSFER_IN_RESOURCE, wareHouseID, type.getValue());
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Authorization", "Bearer " + JWT_TOKEN_DL.KOMAL.getValue());
        requestHeader.put("x-tracker", UUID.randomUUID().toString());
        HashMap<String, String> map = new HashMap<>();
        map.put("whID", wareHouseID);
        map.put("type", type.getValue());
        transferInGeneralMethods.setTranferId(getUniqueTransferId());
        String body = getTransferInApiBody(rtvId, looseQtySKUList, containerSKUList, transferInGeneralMethods);

        report.log("Calling transferIn API for warehouseId:  " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString() +
                "\n Body: "+body , true);

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
        report.log("TransferIn API Response Data: Status Details " + response.getStatusLine() + " \n " +
                "response body" + response.getBody().asString(), true);

        System.out.println("Status Code : " + response.statusLine());
        System.out.println("GetTransferInAPIResponse response: " + response.prettyPrint());
        if (!isSkipValidation) {
            validateResponse(response);
        }
        return response.asString();
    }

    private void validateResponse(Response response) {
        Assert.assertTrue(response.getStatusCode() == 200, response.getStatusLine() + "\n");
        String status = response.getStatusLine();
        Assert.assertTrue(status.contains("0") || status.contains("OK"), response.getStatusLine() + "\n");
    }

    private String getTransferInApiBody(String rtvId, List<HashMap<String, Object>> looseQtySKUList,
                                        List<HashMap<String, Object>> containerSKUList, TransferInGeneralMethods transferInGeneralMethods) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("transfer_id", transferInGeneralMethods.getTransferId());
        jsonObjectBuilder.add("po_id", getPOID());
        jsonObjectBuilder.add("rtv_id", rtvId);
        jsonObjectBuilder.add("created_by", "AUTOMATION");
        jsonObjectBuilder.add("created_on", "2020-06-20T05:01:09.639Z");

        //Adding json object inside loose quantity array
        JsonArrayBuilder looseQtyInfoArray = Json.createArrayBuilder();
        for (int i = 0; i < looseQtySKUList.size(); i++) {
            JsonObjectBuilder looseQtyObj1 = Json.createObjectBuilder();
            looseQtyObj1.add("sku_id", ((int) looseQtySKUList.get(i).get("sku_id")));
            looseQtyObj1.add("quantity", ((int) looseQtySKUList.get(i).get("quantity")));
            looseQtyObj1.add("mfg_date", looseQtySKUList.get(i).get("mfg_date").toString());
            looseQtyObj1.add("exp_date", looseQtySKUList.get(i).get("exp_date").toString());
            looseQtyObj1.add("is_bundlepack", ((boolean) looseQtySKUList.get(i).get("is_bundlepack")));
            looseQtyObj1.add("cp", ((double) looseQtySKUList.get(i).get("cp")));
            looseQtyObj1.add("mrp", ((double) looseQtySKUList.get(i).get("mrp")));
            looseQtyObj1.add("eretail_mrp", ((double) looseQtySKUList.get(i).get("eretail_mrp")));
            looseQtyObj1.add("gst", ((double) looseQtySKUList.get(i).get("gst")));
            looseQtyObj1.add("cess", ((double) looseQtySKUList.get(i).get("cess")));
            looseQtyInfoArray.add(looseQtyObj1);
        }
        jsonObjectBuilder.add("loose_quantity_info", looseQtyInfoArray);


        JsonArrayBuilder containerArrayInfo = Json.createArrayBuilder();

        //Adding json object inside container sku array
        for (int i = 0; i < containerSKUList.size(); i++) {
            JsonObjectBuilder containerQtyObj1 = Json.createObjectBuilder();
            containerQtyObj1.add("container_id", ((int) containerSKUList.get(i).get("container_id")));
            containerQtyObj1.add("container_tag", containerSKUList.get(i).get("container_tag").toString());
            containerQtyObj1.add("container_type", containerSKUList.get(i).get("container_type").toString());
            containerQtyObj1.add("unknown_dimension", ((boolean) containerSKUList.get(i).get("unknown_dimension")));

            JsonArrayBuilder containerQtyObj1SkuArrayInfo1 = Json.createArrayBuilder();
            JsonObjectBuilder containerQtyObj1SkuArrayInfo1Obj1 = Json.createObjectBuilder();
            containerQtyObj1SkuArrayInfo1Obj1.add("sku_id", ((int) containerSKUList.get(i).get("sku_id")));
            containerQtyObj1SkuArrayInfo1Obj1.add("quantity", ((int) containerSKUList.get(i).get("quantity")));
            containerQtyObj1SkuArrayInfo1Obj1.add("mfg_date", containerSKUList.get(i).get("mfg_date").toString());
            containerQtyObj1SkuArrayInfo1Obj1.add("exp_date", containerSKUList.get(i).get("exp_date").toString());
            containerQtyObj1SkuArrayInfo1Obj1.add("is_bundlepack", ((boolean) containerSKUList.get(i).get("is_bundlepack")));
            containerQtyObj1SkuArrayInfo1Obj1.add("cp", ((double) containerSKUList.get(i).get("cp")));
            containerQtyObj1SkuArrayInfo1Obj1.add("mrp", ((double) containerSKUList.get(i).get("mrp")));
            containerQtyObj1SkuArrayInfo1Obj1.add("eretail_mrp", ((double) containerSKUList.get(i).get("eretail_mrp")));
            containerQtyObj1SkuArrayInfo1Obj1.add("gst", ((double) containerSKUList.get(i).get("gst")));
            containerQtyObj1SkuArrayInfo1Obj1.add("cess", ((double) containerSKUList.get(i).get("cess")));
            containerQtyObj1SkuArrayInfo1.add(containerQtyObj1SkuArrayInfo1Obj1);

            containerQtyObj1.add("sku_info", containerQtyObj1SkuArrayInfo1);
            containerArrayInfo.add(containerQtyObj1);
        }
        jsonObjectBuilder.add("container_info", containerArrayInfo);


        JsonObject jsonObject = jsonObjectBuilder.build();

        System.out.println("Body: " + jsonObject);
        return jsonObject.toString();
    }


    private long getUniqueTransferId() {
        String query = "select transferin_id from transferin order by transferin_id DESC limit 1;";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        long currentTansferInID = 0;
        if (jsonObject.getInt("numRows") != 0) {
            currentTansferInID = jsonObject.getJSONArray("rows").getJSONObject(0).getLong("transferin_id");
        }
        return currentTansferInID + 1;
    }

    private int getPOID() {
        return 999;
    }


    public String getTransferInAPIResponse(String wareHouseID, TransferInType type, List<HashMap<String, Object>> looseQtySKUList,
                                           List<HashMap<String, Object>> containerSKUList, TransferInGeneralMethods transferInGeneralMethods, boolean isSkipValidation) {
        String endpoint = String.format(DynamicLocationWarehouse.TRANSFER_IN_RESOURCE, wareHouseID, type.getValue());
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Authorization", "Bearer " + JWT_TOKEN_DL.KOMAL.getValue());
        requestHeader.put("x-tracker", UUID.randomUUID().toString());
        HashMap<String, String> map = new HashMap<>();
        map.put("whID", wareHouseID);
        map.put("type", type.getValue());
        report.log("Calling transferIn API for warehouseId: " + wareHouseID + " " +
                "transferInType+" + type.getValue(), true);
        transferInGeneralMethods.setTranferId(getUniqueTransferId());
        String body = getTransferInApiBody(looseQtySKUList, containerSKUList, transferInGeneralMethods);

        report.log("TransferInAPIRequest body: " + body, true);
        Response response = RestAssured.given()
                .log()
                .all()
                .spec(requestSpecification)
                .headers(requestHeader)
                .body(body)
                .post(DynamicLocationWarehouse.TRANSFER_IN_RESOURCE, wareHouseID, type.getValue())
                .then()
                .log()
                .all()
                .extract().response();
        report.log("TransferIn API Response Data: Status Details " + response.getStatusLine() + " \n " +
                "response body" + response.getBody().asString(), true);

        System.out.println("Status Code : " + response.statusLine());
        System.out.println("GetTransferInAPIResponse response: " + response.asString());
        if (!isSkipValidation) {
            validateResponse(response);
        }
        return response.asString();
    }

    private String getTransferInApiBody(List<HashMap<String, Object>> looseQtySKUList,
                                        List<HashMap<String, Object>> containerSKUList, TransferInGeneralMethods transferInGeneralMethods) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("transfer_id", transferInGeneralMethods.getTransferId());
        jsonObjectBuilder.add("po_id", getPOID());
        jsonObjectBuilder.add("created_by", "AUTOMATION");
        jsonObjectBuilder.add("created_on", "2020-06-20T05:01:09.639Z");

        //Adding json object inside loose quantity array
        JsonArrayBuilder looseQtyInfoArray = Json.createArrayBuilder();
        for (int i = 0; i < looseQtySKUList.size(); i++) {
            JsonObjectBuilder looseQtyObj1 = Json.createObjectBuilder();
            looseQtyObj1.add("sku_id", ((int) looseQtySKUList.get(i).get("sku_id")));
            looseQtyObj1.add("quantity", ((int) looseQtySKUList.get(i).get("quantity")));
            looseQtyObj1.add("mfg_date", looseQtySKUList.get(i).get("mfg_date").toString());
            looseQtyObj1.add("exp_date", looseQtySKUList.get(i).get("exp_date").toString());
            looseQtyObj1.add("is_bundlepack", ((boolean) looseQtySKUList.get(i).get("is_bundlepack")));
            looseQtyObj1.add("cp", ((double) looseQtySKUList.get(i).get("cp")));
            looseQtyObj1.add("mrp", ((double) looseQtySKUList.get(i).get("mrp")));
            looseQtyObj1.add("eretail_mrp", ((double) looseQtySKUList.get(i).get("eretail_mrp")));
            looseQtyObj1.add("gst", ((double) looseQtySKUList.get(i).get("gst")));
            looseQtyObj1.add("cess", ((double) looseQtySKUList.get(i).get("cess")));
            looseQtyInfoArray.add(looseQtyObj1);
        }
        jsonObjectBuilder.add("loose_quantity_info", looseQtyInfoArray);


        JsonArrayBuilder containerArrayInfo = Json.createArrayBuilder();

        //Adding json object inside container sku array
        for (int i = 0; i < containerSKUList.size(); i++) {
            JsonObjectBuilder containerQtyObj1 = Json.createObjectBuilder();
            containerQtyObj1.add("container_id", ((int) containerSKUList.get(i).get("container_id")));
            containerQtyObj1.add("container_tag", containerSKUList.get(i).get("container_tag").toString());
            containerQtyObj1.add("container_type", containerSKUList.get(i).get("container_type").toString());
            containerQtyObj1.add("unknown_dimension", ((boolean) containerSKUList.get(i).get("unknown_dimension")));

            JsonArrayBuilder containerQtyObj1SkuArrayInfo1 = Json.createArrayBuilder();
            JsonObjectBuilder containerQtyObj1SkuArrayInfo1Obj1 = Json.createObjectBuilder();
            containerQtyObj1SkuArrayInfo1Obj1.add("sku_id", ((int) containerSKUList.get(i).get("sku_id")));
            containerQtyObj1SkuArrayInfo1Obj1.add("quantity", ((int) containerSKUList.get(i).get("quantity")));
            containerQtyObj1SkuArrayInfo1Obj1.add("mfg_date", containerSKUList.get(i).get("mfg_date").toString());
            containerQtyObj1SkuArrayInfo1Obj1.add("exp_date", containerSKUList.get(i).get("exp_date").toString());
            containerQtyObj1SkuArrayInfo1Obj1.add("is_bundlepack", ((boolean) containerSKUList.get(i).get("is_bundlepack")));
            containerQtyObj1SkuArrayInfo1Obj1.add("cp", ((double) containerSKUList.get(i).get("cp")));
            containerQtyObj1SkuArrayInfo1Obj1.add("mrp", ((double) containerSKUList.get(i).get("mrp")));
            containerQtyObj1SkuArrayInfo1Obj1.add("eretail_mrp", ((double) containerSKUList.get(i).get("eretail_mrp")));
            containerQtyObj1SkuArrayInfo1Obj1.add("gst", ((double) containerSKUList.get(i).get("gst")));
            containerQtyObj1SkuArrayInfo1Obj1.add("cess", ((double) containerSKUList.get(i).get("cess")));
            containerQtyObj1SkuArrayInfo1.add(containerQtyObj1SkuArrayInfo1Obj1);

            containerQtyObj1.add("sku_info", containerQtyObj1SkuArrayInfo1);
            containerArrayInfo.add(containerQtyObj1);
        }
        jsonObjectBuilder.add("container_info", containerArrayInfo);

        JsonObject jsonObject = jsonObjectBuilder.build();

        System.out.println("Body: " + jsonObject);
        return jsonObject.toString();
    }
}
