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

public class CheckStoreCurrentStatus extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase Checks Store current status ", slug = "Store Current Status")
    @Test(groups = { "bbnow" , "regression", "regression"}, dataProviderClass = DataProviderClass.class)
    public void checkStoreStatusTest()
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


        String fc_id = Config.bbnowConfig.getString("bbnow_stores[0].fc_id");
        report.log("fc_id" + fc_id,true);

        CheckStoreStatusApi checkStoreStatus =new CheckStoreStatusApi(xcaller,entrycontextid,entrycontext,bbDecodedUid, fc_id,report);

        Response response = checkStoreStatus.getCheckStoreStatus("schema//eta//admin//check-store-status-200.json");
        Assert.assertEquals(response.getStatusCode(), 200);


    }

}
