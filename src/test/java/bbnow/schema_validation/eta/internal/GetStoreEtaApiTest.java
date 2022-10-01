package bbnow.schema_validation.eta.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.internal.GetEta;
import org.testng.annotations.Test;


public class GetStoreEtaApiTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for Get Store Eta api.",slug = "Validate Get Store Eta api")
    @Test(groups = {"bbnow","dl2","eta","bbnow-schema-validation","dl2-schema-validation","test"})
    public void getStoreEtaApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="123";//todo
        String entrycontextid="123";//todo
        String entrycontext="123";//todo
        String xservice="123";//todo
        String sa_id = "10001";//todo

        GetEta getEta=new GetEta(sa_id,xcaller,entrycontext,entrycontextid,xservice,report);


        Response response = getEta.getStoreEta("schema//eta//internal//get-eta-200.json");
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Get Store Eta response: " + response.prettyPrint(),true);
    }
}
