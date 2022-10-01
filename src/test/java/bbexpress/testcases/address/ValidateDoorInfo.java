package bbexpress.testcases.address;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.api.bbdaily.RequestAPIs;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.ui_assembler.header.Endpoints;
import msvc.ui_assembler.header.SendDoorInfoApi;
import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.text.SimpleDateFormat;
import java.util.*;

public class ValidateDoorInfo extends BaseTest{
    @DescriptionProvider(author = "Shruti", description = "This testcase validates different cases for SendDoorInfo api when SA mapped with different SAs are used.", slug = "Validate SendDoorInfo api")
    @Test(groups = {"bb-express"})
public void sendDoorInfoApiTest() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        BigBasketApp app = new BigBasketApp(report);
        app.getAppDataDynamic();
        String xcaller = "listing-svc";
        String entrycontextid = Config.bbexpressConfig.getString("entry_context_id");
        String entrycontext = Config.bbexpressConfig.getString("entry_context");
        ArrayList<String> doors = new ArrayList<>();
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbexpressConfig.getString("bbexpress_stores[1].member_sheet_name"));
        String member_id = AutomationUtilities.getMemberIDForGivenID(creds[0], report);

        SendDoorInfoApi sendDoorInfoApi = new SendDoorInfoApi(app.cookie.allCookies, app.cookie.allCookies.get("x-channel"), member_id, xcaller, entrycontextid, entrycontext, report);
        Response response = sendDoorInfoApi.sendDoorInfoApi("true");
        JsonArray array = new JsonObject(response.asString()).getJsonArray("ec_doors");
        Assert.assertTrue(array.size() == 3);
        for (int i = 0; i < array.size(); i++) {
            String slug = array.getJsonObject(i).getString("slug");
            doors.add(slug);
        }

        report.log("Available doors are: " + doors +" when address mapped to SA has 3 EC", true);
    }


}
