package bbnow.testcases.member.address;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.jhulk.CurrentDeliveryApi;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InvalidMemberIdCurrentDelivery extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase verifies that API throws 400 with invalid member id",slug = "Invalid Member Id for Current Delivery")
    @Test(groups = {"bbnow" , "regression","regression"})
    public void invalidMemberIdTestCurrentDelivery()
    {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String EntryContext = "bbnow";
        String EntryContextId = "10";
        String xCaller = "Test";
        String invalidMid = "axby";
        String xService = "123";

        CurrentDeliveryApi currentDeliveryApiWithInvalidMid = new CurrentDeliveryApi(EntryContext,EntryContextId,xCaller,xService,report);
        Response invalidResponse = currentDeliveryApiWithInvalidMid.getCurrentDelivery(invalidMid);
        int status_code_400 = invalidResponse.getStatusCode();
        Assert.assertEquals(status_code_400, 400);
        report.log("Verifying API throws 400 when provided with invalid mid = "+ invalidMid+" :"+status_code_400,true);



    }

}
