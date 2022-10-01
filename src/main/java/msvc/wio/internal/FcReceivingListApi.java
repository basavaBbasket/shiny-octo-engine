package msvc.wio.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import msvc.order.internal.Endpoints;
import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


//Row-3
public class FcReceivingListApi extends WebSettings implements Endpoints {

    IReport report;
    private RequestSpecification requestSpecification;
    private String status;
    private String source_fc_id;
    private String order_type;
    private String order_id;
    private String creation_ts;
    private String UserId;
    private String X_caller;


    public FcReceivingListApi(String UserId , String X_caller , String status,String source_fc_id, String order_type,String order_id,String creation_ts,IReport report)
    {
        this.UserId = UserId;
        this.X_caller = X_caller;
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
        this.status = status;
        this.creation_ts = creation_ts;
        this.source_fc_id = source_fc_id;
        this.order_type = order_type;
        this.order_id = order_id;
    }

    /**
     * API: FC Receiving List --> Gets PO details
     * This method mekes GET request to  FC Receiving List API and validates the schema of response
     * @param expectedResponseSchemaPath schema
     * @param fc_id fc_id
     * @return   response
     */

    public Response getFcReceivingList(String expectedResponseSchemaPath, String fc_id)
    {
        String endpoint = String.format(msvc.wio.internal.Endpoints.FC_RECEIVED_LIST,fc_id);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .urlEncodingEnabled(false)
                .queryParam("status",status)
                .queryParam("creation_ts",creation_ts)
                .queryParam("order_type",order_type)
                .queryParam("order_id",order_id)
                .queryParam("source_fc_id",source_fc_id)
                .header("X-Tracker", UUID.randomUUID().toString())
                .header("X-Timestamp",String.valueOf(Instant.now().toEpochMilli()))
                .header("X-Caller",X_caller)
                .header("UserId",UserId)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n Fc List Response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath))
                .extract().response();
        return response;



    }


    /**
     * API: FC Receiving List --> Gets PO details
     * This method makes GET request to FC Receving List API
     * @return
     */

    public Response getFcReceivingList(String fc_id)
    {
        String endpoint = String.format(msvc.wio.internal.Endpoints.FC_RECEIVED_LIST , fc_id);

        Response response = RestAssured.given().spec(requestSpecification)
                .urlEncodingEnabled(false)
                .queryParam("status",status)
                .queryParam("source_fc_id",source_fc_id)
                .queryParam("order_type",order_type)
                .queryParam("order_id",order_id)
                .queryParam("creation_ts",creation_ts)
                .header("X-Tracker", UUID.randomUUID().toString())
                .header("X-Timestamp",String.valueOf(Instant.now().toEpochMilli()))
                .header("X-Caller",X_caller)
                .header("UserId",UserId)
                .get(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Validate FC List Response: " + response.prettyPrint(),true);
        return response;



    }









}
