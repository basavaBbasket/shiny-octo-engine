package bbnow.testcases.eta.order_eta;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.internal.GetOrderEta;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetOrderEtaWithInvalidOrderId extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase verifies, api is throwing 400 when provided with invalid order_id", slug = "Order Eta with invalid orderId")
    @Test(groups = { "bbnow" , "regression", "regression"})
    public void getOrderEtaWithInvalidField()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String bb_decoded_mid="2";//todo
        String xcaller="bb-now";
        String entrycontextid="10";
        String entrycontext="bbnow";
        String orderId = "99999";

        GetOrderEta getOrder =new GetOrderEta(orderId,xcaller,entrycontext,entrycontextid,bb_decoded_mid,report);

        Response response = getOrder.getOrderEta();
        Assert.assertEquals(response.getStatusCode(), 400);
        report.log("Verify Status code: " +  response.getStatusCode(),true);


    }

}
