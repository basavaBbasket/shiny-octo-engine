package bbnow.schema_validation.picking.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.picking.internal.UpdatePlanogramStatus;
import msvc.wio.external.TransferInApi;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class UpdatePlanogramStatusToCompleteTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates UpdatePlanogramStatusToComplete Api .",slug = "Validate UpdatePlanogramStatusToComplete api")
    @Test(groups = {"bbnow","dl2","bbnowvalidation","dl2validation","api-schema-validation"})
    public void trasnferInApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);


        String bb_decoded_uid="";//todo
        Map<String,String> cookie = new HashMap<String, String>() {{
            put("_bb_locSrc", "default");
            put("_bb_loid", "302");
            put("_bb_nhid", "1723");
            put("_bb_vid", "Mzk1MzQ1NzIxNQ==");
            put("_bb_dsid", "1720");
            put("bb_home_cache", "a16a5193.3630.visitor");
            put("_bb_tc", "0");
            put("_bb_rdt", "\"MzE0MDY1MTU0OQ==.0\"");
        }}; //TODO

        String fcid="1";//todo
        String jobid="564";//todo



        UpdatePlanogramStatus updatePlanogramStatus=new UpdatePlanogramStatus(bb_decoded_uid,cookie,report);
        Response response=updatePlanogramStatus.updatePlanogramStatusToComplete("schema//picking//update-picking-jobs-to-complete-200.json",fcid,jobid);
        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("Fc return api: " + response.prettyPrint(),true);
    }
}
