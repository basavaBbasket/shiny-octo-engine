package msvc.serviceability;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;

import java.time.Instant;
import java.util.UUID;

public class FindSaS extends WebSettings implements Endpoints{
    IReport report;
    private RequestSpecification requestSpecification;

    public FindSaS(IReport report){
        this.report=report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    public Response findSaS(String entryContext, int entryContextId, int memberAddressId, int pincode, String lat, String lng){
        report.log("Calling find sas api to get SA ID if serviceability is there.",true);
        JsonObject body = new JsonObject()
                .put("member_address_id",memberAddressId)
                .put("pincode",pincode)
                .put("lat",lat)
                .put("lng",lng);
        Response response = RestAssured.given().spec(requestSpecification)
                .header("X-Entry-Context-Id",entryContextId)
                .header("X-Timestamp", Instant.now().toString())
                .header("X-Tracker", UUID.randomUUID().toString())
                .header("X-Entry-Context",entryContext)
                .header("X-Caller","abcd")
                .contentType("application/json")
                .body(body.toString())
                .post(Endpoints.FIND_SAS_V1)
                .then().log().all()
                .extract().response();
        report.log("Find SaS response status code: "+ response.statusCode() + "| Body: " + response.prettyPrint(),true);
        return response;
    }
}
