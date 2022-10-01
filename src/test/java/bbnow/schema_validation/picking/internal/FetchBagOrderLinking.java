package bbnow.schema_validation.picking.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.picking.internal.FetchBagOrderLinkingApi;
import msvc.picking.internal.PickingDetailsApi;
import org.testng.annotations.Test;

public class FetchBagOrderLinking extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema",slug ="Fetch bag Order Linking")
    @Test(groups = {"bbnow-payments","bbnow-schema-validation"})

    public void getOrderContainingInfoTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);


        FetchBagOrderLinkingApi fetchBagOrderLinkingApi = new FetchBagOrderLinkingApi(report);
        Response response = fetchBagOrderLinkingApi.getBagOrderLinking("schema//picking//fetch-bag-order-linking-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
    }

}
