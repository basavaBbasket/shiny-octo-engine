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

public class CeeCurrent extends WebSettings implements Endpoints {

    IReport report;
    private String cookie;
    private String xRefreshToken;
    private String include;
    private RequestSpecification requestSpecification;
    private String sa_id;
    private String dm_id;


    public CeeCurrent( String cookie, IReport report) {
        this.report = report;
        this.cookie = cookie;
        this.xRefreshToken=xRefreshToken;
        this.include=include;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request to Get LMD Store Metrics api.
     * sa ID and DM id is set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response ceeCurrent(String expectedResponseSchemaPath,String include ,String xRefreshToken) {
        String endpoint = String.format(Endpoints.CEE_CURRENT,include);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("X-Refresh-Token",xRefreshToken);
        requestHeader.put("Authorization","Bearer auth");

        report.log("Calling Get cee Current  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Get ceeCurrent response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    public Response ceeCurrent() {
        String endpoint = String.format(Endpoints.CEE_CURRENT,include);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("X-Refresh-Token",xRefreshToken);
        requestHeader.put("Authorization","Bearer auth");

        report.log("Calling Get cee Current  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response  response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n CEE Curent  response: \n" + response.prettyPrint(),true);

        return response;
    }

    public Response updateCeeCurrent(String expectedResponseSchemaPath,String sa_id,String dm_id) {
        this.sa_id=sa_id;
        this.dm_id=dm_id;
        String endpoint = String.format(Endpoints.UPDATE_CEE_CURRENT);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("Content-Type","application/json");
        JsonObject body= new JsonObject();
        body.put("sa_id",sa_id).put("dm_id",dm_id);

        report.log("Calling Get cee Current  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .patch(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Get ceeCurrent response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    public Response updateCeeCurrent(String sa_id,String dm_id) {
        this.sa_id=sa_id;
        this.dm_id=dm_id;
        String endpoint = String.format(Endpoints.UPDATE_CEE_CURRENT);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("Content-Type","application/json");
        JsonObject body= new JsonObject();
        body.put("sa_id",sa_id).put("dm_id",dm_id);

        report.log("Calling Get cee Current  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response= RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .patch(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n UpdateCeeCurrent response: \n" + response.prettyPrint(),true);


        return response;
    }

}
