package bbnow.testcases.eta.order_eta;

import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.internal.GetOrderEta;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetOrderEtaWithIncompleteRequest extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase verifies, api is throwing 401 when request has incomplete headers", slug = "Order Eta with incomplete fields")
    @Test(groups = { "bbnow", "regression"})
    public void getOrderEtaWithIncompleteField()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String bb_decoded_mid="2";//todo
        String xcaller="bb-now";
        String entrycontextid="10";
        String entrycontext="bbnow";

        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, "bbnow-seegehalli");
        String[] searchTerms = {"apple"};
        String bbnowArea = "Seegehalli";
        String orderId = OrderPlacement.placeBBNowOrder(creds[0], bbnowArea, 1, searchTerms, true, false, report);


        GetOrderEta getOrder =new GetOrderEta(orderId,xcaller,entrycontext,entrycontextid,bb_decoded_mid,report);

        Response response = getOrder.getOrderEtaWithInvalidHeaders();
        Assert.assertEquals(response.getStatusCode(), 401);
        report.log("Verify Status code: " +  response.getStatusCode(),true);


    }

}
