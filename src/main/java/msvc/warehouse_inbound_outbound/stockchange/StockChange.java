package msvc.warehouse_inbound_outbound.stockchange;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class StockChange extends WebSettings implements Endpoints {

    IReport report;
    private String fc_id;
    private String cookie;
    private int sku_id;
    private int quantity;
    private String locationtype;
    private RequestSpecification requestSpecification;

    /**
     * This api will change the stock of the sku by taking the sku_id and quantity in a given fc_id
     * @param fc_id
     * @param sku_id
     * @param quantity
     * @param locationtype
     * @param report
     */
    public StockChange (String fc_id, int sku_id , int quantity,String locationtype,String cookie, IReport report) {
        this.report = report;
        this.fc_id=fc_id;
        this.sku_id=sku_id;
        this.quantity=quantity;
        this.locationtype=locationtype;
        this.cookie=cookie;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);

    }
    /**
     * This function verifies the response code fails if the response code is other than 200
     * @return
     */
    public Response stockChange() {
        String endpoint = String.format(StockChange.STOCKCHANGE_ENDPOINT,fc_id);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("Content-type","application/json");
        requestHeader.put("Cookie",cookie);

        JsonObject body= new JsonObject();
        JsonArray stock_change_info= new JsonArray();
        JsonObject skuinfo= new JsonObject();
        skuinfo.put("sku_id",sku_id).put("quantity",quantity);
        stock_change_info.add(skuinfo);
        body.put("stock_change_info",stock_change_info).put("created_by","navneet").put("location_type",locationtype);
        report.log("Calling Stock Change api . " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(response.getStatusCode(),200,"Status code is not matching ");
        report.log("Stock Change api: " + response.prettyPrint(),true);
        return response;
    }
}

