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

public class UnStackedBatchesAPI extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;
    private static String contentType = "application/json";

    public UnStackedBatchesAPI(IReport report) {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request to unstacked batches api.
     * sku info with batch details will be provided
     * @param warehouseID
     * @param skuID
     * @return
     */
    public Response getUnstackedBatches(String warehouseID, int skuID) {
        String endpoint = String.format(DynamicLocationWarehouse.UNSTACKED_BATCHES_RESOURCE, warehouseID, skuID);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Authorization", "Bearer " + JWT_TOKEN_DL.KOMAL.getValue());
        requestHeader.put("x-tracker", UUID.randomUUID().toString());
        requestHeader.put("Content-Type", contentType);

        report.log("Calling Unstacked Batches API. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given()
                .spec(requestSpecification)
                .headers(requestHeader)
                .log().all()
                .when()
                .get(endpoint)
                .then()
                .log().all()
                .extract().response();
        report.log("Unstacked Batches API Response : " + response.prettyPrint(), true);
        validateResponse(response);

        return response;
    }

    private void validateResponse(Response response) {
        Assert.assertTrue(response.getStatusCode() == 200, response.getStatusLine() + "\n");
        String status = response.getStatusLine();
        Assert.assertTrue(status.contains("0") || status.contains("OK"), response.getStatusLine() + "\n");
    }
}
