package msvc.wio.external;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class TransferInApi extends WebSettings implements Endpoints {
    IReport report;
    private RequestSpecification requestSpecification;
    private String UserId;
    private String xCaller;


    public TransferInApi( String UserId, String xCaller , IReport report){
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);

        this.UserId = UserId;
        this.xCaller = xCaller;
    }

    public Response getTransferInApi( String expectedResponseSchemaPath,String keys,String transferid){
        String endpoint = String.format(Endpoints.TRANSFER_IN,transferid);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));
        requestHeader.put("UserId",UserId);

        report.log("Calling getTransferInApi api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader, true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .formParam("keys", keys)
                .get(msvcServerName + endpoint)
                .then();

        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n  getTransferInApi response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    public Response getTransferInApi( String keys,String transferid){
        String endpoint = String.format(Endpoints.TRANSFER_IN,transferid);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));
        requestHeader.put("UserId",UserId);


        report.log("Calling getTransferInApi api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader, true);

        Response response= RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .formParam("keys", keys)
                .get(serverName + endpoint)
                .then()
                .extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n  getTransferInApi response: \n" + response.prettyPrint(),true);
        return response;
    }

}
