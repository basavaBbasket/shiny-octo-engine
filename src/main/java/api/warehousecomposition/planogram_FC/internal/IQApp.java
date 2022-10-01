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

        Assert.assertTrue(new JsonObject(response.asString()).getBoolean("success") == true,
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

    public Response stackingAlternateBinAPI(int fcId, int jobId, String body) {
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
      return stockInfoAPI(fcId,skuId,true);
    }

    public Response stockInfoAPI(int fcId, int skuId, boolean logToReport) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.STOCK_INFO, fcId, skuId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        if (logToReport) {
            report.log("Calling stock info api. " +
                    "\n Endpoint:" + endpoint +
                    "\n Headers: " + commonHeader, true);
        }
        System.out.println("Calling stock info api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .get(endpoint)
                .then().log().all()
                .extract().response();
        if (logToReport) {
            report.log("Status code: " + response.getStatusCode() + "\n stock info api: \n" + response.prettyPrint(), true);
        }
        System.out.println("Status code: " + response.getStatusCode() + "\n stock info api: \n" + response.prettyPrint());
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

    public Response jobAssignment(int fcId, HashMap<String, Object> body) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.JOB_ASSIGNMENT, fcId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling job assignment api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader, true);

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

        if (new JsonObject(response.asString()).getJsonArray("picking").getJsonObject(0).getJsonArray("sku_location_info").isEmpty()) {
            int numberOfRetries = 5;
            report.log("Sku location is not returned from job assignment api \n" +
                    "some of the products present in order might not have been mapped in primary bin \n" +
                    "Calling the job assignment api with " + numberOfRetries + " retries", true);

            for (int i = 0; i < numberOfRetries; i++) {
                System.out.println("Calling job assignment api -- count " + (i + 1));
                response = RestAssured.given()
                        .spec(monolithSpec)
                        .headers(commonHeader)
                        .cookies(this.cookie.allCookies)
                        .log().all()
                        .when()
                        .body(body)
                        .post(endpoint)
                        .then().log().all()
                        .extract().response();
                validateApiResponse(response);
                if (!(new JsonObject(response.asString()).getJsonArray("picking").getJsonObject(0).getJsonArray("sku_location_info").isEmpty()))
                    break;
            }
        }
        report.log("Post retrying the job assignment api ", true);
        report.log("Status code: " + response.getStatusCode() + "\n job assignment api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);


        return response;
    }

    public Response orderBagLinking(int fcId, HashMap<String, Object> body, boolean skipValidation) {
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
        if (!skipValidation)
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
                "\n Headers: " + commonHeader, true);

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

        report.log("Status code: " + response.getStatusCode() + "\n picking platform complete api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response updatePickRequestCreatedOnTimeToEarliest(HashMap<String, Object> body, IReport report) {
        String endpoint = String.format(WareHouseCompositionFCPickingPlatformInternal.PERFORM_PICKING_OPERATION);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Updating the Order picking_request created on time to earliest in open orders. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader +
                "\n Body: " + body, true);

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

    public Response writeOffAPI(int fcId, HashMap<String, Object> body) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.WRITE_OFF, fcId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling write off api. " +
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

        report.log("Status code: " + response.getStatusCode() + "\n write off api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response transferInGrnApi(int fcId, String body) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.TRANSFER_IN_GRN, fcId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling TransferIn GRN api. " +
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

        report.log("Status code: " + response.getStatusCode() + "\n Transfer in api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response orderContainerInfo(int orderId) {
        String endpoint = String.format(WareHouseCompositionFCPickingPlatform.ORDER_CONTAINER_INFO, orderId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling order bag linking api. " +
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

        report.log("Status code: " + response.getStatusCode() + "\n order bag linking api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    private void validateApiResponse(Response response) {
        int statusCode = response.statusCode();
        Assert.assertEquals(statusCode, 200, "Incorrect status code. Response: " + response.asString());
    }

    /**
     * curl --location --request GET 'https://uat3.bigbasket.com/warehousecomposition/admin/planogram/v1/fcs/234/delivery/binrecommendation' \
     * --header 'Cookie: BBADMINAUTHTOKEN=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjaGFmZiI6IlFKR0hfeUFON0t6dmRRIiwidWlkIjoxOTY5NTQsInRpbWUiOjE2MzkxMjM2MDMuODY4OTcxfQ.-bpYrq35R9Hsi6ZK2CLRssf8FUl0kHcq7WIfA_Gn6so; _bb_cid=1; _bb_rd=6; _bb_rdt="MzE0MTkyMjA2OA==.0"; _bb_tc=0; _bb_vid="MzkzMzEzNjI0OQ=="; _client_version=2021.10.25.55624; ts="2021-12-01 10:56:45.885"; _bb_aid="MzAwODcxOTQ0OA=="; _bb_cid=1; _bb_hid=638; _bb_rd=6; _bb_rdt="MzEzNDkyMzQ0Ng==.0"; _bb_tc=0; _bb_vid="MzkzMzEwMjEwOQ=="; _client_version=2021.10.25.55624; sessionid=fpf8o8w4hhvjjc4rf449y2n00ycbr1uk; ts="2021-12-01 13:14:43.752"; _bb_tc=0; _bb_rd=6; _bb_rdt="MzE0MTkyMTkxNw==.0"; _bb_vid="MjQ2MjUwNDg5NzM="; _bb_cid=1; _bb_aid="MzAxMzg3MzQyOQ=="; _sp_van_encom_hid=1336; _client_version=2734; _bb_hid=1337; _sp_bike_hid=1334; ts="2021-12-16 12:13:03.540"; _bb_vid="Mzc3MjI1NDY4NQ=="; _bb_cid=1; _bb_tc=0; _bb_rd=6; _sp_van_encom_hid=1722; _bb_hid=1723; _sp_bike_hid=1720; _bb_rdt="MzE0MTkyMTg3OA==.0"; _client_version=2755; ts="2022-01-04 18:26:20.710"; _bb_aid="MzEwMTE2ODY1OQ=="' \
     * --header 'x-tracker: 782b944e-b4ad-4bc4-9c8e-418d38add091' \
     * --header 'bb-decoded-uid: 92' \
     * --header 'x-timestamp: 2021-03-05T11:38:14+00:00'
     *
     * @param fcId
     * @return
     */

    public Response binRecommendationApi(int fcId) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.BIN_RECOMENDATION, fcId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Calling Bin Recommendation Api. " +
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

        report.log("Status code: " + response.getStatusCode() + "\n Bin Recommendation Api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }


    /**
     * curl --location --request POST 'http://qa-svc.bigbasket.com/warehousecomposition/admin/planogram/v1/fcs/213/delivery/orderbinmapping' \
     * --header 'x-entry-context: bb-internal' \
     * --header 'x-entry-context-id: 102' \
     * --header 'x-timestamp: 2021-03-05T11:38:14+00:00' \
     * --header 'x-tracker: 782b944e-b4ad-4bc4-9c8e-418d38add091' \
     * --header 'Content-Type: application/json' \
     * --header 'Cookie: _bb_rdt="MzEzNDkyMzQ0Ng==.0"; _bb_tc=0' \
     * --header 'bb-decoded-uid: 207814' \
     * --data-raw '{
     * "event_type": "binning",
     * "order_id": 1101,
     * "bin_loc": "A-07-A-2",
     * "container_info": [{"id":"1"}, {"id":"2"}]
     * }'
     *
     * @param fcId
     * @return
     */
    public Response orderBinMappingApi(int fcId, String body) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.ORDER_BIN_MAPPING, fcId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());


        report.log("Calling Bin Recommendation Api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader, true);

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .headers(header)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body)
                .post(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n Bin Recommendation Api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response locationDetailUsingBinId(int fcId, int skuId, String binLocation) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.STOCK_MOVEMENT_LOCATION_DETAIL_USING_BINID, fcId, skuId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Fetching source location detail for stock movement using binID Api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .queryParam("bin_loc", binLocation)
                .log().all()
                .when()
                .get(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n source location detail for stock movement using binID Api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response stockMovementCreateJobForBin(int fcId, HashMap<String, Object> body) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.STOCK_MOVEMENT_CREATE_JOB_FOR_BIN, fcId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());


        report.log("Calling Stock Movement Create Job For Bin Api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader +
                "\n Body: " + body, true);

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .headers(header)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body)
                .post(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n Stock Movement Create Job For Bin Api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response stockMovementStackAck(int fcId, int jobId, String body) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.STOCK_MOVEMENT_STACK_ACK, fcId, jobId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());


        report.log("Calling Stock Movement Stack Ack Api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader +
                "\n Body: " + body, true);

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .headers(header)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(body)
                .post(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n Stock Movement Stack Ack Api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response stockMovementComplete(int fcId, int jobId) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.STOCK_MOVEMENT_COMPLETE, fcId, jobId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());


        report.log("Calling Stock Movement Complete Api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader, true);

        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .headers(header)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .put(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n Stock Movement Complete Api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response locationDetailUsingLocationId(int fcId, int skuId, int locationId) {
        String endpoint = String.format(WareHouseCompositionFCPlanogram.STOCK_MOVEMENT_LOCATION_DETAIL_USING_LOCATIONID, fcId, skuId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Fetching source location detail for stock movement using locationId Api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .queryParam("location_id", locationId)
                .log().all()
                .when()
                .get(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n source location detail for stock movement using locationId Api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response planogramAlerts(int fcId, String alertType) {
        String endpoint = String.format(PlanogramInternal.PLANOGRAM_ALERTS, fcId, alertType);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Fetching planogram alerts with Api. " +
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

        report.log("Status code: " + response.getStatusCode() + "\n planogram alerts Api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

    public Response ceeCheckInRequest(String actionType, String date, int id) {
        String endpoint = HubOps.CEE_CHECKIN_REQUEST;
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Acting on cee check in request. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(new JsonObject().put("action", actionType).put("date", date).put("id", id).toString())
                .post(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n cee check in request Api: \n" + response.prettyPrint(), true);
        Assert.assertEquals(response.getStatusCode(), 204, "Incorrect status code. Response: " + response.asString());

        return response;
    }

    public Response completePlanogramJob(int fcId, int jobId) {
        String endpoint = String.format(PlanogramInternal.PLANOGRAM_JOBS, fcId, jobId);
        commonHeader.put("X-tracker", UUID.randomUUID().toString());

        report.log("Completing the planogram job " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + commonHeader, true);

        Response response = RestAssured.given()
                .spec(monolithSpec)
                .headers(commonHeader)
                .cookies(this.cookie.allCookies)
                .log().all()
                .when()
                .body(new JsonObject().put("status", "Completed").toString())
                .patch(endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() + "\n Complete planogram job Api: \n" + response.prettyPrint(), true);
        validateApiResponse(response);
        return response;
    }

}
