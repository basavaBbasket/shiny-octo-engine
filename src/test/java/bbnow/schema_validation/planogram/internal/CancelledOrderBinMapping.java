package bbnow.schema_validation.planogram.internal;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.planogram.internal.CancelledOrderBinMappingApi;
import org.testng.annotations.Test;

public class CancelledOrderBinMapping extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This testcase validates response schema for Cancelled Order Bin Mapping.", slug = "Return/Cancelled Order-Bin Mapping")
    @Test(groups = {"bbnow-payments", "bbnow-schema-validation"})
    public void postCancelledOrderBinMappingApiTest() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String fc_id = Config.bbnowConfig.getString("bbnow_stores[1].fc_id");
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, "bbnow-seegehalli");
        report.log("credn" + creds[0], true);
        String[] searchTerms = {"apple"};
        String bbnowArea = "Seegehalli";
        String orderId = OrderPlacement.placeBBNowOrder("bbnow", 10, creds[0], bbnowArea, 1, "ps", searchTerms, true, false, report);


        report.log("order Id:" + orderId, true);


        CancelledOrderBinMappingApi cancelledOrderBinMappingApi = new CancelledOrderBinMappingApi(orderId, report);
        Response response = cancelledOrderBinMappingApi.getCancelledOrderBinMappingApi("schema//planogram//cancelled-order-bin-mapping-200.json", fc_id);
        report.log("Status code: " + response.getStatusCode(), true);
    }

}
