package msvc.order.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;


import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class UpdateOrderPaymentStatus extends WebSettings implements Endpoints {

    IReport report;
    private HashMap commonheaders;
    private String xCaller;
    private String xservice;
    private Map<String,String> cookie;
    private RequestSpecification requestSpecification;


    public UpdateOrderPaymentStatus(  HashMap CommonHeaders,Map cookie,String xCaller,String xservice, IReport report ){

        this.commonheaders=CommonHeaders;
        this.xCaller=xCaller;
        this.cookie=cookie;
        this.xservice=xservice;
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }


    public Response updateOrderPaymentStatus(int orderid,double sub_total)
    {
        String endpoint = String.format(Endpoints.UPDATE_PAYMENT_STATUS);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Service",xservice);
        requestHeader.put("Cookie", cookie.toString());
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));

        JsonObject body=new JsonObject();

        ArrayList<Integer> orderlist=new ArrayList<>();
        orderlist.add(orderid);

        body.put("app_version","4.11.0").put("txn_type", "checkout").put("payment_status", "success").put("purchased_txn_list",orderlist).put("bb_txn_id","4a5223beccd0f2e8a276").put("discount","0.0").put("device", "android").put("payment_method", "NB_HDFC").put("tcp_loyalty_points",0).put("payment_amount", sub_total);
        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .headers(commonheaders)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n create potential order response: \n" + response.prettyPrint(),true);

        return response;

    }
}
