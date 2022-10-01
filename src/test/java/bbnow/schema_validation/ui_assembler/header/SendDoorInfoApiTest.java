package bbnow.schema_validation.ui_assembler.header;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.replenishment.internal.GetPoDetails;
import msvc.ui_assembler.header.SendDoorInfoApi;
import org.testng.annotations.Test;

public class SendDoorInfoApiTest extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This testcase validates response schema for SendDoorInfo api.",slug = "Validate SendDoorInfo api")
    @Test(groups = {"bbnow"})
    public void sendDoorInfoApiTest(){
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xcaller="listing-svc";//todo
        String entrycontextid = Config.bbnowConfig.getString("entry_context_id");
        String entrycontext = Config.bbnowConfig.getString("entry_context");
        String xChannel="BB-Android";

        BigBasketApp app =new BigBasketApp(report);
        app.getAppDataDynamic();

        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[0].member_sheet_name"));
        String member_id= AutomationUtilities.getMemberIDForGivenID(creds[0],report);

        SendDoorInfoApi sendDoorInfoApi=new SendDoorInfoApi(app.cookie.allCookies,xChannel,member_id,xcaller,entrycontextid,entrycontext,report);
        Response response=sendDoorInfoApi.sendDoorInfoApi("schema//ui_assembler//header//send-door-info-api-200.json",true);

        report.log("Status code: " +  response.getStatusCode(),true);
        report.log("sendDoorInfoApi  response: " + response.prettyPrint(),true);
        response= sendDoorInfoApi.sendDoorInfoApi("true");
        report.log("sendDoorInfoApi  response: " + response.prettyPrint(),true);
    }

}
