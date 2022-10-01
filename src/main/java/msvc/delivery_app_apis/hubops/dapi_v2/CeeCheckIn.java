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

public class CeeCheckIn extends WebSettings implements Endpoints {

    IReport report;
    private String cookie;
    private String latitude;
    private String longitude;
    private String qrcode;
    private String xRefreshToken;
    private String Authorization;
    private RequestSpecification requestSpecification;


    public CeeCheckIn( String cookie,String xRefreshToken,String authorization, IReport report) {
        this.report = report;
        this.cookie = cookie;
        this.xRefreshToken=xRefreshToken;
        this.Authorization=authorization;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes Post request to ceeCheckIn api.
     * long, lat   is set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response ceeCheckIn(String expectedResponseSchemaPath,String latitude, String longitude) {
        this.latitude=latitude;
        this.longitude=longitude;
        String endpoint = String.format(Endpoints.CEE_CHECKIN);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Refresh-Token",xRefreshToken);
        requestHeader.put("Authorization",Authorization);


        report.log("Calling ceeCheckIn  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        JsonObject body= new JsonObject();
        body.put("latitude",latitude).put("longitude",longitude);
        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n ceeCheckIn response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }


    /**
     * This function makes post call to Cee CheckIn api using lot long
     * @param latitude
     * @param longitude
     * @return
     */
    public Response ceeCheckIn(String latitude, String longitude) {
        String endpoint = String.format(Endpoints.CEE_CHECKIN);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("X-Refresh-Token",xRefreshToken);
        requestHeader.put("Authorization",Authorization);


        report.log("Calling ceeCheckIn  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        JsonObject body= new JsonObject();
        body.put("latitude",latitude).put("longitude",longitude);
        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n ceeCheckIn response: \n" + response.prettyPrint(),true);

        return response;
    }


    /**
     * This function makes a post all to Cee CheckIn api using qrcode
     * valiates the response
     * @param expectedResponseSchemaPath
     * @param qrcode
     * @return
     */
    public Response ceeCheckInWithQr(String expectedResponseSchemaPath,String qrcode) {
        this.qrcode=qrcode;
        String endpoint = String.format(Endpoints.CEE_CHECKIN);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("Content-Type","application/json");


        report.log("Calling ceeCheckIn  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        JsonObject body= new JsonObject();
        body.put("qr_code",qrcode);
        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n ceeCheckIn response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }


    /**
     *      * This function makes a post all to Cee CheckIn api using qrcode
     * @param qrcode
     * @return
     */
    public Response ceeCheckInWithQr(String qrcode) {
        this.qrcode=qrcode;
        String endpoint = String.format(Endpoints.CEE_CHECKIN);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("Content-Type","application/json");


        report.log("Calling ceeCheckIn  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        JsonObject body= new JsonObject();
        body.put("qr_code",qrcode);
        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n ceeCheckIn response: \n" + response.prettyPrint(),true);
        return response;
    }
}
