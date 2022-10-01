package msvc.deliverybin;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.joda.time.DateTime;


import java.text.SimpleDateFormat;
import java.util.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BinRecomendation extends WebSettings {



    IReport report;
    private String Cookie;
    private  String bb_decoded_uid;
    private RequestSpecification requestSpecification;


    public BinRecomendation(String Cookie,String bb_decoded_uid,  IReport report) {
        this.report = report;

        this.bb_decoded_uid=bb_decoded_uid;
        this.Cookie=Cookie;

        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }


    public Response getBinRecomendation(String expectedResponseSchemaPath, String fcid) {
        String endpoint = String.format(EndPoints.BIN_RECOMENDATION,fcid);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Cookie",Cookie);
        requestHeader.put("x-tracker", UUID.randomUUID().toString());
        requestHeader.put("bb-decoded-uid",bb_decoded_uid);
        requestHeader.put("x-timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));


        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(serverName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Get Bin Recomendation response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }


    public Response getBinRecomendation( String fcid) {
        String endpoint = String.format(EndPoints.BIN_RECOMENDATION,fcid);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Cookie",Cookie);
        requestHeader.put("x-tracker", UUID.randomUUID().toString());
        requestHeader.put("bb-decoded-uid",bb_decoded_uid);
        requestHeader.put("x-timestamp",new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));


        Response response= RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .get(serverName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Get Bin Recomendation response: \n" + response.prettyPrint(),true);
        return response;
    }
}
