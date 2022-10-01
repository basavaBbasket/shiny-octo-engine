package utility.api;

import com.bigbasket.automation.reports.AutomationReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import java.util.Map;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

/**
 *  Its a general class to have the request methods that would be used as part of bb2.0 foundation.
 *  Overload predefined methods for various types of request variants.
 */


public class FnvAPI {

    public static  Response getWithHeaders(String url , Map<String,String> headers, AutomationReport report, String expectedResponseSchemaPath,String apiName){
        report.log("Making get request to "+apiName+" api",true);
        report.log( url, true);
        report.log(apiName  + " api. " +
                "\n Endpoint:" + url +
                "\n Headers: " + headers.toString(), true);
        ValidatableResponse validatableResponse= RestAssured.given()
                .headers(headers)
                .get(url)
                .then().log().all();
        Response response=validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode(),true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    public static  Response getWithHeaderAndCookies(String url , Map<String,String> headers,Map<String,String> cookies, AutomationReport report, String expectedResponseSchemaPath,String apiName) {
        report.log("Making get request to " + apiName + " api", true);
        report.log(url, true);
        report.log(apiName + " api. " +
                "\n Endpoint:" + url +
                "\n Headers: " + headers.toString(), true);
        ValidatableResponse validatableResponse = RestAssured.given()
                .headers(headers).cookies(cookies)
                .get(url)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode(), true);
        return response;
    }
    public static  Response postWithHeaderAndCookies(String url , Map<String,String> headers,Map<String,String> cookies,String requestBody, AutomationReport report, String expectedResponseSchemaPath,String apiName) {
        report.log("Making post request to " + apiName + " api", true);
        report.log(url, true);
        report.log(apiName + " api. " +
                "\n Endpoint:" + url +
                "\n Headers: " + headers.toString(), true);
        report.log(apiName + " api. " +
                "\n Cookies: " + cookies.toString(), true);
        ValidatableResponse validatableResponse = RestAssured.given()
                .headers(headers).cookies(cookies).body(requestBody)
                .post(url)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode(), true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }
    public static  Response postWithHeader(String url , Map<String,String> headers,String requestBody, AutomationReport report, String expectedResponseSchemaPath,String apiName) {
        report.log("Making post request to " + apiName + " api", true);
        report.log(url, true);
        report.log(apiName + " api. " +
                "\n Endpoint:" + url +
                "\n Headers: " + headers.toString(), true);
        ValidatableResponse validatableResponse = RestAssured.given()
                .headers(headers).body(requestBody)
                .post(url)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode(), true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }

    public static  Response getWithHeaderCookiesAndQueryParms(String url , Map<String,String> headers,Map<String,String> cookies,Map<String,String> queryParms, AutomationReport report, String expectedResponseSchemaPath,String apiName) {
        report.log("Making get request to " + apiName + " api", true);
        report.log(url, true);
        report.log(apiName + " api. " +
                "\n Endpoint:" + url +
                "\n Headers: " + headers.toString(), true);
        ValidatableResponse validatableResponse = RestAssured.given()
                .headers(headers).cookies(cookies).queryParams(queryParms)
                .get(url)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode(), true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).log().all()
                .extract().response();
        return response;
    }
    public static  Response postWithHeaderAndCookies(String url , Map<String,String> headers,Map<String,String> cookies,String requestBody, AutomationReport report, String apiName) {
        report.log("Making post request to " + apiName + " api", true);
        report.log(url, true);
        report.log(apiName + " api. " +
                "\n Endpoint:" + url +
                "\n Headers: " + headers.toString(), true);
        report.log(apiName + " api. " +
                "\n Cookies: " + cookies.toString(), true);
        ValidatableResponse validatableResponse = RestAssured.given()
                .headers(headers).cookies(cookies).body(requestBody)
                .post(url)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode(), true);
        return response;
    }

    public static  Response getWithHeaders(String url , Map<String,String> headers, AutomationReport report, String apiName){
        report.log("Making get request to "+apiName+" api",true);
        report.log( url, true);
        report.log(apiName  + " api. " +
                "\n Endpoint:" + url +
                "\n Headers: " + headers.toString(), true);
        ValidatableResponse validatableResponse= RestAssured.given()
                .headers(headers)
                .get(url)
                .then().log().all();
        Response response=validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode(),true);
        return response;
    }
    public static  Response postWithHeader(String url , Map<String,String> headers,String requestBody, AutomationReport report, String apiName) {
        report.log("Making post request to " + apiName + " api", true);
        report.log(url, true);
        report.log(apiName + " api. " +
                "\n Endpoint:" + url +
                "\n Headers: " + headers.toString(), true);
        ValidatableResponse validatableResponse = RestAssured.given()
                .headers(headers).body(requestBody)
                .post(url)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode(), true);
        return response;
    }
}
