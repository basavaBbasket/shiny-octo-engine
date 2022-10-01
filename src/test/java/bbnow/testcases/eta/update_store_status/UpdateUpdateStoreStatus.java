package bbnow.testcases.eta.update_store_status;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UpdateUpdateStoreStatus extends BaseTest {

    @DescriptionProvider(author = "tushar", description = "This TestCase Updates store status and validates the response", slug = "Update Current Store Status")
    @Test(groups = {   "bbnow", "regression"})
    public void updateStoreStatusTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="bbnow";
        String entrycontextid="10";
        String entrycontext="bbnow";
        String bbDecodedUid = "92";//todo




        msvc.eta.admin.UpdateStoreStatus storeStatus =new msvc.eta.admin.UpdateStoreStatus(xcaller,entrycontextid,entrycontext,bbDecodedUid, report);
        Response response  = storeStatus.getStoreStatus();
        Assert.assertEquals(response.getStatusCode(), 200);
        report.log("Verifying status code of Update store staus api: "+ response.getStatusCode(),true);
        storeStatus.getStoreStatus("schema//eta//admin//store-status-200.json");



    }
}
