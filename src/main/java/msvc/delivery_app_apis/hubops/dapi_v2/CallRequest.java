package msvc.delivery_app_apis.hubops.dapi_v2;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import com.google.common.collect.ForwardingDeque;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CallRequest extends WebSettings {
    IReport report;
    private String cookie;
    private  String order_id;
    private String xRefreshToken;
    private String Authorization;
    private RequestSpecification requestSpecification;


    public CallRequest( String cookie,String xRefreshToken,String authorization,String order_id, IReport report) {
        this.report = report;
        this.cookie = cookie;
        this.order_id= order_id;
        this.xRefreshToken=xRefreshToken;
        this.Authorization=authorization;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes put request to Call request api.
     * order_id  ,recipient string is set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response callRequest(String expectedResponseSchemaPath,String recipient_string) {
        String endpoint = String.format(Endpoints.CALL_REQUEST,order_id);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Refresh-Token",xRefreshToken);
        requestHeader.put("Authorization",Authorization);


        report.log("Calling callRequest  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);
        JsonObject body= new JsonObject();
        body.put("recipient",recipient_string);
        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .put(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n callRequest response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }


    public Response callRequest(String recipient_string) {
        String endpoint = String.format(Endpoints.CALL_REQUEST,order_id);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Refresh-Token",xRefreshToken);
        requestHeader.put("Authorization",Authorization);


        report.log("Calling callRequest  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);


        JsonObject body= new JsonObject();
        body.put("recipient",recipient_string);
        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .put(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n callRequest response: \n" + response.prettyPrint(),true);

        return response;
    }

    }
