package msvc.basketsvc;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.joda.time.DateTime;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AddListItemToBasket extends WebSettings implements Endpoints{


    IReport report;
    private HashMap commonheaders;
    private RequestSpecification requestSpecification;


    public AddListItemToBasket(  HashMap CommonHeaders, IReport report ){

        this.commonheaders=CommonHeaders;
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }


    public Response addItemToBasket(String skuid, String qty)
    {
        String endpoint = String.format(Endpoints.ADD_ITEM_TO_BASKET);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        String requestbody="items[]=%s_%s";

        Response response = RestAssured.given().spec(requestSpecification)
                .headers(requestHeader)
                .headers(commonheaders)
                .body(String.format(requestbody,skuid,qty))
                .post(msvcServerName + endpoint)
                .then().log().all()
                .extract().response();

        report.log("Status code: " + response.getStatusCode() +"\n Live order tracking response: \n" + response.prettyPrint(),true);
        report.log("Verifying response schema.",true);
        return response;


    }

}
