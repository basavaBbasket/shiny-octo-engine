package bbnow.schema_validation.delivery_app_apis.hubops;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.delivery_app_apis.hubops.GetLMDStoreMetrics;
import org.testng.annotations.Test;


public class GetLMDStoreMetricsTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for GetLMDStoreMetrics api.",slug = "Validate Get LMD Store Metrics api")
    @Test(groups = {"dl2","bbnow","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation"})
    public void getLMDStoreMetricsTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";//todo
        String said="1";//todo
        String dmid="1";//todo


        GetLMDStoreMetrics getLMDStoreMetrics=new GetLMDStoreMetrics(said,dmid,cookie,report);
        Response response =getLMDStoreMetrics.getLMDStoreMetrics("schema/delivery_app/hubops/get-lmd-store-metrics-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("GetLMDStoreMetrics  response: " + response.prettyPrint(),true);


    }

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for GetLMDStoreMetrics api OTOR.",slug = "Validate Get LMD Store Metrics api with otor")
    @Test(groups = {"dl2","bbnow","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation"})
    public void getLMDStoreMetricsOtorTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";//todo
        String said="1";//todo
        String dmid="1";//todo
        String otor="5";//todo


        GetLMDStoreMetrics getLMDStoreMetrics=new GetLMDStoreMetrics(said,dmid,cookie,report);
        Response response =getLMDStoreMetrics.getLMDStoreMetricsOtor("schema/delivery_app/hubops/get-lmd-store-metrics-200.json",otor);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("GetLMDStoreMetrics with otor  response: " + response.prettyPrint(),true);


    }
}
