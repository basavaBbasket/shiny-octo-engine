package bbnow.schema_validation.delivery_app_apis.hubops;


import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.delivery_app_apis.hubops.CeeAttributesForOrderIds;
import org.testng.annotations.Test;

import java.util.ArrayList;

public class CeeAttributesForOrderIdsTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for CeeAttributesForOrderIds api.",slug = "Validate Cee Attributes For OrderIds api")
    @Test(groups = {"bbnow" , "regression","dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation","unstable"})
    public void ceeAttributesForOrderIdsApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";//todo
        String xcaller="fe";//todo
        String entrycontextid="102";//todo
        String entrycontext="bb-internal";//todo
        ArrayList Orderids=new ArrayList();
        Orderids.add(1);//todo
        CeeAttributesForOrderIds ceeAttributesForOrderIds=new CeeAttributesForOrderIds(xcaller,entrycontextid,entrycontext,cookie,Orderids,report);

        Response response = ceeAttributesForOrderIds.getCeeAttributesForOrderIds("schema//delivery_app//hubops//cee-attributes-for-order-ids-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Cee Attributes For OrderIds  response: " + response.prettyPrint(),true);
    }

}
