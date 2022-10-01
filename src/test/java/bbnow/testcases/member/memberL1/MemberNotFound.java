package bbnow.testcases.member.memberL1;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.jhulk.MemberL1DetailsApi;
import org.testng.Assert;
import org.testng.annotations.Test;

public class MemberNotFound extends BaseTest {

    @DescriptionProvider(author = "tushar", description = "This TestCase is to identify that API throws 404 when member is not found",slug = "Member Not found")
    @Test(groups = {"bbnow","regression"})
    public void memberNotFoundTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String mId = "99999999999999";//todo
        String EntryContext = "bb-b2c";//todo
        String EntryContextId = "100";//todo
        String xCaller = "JPlaces";

        MemberL1DetailsApi addNewKeyApi = new MemberL1DetailsApi(EntryContext, EntryContextId, xCaller, report);
        Response response = addNewKeyApi.getMemberL1Details(mId);
        int status_code = response.getStatusCode();
        Assert.assertEquals(status_code, 404, "Incorrect status code found. ");
    }

}
