package bbnow.schema_validation.eta.admin;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.fasterxml.jackson.databind.ser.Serializers;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.admin.StoreStatus;
import msvc.eta.internal.GetOrderEta;
import org.testng.annotations.Test;

public class StoreStatusTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for Get order Eta api.",slug = "Validate Get order Eta api")
    @Test(groups = {"dl2","bbnow","eta","bbnow-schema-validation","dl2-schema-validation"})
    public void orderTrackingApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String cookie = "_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";//todo

        String xchannel="BB-WEB";
        String xcaller="Monster-SVC";
        String entrycontextid="100";
        String entrycontext="bb-b2c";
        StoreStatus storeStatus =new StoreStatus(xcaller,entrycontextid,entrycontext,xchannel,cookie,report);

        Response response = storeStatus.getStoreStatus("schema//eta//admin//store-status-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Get Order Eta response: " + response.prettyPrint(),true);
    }
}
