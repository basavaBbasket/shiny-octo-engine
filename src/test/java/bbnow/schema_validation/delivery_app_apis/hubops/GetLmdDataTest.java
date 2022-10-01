package bbnow.schema_validation.delivery_app_apis.hubops;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.delivery_app_apis.hubops.GetLMDStoreMetrics;
import msvc.delivery_app_apis.hubops.GetLmdData;
import org.testng.annotations.Test;

public class GetLmdDataTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for GetLmdData api.",slug = "Validate Get Get Lmd Data api")
    @Test(groups = {"bbnow" , "regression","dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation","revisit"})
    public void getLmdDataTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);
        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";//todo
        String order_id="1";//todo
        String xCaller="fe";//todo
        String xEntryContext="bb-internal";//todo
        String xEntryContextId="102";//todo


       GetLmdData getLmdData=new GetLmdData(order_id,cookie,xEntryContext,xEntryContextId,xCaller,report);
        Response response =getLmdData.getLmdData("schema//delivery_app//hubops//get-lmd-data-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("GetLmdData  response: " + response.prettyPrint(),true);


    }

}
