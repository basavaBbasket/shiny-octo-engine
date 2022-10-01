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
import java.util.PrimitiveIterator;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CeeLocationData extends WebSettings implements Endpoints{

    IReport report;
    private String cookie;
    private String xRefreshToken;
    private String Authorization;
    private String latitude;
    private String longitude;
    private RequestSpecification requestSpecification;


    public CeeLocationData( String cookie,String xRefreshToken,String authorization, IReport report) {
        this.report = report;
        this.cookie = cookie;
        this.Authorization=authorization;
        this.xRefreshToken=xRefreshToken;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes Post request to CeeLocationData  api.
     * This will validate response schema with the given schema file.
     *lat log is set during object creation
     * @return response
     */
    public Response ceeLocationData(String expectedResponseSchemaPath,String latitude,String longitute) {
        this.latitude=latitude;
        this.longitude=longitute;
        String endpoint = String.format(Endpoints.CEE_LOCATION_DATA);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("X-Refresh-Token",xRefreshToken);
        requestHeader.put("Authorization",Authorization);

        report.log("Calling CeeLocationData  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        JsonObject body =new JsonObject();
        JsonObject payload=new JsonObject();
        JsonObject latlong=new JsonObject();

        latlong.put("lat",latitude).put("lng",longitute);

        payload.put("location",latlong);
        body.put("payload",payload);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n CeeLocationData response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }


    /**
     *  This method makes Post request to CeeLocationData  api.
     *  This will validate response schema with the given schema file.
     *  lat log is set during object creation
     * @param latitude
     * @param longitute
     * @return
     */
    public Response ceeLocationData(String latitude,String longitute) {
        this.latitude = latitude;
        this.longitude = longitute;
        String endpoint = String.format(Endpoints.CEE_LOCATION_DATA);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);
        requestHeader.put("X-Refresh-Token", xRefreshToken);
        requestHeader.put("Authorization", Authorization);

        report.log("Calling CeeLocationData  api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        JsonObject body = new JsonObject();
        JsonObject payload = new JsonObject();
        JsonObject latlong = new JsonObject();

        latlong.put("lat", latitude).put("lng", longitute);

        payload.put("location", latlong);
        body.put("payload", payload);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n CeeLocationData response: \n" + response.prettyPrint(),true);

        return response;
    }
}
