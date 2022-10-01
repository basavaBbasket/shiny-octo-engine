package bbnow.testcases.cart;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.ProductListing;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import msvc.basketsvc.PerformCartServices;

import java.util.List;

public class PerformCartServicesTest extends BaseTest{
    @DescriptionProvider(author = "Shruti", description = "This testcase validates response for different Cart Services.",slug = "Validate Response when items are added,deleted and updated in cart")
    @Test(groups = {""})
    public void addBulkItemToCartTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String entrycontextid= Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");
        PerformCartServices cartObj=new PerformCartServices(entrycontextid,entrycontext,report);
        cartObj.cartServices();
    }}