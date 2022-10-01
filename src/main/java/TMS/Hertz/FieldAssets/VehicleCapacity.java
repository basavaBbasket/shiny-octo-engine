package TMS.Hertz.FieldAssets;

import TMS.configAndUtilities.Config;
import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;
import org.junit.Assert;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VehicleCapacity extends WebSettings implements  EndPoints {
    IReport report;
    private String xCaller;
    private String xEntryContext;
    private String xEntryContextId;

    private String xTenantId;
    private String xProject;
    private String xUserId;
    private RequestSpecification requestSpecification;

    public VehicleCapacity(IReport report, String xCaller, String xEntryContext, String xEntryContextId, String xTenantId, String xProject, String xUserId) {
        this.report = report;
        this.xCaller = xCaller;
        this.xUserId = xUserId;
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;

        this.xTenantId = xTenantId;
        this.xProject = xProject;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);

    }


    public Response vehiclecapacityupload(int sa_id, int dm_id, int slot_definition_id,String slot_data, String vehicle_type_name, int base_breakup, int extra_breakup ) throws IOException {


        String endpoint = String.format(EndPoints.VEHICLE_CAPACITY);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("X-Timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Tenant-Id", xTenantId);
        requestHeader.put("X-Project", xProject);
        requestHeader.put("X-User-Id", xUserId);
        //requestHeader.put("Host","hqa-svc.bigbasket.com");
        requestHeader.put("Content-Type", "application/json");


        JsonObject body = new JsonObject()
                .put("sa_id", sa_id)
                .put("dm_id", dm_id)
                .put("slot_definition_id", slot_definition_id)
                .put("slot_date", slot_data)
                .put("vehicle_type_name",vehicle_type_name)
                .put("base_breakup", base_breakup)
                .put("extra_breakup", extra_breakup);

        report.log("Updating actions details", true);
        report.log("Calling Update Actions api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString() +
                "\n body: " + body.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .log().all()
                .when()
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        Assert.assertEquals(response.getStatusCode() /*actual value*/, 200 /*expected value*/);
        report.log("Status code: " + response.getStatusCode() + "\n Response: \n" + response.prettyPrint(), true);

        return response;

    }



}
