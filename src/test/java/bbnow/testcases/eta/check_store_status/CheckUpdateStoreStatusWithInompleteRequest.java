package bbnow.testcases.eta.check_store_status;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import msvc.eta.admin.CheckStoreStatusApi;
import org.testng.Assert;
import org.testng.annotations.Test;


public class CheckUpdateStoreStatusWithInompleteRequest extends BaseTest{
    @DescriptionProvider(author = "tushar", description = "This TestCase verifies, api is throwing 401 when not provided required field ", slug = "Store Current Status without required parameter")
    @Test(groups = { "bbnow", "regression"}, dataProviderClass = DataProviderClass.class)
    public void checkStoreStatusWithInvalidFcIdTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String entrycontextid="10";//todo
        String xcaller="1234";//todo
        String entrycontext="bbnow";//todo
        String bbDecodedUid = "92";//todo
        String fc_id = "236";//todo

        CheckStoreStatusApi checkStoreStatus =new CheckStoreStatusApi(xcaller,entrycontextid,entrycontext,bbDecodedUid, fc_id,report);
        Response response = checkStoreStatus.getCheckStoreStatusWithOutRequiredFeild();

        Assert.assertEquals(response.getStatusCode(), 401);
        report.log("Verifying API throws 401 when provided when not provided with all required fields = "+" :"+response.getStatusCode(),true);

    }

}
