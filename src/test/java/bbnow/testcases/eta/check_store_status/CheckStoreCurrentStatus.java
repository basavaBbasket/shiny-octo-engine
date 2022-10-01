package bbnow.testcases.eta.check_store_status;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.admin.CheckStoreStatusApi;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckStoreCurrentStatus extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase Checks Store current status ", slug = "Store Current Status")
    @Test(groups = { "bbnow", "regression"}, dataProviderClass = DataProviderClass.class)
    public void checkStoreStatusTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="1234";//todo
        String entrycontextid="10";
        String entrycontext="bbnow";
        String bbDecodedUid = "92";//todo

        String fc_id = Config.bbnowConfig.getString("bbnow_stores[0].fc_id");
        report.log("fc_id" + fc_id,true);

        CheckStoreStatusApi checkStoreStatus =new CheckStoreStatusApi(xcaller,entrycontextid,entrycontext,bbDecodedUid, fc_id,report);

        Response response = checkStoreStatus.getCheckStoreStatus("schema//eta//admin//check-store-status-200.json");
        Assert.assertEquals(response.getStatusCode(), 200);


    }

}
