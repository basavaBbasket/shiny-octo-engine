package msvc.ui_assembler.header;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class SendDoorInfoApi extends WebSettings implements Endpoints {

    IReport report;
    private String XChannel;
    private String xCaller;
    private String xEntryContextId;
    private String xEntryContext;
    private String cookie;
    private RequestSpecification requestSpecification;

    public SendDoorInfoApi(String XChannel, String xCaller, String xEntryContextId, String xEntryContext,String cookie, IReport report) {
        this.report = report;
        this.XChannel = XChannel;

        this.xCaller = xCaller;
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;
        this.cookie=cookie;

        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request to sendDoorInfoApi .
     * send_door_info is set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response sendDoorInfoApi(String expectedResponseSchemaPath,boolean send_door_info) {
        String endpoint = String.format(Endpoints.SEND_DOOR_INFO);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Channel", XChannel);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
       // requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Timestamp", String.valueOf(Instant.now().toEpochMilli()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Cookie",cookie);

        report.log("Calling sendDoorInfoApi api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .formParam("send_door_info", send_door_info)
                .get(serverName + endpoint)
                .then();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Live sendDoorInfoApi response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    /**
     * This method makes GET request to sendDoorInfoApi .
     *       send_door_info is set during object creation.
     * @param send_door_info
     * @return
     */
    public Response sendDoorInfoApi(boolean send_door_info) {
        String endpoint = String.format(Endpoints.SEND_DOOR_INFO);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Channel", XChannel);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Cookie",cookie);

        report.log("Calling sendDoorInfoApi api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .queryParam("send_door_info", send_door_info)
                .get(serverName + endpoint)
                .then()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n Live sendDoorInfoApi response: \n" + response.prettyPrint(),true);
        return response;
    }
}
