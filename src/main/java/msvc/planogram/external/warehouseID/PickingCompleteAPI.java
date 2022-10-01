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

public class PickingCompleteAPI extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;

    public PickingCompleteAPI(IReport report) {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes post request to picking complete api.
     * it will mark the picking job complete
     * @param jobID
     * @param warehouseID
     * @return
     */
    public String callCompleteApi(int jobID, String warehouseID) {
        String endpoint = String.format(DynamicLocationWarehouse.PICKING_COMPLETE_RESOURCE, warehouseID, jobID);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Authorization", "Bearer " + JWT_TOKEN_DL.KOMAL.getValue());
        requestHeader.put("x-tracker", UUID.randomUUID().toString());

        report.log("Calling Picking complete API: " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given()
                .log()
                .all()
                .spec(requestSpecification)
                .headers(requestHeader)
                .post(endpoint)
                .then()
                .log()
                .all()
                .extract().response();
        report.log("Picking Complete API response : " + response.prettyPrint(), true);
        validateResponse(response);
        return response.asString();
    }

    private void validateResponse(Response response) {
        Assert.assertTrue(response.getStatusCode() == 200, response.asString() + "<br><br><br>");
    }

}
