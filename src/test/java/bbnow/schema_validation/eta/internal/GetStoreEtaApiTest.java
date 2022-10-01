package bbnow.schema_validation.eta.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.internal.GetEta;
import org.testng.annotations.Test;


public class GetStoreEtaApiTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for Get Store Eta api.",slug = "Validate Get Store Eta api")
    @Test(groups = {"dl2","bbnow","eta","bbnow-schema-validation","dl2-schema-validation"})
    public void orderTrackingApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";//todo
        String sa_id = "1";//todo

        GetEta getEta=new GetEta(sa_id,cookie,report);


        Response response = getEta.getStoreEta("schema//eta//internal//get-eta-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Get Store Eta response: " + response.prettyPrint(),true);
    }
}
