package bbnow.schema_validation.delivery_app_apis.hubops.dapi_v2;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.delivery_app_apis.hubops.dapi_v2.CallRequest;
import msvc.delivery_app_apis.hubops.dapi_v2.CeeCheckIn;
import org.testng.annotations.Test;

public class CallRequestTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for CallRequest api.", slug = "Validate CallRequest api")
    @Test(groups = {"bbnow","dl2", "eta", "bbnow-schema-validation", "dl2-schema-validation", "api-schema-validation","unstable"})
    public void ceeCheckinTest() {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";//todo
        String order_id = "1000000";
        String recipient = "customer";
        String xRefreshtoken="refresh";//todo
        String authorization="Bearer auth_token";//todo


        CallRequest callRequest=new CallRequest(cookie,xRefreshtoken,authorization,order_id,report);
        Response response=callRequest.callRequest("schema//delivery_app//hubops//dapi_v2//call-request-200.json",recipient);

        report.log("Status code: " + response.getStatusCode(), true);
        report.log("VerifyLogin  response: " + response.prettyPrint(), true);
    }
}
