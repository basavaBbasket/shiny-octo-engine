package bbnow.testcases.eta.check_store_status;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.admin.CheckStoreStatusApi;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckUpdateStoreStatusWithInvalidFcId extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase verifies, api is throwing 400 when provided with invalid fc_id ", slug = "Store Current Status with Invalid Fc_id")
    @Test(groups = { "bbnow", "regression"}, dataProviderClass = DataProviderClass.class)
    public void checkStoreStatusWithInvalidField()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="1234";//todo
        String entrycontextid="10";//todo
        String entrycontext="bbnow";//todo
        String bbDecodedUid = "92";//todo
        String fc_id = "9999999";//todo

        CheckStoreStatusApi checkStoreStatus =new CheckStoreStatusApi(xcaller,entrycontextid,entrycontext,bbDecodedUid, fc_id,report);

        Response response = checkStoreStatus.getCheckStoreStatus();
        Assert.assertEquals(response.getStatusCode(), 400);
        report.log("Verifying API throws 400 when provided when provided with invalid field = "+" :"+response.getStatusCode(),true);



    }

}
