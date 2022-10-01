package msvc.order.external;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CancelOrder extends WebSettings implements Endpoints {


    IReport report;
    private String xCaller;
    private String xEntryContextId;
    private String xEntryContext;
    private RequestSpecification requestSpecification;



    public CancelOrder(  String xEntryContext,String xEntryContextId,String xCaller, IReport report ){

        this.xCaller=xCaller;
        this.xEntryContext=xEntryContext;
        this.xEntryContextId=xEntryContextId;

        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }


    public Response cancelOrder(String order_id)
    {
        String endpoint = String.format(Endpoints.CANCEL_ORDER,order_id);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);
        requestHeader.put("X-TimeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));


        JsonObject body=new JsonObject();
        body.put("reason_id",89).put("user_comment","cancel order").put("copy_cart",false).put("waive_cancellation_charge",true).put("kapture_ticket_creation_data",new JsonObject().put("ss_action","order_cancel").put("l2_id",10));

        Response response = RestAssured.given().spec(requestSpecification)
                .urlEncodingEnabled(false)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n Cancel order response: \n" + response.prettyPrint(),true);
        Assert.assertEquals(response.getStatusCode(),200,"Failed to cancel order");
        return response;
    }

}
