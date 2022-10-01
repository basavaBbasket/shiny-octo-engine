package bbexpress.testcases.cart;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.api.bbdaily.RequestAPIs;
import com.bigbasket.automation.mapi.mapi_4_1_0.CartMethods;
import com.bigbasket.automation.mapi.mapi_4_1_0.ProductSearch;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import msvc.basketsvc.Endpoints;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PerformCartServices extends BaseTest {
    double total_weight = 0;

    @DescriptionProvider(author = "Shruti", description = "This testcase validates response for Cart API when item is added, deleted, updated from cart.", slug = "Validate Response when items are added,deleted and updated in cart")
    @Test(groups = {"bb-express"})

    public void addBulkItemToCartTest() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String entrycontextid = Config.bbexpressConfig.getString("entry_context_id");
        String entrycontext = Config.bbexpressConfig.getString("entry_context");
        BigBasketApp app = new BigBasketApp(report);
        String areaname = Config.bbexpressConfig.getString("bbexpress_stores[1].area");
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbexpressConfig.getString("bbexpress_stores[1].member_sheet_name"));
        List<String> availableSkus = ProductSearch.getAvailableProductFromSearch(creds[0], "ps", "rice", areaname, entrycontext, Integer.parseInt(entrycontextid), report);
        CartMethods.emptyCart(creds[0], report);
        report.log("Cart Emptied", true);
        String member_id = AutomationUtilities.getMemberIDForGivenID(creds[0], report);
        String query = "select total_weight from cart_cart where member_id=" + member_id + ";";
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(AutomationUtilities.getCartDB(), query, report));
        String cartweight = jsonObject.getJSONArray("rows").getJSONObject(0).getString("total_weight");
        report.log("When cart is empty cart total weight is: " + cartweight, true);
        if (((availableSkus != null) && !availableSkus.isEmpty()))
            app.addToCart(availableSkus.get(0), member_id, entrycontext, entrycontextid);
        while (checkAddCartResponse(app.response, report))
            app.addToCart(availableSkus.get(0), member_id, entrycontext, entrycontextid);
        checkCart(member_id, report);
        app.removeItemFromCart(availableSkus.get(0), member_id, entrycontextid, entrycontext);
        app.bulkAddInCart(availableSkus.get(0), 3, member_id, entrycontextid, entrycontext);
        app.emptyCart();

    }

    public boolean checkAddCartResponse(Response response, AutomationReport report) {
        boolean flag = true;
        try {
            JSONObject jsonObject = new JSONObject(response.asString());
            if (jsonObject.get("errType").toString().equalsIgnoreCase("ProdAddQtyExceeded")) {
                report.log(jsonObject.get("message").toString(), true);
                flag = false;
            }
        } catch (Exception e) {
        }
        return flag;
    }

    public void checkCart(String m_id, AutomationReport report) {

        String endpoint = String.format(Endpoints.CHECK_BASKET);
        String entrycontextid = Config.bbexpressConfig.getString("entry_context_id");
        String entrycontext = Config.bbexpressConfig.getString("entry_context");
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
        if (statusCode == 200) {
            String jsonString = response.body().asString();
            JSONArray json = new JSONArray(jsonString);
            JSONObject jsonObject = json.getJSONObject(0);
            total_weight = jsonObject.getDouble("item_total_weight");
            report.log("Cart weight after adding items is: " + total_weight, true);
        }


    }

}
