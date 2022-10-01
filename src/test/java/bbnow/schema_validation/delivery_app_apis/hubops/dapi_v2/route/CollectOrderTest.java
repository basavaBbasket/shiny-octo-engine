package bbnow.schema_validation.delivery_app_apis.hubops.dapi_v2.route;


import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.delivery_app_apis.hubops.dapi_v2.CeeCheckIn;
import msvc.delivery_app_apis.hubops.dapi_v2.route.CollectOrder;
import org.testng.annotations.Test;

public class CollectOrderTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for CollectOrder api.", slug = "Validate CollectOrder api")
    @Test(groups = {"bbnow" , "regression","dl2", "eta", "bbnow-schema-validation", "dl2-schema-validation", "api-schema-validation","unstable"})
    public void collectOrderTest() {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";//todo
        boolean collected_status=true;
        String route_id="1";//todo
        String xRefreshtoken="refresh";//todo
        String authorization="Bearer auth_token";//todo


        CollectOrder collectOrder=new CollectOrder(route_id,xRefreshtoken,authorization,cookie,report);
        Response response=collectOrder.collectOrder(collected_status);

        report.log("Status code: " + response.getStatusCode(), true);
        report.log("collectOrder  response: " + response.prettyPrint(), true);
    }
}
