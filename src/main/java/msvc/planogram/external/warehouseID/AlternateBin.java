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
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class AlternateBin extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;

    public AlternateBin(IReport report)
    {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes post request to grn alternatebin api.
     * alternate bins will be recommended based on the provided parameter
     * @param jobID
     * @param warehouseID
     * @param skuID
     * @param pendingQty
     * @return
     */
    public String callAlternateBinAPI(int jobID, String warehouseID,int skuID, int pendingQty) {
        String endpoint = String.format(DynamicLocationWarehouse.ALTERNATE_BIN_RESOURCE, warehouseID, jobID);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Authorization", "Bearer " + JWT_TOKEN_DL.KOMAL.getValue());
        requestHeader.put("x-tracker", UUID.randomUUID().toString());
        String body = createAlternateBinApiBody(skuID,pendingQty);

        report.log("Calling AlternateBin API: " +
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
        report.log("AlternateBin API response : " + response.prettyPrint(), true);
        validateResponse(response);
        return response.asString();
    }

    private void validateResponse(Response response) {
        Assert.assertTrue(response.getStatusCode() == 200, response.asString() + "<br><br><br>");
    }

    private String createAlternateBinApiBody(int skuID, int pendingQty) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder skusArrayInfo = Json.createArrayBuilder();
        JsonObjectBuilder indiviualSkuObj = Json.createObjectBuilder();
        indiviualSkuObj.add("sku_id", skuID);
        indiviualSkuObj.add("pending_quantity", pendingQty);
        skusArrayInfo.add(indiviualSkuObj);
        jsonObjectBuilder.add("skus",skusArrayInfo);
        JsonObject jsonObject = jsonObjectBuilder.build();
        return jsonObject.toString();
    }
}
