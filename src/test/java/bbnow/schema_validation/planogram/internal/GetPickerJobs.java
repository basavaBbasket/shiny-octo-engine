package bbnow.schema_validation.planogram.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.internal.GetPickerJobsApi;
import org.testng.annotations.Test;

public class GetPickerJobs extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for Get picker jobs api.",slug = "Get Picker jobs")
    @Test(groups = {"bbnow-payments","bbnow-schema-validation","bbnow"})
    public void getPickerJobTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        GetPickerJobsApi getPickerJobsApi = new GetPickerJobsApi(report);
        Response response = getPickerJobsApi.getPickerJob("schema//planogram//get-picker-job-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
    }

}
