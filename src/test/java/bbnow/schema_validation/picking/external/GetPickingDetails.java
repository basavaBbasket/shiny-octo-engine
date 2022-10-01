package bbnow.schema_validation.picking.external;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
//import msvc.order.internal.ValidatePaymentsApi;
import msvc.picking.external.GetPickingDetailsApi;
import org.testng.annotations.Test;

public class GetPickingDetails extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for payments.",slug ="Picking: Get Picking Details")
    @Test(groups = {"bbnow" , "regression","bbnow-payments","bbnow-schema-validation","unstable"})

    public void getPickingDetailsTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String Order_id = "1000097039";

        GetPickingDetailsApi getPickingDetailsApi = new GetPickingDetailsApi(report);
        Response response = getPickingDetailsApi.getPickingDetails("schema//picking//get-picking-details-200.json", Order_id);
        report.log("Status code: " +  response.getStatusCode(),true);
    }



}
