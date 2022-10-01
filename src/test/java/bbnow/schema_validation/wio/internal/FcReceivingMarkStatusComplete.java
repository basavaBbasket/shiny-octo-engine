package bbnow.schema_validation.wio.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.wio.internal.FcReceivingMarkStatusCompleteApi;
import org.testng.annotations.Test;

public class FcReceivingMarkStatusComplete  extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for FC Status Complete Api.",slug = "Validate FC Receiving status mark comeplete")
    @Test(groups = {"test_2","bbnow-payments","bbnow-schema-validation","bbnow"})
    public void fcReceivingMarkStatusCompleteTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xEntryContext  = "1";
        String xEntryContextId = "1";
        String xTracker = "a22b81a3-ad24-456b-ab9d-3a2cc03a04fa";
        String Service = "1";
        String ContentType = "application/json";
        String xCaller = "1";
        String UserId = "1";
        String UserName = "y";

        FcReceivingMarkStatusCompleteApi fcReceivingMarkStatusCompleteApi = new FcReceivingMarkStatusCompleteApi(xEntryContext,xEntryContextId,xTracker,Service,ContentType,xCaller,UserId,UserName,report);
        Response response = fcReceivingMarkStatusCompleteApi.postFcReceivingMarkStatusComplete("schema//wio//fc-receiving-mark-status-complete-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);



    }


}
