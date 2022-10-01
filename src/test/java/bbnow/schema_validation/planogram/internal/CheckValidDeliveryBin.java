package bbnow.schema_validation.planogram.internal;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.internal.CheckValidDeliveryBinApi;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CheckValidDeliveryBin extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This testcase validates the schema of response", slug = "Check if delivery bin is valid")
    @Test(groups = { "bbnow" , "regression","bbnow-payments", "bbnow-schema-validation"})
    public void getCheckValidDeliveryBinApiTest() {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        String binloc = "B-12-A-3"; //todo
        String fc_id = Config.bbnowConfig.getString("bbnow_stores[0].fc_id");

        CheckValidDeliveryBinApi checkValidDeliveryBinApi = new CheckValidDeliveryBinApi(binloc, report);
        Response response = checkValidDeliveryBinApi.getCheckValidDeliveryBinApi("schema//planogram//check-valid-delivery-bin-200.json",fc_id);

        report.log("Status code: " + response.getStatusCode(), true);

    }
}
