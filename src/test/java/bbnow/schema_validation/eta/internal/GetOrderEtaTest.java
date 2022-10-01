package bbnow.schema_validation.eta.internal;

import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.internal.GetEta;
import msvc.eta.internal.GetOrderEta;
import org.testng.annotations.Test;

public class GetOrderEtaTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for Get order Eta api.",slug = "Validate Get order Eta api")
    @Test(groups = {"bbnow","dl2","eta","bbnow-schema-validation","dl2-schema-validation","unstable"})
    public void getOrderEtaTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, "bbnow-seegehalli");

        String bb_decoded_mid="2";
        String xcaller="bb-now";
        String entrycontextid="10";
        String entrycontext="bbnow";
        String[] searchTerms = {"apple"};
        String bbnowArea = "Seegehalli";
        String orderId = OrderPlacement.placeBBNowOrder(creds[0], bbnowArea, 1, searchTerms, true, false, report);
        GetOrderEta getOrderEta =new GetOrderEta(orderId,xcaller,entrycontext,entrycontextid,bb_decoded_mid,report);

        Response response = getOrderEta.getOrderEta("schema//eta//internal//get-order-eta-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Get Order Eta response: " + response.prettyPrint(),true);
    }
}
