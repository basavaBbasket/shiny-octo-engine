package bbnow.schema_validation.picking.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.picking.internal.PickingDetailsApi;
import org.testng.annotations.Test;

public class PickingDetails extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema",slug ="Picking: Order container Info")
    @Test(groups = {"check_bbnow","bbnow" , "regression","bbnow-payments","bbnow-schema-validation","unstable"},enabled = false)

    public void getOrderContainingInfoTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        //todo order placement, picking complete , then call ordercontainer info api to verify response
        PickingDetailsApi pickingDetailsApi = new PickingDetailsApi(report);
        Response response = pickingDetailsApi.getOrderContainingInfo("schema//picking//picking-details-order-containg-info-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
    }

}
