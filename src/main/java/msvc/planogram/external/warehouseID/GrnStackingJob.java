package msvc.planogram.external.warehouseID;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import api.warehousecomposition.planogram_FC.internal.Endpoints;
import utility.dataset.JWT_TOKEN_DL;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class GrnStackingJob extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;

    public GrnStackingJob(IReport report)
    {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes post request to grn stacking job api.
     * stacking job will be created with the provided parameters.
     * @param warehouseID
     * @param userId
     * @param userName
     * @param batchID
     * @param totalQty
     * @param numOfContainers
     * @param looseQty
     * @param skipValidation
     * @return
     */
    public String callGrnStackingJobApi(String warehouseID, String userId, String userName, int batchID, int totalQty, int numOfContainers, int looseQty,boolean skipValidation) {
        String endpoint = String.format(DynamicLocationWarehouse.GRN_STACKING_JOB_RESOURCE, warehouseID);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Authorization", "Bearer " + JWT_TOKEN_DL.KOMAL.getValue());
        requestHeader.put("x-tracker", UUID.randomUUID().toString());
        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("username", userName);
        map.put("batch_id", batchID);
        map.put("total_quantity", totalQty);
        map.put("num_of_containers", numOfContainers);
        map.put("loose_quantity", looseQty);

        report.log("Calling GRN stacking job api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString() +
                "\n Body: "+map.toString() , true);

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
        report.log( "GrnStackingJob API response : " + response.prettyPrint(), true);
        if(!skipValidation){
            validateResponse(response);
        }
        return response.asString();
    }

    private void validateResponse(Response response) {
        Assert.assertTrue(response.getStatusCode() == 200, response.asString() + "<br><br><br>");
    }
}
