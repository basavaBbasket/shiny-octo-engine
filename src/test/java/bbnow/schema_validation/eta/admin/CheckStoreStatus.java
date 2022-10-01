package bbnow.schema_validation.eta.admin;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.admin.CheckStoreStatusApi;
import msvc.eta.admin.StoreStatus;
import org.testng.annotations.Test;

public class CheckStoreStatus extends BaseTest{

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for Get order Eta api.",slug = "Validate Get order Eta api")
    @Test(groups = {"bbnow","dl2","eta","bbnow-schema-validation","dl2-schema-validation"})
    public void orderTrackingApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);



        String xcaller="1234";//todo
        String entrycontextid="10";//todo
        String entrycontext="bbnow";//todo
        String bbDecodedUid = "92";//todo
        String fc_id = "236";//todo

        CheckStoreStatusApi checkStoreStatus =new CheckStoreStatusApi(xcaller,entrycontextid,entrycontext,bbDecodedUid, fc_id,report);

        Response response = checkStoreStatus.getCheckStoreStatus("schema//eta//admin//check-store-status-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Get Order Eta response: " + response.prettyPrint(),true);
    }
}
