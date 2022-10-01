package msvc.planogram.external.warehouseID;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import api.warehousecomposition.planogram_FC.internal.Endpoints;
import org.testng.Assert;
import utility.dataset.JWT_TOKEN_DL;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class WriteOff extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;

    public WriteOff(IReport report) {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes post request to writeoff api.
     * this will writeoff the product from mentioned location
     * @param binLocation
     * @param locationID
     * @param skuID
     * @param qty
     * @param reasonID
     * @param reason
     * @param warehouseID
     * @param isSkipValidation
     * @return
     */
    public Response callWriteOffApi(String binLocation, int locationID, int skuID, int qty, int reasonID, String reason, String warehouseID, boolean isSkipValidation) {
        String endpoint = String.format(DynamicLocationWarehouse.WRITE_OFF_RESOURCE, warehouseID);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Authorization", "Bearer " + JWT_TOKEN_DL.KOMAL.getValue());
        requestHeader.put("x-tracker", UUID.randomUUID().toString());
        HashMap<String, Object> map = new HashMap<>();
        map.put("bin_location", binLocation);
        map.put("location_id", locationID);
        map.put("sku_id", skuID);
        map.put("quantity", qty);
        map.put("reason_id", reasonID);
        map.put("reason", reason);

        report.log("Calling Writeoff api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString() +
                "\n Body: " + map.toString(), true);

        Response response = RestAssured.given()
                .log()
                .all()
                .spec(requestSpecification)
                .headers(requestHeader)
                .body(map)
                .post(endpoint)
                .then()
                .log()
                .all()
                .extract().response();
        report.log("WriteOff API response : " + response.prettyPrint(), true);
        if (!isSkipValidation) {
            validateResponse(response);
        }
        return response;
    }

    private void validateResponse(Response response) {
        Assert.assertTrue(response.getStatusCode() == 200, response.asString() + "<br><br><br>");
    }

}
