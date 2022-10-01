package bbnow.testcases.member.address;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.Helper;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.serviceability.FindSaS;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Map;

public class GetMemberAddressList extends BaseTest {

    @DescriptionProvider(slug = "Get member address list", description = "This testcase call address list api & verifies response", author = "rakesh")
    @Test(groups = {"bbnow" , "regression"})
    public void getMemberAddressList() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));

        BigBasketApp app = new BigBasketApp(report);
        Response response = app.getAppDataDynamic()
                .login(creds[0])
                .getAppDataDynamic()
                .getAddressListV2();

        ArrayList<Integer> expectedEcIds = Config.bbnowConfig.get("bbnow_stores[1].sample_address.applicable_ec_ids");
        report.log("Expected EC IDs: " + expectedEcIds, true);

        JsonPath responseBody = response.jsonPath();
        Map<Object, Object> jsonMap = responseBody.get("response.addresses[0]");

        Assert.assertNotNull(jsonMap.get("applicable_ec_ids"), "applicable_ec_ids not found in response");
        ArrayList<Integer> fetchedEcIds = (ArrayList<Integer>) jsonMap.get("applicable_ec_ids");
        report.log("EC IDs returned by api: " + fetchedEcIds.toString(), true);
        Assert.assertEquals(fetchedEcIds, expectedEcIds, "Fetched ecIds from api is not equal to expected ecIds");

        JsonArray addressArray = new JsonObject(response.asString()).getJsonObject("response").getJsonArray("addresses");
        JsonObject defaultAddress = Helper.extractDefaultDeliveryAddress(addressArray);
        FindSaS findSaS = new FindSaS(report);
        Response response1 = findSaS.findSaS(Config.bbnowConfig.getString("entry_context"), 10, defaultAddress.getInteger("id"), defaultAddress.getInteger("pin"), String.valueOf(defaultAddress.getDouble("address_lat")), String.valueOf(defaultAddress.getDouble("address_lng")));
        Assert.assertTrue(response1.statusCode() == 200, " Incorrect status code from find sas api.");

        int saId = new JsonArray(response1.asString()).getJsonObject(0).getInteger("id");
        Assert.assertEquals(saId, Config.bbnowConfig.getInt("bbnow_stores[1].sa_id"), "SA Id returned from find sas is not equal to expected SA id.");
    }
}
