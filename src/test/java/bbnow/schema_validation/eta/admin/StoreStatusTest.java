package bbnow.schema_validation.eta.admin;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.fasterxml.jackson.databind.ser.Serializers;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.admin.CheckStoreStatusApi;
import msvc.eta.admin.StoreStatus;
import msvc.eta.internal.GetOrderEta;
import org.testng.annotations.Test;

public class StoreStatusTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for Get order Eta api.",slug = "Validate get store status api")
    @Test(groups = {"bbnow" , "regression","dl2","eta","bbnow-schema-validation","dl2-schema-validation"})
    public void orderTrackingApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="Monster-SVC";//todo
        String entrycontextid="10";//todo
        String entrycontext="bbnow";
        String bbdecoded_uid="92";
        String fcid= Config.bbnowConfig.getString("bbnow_stores[1].fc_id");


        CheckStoreStatusApi checkStoreStatusApi=new CheckStoreStatusApi(xcaller,entrycontextid,entrycontext,bbdecoded_uid,fcid,report);

        Response response = checkStoreStatusApi.getCheckStoreStatus("schema//eta//admin//check-store-status-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Get Order Eta response: " + response.prettyPrint(),true);
    }
}
