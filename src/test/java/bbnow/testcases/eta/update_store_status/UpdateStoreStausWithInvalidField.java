package bbnow.testcases.eta.update_store_status;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.admin.UpdateStoreStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UpdateStoreStausWithInvalidField extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase verifies, api is throwing 400 when provided with invalid bb decoded uid ", slug = "Update Store Status with Invalid bb decoded uid")
    @Test(groups = { "bbnow" , "regression", "regression"}, dataProviderClass = DataProviderClass.class)
    public void updateStoreStatusWithInCompleteRequest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="bbnow";
        String entrycontextid= Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");
        String sa_id = Config.bbnowConfig.getString("bbnow_stores[1].sa_id");
        String bbDecodedUid = "zxd";//todo



        UpdateStoreStatus storeStatus =new UpdateStoreStatus(xcaller,entrycontextid,entrycontext,bbDecodedUid,sa_id, report);
        Response response  = storeStatus.getStoreStatus();
        Assert.assertEquals(response.getStatusCode(), 400);
        report.log("Verifying status code of Update store staus api: "+ response.getStatusCode(),true);




    }


}
