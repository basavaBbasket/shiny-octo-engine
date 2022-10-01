package msvc.delivery_app_apis.hubops.dapi_v2;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetConfig extends WebSettings implements Endpoints {
    IReport report;
    private String cookie;
    private String xRefreshToken;
    private String Authorization;
    private RequestSpecification requestSpecification;


    public GetConfig( String cookie,String xRefreshToken,String authorization, IReport report) {
        this.report = report;
        this.cookie = cookie;
        this.Authorization=authorization;
        this.xRefreshToken=xRefreshToken;

        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes Post request to Get Config  api.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response getConfig(String expectedResponseSchemaPath) {
        String endpoint = String.format(Endpoints.GET_CONFIG);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("X-Refresh-Token",xRefreshToken);
        requestHeader.put("Authorization",Authorization);

        report.log("Calling GetConfig  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);


        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .post(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n getConfig response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    /**
     * this function makes a post call to get config api
     * @return
     */
    public Response getConfig() {
        String endpoint = String.format(Endpoints.GET_CONFIG);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("X-Refresh-Token",xRefreshToken);
        requestHeader.put("Authorization",Authorization);

        report.log("Calling GetConfig  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);


       Response response= RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .post(msvcServerName + endpoint)
                .then().log().all()
               .extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n GetConfig response: \n" + response.prettyPrint(),true);
        return response;
    }
}
