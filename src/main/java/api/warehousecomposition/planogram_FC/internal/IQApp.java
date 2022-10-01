package api.warehousecomposition.planogram_FC.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.RestAssuredConfig.config;

public class IQApp extends WebSettings implements Endpoints {
    public static Logger logger = Logger.getLogger(IQApp.class);
    public Cookie cookie;
    private Map<String, String> commonHeader;
    private Map<String, String> loginHeader;
    private RequestSpecification monolithSpec;
    private RequestSpecification msvcSpec;
    private IReport report;

    public IQApp(IReport report) {
        RestAssured.config = config().logConfig(LogConfig.logConfig().enablePrettyPrinting(true));

        this.monolithSpec = new RequestSpecBuilder()
                .setBaseUri(serverName)
                .setConfig(config().logConfig(LogConfig.logConfig().defaultStream(report.getRestAssuredLogger()))
                        .encoderConfig(encoderConfig()
                                .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .build().log().all();
        this.msvcSpec = new RequestSpecBuilder()
                .setBaseUri(msvcServerName)
                .setConfig(config().encoderConfig(encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false)))
                .build().log().all();
        this.report = report;
        this.cookie = new Cookie(new HashMap<>());
        this.commonHeader = new HashMap<>();
        commonHeader.put("X-caller", "IQ-APP");
        commonHeader.put("X-timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        commonHeader.put("X-tracker", UUID.randomUUID().toString());
        commonHeader.put("x-entry-context-id", "102");
        commonHeader.put("x-entry-context", "bb-internal");
    }

    public IQApp iqAppLogin(String userName, String pwd) {
        Response response = this.externalAppLogin(userName, pwd);
        if (Boolean.valueOf(new JsonObject(response.asString()).getBoolean("is_password_expired")) == true) {
            report.log("Password is expired for the user: " + userName + "\nReseting the same", true);
            resetUserPassword(userName, pwd, pwd, pwd);
            response = this.externalAppLogin(userName, pwd);
        }
        Assert.assertTrue(Boolean.valueOf(response.path("mfa_enabled").toString()) == true, "User " + userName + " is not" +
                " mfa enabled");
        Assert.assertTrue(!StringUtils.isEmpty(response.path("otp_token")), "otp_token is not returned from external app login api call" +
                "\n External App Login response: " + response.prettyPrint());
        this.loginHeader = new HashMap<>();
        loginHeader.put("X-OtpToken", new JsonObject(response.asString()).getString("otp_token"));
        return this;
    }

    private Response externalAppLogin(String userName, String pwd) {
        commonHeader.put("X-tracker", UUID.randomUUID().toString());
        String endpoint = String.format(StoreAppLogin.EXTERNAL_APP_LOGIN);
        Map<String, String> formParamMap = new HashMap<>();
        formParamMap.put("username", userName);
        formParamMap.put("password", pwd);

        report.log("Calling external app login api." +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader +
                "\n FormParams: " + formParamMap.toString(), true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .formParams(formParamMap)
                .log().all()
                .when()
                .post(endpoint)
                .then()
                .log().all().extract().response();
        validateApiResponse(response);
        report.log(prettifyAutomationHtml("External App Login response: " + response.prettyPrint()), true);
        this.cookie = new Cookie(response.cookies());
        return response;
    }

    public IQApp mfaAuthenticate(String userName) {
        String otp = AutomationUtilities.getAdminOtp(userName);
        String endpoint = String.format(StoreAppLogin.MFA_AUTHENTICATE);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());
        Map<String, String> formParamMap = new HashMap<>();
        formParamMap.put("username", userName);
        formParamMap.put("mfa_code", otp);

        report.log("Calling mfa authenticate api." +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader +
                "\n FormParams: " + formParamMap.toString(), true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .headers(loginHeader)
                .cookies(this.cookie.allCookies)
                .formParams(formParamMap)
                .log().all()
                .when()
                .post(endpoint)
                .then()
                .log().all().extract().response();
        validateApiResponse(response);
        report.log(prettifyAutomationHtml("mfa authenticate api response: " + response.prettyPrint()), true);

        Assert.assertTrue(!StringUtils.isEmpty(response.path("access_token")), "access_token is not returned from mfa authenticate api call" +
                "\n mfa authenticate api response: " + response.prettyPrint());
        cookie.updateCookie(response.cookies());
        cookie.allCookies.put("BBADMINAUTHTOKEN", new JsonObject(response.asString()).getString("access_token"));
        return this;
    }

    public IQApp resetUserPassword(String userName, String oldPassword, String newPassword, String confirmPassword) {
        commonHeader.put("X-tracker", UUID.randomUUID().toString());
        String endpoint = String.format(StoreAppLogin.RESET_USER_PASSWORD);
        Map<String, String> formParamMap = new HashMap<>();
        formParamMap.put("username", userName);
        formParamMap.put("old_password", oldPassword);
        formParamMap.put("new_password", newPassword);
        formParamMap.put("confirm_password", confirmPassword);

        report.log("Calling reset user password api." +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader +
                "\n FormParams: " + formParamMap.toString(), true);


        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .formParams(formParamMap)
                .log().all()
                .when()
                .post(endpoint)
                .then()
                .log().all().extract().response();
        validateApiResponse(response);
        report.log(prettifyAutomationHtml("Reset user password api response: " + response.prettyPrint()), true);

        Assert.assertTrue(new JsonObject(response.asString()).getBoolean("success")==true,
                "Password reset failed for the user: " + userName + " " +
                        "\n Reset user password api response: " + response.prettyPrint());
        cookie.updateCookie(response.cookies());
        return this;
    }

    public Response grnStackingCreateJobAPI(int fcId, HashMap<String, Object> body) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.GRN_STACKING_CREATE_JOB, fcId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling grn stacking create job api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader +
                "\n Body: " + body, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body)
                .post(endpoint)
                .then().log().all()
                .extract().response();
        report.log("Status code: " + response.getStatusCode() + "\n grn stacking create job api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response stackingAlternateBinAPI(int fcId, int jobId, HashMap<String, Object> body) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.GRN_STACKING_ALTERNATE_BIN, fcId, jobId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling Grn Stacking alternate bin api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader +
                "\n Body: " + body, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body)
                .post(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n grn stacking alternate bin api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public void grnStackingJobAckAPI(int fcId, int jobId, String body) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.GRN_STACKING_ACK, fcId, jobId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling Grn Stacking Ack api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader +
                "\n Body: " + body, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body)
                .put(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n grn stacking ack api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
    }

    public Response grnStackingUnstackedAPI(int fcId, int skuId) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.GRN_STACKING_UNSTACKED_BATCH, fcId, skuId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling Grn UnStacked api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .get(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n grn stacking unstacked api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response stockInfoAPI(int fcId, int skuId) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.STOCK_INFO, fcId, skuId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling stock info api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .get(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n stock info api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response grnStackingCompleteAPI(int fcId, int jobId) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.GRN_STACKING_COMPLETE, fcId, jobId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling grn stacking complete api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .put(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n grn stacking complete api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response pickingJobCreationAPI(int fcId, HashMap<String, Object> body) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.PICKING_CREATE_JOB, fcId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling picking create job api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader +
                "\n Body: " + body, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body)
                .post(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n picking create job api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response jobAssignment(int fcId,HashMap<String, Object> body) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.JOB_ASSIGNMENT, fcId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling job assignment api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader , true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body)
                .post(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n job assignment api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response orderBagLinking(int fcId, HashMap<String, Object> body) {
        String endpoint = String.format(WareHouseCompositionFCPickingPlatform.ORDER_BAG_LINKING, fcId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling order bag linking api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader +
                "\n Body: " + body, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body)
                .post(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n order bag linking api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response pickingJobAckAPI(int fcId, int jobId, String body) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.PICKING_ACK, fcId, jobId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling picking Ack api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader +
                "\n Body: " + body, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body)
                .put(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n picking ack api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response pickingCompleteAPI(int fcId, int jobId) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.PICK_JOB_COMPLETE, fcId, jobId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling picking job complete api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader , true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .post(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n picking complete api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response pickingPlatformCompleteAPI(int fcId, HashMap<String, Object> body) {
        String endpoint = String.format(WareHouseCompositionFCPickingPlatform.PICK_JOB_COMPLETE, fcId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling picking platform job complete api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader+
                "\n Body: " + body , true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body)
                .post(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n picking platform complete api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response updatePickRequestCreatedOnTimeToEarliest(HashMap<String, Object> body, IReport report) {
        String endpoint = String.format(WareHouseCompositionFCPickingPlatformInternal.PERFORM_PICKING_OPERATION);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Updating the Order picking_request created on time to earliest in open orders. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader+
                "\n Body: " + body , true);

        Response response = RestAssured.given()
                .spec(msvcSpec)
                .headers(commonHeader)
                .log().all()
                .when()
                .body(body)
                .post(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n Perform picking operation api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    private void validateApiResponse(Response response) {
        int statusCode = response.statusCode();
        Assert.assertEquals(statusCode, 200, "Incorrect status code. Response: " + response.asString());
    }

}
