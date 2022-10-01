package bbnow.schema_validation.planogram.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.internal.GetAllFCAlertsApi;
import org.testng.annotations.Test;

public class GetAllFCAlerts extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for alert api.",slug = "alert")
    @Test(groups = {"bbnow-payments","bbnow-schema-validation","bbnow"})
    public void getAllFcAlertTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        GetAllFCAlertsApi getAllFCAlertsApi = new GetAllFCAlertsApi(report);
        Response response = getAllFCAlertsApi.getAllFcAlerts("schema//planogram//get-all-fc-alerts-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
    }

}
