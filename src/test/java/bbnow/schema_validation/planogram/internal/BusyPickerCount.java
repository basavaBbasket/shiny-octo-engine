package bbnow.schema_validation.planogram.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.internal.BusyPickerCountApi;
import org.testng.annotations.Test;



public class BusyPickerCount extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for count busy picker.",slug = "Count of Busy of Picker Doing Some Time")
    @Test(groups = {"bbnow","test_case","bbnow-payments","bbnow-schema-validation"})
    public void getPickerApiTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String fc_id = "236";

        BusyPickerCountApi busyPickerCountApi = new BusyPickerCountApi(report);
        Response response = busyPickerCountApi.getCountOfBusyPickerApi("schema//planogram//busy-picker-200.json" , fc_id);
        report.log("Status code: " +  response.getStatusCode(),true);
    }

}

