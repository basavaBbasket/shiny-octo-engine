package msvc.basketsvc;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.api.bbdaily.RequestAPIs;
import com.bigbasket.automation.mapi.mapi_4_1_0.CartMethods;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
import com.bigbasket.automation.mapi.mapi_4_1_0.ProductListing;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerformCartServices extends WebSettings {

    private String entrycontextid;
    private String entrycontext;
    IReport report;
    private RequestSpecification requestSpecification;
    double total_weight=0;
    boolean flag= true;



    public PerformCartServices(String entrycontextid, String entryContext, IReport report) {

        this.report = report;
        this.entrycontextid = entrycontextid;
        this.entrycontext = entryContext;
        this.requestSpecification = getSimpleRequestSpecification(msvcServerName, this.report);
    }

    /**
     * Fetchs the memberid for given ID
     * @param id User id (email/mobile)
     * @param report
     * @return
     */
    public static String getMemberIDForGivenID(String id,IReport report){
        String identifierName = id.contains("@") ? "email" : "mobile_no";
        String query = "select id from member_member where "+identifierName+"=\""+id+"\";";
        report.log("Fetching the memberID with query \n " + query, true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeDatabaseQuery(serverName, query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No entry returned with the query: " + query);
        }
        String memberID = String.valueOf(jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id"));
        report.log("MemberID  "+memberID+" linked with  "+identifierName+" "+id, true);
        return memberID;
    }
    public void cartServices()
    {
        BigBasketApp app= new BigBasketApp(report);
        double cartWeight=0;
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[0].member_sheet_name"));
        List<String> availableSkus= ProductListing.getAvailableProductFromSearch(creds[0],"rice","seegehalli","bbnow",10,report);
        CartMethods.emptyCart(creds[0],report);
        report.log("Cart Emptied", true);
        String member_id= getMemberIDForGivenID(creds[0],report);
        String query = "select total_weight from cart_cart where member_id="+member_id+";";
        JSONObject jsonObject=new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getCartDB(), query,report));
        String cartweight = jsonObject.getJSONArray("rows").getJSONObject(0).getString("total_weight");
        report.log("When cart is empty cart total weight is: "+cartweight, true);
        if(((availableSkus != null) && !availableSkus.isEmpty()))
            app.addToCart(availableSkus.get(0),1);
            while(checkAddCartResponse(app.response))
                app.addToCart(availableSkus.get(0),1);
                checkCart(member_id);
       app.removeItemFromCart(availableSkus.get(0));
       app.bulkAddInCart(availableSkus.get(0),3);
       app.emptyCart();

   }
   public boolean checkAddCartResponse(Response response)
   {
       boolean flag= true;
       try{
       JSONObject jsonObject = new JSONObject(response.asString());
       if(jsonObject.get("errType").toString().equalsIgnoreCase("ProdAddQtyExceeded"))
       {
           report.log(jsonObject.get("message").toString(), true);
           flag =false;
       }}
       catch(Exception e){}
       return flag;
   }

    public void checkCart(String m_id) {

        String endpoint = String.format(Endpoints.CHECK_BASKET);
        RequestSpecification requestSpecification = getSimpleRequestSpecification(msvcServerName, report);
        Map<String, String> requestHeader = new HashMap<>();
        requestHeader.put("X-Entry-Context-Id", entrycontextid);
        requestHeader.put("X-Entry-Context", entrycontext);
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("member_id", m_id);
        paramsMap.put("city_id", "1");
        paramsMap.put("cart_type_id", "1");
        paramsMap.put("total_weight_needed", "1");
        RequestAPIs request = new RequestAPIs(msvcServerName, endpoint);
        Response response = request.getRequestAPIResponse(requestHeader, paramsMap);
        int statusCode = response.getStatusCode();
        if(statusCode==200)
        {
            String jsonString = response.body().asString();
            JSONArray json = new JSONArray(jsonString);
            JSONObject jsonObject = json.getJSONObject(0);
            total_weight = jsonObject.getDouble("item_total_weight");
            report.log("Cart weight after adding items is: " + total_weight, true);
        }


        }


}
