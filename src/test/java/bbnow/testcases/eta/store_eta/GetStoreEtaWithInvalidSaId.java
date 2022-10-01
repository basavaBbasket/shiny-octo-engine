package bbnow.testcases.eta.store_eta;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.eta.internal.GetEta;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GetStoreEtaWithInvalidSaId extends BaseTest {
    @DescriptionProvider(author = "Tushar", description = "This testcase verifies stores eta with invalid sa_id.",slug = "store eta invalid sa_id- 400")
    @Test(groups = {"bbnow" , "regression","eta"})
    public void getStoreCurrentEtaApiWithInvalidEta(){

        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="123";
        String entrycontextid= Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");
        String xservice="123";
        String sa_id = "99999";

        GetEta getEta=new GetEta(sa_id,xcaller,entrycontext,entrycontextid,xservice,report);
        Response response  = getEta.getStoreEta();
        Assert.assertEquals(response.getStatusCode(),400);
        report.log("Status Code: "+ response.getStatusCode(),true);





    }

}
