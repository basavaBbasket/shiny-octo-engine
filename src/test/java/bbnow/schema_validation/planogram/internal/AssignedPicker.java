package bbnow.schema_validation.planogram.internal;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.internal.AssignerPickerApi;
import org.testng.annotations.Test;

import java.util.UUID;

public class AssignedPicker extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for picker api.",slug = "alert")
    @Test(groups = {"bbnow" , "regression","bbnow-payments","bbnow-schema-validation"})
    public void getPickerApiTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String xTracker = UUID.randomUUID().toString();
        String fc_id = Config.bbnowConfig.getString("bbnow_stores[1].fc_id");

        AssignerPickerApi assignerPickerApi= new AssignerPickerApi(xTracker , report);
        Response response = assignerPickerApi.getAssignedPickerApi("schema//planogram//assigned-picker-200.json" , fc_id);
        report.log("Status code: " +  response.getStatusCode(),true);
    }

}
