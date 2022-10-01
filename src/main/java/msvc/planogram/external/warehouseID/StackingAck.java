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

public class StackingAck extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;

    public StackingAck(IReport report)
    {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes put request to stacking ack api.
     * stacking ack will receive for recommended bin
     * @param jobID
     * @param warehouseID
     * @param taskID
     * @param skuID
     * @param batchID
     * @param binID
     * @param locationID
     * @param stackedQty
     * @param pendingQty
     * @param underStackReasonID
     * @param underStackReason
     * @param stackingTimeStamp
     * @return
     */
    public String callStackingAckApi(int jobID, String warehouseID, int taskID, int skuID, int batchID, int binID, int locationID, int stackedQty, int pendingQty, int underStackReasonID, String underStackReason,String stackingTimeStamp) {
        String endpoint = String.format(DynamicLocationWarehouse.STACKING_ACK_RESOURCE, warehouseID, jobID);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Authorization", "Bearer " + JWT_TOKEN_DL.KOMAL.getValue());
        requestHeader.put("x-tracker", UUID.randomUUID().toString());
        String body = createStackingAckApiBody(taskID, skuID, batchID, binID, locationID, stackedQty, pendingQty, underStackReasonID, underStackReason,stackingTimeStamp);

        report.log("Calling StackingACK api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString() +
                "\n Body: "+body , true);

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
        report.log("StackingACK API response : " + response.prettyPrint(), true);
        validateResponse(response);
        return response.asString();
    }

    private void validateResponse(Response response) {
        Assert.assertTrue(response.getStatusCode() == 200, response.asString() + "<br><br><br>");
    }

    private String createStackingAckApiBody(int taskID, int skuID, int batchID, int binID, int locationID, int stackedQty, int pendingQty, int underStackReasonID, String underStackReason,String stackingTimeStamp) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder binsArrayInfo = Json.createArrayBuilder();
        JsonObjectBuilder indiviualBinObj = Json.createObjectBuilder();
        indiviualBinObj.add("task_id", taskID);
        indiviualBinObj.add("sku_id", skuID);
        indiviualBinObj.add("batch_id", batchID);
        indiviualBinObj.add("bin_id", binID);
        indiviualBinObj.add("location_id", locationID);
        indiviualBinObj.add("stacked_quantity", stackedQty);
        indiviualBinObj.add("pending_quantity", pendingQty);
        indiviualBinObj.add("stacking_timestamp", stackingTimeStamp.replaceAll("\\.[0-9]+","Z"));
        indiviualBinObj.add("understack_reason_id", underStackReasonID);
        indiviualBinObj.add("understack_reason", underStackReason);
        binsArrayInfo.add(indiviualBinObj);
        jsonObjectBuilder.add("bins",binsArrayInfo);
        JsonObject jsonObject = jsonObjectBuilder.build();
        return jsonObject.toString();
    }
}
