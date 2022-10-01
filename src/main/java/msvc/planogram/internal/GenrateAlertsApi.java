package msvc.planogram.internal;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import msvc.wio.internal.Endpoints;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.bigbasket.automation.utilities.Libraries.getSimpleRequestSpecification;

public class GenrateAlertsApi extends WebSettings implements Endpoints {

    IReport report;
    private String xTracker;
    private RequestSpecification requestSpecification;

    public GenrateAlertsApi(String xTracker, IReport report)
    {
        this.xTracker = xTracker;
        this.report = report;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);

    }

    /**
     * API: Genrates all the alerts
     * This method makes POST request to Alert API and validates status code
     */

     public Response postGenrateAlerts(String fc_id)
     {
         String endpoint = String.format(msvc.planogram.internal.Endpoints.GENRATE_ALERTS, fc_id);

         Map<String, String> requestHeader = new HashMap<>();
         requestHeader.put("X-Tracker",xTracker);

         JSONObject requestBody = new JSONObject();
         requestBody.put("alert_type","stacking_delay");

         JSONObject aletInfoJson = new JSONObject();
         aletInfoJson.put("message", "Total 4 skus of GRNed/Auto-TIed are pending for stacking at 2021-11-15 16:07:06");

         JSONObject metaJson = new JSONObject();
         ArrayList<ArrayList<String> > aList =
                 new ArrayList<ArrayList<String> >();

         ArrayList<String> a1 = new ArrayList<String>();
         a1.add("4002");
         a1.add("60");
         a1.add("grn");
         aList.add(a1);

         ArrayList<String> a2 = new ArrayList<String>();
         a2.add("241600");
         a2.add("461");
         a2.add("grn");
         aList.add(a2);

         ArrayList<String> a3 = new ArrayList<String>();
         a3.add("10000199");
         a3.add("310");
         a3.add("grn");
         aList.add(a3);

         ArrayList<String> a4 = new ArrayList<String>();
         a4.add("10000103");
         a4.add("925");
         a4.add("grn");
         aList.add(a4);


         metaJson.put("values", aList);

         ArrayList<String> blist = new ArrayList<String>();
         blist.add("Sku ID");
         blist.add("Quntity");
         blist.add("Source");

         metaJson.put("headers", blist);


         aletInfoJson.put("meta",metaJson);

         aletInfoJson.put("actions", (Object) null);

         requestBody.put("alert_info", aletInfoJson);

         requestBody.put("message", "Total 4 skus of GRNed/Auto-TIed are pending for stacking at 2021-11-15 16:07:06");
         requestBody.put("display_type","Stacking Delay");


         report.log(" Making Post request API " +
                 "\n Endpoint:" + endpoint +
                 "\n Headers: " + requestHeader.toString(), true);



         Response response = RestAssured.given().spec(requestSpecification)
                 .headers(requestHeader)
                 .body(requestBody.toString())
                 .post(msvcServerName + endpoint)
                 .then().log().all()
                 .extract().response();

         report.log("Validate Status Code: " + response.prettyPrint(),true);
         return response;















     }


}
