package msvc.eta.admin;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class StoreStatus extends WebSettings implements Endpoints {


    IReport report;
    private String cookie;
    private String xChannel;
    private String xCaller;
    private String xEntryContextId;
    private String xEntryContext;
    private RequestSpecification requestSpecification;

    public StoreStatus( String xCaller, String xEntryContextId, String xEntryContext,String xChannel,String cookie, IReport report) {
        this.report = report;
        this.cookie = cookie;
        this.xCaller = xCaller;
        this.xChannel=xChannel;
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;


        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request to Get Store Eta api.
     * sa ID is set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response getStoreStatus(String expectedResponseSchemaPath) {
        String endpoint = String.format(Endpoints.STORE_STATUS);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("X-Channel",xChannel);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller",xCaller);
        requestHeader.put("X-Entry-Context-Id",xEntryContextId);
        requestHeader.put("X-Entry-Context",xEntryContext);


        report.log("Calling Get Store Status  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .post(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Get Store Status response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }



    public Response getStoreStatus() {
        String endpoint = String.format(Endpoints.STORE_STATUS);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("X-Channel", xChannel);
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);


        report.log("Calling Get Store Status  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);


        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .post(serverName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Store Status  response: " + response.prettyPrint(), true);
        return  response;
    }
}
