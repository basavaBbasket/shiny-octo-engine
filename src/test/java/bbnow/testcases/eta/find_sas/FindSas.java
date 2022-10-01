package bbnow.testcases.eta.find_sas;

import bbnow.testcases.member.DataProviderClass;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.Helper;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import com.bigbasket.automation.utilities.Member;
import framework.BaseTest;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.eta.admin.CheckStoreStatusApi;
import msvc.eta.internal.FindSasApi;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FindSas extends BaseTest {

    @DescriptionProvider(author = "tushar", description = "This TestCase verifies status and schema of find-sas", slug = "find sas response validation")
    @Test(groups = { "bbnow" , "regression", "regression","disable"})
    public void findSasTest()
    {
        AutomationReport report = getInitializedReport(this.getClass(),false);

        String xService="Serviceability";//todo
        String entrycontextid="10";
        String entrycontext="bbnow";

        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[0].member_sheet_name"));

        BigBasketApp app = new BigBasketApp(report);
        Response response = app.getAppDataDynamic()
                .login(creds[0])
                .getAppDataDynamic()
                .getAddressListV2();

        JsonArray addressArray = new JsonObject(response.asString()).getJsonObject("response").getJsonArray("addresses");
        JsonObject defaultAddress = Helper.extractDefaultDeliveryAddress(addressArray);


        // req params for body
        String lng =String.valueOf(defaultAddress.getDouble("address_lng")) ;//todo
        String lat = String.valueOf(defaultAddress.getDouble("address_lat"));//todo
        report.log("lat: "+ lat +"\n lng: "+ lng,true);

        FindSasApi findSasApi =new FindSasApi(entrycontext,entrycontextid,xService,lng , lat, report);

        Response response1 = findSasApi.findSAS("schema//eta//internal//find-sas-200.json");
        Assert.assertEquals(response1.getStatusCode(), 200);
        report.log("Status Code: "+ response1.getStatusCode(),true);


    }


}
