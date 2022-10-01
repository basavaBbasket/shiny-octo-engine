package bbnow.schema_validation.delivery_app_apis.hubops.dapi_v2;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.delivery_app_apis.hubops.dapi_v2.CeeCurrent;

import org.testng.annotations.Test;

public class CeeCurrentTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for CeeCurrent api.",slug = "Validate CeeCurrent api")
    @Test(groups = {"dl2","bbnow","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation"})
    public void ceeCurrentTestApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";
        String xRefreshtoken="refresh";//todo
        String include="has_route_assigned,stores";//todo

        CeeCurrent ceeCurrent=new CeeCurrent(cookie,report);
        Response response=ceeCurrent.ceeCurrent("schema//delivery_app//hubops//dapi_v2//cee-current-200.json",include, xRefreshtoken);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("VerifyLogin  response: " + response.prettyPrint(),true);

    }




    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for CeeCurrent api.",slug = "Validate Update CeeCurrent api")
    @Test(groups = {"dl2","bbnow","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation"})
    public void updateCeeCurrentTestApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";
        String sa_id="7";
        String dm_id="2";

        CeeCurrent ceeCurrent=new CeeCurrent(cookie,report);
        Response response=ceeCurrent.updateCeeCurrent("schema//delivery_app//hubops//dapi_v2//update-cee-update-200.json",sa_id,dm_id);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("VerifyLogin  response: " + response.prettyPrint(),true);

    }


}
