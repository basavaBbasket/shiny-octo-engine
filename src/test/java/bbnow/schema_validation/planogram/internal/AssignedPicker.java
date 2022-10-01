package bbnow.schema_validation.planogram.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.internal.AssignerPickerApi;
import org.testng.annotations.Test;

public class AssignedPicker extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for picker api.",slug = "alert")
    @Test(groups = {"bbnow-payments","bbnow-schema-validation","bbnow"})
    public void getPickerApiTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String xTracker = "11111111111111111111111111111111";

        AssignerPickerApi assignerPickerApi= new AssignerPickerApi(xTracker , report);
        Response response = assignerPickerApi.getAssignedPickerApi("schema//planogram//assigned-picker-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
    }

}
