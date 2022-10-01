package TMS.api.OMS;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import msvc.eta.admin.Endpoints;
import org.joda.time.DateTime;
import org.junit.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderDetailsExternalApi extends WebSettings implements Endpoints {

    IReport report;
    private String xCaller;
    private String xEntryContext;
    private String xEntryContextId;
    private String xOriginContext;
    private String xTenantId;
    private String xProject;
    private RequestSpecification requestSpecification;


    public OrderDetailsExternalApi(IReport report, String xCaller, String xEntryContext, String xEntryContextId, String xOriginContext, String xTenantId, String xProject) {
        this.report = report;
        this.xCaller = xCaller;
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;
        this.xOriginContext = xOriginContext;
        this.xTenantId = xTenantId;
        this.xProject = xProject;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);

    }

    public Response getOrderDetails(Integer orderId) {
        String endpoint = String.format(TMS.api.OMS.Endpoints.ORDER_DETAILS_EXTERNAL, orderId);

        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("X-Timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Origin-Context", xOriginContext);
        requestHeader.put("X-Tenant-Id", xTenantId);
        requestHeader.put("X-Project", xProject);
        //requestHeader.put("Host","qa-svc.bigbasket.com");
        requestHeader.put("Content-Type", "application/json");

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .log().all()
                .when()
                .get(msvcServerName+ endpoint)
                .then().log().all()
                .extract().response();

        Assert.assertEquals(response.getStatusCode() /*actual value*/, 200 /*expected value*/);
        report.log("Status code: " + response.getStatusCode() +"\n Response: \n" + response.prettyPrint(),true);

        return response;


    }
}
