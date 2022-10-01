package bbnow.testcases.eta.store_eta;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import io.restassured.response.Response;
import msvc.eta.internal.GetEta;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetStoreCurrentEtaWithIncompleteRequest extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This testcase verifies stores eta.",slug = "Validate Store Eta api")
    @Test(groups = {"bbnow","dl2","eta","bbnow-schema-validation","dl2-schema-validation","test"})
    public void getStoreCurrentEtaWithIncompleteHeaderrs(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="123";//todo
        String entrycontextid="10";
        String entrycontext="bbnow";
        String xservice="123";

        int saId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_said"));
        String sa_id = Integer.toString(saId);//todo

        GetEta getEta=new GetEta(sa_id,xcaller,entrycontext,entrycontextid,xservice,report);
        Response response  = getEta.getStoreEtaWithIncompleteHeaders();
        Assert.assertEquals(response.getStatusCode(),401);
        report.log("Status Code: "+ response.getStatusCode(),true);


    }


}
