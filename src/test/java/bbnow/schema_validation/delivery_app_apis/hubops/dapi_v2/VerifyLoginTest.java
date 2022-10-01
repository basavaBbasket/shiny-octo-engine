package bbnow.schema_validation.delivery_app_apis.hubops.dapi_v2;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.delivery_app_apis.hubops.dapi_v2.VerifyLogin;
import org.testng.annotations.Test;

public class VerifyLoginTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for VerifyLogin api.",slug = "Validate VerifyLogin api")
    @Test(groups = {"bbnow","dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation","unstable"})
    public void verifyLoginTestApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";//todo
        String cee_id="550000";//todo
        String deviceid="yoloyolo";//todo
        String password="p";//todo
        String app_version="3.4.0";//todo
        String registration_id= "";//todo

        VerifyLogin verifyLogin=new VerifyLogin(cee_id,deviceid,password,app_version,registration_id,cookie,report);

        Response response = verifyLogin.verifyLogin("schema//delivery_app//hubops//dapi_v2//verify-login-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("VerifyLogin  response: " + response.prettyPrint(),true);
    }
}
