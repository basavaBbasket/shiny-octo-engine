package bbnow.testcases.member.address;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.jhulk.CurrentDeliveryApi;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MemberNotFoundCurrentDelivery extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase verifies that API throws 400 with invalid member id",slug = "Invalid Memeber Id for Current Delivery")
    @Test(groups = {"bbnow","regression"})
    public void invalidMemberIdTestCurrentDelivery()
    {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String EntryContext = "bbnow";
        String EntryContextId = "10";
        String xCaller = "Test";
        String memberNotFoundMid = "9999999999999";
        String xService = "123";

        CurrentDeliveryApi currentDeliveryApiWithMemberNotFoundMid = new CurrentDeliveryApi(EntryContext,EntryContextId,xCaller,xService,report);
        Response memberNotFoundResponse = currentDeliveryApiWithMemberNotFoundMid.getCurrentDelivery(memberNotFoundMid);
        int status_code_404 = memberNotFoundResponse.getStatusCode();
        Assert.assertEquals(status_code_404, 404);
        report.log("Verifying API throws 404 when member not found mid = "+ memberNotFoundMid+" :"+status_code_404,true);
    }

}


