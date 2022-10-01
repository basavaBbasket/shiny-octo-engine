package bbnow.testcases.eta.order_eta;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import io.vertx.core.json.JsonObject;
import org.testng.annotations.Test;

public class GetOrderEtaStatus extends BaseTest {


    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for Get order Eta api.",slug = "Validate  order Eta api")
    @Test(groups = { "bbnow" , "regression","dl2","eta","bbnow-schema-validation","dl2-schema-validation"})
    public void getOrderEtaTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, "bbnow-seegehalli");
        report.log("credn" + creds[0],true);
        String bb_decoded_mid;//todo

        JsonObject jsonObject = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select id from member_member where mobile_no="+creds[0] +";"));
        if (jsonObject.getInteger("numRows") == 0)
            bb_decoded_mid = null;
        else
            bb_decoded_mid =  String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("id"));


        String xcaller="bb-now";
        String entrycontextid="10";
        String entrycontext="bbnow";

        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[0].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[0].search_term2");
        String[] searchTerms = {searchTerm1,searchTerm2};

        String areaName = Config.bbnowConfig.getString("bbnow_stores[0].area");
        String orderId = OrderPlacement.placeBBNowOrder("bbnow" , 10 , creds[0], areaName, 1, searchTerms, true, false, report);

        report.log("order Id:" + orderId ,true);
        msvc.eta.internal.GetOrderEta getOrderEta =new msvc.eta.internal.GetOrderEta(orderId,xcaller,entrycontext,entrycontextid,bb_decoded_mid,report);

        Response response = getOrderEta.getOrderEta("schema//eta//internal//get-order-eta-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Get Order Eta response: " + response.prettyPrint(),true);
        AutomationUtilities.executeMicroserviceDatabaseQuery("UAT-HULK","show databases;");
    }
}
