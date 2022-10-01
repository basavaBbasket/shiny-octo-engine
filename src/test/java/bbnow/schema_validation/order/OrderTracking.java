package bbnow.schema_validation.order;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.order.external.GetOrderDetailsApi;
import msvc.order.external.InitiateCeeCallApi;
import msvc.order.external.OrderTrackingApi;
import org.testng.annotations.Test;

/**
 * Test class for schema validation.
 * Service name: OrderSvc
 */
public class OrderTracking extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for order tracking api.", slug = "Validate order tracking api")
    @Test(groups = {"bbnow", "regression", "bbnow-schema-validation", "api-schema-validation"})
    public void orderTrackingApiTest() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        String xservice = "123";//todo
        String xCaller = "123";//todo
        String xEcId = "10";//todo
        String ec = "bbnow";//todo

        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[1].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[1].search_term2");
        String[] searchTerms = {searchTerm1, searchTerm2};
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");

        String orderId = OrderPlacement.placeBBNowOrder("bbnow", 10, creds[0], areaName, 1, "ps", searchTerms, true, false, report);

        OrderTrackingApi orderTrackingApi = new OrderTrackingApi(xservice, xCaller, xEcId, ec, orderId, report);
        orderTrackingApi.getLiveOrderTrackingData("schema//order-svc//order-live-tracking-200.json");
    }


    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for getOrderDetails api.", slug = "Validate getOrderDetails api")
    @Test(groups = {"bbnow-schema-validation", "api-schema-validation", "bbnow", "regression"})
    public void orderDetailsTest() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));

        String xservice = "123";//todo
        String xCaller = "123";//todo
        String xEcId = "10";//todo
        String ec = "bbnow";//todo

        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[1].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[1].search_term2");
        String[] searchTerms = {searchTerm1, searchTerm2};
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");

        String orderId = OrderPlacement.placeBBNowOrder("bbnow", 10, creds[0], areaName, 1, "ps",searchTerms, true, false, report);

        GetOrderDetailsApi getOrderDetailsApi = new GetOrderDetailsApi(xservice, xCaller, xEcId, ec, orderId, report);
        getOrderDetailsApi.getOrderDetails("schema//order-svc//order-details-200.json");
    }


    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for initiateCeeCall api.", slug = "Validate initiateCeeCall api")
    @Test(groups = {"bbnow-schema-validation", "api-schema-validation"})
    public void initiateCeeCallApiTest() {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        String xService = "123";//todo
        String xCaller = "123";//todo
        String xEcId = "10";//todo
        String ec = "bbnow";//todo
        String orderId = "1000699705";//todo - replace with actual order placement

        InitiateCeeCallApi initiateCeeCallApi = new InitiateCeeCallApi(xService, xCaller, xEcId, ec, orderId, report);
        Response response = initiateCeeCallApi.initiateCeeCallApi("schema//order-svc//cee-call-200.json");
        report.log("Status code: " + response.getStatusCode(), true);
        report.log("Cee Attributes For OrderIds  response: " + response.prettyPrint(), true);
    }
}
