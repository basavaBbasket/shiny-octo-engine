package bbnow.schema_validation.planogram.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.internal.CancelledOrderBinListPiApi;
import msvc.planogram.internal.GetAllFCAlertsApi;
import org.testng.annotations.Test;

public class CancelledOrderBinListPi extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for Cancelled Order api.",slug = "Return/Cancelled Order-Bin List PI")
    @Test(groups = {"bbnow-payments","bbnow-schema-validation","bbnow"})
    public void getCancelledOrderBinListTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        CancelledOrderBinListPiApi cancelledOrderBinListPiApi = new CancelledOrderBinListPiApi(report);
        Response response = cancelledOrderBinListPiApi.getCancelledOrderBinList("schema//planogram//cancelled-order-bin-list-pi-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
    }


}
