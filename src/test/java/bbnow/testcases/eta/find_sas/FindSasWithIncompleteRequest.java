package bbnow.testcases.eta.find_sas;

import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.Helper;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.eta.internal.FindSasApi;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FindSasWithIncompleteRequest extends BaseTest {
    @DescriptionProvider(author = "tushar", description = "Api throws 401 when all required parameters are not passed", slug = "Check Api Throws 401")
    @Test(groups = { "bbnow" , "regression", "regression"})
    public void findSasWithIncompleteRequestTest()
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

        Response response1 = findSasApi.findSASWithIncompleteHeaders();
        Assert.assertEquals(response1.getStatusCode(), 401);
        report.log("Status Code: "+ response1.getStatusCode(),true);

    }


}
