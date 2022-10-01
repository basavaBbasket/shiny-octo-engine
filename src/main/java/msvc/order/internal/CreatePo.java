package msvc.order.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class CreatePo extends WebSettings implements Endpoints{


    IReport report;
    private HashMap commonheaders;
    private String xCaller;
    private String channeltype;
    private RequestSpecification requestSpecification;


    public CreatePo(  HashMap CommonHeaders,String channeltype,String xCaller, IReport report ){

        this.commonheaders=CommonHeaders;
        this.xCaller=xCaller;
        this.channeltype=channeltype;
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }


    public Response createPotentialorder(String member_add_id, boolean is_qc)
    {
        String endpoint = String.format(Endpoints.CREATE_PO);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("bb-channel-type",channeltype);

        requestHeader.put("X-TimeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));

        JsonObject body=new JsonObject();
        body.put("member_address_id",member_add_id).put("is_qc",is_qc);

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

    public String getpo(Response response){
        return (String) response.path("p_order_id");
    }
}
