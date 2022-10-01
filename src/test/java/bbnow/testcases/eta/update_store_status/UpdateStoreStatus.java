package bbnow.testcases.eta.update_store_status;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UpdateStoreStatus extends BaseTest {

    @DescriptionProvider(author = "tushar", description = "This TestCase Updates store status and validates the response", slug = "Update Current Store Status")
    @Test(groups = { "sat1","update store status" , "bbnow" , "regression", "regression","store_test"})
    public void updateStoreStatusTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="bbnow";// cannot be null
        String entrycontextid=Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");
        int sa_id = Config.bbnowConfig.getInt("bbnow_stores[1].sa_id");
        String bbDecodedUid ;
        JsonObject jsonObject = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select * from auth_user where username="+adminUsers.get("userName")+";"));
        bbDecodedUid= String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("id"));


        msvc.eta.admin.UpdateStoreStatus storeStatus =new msvc.eta.admin.UpdateStoreStatus(xcaller,entrycontextid,entrycontext,bbDecodedUid,Integer.toString(sa_id), report);
        Response response  = storeStatus.verifyStoreStatus("schema//eta//admin//store-status-200.json");
        Assert.assertEquals(response.getStatusCode(), 200);
        report.log("Verifying status code of Update store staus api: "+ response.getStatusCode(),true);



    }
}
