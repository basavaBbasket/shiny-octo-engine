package bbnow.testcases.eta.check_store_status;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import msvc.eta.admin.CheckStoreStatusApi;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckStoreStatusWithInvalidFcId extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase verifies, api is throwing 400 when provided with invalid fc_id ", slug = "Store Current Status with Invalid Fc_id")
    @Test(groups = { "bbnow" , "regression", "regression"}, dataProviderClass = DataProviderClass.class)
    public void checkStoreStatusWithInCompleteRequest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="1234";// cannot be null
        String entrycontextid= Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");
        String bbDecodedUid ;
        JsonObject jsonObject = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select * from auth_user where username=\"arunuk\";"));
        if (jsonObject.getInteger("numRows") == 0)
            bbDecodedUid = "null";
        else
            bbDecodedUid= String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("id"));

        String fc_id = "999999";

        CheckStoreStatusApi checkStoreStatus =new CheckStoreStatusApi(xcaller,entrycontextid,entrycontext,bbDecodedUid, fc_id,report);

        Response response = checkStoreStatus.getCheckStoreStatus();
        Assert.assertEquals(response.getStatusCode(), 400);
        report.log("Verifying API throws 400 when provided when provided with invalid field = "+" :"+response.getStatusCode(),true);



    }

}
