package msvc.delivery_app_apis.hubops.dapi_v2;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;


import java.util.*;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class VerifyLogin extends WebSettings implements Endpoints {

    IReport report;
    private String cookie;
    private String cee_id;
    private String device_id;
    private String password;
    private String app_version;
    private String registration_id;
    private RequestSpecification requestSpecification;


    public VerifyLogin( String cee_id, String device_id, String password,String app_version,String registration_id,String cookie, IReport report) {
        this.report = report;
        this.cookie = cookie;
        this.cee_id=cee_id;
        this.device_id=device_id;
        this.password=password;
        this.app_version=app_version;
        this.registration_id=registration_id;

        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes Post request to VerifyLogin api.
     * password,cee_id,device_id,app_version, registration id  is set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response verifyLogin(String expectedResponseSchemaPath) {
        String endpoint = String.format(Endpoints.VERIFY_LOGIN);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("Content-Type","application/json");


        report.log("Calling VerifyLogin  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        JsonObject body= new JsonObject();
        body.put("cee_id",cee_id).put("device_id",device_id).put("password",password).put("app_version",app_version).put("registration_id",registration_id);
        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n VerifyLogin response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    public Response verifyLogin() {
        String endpoint = String.format(Endpoints.VERIFY_LOGIN);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("Content-Type","application/json");


        report.log("Calling VerifyLogin  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        JsonObject body= new JsonObject();
        body.put("cee_id",cee_id).put("device_id",device_id).put("password",password).put("app_version",app_version).put("registration_id",registration_id);
        Response  response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n VerifyLogin response: \n" + response.prettyPrint(),true);

        return response;
    }

}
