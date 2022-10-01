package bbnow.schema_validation.planogram.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.internal.PickingJobCreationApi;
import org.testng.annotations.Test;

public class PickingJobCreation extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for Picking JOB creation api.",slug = "Picking JOB Creation")
    @Test(groups = {"bbnow-payments","bbnow-schema-validation","bbnow"})
    public void getPickingJobCreationApiTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        PickingJobCreationApi pickingJobCreationApi = new PickingJobCreationApi(report);
        Response response = pickingJobCreationApi.getPickingJobCreationApi("schema//planogram//picking-job-creation-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
    }


}
