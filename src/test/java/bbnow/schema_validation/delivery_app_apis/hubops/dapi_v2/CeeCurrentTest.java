package bbnow.schema_validation.delivery_app_apis.hubops.dapi_v2;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.delivery_app_apis.hubops.dapi_v2.CeeCurrent;

import org.testng.annotations.Test;

public class CeeCurrentTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for CeeCurrent api.",slug = "Validate StoreSelection api")
    @Test(groups = {"bbnow" , "regression","dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation","revisit"})
    public void storeSelectionTestApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";
        String xRefreshtoken="refresh";//todo
        String authorization="Bearer auth_token";//todo
        String include="has_route_assigned,stores";//todo

        CeeCurrent ceeCurrent=new CeeCurrent(cookie,xRefreshtoken,authorization,report);
        Response response=ceeCurrent.ceeCurrent("schema//delivery_app//hubops//dapi_v2//cee-current-200.json",include);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("VerifyLogin  response: " + response.prettyPrint(),true);

    }


    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for CeeCurrent api.",slug = "Validate CeeCurrent api")
    @Test(groups = {"dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation"},enabled = false)
    public void ceeCurrentTestApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";
        String xRefreshtoken="refresh";//todo
        String authorization="Bearer auth_token";//todo
        String include="has_route_assigned,stores,pool_status";//todo

        CeeCurrent ceeCurrent=new CeeCurrent(cookie,xRefreshtoken,authorization,report);
        Response response=ceeCurrent.ceeCurrent("schema//delivery_app//hubops//dapi_v2//cee-current-200.json",include);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("VerifyLogin  response: " + response.prettyPrint(),true);

    }


    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for updateCeeCurrentApi api.",slug = "Validate Update CeeCurrent api")
    @Test(groups = {"dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation"},enabled = false)
    public void updateCeeCurrentApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";
        String sa_id="7";
        String dm_id="2";
        String xRefreshtoken="refresh";//todo
        String authorization="Bearer auth_token";//todo

        CeeCurrent ceeCurrent=new CeeCurrent(cookie,xRefreshtoken,authorization,report);
        Response response=ceeCurrent.updateCeeCurrent("schema//delivery_app//hubops//dapi_v2//update-cee-update-200.json",sa_id,dm_id);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("VerifyLogin  response: " + response.prettyPrint(),true);

    }


    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for CeeCurrentRouteAssignenment api.",slug = "Validate CeeCurrentRouteAssignenment api")
    @Test(groups = {"dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation"},enabled = false)
    public void CeeCurrentRouteAssignenmentApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";
        String qrcode="1|1|123x1|2021-11-18T16:00:00";//todo
        String xRefreshtoken="refresh";//todo
        String authorization="Bearer auth_token";//todo

        CeeCurrent ceeCurrent=new CeeCurrent(cookie,xRefreshtoken,authorization,report);
        Response response=ceeCurrent.ceeCurrentRouteAssignment("schema//delivery_app//hubops//dapi_v2//cee-current-route-assignment-200.json",qrcode);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("VerifyLogin  response: " + response.prettyPrint(),true);

    }
}
