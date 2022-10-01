package msvc.warehouse_inbound_outbound.fcreturn.internal;

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
import java.util.*;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;


public class FcReturnDetails extends WebSettings {
    IReport report;
    private String xCaller;
    private String UserId;
    private RequestSpecification requestSpecification;
    private String fc_id;
    private int sku_id ;
    private int quantity;
    private double cp;
    private double mrp;
    private String Cookie;

    /**
     * This function triggers the fc return event when the following parameters are given it will raise the event for the sku with the specified quantity
     * @param xCaller
     * @param UserId
     * @param fc_id
     * @param sku_id
     * @param quantity
     * @param cp
     * @param mrp
     * @param report
     */
    public FcReturnDetails ( String xCaller, String UserId,String fc_id,int sku_id ,int quantity,double cp, double mrp,String Cookie, IReport report) {
        this.report = report;
        this.xCaller = xCaller;
        this.UserId= UserId;
        this.fc_id=fc_id;
        this.sku_id=sku_id;
        this.quantity=quantity;
        this.cp=cp;
        this.mrp=mrp;
        this.Cookie=Cookie;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);

    }

    /**
     * This function verifies the response code fails if the response code is other than 200
     * @return
     */
    public Response fcReturn() {
        String endpoint = String.format(Endpoints.FcReturn.FC_ENDPOINT,fc_id);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-TimeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("Cookie",Cookie);


        JsonObject body= new JsonObject();
        JsonArray fcreturn_sku_info= new JsonArray();
        JsonObject skuinfo= new JsonObject();
        skuinfo.put("sku_id",sku_id).put("quantity",quantity).put("cp",cp).put("mrp",mrp);
        fcreturn_sku_info.add(skuinfo);
        body.put("fcreturn_sku_info",fcreturn_sku_info).put("created_by","5000");
        report.log("Calling Fc Return api . " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        Assert.assertEquals(response.getStatusCode(),200,"Status code is not matching ");
        report.log("Fc Return api response: " + response.prettyPrint(),true);
        return response;
    }
}
