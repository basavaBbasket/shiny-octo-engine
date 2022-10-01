package bbnow.testcases.eta.update_store_status;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.admin.UpdateStoreStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UpdateStoreStausWithInvalidField extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase verifies, api is throwing 400 when provided with invalid bb decoded uid ", slug = "Update Store Status with Invalid bb decoded uid")
    @Test(groups = { "bbnow", "regression"}, dataProviderClass = DataProviderClass.class)
    public void updateStoreStatusWithInCompleteRequest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="bbnow";
        String entrycontextid="10";
        String entrycontext="bbnow";
        String bbDecodedUid = "zxd";//todo



        UpdateStoreStatus storeStatus =new UpdateStoreStatus(xcaller,entrycontextid,entrycontext,bbDecodedUid, report);
        Response response  = storeStatus.getStoreStatus();
        Assert.assertEquals(response.getStatusCode(), 400);
        report.log("Verifying status code of Update store staus api: "+ response.getStatusCode(),true);




    }


}
