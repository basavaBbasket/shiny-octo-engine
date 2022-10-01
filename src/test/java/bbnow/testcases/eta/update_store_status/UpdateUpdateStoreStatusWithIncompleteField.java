package bbnow.testcases.eta.update_store_status;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import msvc.eta.admin.UpdateStoreStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

public class UpdateUpdateStoreStatusWithIncompleteField extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase verifies, api is throwing 401 when not provided required field ", slug = "Update Current Store Status without required parameter")
    @Test(groups = { "bbnow" , "regression", "regression"}, dataProviderClass = DataProviderClass.class)
    public void updateStoreStatusWithIncompleteRequestTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="bbnow"; // cannot be null
        String entrycontextid=Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");
        String sa_id = Config.bbnowConfig.getString("bbnow_stores[1].sa_id");
        String bbDecodedUid ;
        JsonObject jsonObject = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select * from auth_user where username=\"arunuk\";"));
        if (jsonObject.getInteger("numRows") == 0)
            bbDecodedUid = "null";
        else
            bbDecodedUid= String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("id"));


        UpdateStoreStatus storeStatus =new UpdateStoreStatus(xcaller,entrycontextid,entrycontext,bbDecodedUid,sa_id ,report);
        Response response  = storeStatus.getStoreStatusWithIncompleteRequest();
        Assert.assertEquals(response.getStatusCode(), 401);
        report.log("Verifying status code of Update store staus api With missing Headers: "+ response.getStatusCode(),true);

    }

}
