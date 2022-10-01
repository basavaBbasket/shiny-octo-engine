package bbnow.schema_validation.wio.internal;

import api.warehousecomposition.planogram_FC.AdminCookie;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.wio.internal.FcReceivingMarkStatusCompleteApi;
import org.testng.annotations.Test;

import java.util.Map;

public class FcReceivingMarkStatusComplete  extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for FC Status Complete Api.",slug = "Validate FC Receiving status mark comeplete")
    @Test(groups = {"bbnow" , "regression", "bbnow-payments","bbnow-schema-validation","unstable"})
    public void fcReceivingMarkStatusCompleteTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xEcID = "1";//todo
        String xEntryContext = "1";//todo
        String xService = "1";//todo
        String contentType = "application/json";
        String xCaller  = "1";//todo
        String xUserID = "1";//todo
        String UserName = "y";//todo

        FcReceivingMarkStatusCompleteApi fcReceivingMarkStatusCompleteApi = new FcReceivingMarkStatusCompleteApi(xEcID,xEntryContext,xService,contentType,xCaller,xUserID,UserName,report);
        Response response = fcReceivingMarkStatusCompleteApi.postFcReceivingMarkStatusComplete("schema//wio//fc-receiving-mark-status-complete-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);



    }


}
