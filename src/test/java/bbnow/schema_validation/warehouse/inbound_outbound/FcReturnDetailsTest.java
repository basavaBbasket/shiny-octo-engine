package bbnow.schema_validation.warehouse.inbound_outbound;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import io.restassured.response.Response;
import msvc.wio.internal.fcreturn.internal.FcReturnDetails;
import org.testng.annotations.Test;


public class FcReturnDetailsTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates Fc Return api .",slug = "Validate Fc Return api")
    @Test(groups = {"bbnow" , "regression","dl2","catalogfc","bbnowvalidation","dl2validation","api-schema-validation","unstable"})
    public void fcReturnApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xCaller = "postman";//todo
        String UserId="1";//todo
        int sku_id =40001610;//todo
        int quantity = 10;//todo
        Double cp =10.20;//todo
        Double mrp =13.00;//todo
        String fc_id=String.valueOf(204);//todo
        String entrycontextid= Config.bbnowConfig.getString("entry_context_id");
        String entrycontext=Config.bbnowConfig.getString("entry_context");
        int destination_fc_id=Integer.valueOf(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));//todo
        String Cookie="_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";


       FcReturnDetails fcReturnDetails = new FcReturnDetails(entrycontext,entrycontextid,xCaller,UserId,fc_id,sku_id,quantity,cp,mrp,destination_fc_id,Cookie,report);

        Response response = fcReturnDetails.fcReturn();
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Fc return api: " + response.prettyPrint(),true);
    }
}
