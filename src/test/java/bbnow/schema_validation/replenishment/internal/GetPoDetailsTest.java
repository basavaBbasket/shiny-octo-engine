package bbnow.schema_validation.replenishment.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.delivery_app_apis.hubops.CeeAttributesForOrderIds;
import msvc.replenishment.internal.GetPoDetails;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class GetPoDetailsTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for GetPoDetails api.",slug = "Validate GetPoDetails api")
    @Test(groups = {"bbnow" , "regression","dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation","earlyrelease"})
    public void getPoDetailsTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="hertz";//todo
        String entrycontextid="102";//todo
        String entrycontext="bb-internal";//todo
        String po_id="SGI-1";
        boolean is_sku_info_needed=true;

        GetPoDetails getPoDetails=new GetPoDetails(po_id,entrycontext,entrycontextid,xcaller,report);
        Response response=getPoDetails.getPoDetails("schema//replenishment//internal//get-po-details-200.json",is_sku_info_needed);

        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("GetPoDetails  response: " + response.prettyPrint(),true);
        report.log("GetPoDetails  response: " + response.prettyPrint(),true);
    }

}
