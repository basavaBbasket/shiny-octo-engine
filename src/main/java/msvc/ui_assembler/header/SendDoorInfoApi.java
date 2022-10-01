package msvc.ui_assembler.header;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
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
    private String bb_decoded_uid;
    private String cookie;
    private String m_id;
    private RequestSpecification requestSpecification;

    public SendDoorInfoApi(Map<String,String> cookie,String XChannel,String m_id,String xCaller, String xEntryContextId, String xEntryContext, IReport report) {
        this.cookie= String.valueOf(cookie);
        this.report = report;
        this.xCaller = xCaller;
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;
        this.m_id=m_id;
        this.XChannel=XChannel;
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
        String endpoint = String.format(Endpoints.SEND_DOOR_INFO,send_door_info);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Channel", XChannel);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-TimeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("bb-decoded-mid",m_id);
        requestHeader.put("Content-Type", "application/json");

        report.log("Calling sendDoorInfoApi api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .cookie(cookie)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Live sendDoorInfoApi response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
      // Assert.assertEquals(response.getStatusCode(),200,"Test cases is failing as asserting is failed");
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
    public Response sendDoorInfoApi(String send_door_info) {
        BigBasketApp app = new BigBasketApp(report);
        String endpoint = String.format(Endpoints.SEND_DOOR_INFO,send_door_info);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Channel", XChannel);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-TimeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("bb-decoded-mid",m_id);
        requestHeader.put("Content-Type", "application/json");

        report.log("Calling sendDoorInfoApi api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .cookie(cookie)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n Live sendDoorInfoApi response: \n" + response.prettyPrint(),true);
        return response;
    }
}
