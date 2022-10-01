package bbnow.testcases.eta.update_store_status;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.admin.UpdateStoreStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UpdateUpdateStoreStatusWithIncompleteField extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase verifies, api is throwing 401 when not provided required field ", slug = "Update Current Store Status without required parameter")
    @Test(groups = { "bbnow", "regression"}, dataProviderClass = DataProviderClass.class)
    public void updateStoreStatusWithIncompleteRequestTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="bbnow";
        String entrycontextid="10";
        String entrycontext="bbnow";
        String bbDecodedUid = "92";//todo

        UpdateStoreStatus storeStatus =new UpdateStoreStatus(xcaller,entrycontextid,entrycontext,bbDecodedUid, report);
        Response response  = storeStatus.getStoreStatusWithIncompleteRequest();
        Assert.assertEquals(response.getStatusCode(), 401);
        report.log("Verifying status code of Update store staus api With missing Headers: "+ response.getStatusCode(),true);

    }

}
