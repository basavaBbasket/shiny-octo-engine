package msvc.basketsvc;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.api.bbdaily.RequestAPIs;
import com.bigbasket.automation.mapi.mapi_4_1_0.CartMethods;
import com.bigbasket.automation.mapi.mapi_4_1_0.ProductSearch;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;

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
    public void cartServices()
    {
        BigBasketApp app= new BigBasketApp(report);
        String searchTerm = Config.bbnowConfig.getString("bbnow_stores[1].category_slug1");
        String searchType = Config.bbnowConfig.getString("bbnow_stores[1].search_type2");
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        List<String> availableSkus= ProductSearch.getAvailableProductFromSearch(creds[0],searchType,searchTerm,areaName,entrycontext, Integer.parseInt(entrycontextid),report);
        CartMethods.emptyCart(creds[0],report);
        report.log("Cart Emptied", true);
        String member_id= AutomationUtilities.getMemberIDForGivenID(creds[0],report);
        String query = "select total_weight from cart_cart where member_id="+member_id+";";
        JSONObject jsonObject=new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getCartDB(), query,report));
        String cartweight = jsonObject.getJSONArray("rows").getJSONObject(0).getString("total_weight");
        report.log("When cart is empty cart total weight is: "+cartweight, true);
        if(((availableSkus != null) && !availableSkus.isEmpty()))
        app.addToCart(availableSkus.get(0),member_id,entrycontext,entrycontextid);
        while(checkAddCartResponse(app.response))
        app.addToCart(availableSkus.get(0),member_id,entrycontext,entrycontextid);
        checkCart(member_id);
        app.removeItemFromCart(availableSkus.get(0),member_id,entrycontextid,entrycontext);
        app.bulkAddInCart(availableSkus.get(0),3,member_id,entrycontextid,entrycontext);
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
