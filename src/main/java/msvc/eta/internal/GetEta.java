package msvc.eta.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetEta extends WebSettings implements Endpoints {

    IReport report;
    private String cookie;
    private String sa_id;
    private RequestSpecification requestSpecification;

    public GetEta( String sa_id,String cookie, IReport report) {
        this.report = report;
        this.cookie = cookie;
        this.sa_id=sa_id;

        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This method makes GET request to Get Store Eta api.
     * sa ID is set during object creation.
     * This will validate response schema with the given schema file.
     *
     * @return response
     */
    public Response getStoreEta(String expectedResponseSchemaPath) {
        String endpoint = String.format(Endpoints.GET_ETA,sa_id);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);


        report.log("Calling Get Store ETA api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Get Store Eta response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    /**
     * This method makes GET request to get store eta api.
     * Sa ID is set during object creation.
     *
     * @return response
     */
    public Response getStoreEta() {
        String endpoint = String.format(Endpoints.GET_ETA,sa_id);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("cookie", cookie);


        report.log("Calling Get Store ETA api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(serverName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("getting getStoreEta response: " + response.prettyPrint(),true);
        return response;
    }


}
