package msvc.catalogfc.visibility;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import msvc.catalogfc.Endpoints;
import org.joda.time.DateTime;
import org.testng.Assert;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.text.SimpleDateFormat;
import java.util.*;

public class CatalogVisibilityApi extends WebSettings implements Endpoints {
    IReport report;
    private String xCaller;
    private String xEntryContextId;
    private String xEntryContext;
    private RequestSpecification requestSpecification;

    /**
     * This api will return Supplier information by taking Supplier id as input
     *
     * @param xEntryContextId
     * @param xEntryContext
     * @param report
     */
    public CatalogVisibilityApi(String xEntryContextId, String xEntryContext, IReport report) {
        this.report = report;
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This api will return Supplier information by taking Supplier id as input
     *
     * @param xEntryContextId
     * @param xEntryContext
     */
    public CatalogVisibilityApi(String xEntryContextId, String xEntryContext) {
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;
        this.requestSpecification = new RequestSpecBuilder()
                .setBaseUri(msvcServerName).build().log().all();
    }

    /**
     * fetchs the sku visibility data
     *
     * @param skuIdList
     * @param saIdList
     * @return
     */
    public Response getSkuPathInfo(int[] skuIdList, int[] saIdList) {
        String endpoint = String.format(VisibilityInternal.CATALOG_VISIBILITY);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", "Automation");
        requestHeader.put("X-TimeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("Content-type", "application/json");

        String body = prepareCatalogVisibilityApiBody(skuIdList, saIdList);
        report.log("Calling Get Supplier Information api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString() +
                "\n Body: " + body, true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body)
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Catalog visibiltiy api response: " + response.prettyPrint(), true);
        Assert.assertEquals(response.statusCode(), 200, "Incorrect status code. Response: " + response.asString());
        return response;
    }

    /**
     * fetchs the sku visibility data, use this util while loading the skulist in beforesuite
     * report instance won't be used  here for logging
     *
     * @param skuIdList
     * @param saIdList
     * @return
     */
    public Response getSkuPathInfov1(int[] skuIdList, int[] saIdList) {
        String endpoint = String.format(VisibilityInternal.CATALOG_VISIBILITY);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", "Automation");
        requestHeader.put("X-TimeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("Content-type", "application/json");

        String body = prepareCatalogVisibilityApiBody(skuIdList, saIdList);
        System.out.println("Calling Get Supplier Information api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString() +
                "\n Body: " + body);

        Response response = RestAssured.given()
                .spec(requestSpecification)
                .headers(requestHeader)
                .body(body)
                .post(endpoint)
                .then().log().all()
                .extract().response();
        System.out.println("Catalog visibiltiy api response: " + response.prettyPrint());
        Assert.assertEquals(response.statusCode(), 200, "Incorrect status code. Response: " + response.asString());
        return response;
    }
    /*
    {
     "sku_id": [
         10000137,10000041,10000188
     ],
     "sa_id": [
         83
     ]
 }
     */

    private static String prepareCatalogVisibilityApiBody(int[] skuIdList, int[] saIdList) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder skuArray = Json.createArrayBuilder();

        for (int i = 0; i < skuIdList.length; i++) {
            skuArray.add(skuIdList[i]);
        }
        jsonObjectBuilder.add("sku_id", skuArray);
        JsonArrayBuilder saArray = Json.createArrayBuilder();
        for (int i = 0; i < saIdList.length; i++) {
            saArray.add(saIdList[i]);
        }
        jsonObjectBuilder.add("sa_id", saArray);
        JsonObject jsonObject = jsonObjectBuilder.build();
        return jsonObject.toString();
    }

}
