package bbnow.schema_validation.listing_assembler;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.Helper;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ListingAssembler extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for listing assembler api.", slug = "shorlist api")
    @Test(groups = {"bbnow" , "regression", "bbnow-payments", "bbnow-schema-validation","testing-bb2"}, dataProvider = "short-list-parameters")
    public void ListingAssemblerApiTest(String type) {
        AutomationReport report = getInitializedReport(this.getClass(), "Type: " + type, false, String.class);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));

        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[1].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[1].search_term2");
        String[] searchTerms = {searchTerm1,searchTerm2};

        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");
        String entryContext = Config.bbnowConfig.getString("entry_context");
        int entryContextId = Config.bbnowConfig.getInt("entry_context_id");


        BigBasketApp app = new BigBasketApp(report).getAppDataDynamic()
                .login(creds[0])
                .getAppDataDynamic()
                .setEntryContext(entryContext, entryContextId)
                .setCustomDeliveryAddress(areaName);
        for (String s : searchTerms) {
            JsonObject result = app.listProduct(s,"ps");
            try {
                String availableSku = Helper.getAvailableProductFromListingApiResponse(result);
                app = app.addToCart(availableSku, 1, s);
            } catch (AssertionError error){
                report.log(error.getMessage(),false);
                continue;
            }
        }
        Assert.assertTrue(app.cartCount>=1, "No products could be added to cart.");

        Response response = app.productShortList(type, "all");
        report.log("Product short list api response: " + response.prettyPrint(), true);
    }

    @DataProvider(name = "short-list-parameters")
    private Object[] dataProviderMethod() {
        Object[] obj = new Object[4];
        obj[0] = "ms";
        obj[1] = "dyf";
        obj[2] = "sfl";
        obj[3] = "fp";
        return obj;
    }


}

