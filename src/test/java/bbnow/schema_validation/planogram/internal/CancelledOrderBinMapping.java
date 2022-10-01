package bbnow.schema_validation.planogram.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.internal.CancelledOrderBinMappingApi;
import org.testng.annotations.Test;

public class CancelledOrderBinMapping extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for Cancelled Order Bin Mapping.",slug = "Return/Cancelled Order-Bin Mapping")
    @Test(groups = {"bbnow-payments","bbnow-schema-validation","bbnow"})
    public void postCancelledOrderBinMappingApiTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        CancelledOrderBinMappingApi cancelledOrderBinMappingApi = new CancelledOrderBinMappingApi(report);
        Response response = cancelledOrderBinMappingApi.postCancelledOrderBinMappingApi("schema//planogram//cancelled-order-bin-mapping-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
    }

}
