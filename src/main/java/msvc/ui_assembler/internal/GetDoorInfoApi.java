package msvc.ui_assembler.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetDoorInfoApi extends WebSettings implements Endpoints {

    IReport report;
    private String XChannel;
    private String xCaller;
    private String xEntryContextId;
    private String xEntryContext;
    private String bb_decoded_vid;
    private String bb_decoded_aid;
    private String cookie;
    private RequestSpecification requestSpecification;

    public GetDoorInfoApi(String XChannel, String xCaller, String xEntryContextId, String xEntryContext,String bb_decoded_aid, String bb_decoded_vid,String cookie, IReport report) {
        this.report = report;
        this.XChannel = XChannel;

        this.xCaller = xCaller;
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;
        this.bb_decoded_vid = bb_decoded_vid;
        this.bb_decoded_aid = bb_decoded_aid;
        this.cookie=cookie;

        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request to getDoorInfoApi.
     *
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response getDoorInfoApi(String expectedResponseSchemaPath,boolean send_door_info, int bb2_enabled) {
        String endpoint = String.format(Endpoints.GET_DOOR_INFO);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Channel", XChannel);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("bb-decoded-vid",bb_decoded_vid);
        requestHeader.put("bb-decoded-aid",bb_decoded_aid);
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Cookie",cookie);

        report.log("Calling getDoorInfoApi api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .formParam("send_door_info",send_door_info)
                .formParam("bb2_enabled",bb2_enabled)
                .get(msvcServerName + endpoint)
                .then();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n  getDoorInfoApi response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }


    /**
     * This method makes GET request to getDoorInfoApi.
     * @return
     */
    public Response getDoorInfoApi() {
        String endpoint = String.format(Endpoints.GET_DOOR_INFO);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Channel", XChannel);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-TimeStamp", String.valueOf(Instant.now().toEpochMilli()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("bb-decoded-vid", bb_decoded_vid);
        requestHeader.put("bb-decoded-aid",bb_decoded_aid);
        requestHeader.put("Content-Type", "application/json");
        requestHeader.put("Cookie",cookie);

        report.log("Calling getDoorInfoApi api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then()
                .extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n  getDoorInfoApi response: \n" + response.prettyPrint(),true);
        return response;
    }

}
