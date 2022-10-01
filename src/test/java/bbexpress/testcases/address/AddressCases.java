package bbexpress.testcases.address;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.ProductSearch;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.ui_assembler.header.SendDoorInfoApi;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class AddressCases extends BaseTest {
    @DescriptionProvider(author = "Shruti", description = "This testcase validates correct doors are coming up for selected entry-context.And validates address switching before door selection and during checkout", slug = "Validate Address api")
    @Test(groups = {"bb-express"})
    public void sendDoorInfoApiTest() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        BigBasketApp app = new BigBasketApp(report);
        String xcaller = "listing-svc";
        String entrycontextid = Config.bbexpressConfig.getString("entry_context_id");
        String entrycontext = Config.bbexpressConfig.getString("entry_context");
        ArrayList<String> doors = new ArrayList<>();
        String defaultAddress = null;
        String newAddress = null;
        String sheet1 = Config.bbexpressConfig.getString("bbexpress_stores[1].member_sheet_name");
        String sheet2 = Config.meatnowConfig.getString("bbnow_stores[1].member_sheet_name");
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, sheet2 + "_" + sheet1);
        String member_id = AutomationUtilities.getMemberIDForGivenID(creds[0], report);

        JsonArray addressesList = app.getAppDataDynamic().login(creds[0]).getAppDataDynamic().getAddressesList();
        for (int i = 0; i < addressesList.size(); i++) {
            Boolean defaultAdd = addressesList.getJsonObject(i).getBoolean("is_default");
            if (defaultAdd)
                defaultAddress = addressesList.getJsonObject(i).getString("area");
            else
                newAddress = addressesList.getJsonObject(i).getString("area");
        }
        app.setCustomDeliveryAddress(newAddress);
        report.log("Address successfully changed before door selection", true);

        List<String> availableSkus = ProductSearch.getAvailableProductFromSearch(creds[0], "ps", "rice", newAddress, entrycontext, Integer.parseInt(entrycontextid), report);
        app.addToCart(availableSkus.get(0), 1);
        app.setCustomDeliveryAddress(defaultAddress);
        report.log("Address successfully changed during checkout", true);

        SendDoorInfoApi sendDoorInfoApi = new SendDoorInfoApi(app.cookie.allCookies, app.cookie.allCookies.get("x-channel"), member_id, xcaller, entrycontextid, entrycontext, report);
        Response response = sendDoorInfoApi.sendDoorInfoApi("true");
        JsonArray array = new JsonObject(response.asString()).getJsonArray("ec_doors");
        Assert.assertTrue(array.size() == 3);
        for (int i = 0; i < array.size(); i++) {
            String slug = array.getJsonObject(i).getString("slug");
            doors.add(slug);
        }

        report.log("Available doors are: " + doors + " when address mapped to SA has 3 EC", true);
        doors.clear();

        creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        member_id = AutomationUtilities.getMemberIDForGivenID(creds[0], report);

        sendDoorInfoApi = new SendDoorInfoApi(app.cookie.allCookies, app.cookie.allCookies.get("x-channel"), member_id, xcaller, "10", "bbnow", report);
        response = sendDoorInfoApi.sendDoorInfoApi("true");
        array = new JsonObject(response.asString()).getJsonArray("ec_doors");
        Assert.assertTrue(array.size() == 2);
        for (int i = 0; i < array.size(); i++) {
            String slug = array.getJsonObject(i).getString("slug");
            doors.add(slug);
        }

        report.log("Available doors are: " + doors + " when address mapped to SA has 2 EC", true);

    }


}
