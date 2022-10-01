package bbnow.testcases.eta.store_eta;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import io.restassured.response.Response;
import msvc.eta.internal.GetEta;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetCurrentStoreEta extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This testcase verifies stores eta.",slug = "Validate Store Eta api")
    @Test(groups = {"bbnow" , "regression","eta"})
    public void getStoreCurrentEtaApi(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="123";//cannot be null
        String entrycontextid=Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");
        String xservice="123";// cannot be null
        String sa_id = Config.bbnowConfig.getString("bbnow_stores[0].sa_id");

        GetEta getEta=new GetEta(sa_id,xcaller,entrycontext,entrycontextid,xservice,report);
        Response response  = getEta.getStoreEta();
        Assert.assertEquals(response.getStatusCode(),200);
        report.log("Status Code: "+ response.getStatusCode(),true);
        getEta.getStoreEta("schema//eta//internal//get-eta-200.json");

    }
}
