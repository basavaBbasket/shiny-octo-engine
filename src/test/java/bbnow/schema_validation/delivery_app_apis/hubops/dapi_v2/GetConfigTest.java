package bbnow.schema_validation.delivery_app_apis.hubops.dapi_v2;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.delivery_app_apis.hubops.dapi_v2.GetConfig;
import msvc.delivery_app_apis.hubops.dapi_v2.VerifyLogin;
import org.testng.annotations.Test;

public class GetConfigTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for GetConfig api.",slug = "Validate GetConfig api")
    @Test(groups = {"bbnow" , "regression","dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation","unstable"},enabled = false)
    public void getConfigApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";//todo

        String xRefreshtoken="refresh";//todo
        String authorization="Bearer auth_token";//todo

        GetConfig getConfig=new GetConfig(cookie,xRefreshtoken,authorization,report);
        Response response = getConfig.getConfig("schema//delivery_app//hubops//dapi_v2//get-config-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("GetConfig  response: " + response.prettyPrint(),true);
    }
}
