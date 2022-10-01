package msvc.fandv;

import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.vertx.core.json.JsonObject;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CustomerOTPRequest {
    String csurftoken;
    String channel;
    String tracker;
    public CustomerOTPRequest(
                           String csurftoken,
                           String channel,
                           String tracker){
        this.csurftoken = csurftoken;
        this.channel = channel;
        this.tracker = tracker;
    }
    public Response validateOTPRequestResponse(String serverName,String expectedResponseSchemaPath, String identifier, IReport report) {
        String endpoint = String.format(Endpoints.BB_Customer_OTP_Request);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("x-csurftoken",csurftoken);
        requestHeader.put("X-Channel",channel);
        requestHeader.put("X-Tracker",tracker);
        report.log( serverName+ endpoint, true);
        report.log("Calling Customer OTP Request api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);
        JsonObject body= new JsonObject();
        body.put("identifier",identifier);
        ValidatableResponse validatableResponse = RestAssured.given()
                .headers(requestHeader)
                .body(body.toString())
                .post(serverName+ endpoint)
                 .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Customer OTP request response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

}
