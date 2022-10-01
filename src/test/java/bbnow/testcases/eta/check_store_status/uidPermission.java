package bbnow.testcases.eta.check_store_status;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.eta.admin.CheckStoreStatusApi;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

public class uidPermission extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase api response when uid has limited permission", slug = "Response in limited permission uid")
    @Test(groups = { "bbnow" , "regression", "regression"})
    public void checkResponseWithLimitedPermissionUid()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="1234";//cannot be null
        String entrycontextid=Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");
        String bbDecodedUid ;
        JsonObject jsonObject = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select * from auth_user where username=\"no_permission_uid\";"));
        if (jsonObject.getInteger("numRows") == 0)
            bbDecodedUid = "null";
        else
            bbDecodedUid= String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("id"));
        report.log("Picking uid with limited/least permission",true);

        String fc_id = Config.bbnowConfig.getString("bbnow_stores[1].fc_id");
        report.log("fc_id" + fc_id,true);

        CheckStoreStatusApi checkStoreStatus =new CheckStoreStatusApi(xcaller,entrycontextid,entrycontext,bbDecodedUid, fc_id,report);

        Response response = checkStoreStatus.getCheckStoreStatus();
        Assert.assertEquals(response.getStatusCode(), 400);
        report.log("Status_code: "+response.getStatusCode() ,true);

        JsonPath jsonPath = response.jsonPath();
        ArrayList<JsonObject> ar = jsonPath.get("errors");
        Map<String,String> innerJson = (Map<String, String>) ar.get(0);
        report.log("Display_msg " + innerJson.get("display_msg"),true);
        String msg= innerJson.get("display_msg");
        report.log("Checking response contains required message : ",msg.matches("(.*)does not have permission 'get_sa_store_against_fc'(.*)"));



    }

}
