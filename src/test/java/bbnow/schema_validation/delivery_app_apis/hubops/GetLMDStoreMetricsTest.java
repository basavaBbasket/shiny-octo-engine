package bbnow.schema_validation.delivery_app_apis.hubops;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.delivery_app_apis.hubops.GetLMDStoreMetrics;
import org.testng.annotations.Test;


public class GetLMDStoreMetricsTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for GetLMDStoreMetrics api.",slug = "Validate Get LMD Store Metrics api")
    @Test(groups = {"bbnow","dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation"})
    public void getLMDStoreMetricsTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String said="16";//todo
        String dmid="1";//todo
        String otor="5";//todo

        String xCaller="fe";//todo
        String xEntryContext="bb-internal";//todo
        String xEntryContextId="102";//todo


        GetLMDStoreMetrics getLMDStoreMetrics=new GetLMDStoreMetrics(said,dmid,xEntryContext,xEntryContextId,xCaller,report);
        Response response =getLMDStoreMetrics.getLMDStoreMetrics(otor);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("GetLMDStoreMetrics  response: " + response.prettyPrint(),true);


    }

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for GetLMDStoreMetrics api OTOR.",slug = "Validate Get LMD Store Metrics api with otor")
    @Test(groups = {"dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation","test2"})
    public void getLMDStoreMetricsOtorTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String said="16";//todo
        String dmid="1";//todo
        String otor="5";//todo
        String xCaller="fe";//todo
        String xEntryContext="bb-internal";//todo
        String xEntryContextId="102";//todo


        GetLMDStoreMetrics getLMDStoreMetrics=new GetLMDStoreMetrics(said,dmid,xEntryContext,xEntryContextId,xCaller,report);
        Response response =getLMDStoreMetrics.getLMDStoreMetricsOtor("schema//delivery_app//hubops//get-lmd-store-metrics-200.json",otor);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("GetLMDStoreMetrics with otor  response: " + response.prettyPrint(),true);


    }
}
