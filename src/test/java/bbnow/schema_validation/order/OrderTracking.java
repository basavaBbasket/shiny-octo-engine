package bbnow.schema_validation.order;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.order.external.OrderTrackingApi;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

/**
 * Test class for schema validation.
 * Service name: OrderSvc
 */
public class OrderTracking extends BaseTest {

    @DescriptionProvider(author = "rakesh", description = "This testcase validates response schema for order tracking api.",slug = "Validate order tracking api")
    @Test(groups = {"bbnow-schema-validation","bbnow","api-schema-validation"})
    public void orderTrackingApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String originContext = "";//todo
        String authToken = "";//todo
        String xCaller = "";//todo
        String xEcId = "9";//todo
        String ec = "bb-now";//todo
        String orderId = "1000097039";//todo - replace with actual order placement

        OrderTrackingApi orderTrackingApi = new OrderTrackingApi(originContext,authToken,xCaller,xEcId,ec,orderId,report);
        orderTrackingApi.getLiveOrderTrackingData("schema//order-svc//order-live-tracking-200.json");
    }
}
