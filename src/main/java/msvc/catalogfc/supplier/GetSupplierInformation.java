package msvc.catalogfc.supplier;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.catalogfc.Endpoints;
import org.joda.time.DateTime;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class GetSupplierInformation extends WebSettings implements Endpoints {
    IReport report;
    private String xCaller;
    private String xEntryContextId;
    private String xEntryContext;
    private RequestSpecification requestSpecification;

    /**
     * This api will return Supplier information by taking Supplier id as input
     *
     * @param xCaller
     * @param xEntryContextId
     * @param xEntryContext
     * @param report
     */
    public GetSupplierInformation( String xCaller, String xEntryContextId, String xEntryContext, IReport report) {
        this.report = report;
        this.xCaller = xCaller;
        this.xEntryContextId = xEntryContextId;
        this.xEntryContext = xEntryContext;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * This function validates the schema of the response from the suppliers details api
     * @param expectedResponseSchemaPath
     * @param supplierIds
     * @return
     */
    public Response getSupplierDetails(String expectedResponseSchemaPath, ArrayList<Integer> supplierIds,ArrayList<Integer> fcids,ArrayList<Integer> mcids) {
        String endpoint = String.format(Endpoints.Suppliers.GET_SUPPLIER_INFO);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-TimeStamp",  new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("Content-type","application/json");
        JsonObject body= new JsonObject();
        JsonArray supplierIdsArray= new JsonArray();
        JsonArray fcIdsArray= new JsonArray();

        JsonArray mcIdsArray= new JsonArray();
        for(int sid :supplierIds)
            supplierIdsArray.add(sid);
        for(int fcid :fcids)
            fcIdsArray.add(fcid);
        for(int mcid :mcids)
            mcIdsArray.add(mcid);

        body.put("supplier_ids",supplierIdsArray).put("fc_ids",fcIdsArray).put("mc_ids",mcIdsArray);
        report.log("Calling Get Supplier Information api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body.toString())
                .post(msvcServerName + endpoint)
                .then().log().all();
        Response response = validatableResponse.extract().response();
        report.log("Status code: " + response.getStatusCode() +"\n Get Supplier Information response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        validatableResponse.assertThat().body(matchesJsonSchemaInClasspath(expectedResponseSchemaPath)).extract().response();
        return response;
    }


    /**
     * This function check whether api response is 200 with out checking the schema
     * @param supplierIds
     * @return
     * @throws JsonProcessingException
     */
    public Response getSupplierDetails(ArrayList<Integer> supplierIds) throws JsonProcessingException {
        String endpoint = String.format(Endpoints.Suppliers.GET_SUPPLIER_INFO);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Tracker", UUID.randomUUID().toString());
        requestHeader.put("X-Caller", xCaller);
        requestHeader.put("X-TimeStamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate()));
        requestHeader.put("X-Entry-Context-Id", xEntryContextId);
        requestHeader.put("X-Entry-Context", xEntryContext);
        requestHeader.put("Content-type","application/json");
        requestHeader.put("X-Source","123");
        requestHeader.put("X-user","123");
        requestHeader.put("User-Agent","PostmanRuntime/7.26.8");
        requestHeader.put("Accept-Encoding","gzip, deflate, br");

        JsonObject body= new JsonObject();
        JsonArray supplierIdsArray= new JsonArray();
        JsonArray fcIdsArray= new JsonArray();
        JsonArray mcIdsArray= new JsonArray();
        for(int sid :supplierIds)
            supplierIdsArray.add(sid);
        body.put("supplier_ids",supplierIdsArray).put("fc_ids",fcIdsArray).put("mc_ids",mcIdsArray);
        report.log("Calling Get Supplier Information api. " +
                "\n Endpoint:" + endpoint +
                "\n Headers: " + requestHeader.toString(), true);

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .body(body)
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();
        report.log("Live order tracking response: " + response.prettyPrint(),true);
        return response;
    }


}
