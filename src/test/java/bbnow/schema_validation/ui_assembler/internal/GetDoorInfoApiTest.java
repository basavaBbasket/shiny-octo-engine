package bbnow.schema_validation.ui_assembler.internal;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.ui_assembler.header.SendDoorInfoApi;
import msvc.ui_assembler.internal.GetDoorInfoApi;
import org.testng.annotations.Test;

public class GetDoorInfoApiTest extends BaseTest {
    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for GetDoorInfoApi.",slug = "Validate GetDoorInfoApi ")
    @Test(groups = {"dl2","eta","bbnow-schema-validation","dl2-schema-validation","api-schema-validation"})
    public void getDoorInfoApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String cookie = "_bb_visaddr=\"fEJlbGF0dXIgQ29sb255LCBLcmlzaG5hcmFqYXB1cmF8MTIuOTk2NzAxMDM4NjA2NDM5fDc3Ljc1ODE5Njg5MDM1NDE2fDU2MDA2N3w=\";; ts=2021-12-07%2012:36:13.841; _bb_tc=0; _bb_rdt=\"MzE0MDY1MTU0OQ==.0\"";//todo
        String xcaller="listing-svc";//todo
        String entrycontextid="100";//todo
        String entrycontext="bb-b2c";//todo
        String xChannel="BB-IOS";
        String bb_decoded_aid="157064798";
        String bb_decoded_vid="1234";
        boolean send_door_info=true;
        int bb2_enabled=0;


        GetDoorInfoApi getDoorInfoApi=new GetDoorInfoApi(xChannel,xcaller,entrycontextid,entrycontext,bb_decoded_aid,bb_decoded_vid,cookie,report);
        Response response=getDoorInfoApi.getDoorInfoApi("schema//ui_assembler//internal//get-door-info-200.json",send_door_info,bb2_enabled);

        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("getDoorInfoApi  response: " + response.prettyPrint(),true);

    }
}
