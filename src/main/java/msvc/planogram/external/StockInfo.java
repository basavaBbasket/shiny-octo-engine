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
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class StockInfo extends WebSettings implements Endpoints{
    IReport report;
    private RequestSpecification requestSpecification;

    public StockInfo(IReport report) {
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     *  This method makes get request to stock info api.
     *  It will list down all the details of the location where the product currently exists
     * @param skuID
     * @param warehouseID
     * @param skipValidation
     * @return
     */
    public Response callStockInfoApi(String skuID, String warehouseID, boolean skipValidation) {
        String endpoint = String.format(DynamicLocationWarehouse.STOCK_INFO_RESOURCE, warehouseID, skuID);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Authorization", "Bearer " + JWT_TOKEN_DL.KOMAL.getValue());
        requestHeader.put("x-tracker", UUID.randomUUID().toString());

        report.log("Calling stock info API: " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(),true);

        Response response = RestAssured.given()
                .log()
                .all()
                .spec(requestSpecification)
                .headers(requestHeader)
                .get(endpoint)
                .then()
                .log()
                .all()
                .extract().response();
        report.log( "stock info API response : " + response.prettyPrint(), true);
        if(!skipValidation){
            validateResponse(response);
        }
        return response;
    }

    private void validateResponse(Response response) {
        Assert.assertTrue(response.getStatusCode() == 200, response.asString() + "<br><br><br>");
    }
}
