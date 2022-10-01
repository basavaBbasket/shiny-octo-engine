package api.mapi;

import framework.Settings;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class Members extends Settings implements MapiRoutes {

    protected static RequestSpecification reqSpecc = new RequestSpecBuilder().setBaseUri(serverName).build();

    public Cookies registerDevice() {
        Response response = given().spec(reqSpecc).contentType(ContentType.JSON)
                .formParam("device_id", config.getProperty("device_id"))
                .formParam("city_id", config.getProperty("default_city"))
                .formParam("properties", config.getProperty("device_properties"))
                .post(MemberRoutes.REGISTER_DEVICE).then().log().ifError().and().extract().response();

        Cookies registerDeviceCookie = response.getDetailedCookies();
        return registerDeviceCookie;
    }
}
