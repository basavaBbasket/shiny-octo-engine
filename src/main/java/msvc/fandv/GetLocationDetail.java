package msvc.fandv;

import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetLocationDetail {
    Map<String, String>  cookie;
    String csurftoken;
    String channel;
    String tracker;
    public GetLocationDetail( Map<String, String>  cookie,
                          String csurftoken,
                          String channel,
                          String tracker){
        this.cookie = cookie;
        this.csurftoken = csurftoken;
        this.channel = channel;
        this.tracker = tracker;
    }
    public Response validateLocationDetail(String serverName,Map<String,String> queryParams,String expectedResponseSchemaPath, IReport report) {
        String endpoint = String.format(Endpoints.BB_Get_Location_Detail);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/json");
        requestHeader.put("x-csurftoken",csurftoken);
        requestHeader.put("X-Channel",channel);
        requestHeader.put("X-Tracker",tracker);

        report.log( serverName + endpoint, true);
        report.log("Calling Get Location Detail api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);
        ValidatableResponse validatableResponse = RestAssured.given()
                .headers(requestHeader).queryParams(queryParams).cookies(cookie)
                .get(serverName+ endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Location Detail response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

}
