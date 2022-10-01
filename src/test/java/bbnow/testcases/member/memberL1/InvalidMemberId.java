package bbnow.testcases.member.memberL1;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.jhulk.MemberL1DetailsApi;
import org.testng.Assert;
import org.testng.annotations.Test;

public class InvalidMemberId extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "This TestCase verifies that API throws 400 with invalid member id",slug = "Invalid Memeber Id")
    @Test(groups = {"bbnow","regression"})
    public void invalidMemberIdTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String mId = "axby";
        String EntryContext = "bbnow";
        String EntryContextId = "10";//todo
        String xCaller = "JPlaces";

        MemberL1DetailsApi addNewKeyApi = new MemberL1DetailsApi(EntryContext, EntryContextId, xCaller, report);
        Response response = addNewKeyApi.getMemberL1Details(mId);
        int status_code = response.getStatusCode();
        Assert.assertEquals(status_code, 400, "Incorrect status code found. ");
    }

}
