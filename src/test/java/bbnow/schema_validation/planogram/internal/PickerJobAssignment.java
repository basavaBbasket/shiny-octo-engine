package bbnow.schema_validation.planogram.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.internal.GetPickerJobsApi;
import msvc.planogram.internal.PickerJobAssignmentApi;
import org.testng.annotations.Test;

public class PickerJobAssignment extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for picker job assignment api.",slug = "picker job assignment")
    @Test(groups = {"bbnow-payments","bbnow-schema-validation","bbnow"})
    public void getPickerJobAssignmentTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        PickerJobAssignmentApi pickerJobAssignmentApi = new PickerJobAssignmentApi(report);
        Response response = pickerJobAssignmentApi.getPickerJobAssignment("schema//planogram//picker-job-assignment-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
    }

}
