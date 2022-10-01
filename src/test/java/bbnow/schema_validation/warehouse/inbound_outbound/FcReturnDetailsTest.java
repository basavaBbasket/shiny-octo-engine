package bbnow.schema_validation.warehouse.inbound_outbound;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.wio.internal.fcreturn.internal.FcReturnDetails;
import org.testng.annotations.Test;


public class FcReturnDetailsTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates Fc Return api .",slug = "Validate Fc Return api")
    @Test(groups = {"bbnow","dl2","catalogfc","bbnowvalidation","dl2validation","api-schema-validation","unstable"})
    public void fcReturnApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xCaller = "postman";//todo
        String UserId="1";//todo
        int sku_id =40001610;//todo
        int quantity = 10;//todo
        Double cp =10.20;//todo
        Double mrp =13.00;//todo
        String fc_id="1";//todo
        int destination_fc_id=12;//todo
        String Cookie="_bb_cid=1; _bb_vid=\"Mzk1NDA1MzM1Ng==\"; _bb_tc=0; _bb_rdt=\"MzE0NDc3NjE4NQ==.0\"; _bb_rd=6; _client_version=2748; sessionid=c7mtx66jcad0mfz5cl5z52yrz1mwd4lh; ts=\"2021-11-18 16:30:35.220\"";


       FcReturnDetails fcReturnDetails = new FcReturnDetails(xCaller,UserId,fc_id,sku_id,quantity,cp,mrp,destination_fc_id,Cookie,report);

        Response response = fcReturnDetails.fcReturn();
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Fc return api: " + response.prettyPrint(),true);
    }
}
