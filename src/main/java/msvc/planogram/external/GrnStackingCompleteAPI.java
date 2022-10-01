package msvc.planogram.external;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import utility.dataset.JWT_TOKEN_DL;

import java.util.HashMap;
import java.util.Map;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class GrnStackingCompleteAPI extends WebSettings implements Endpoints{
    IReport report;
    private RequestSpecification requestSpecification;

    public GrnStackingCompleteAPI(IReport report)
    {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes put request to stacking complete api.
     * it will mark the stacking job complete
     * @param jobID
     * @param warehouseID
     * @return
     */
    public String callCompleteApi(int jobID, String warehouseID) {
        String endpoint = String.format(DynamicLocationWarehouse.GRN_STACKING_COMPLETE_RESOURCE, warehouseID, jobID);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Authorization", "Bearer " + JWT_TOKEN_DL.KOMAL.getValue());

        report.log("Calling stacking complete API: " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(),true);

                Response response = RestAssured.given()
                .log()
                .all()
                .spec(requestSpecification)
                .put(endpoint)
                .then()
                .log()
                .all()
                .extract().response();

        report.log( "GRN Stacking Complete API response : " + response.prettyPrint(), true);
        validateResponse(response);
        return response.asString();
    }

    private void validateResponse(Response response) {
        Assert.assertTrue(response.getStatusCode() == 200, response.asString() + "<br><br><br>");
    }

}
