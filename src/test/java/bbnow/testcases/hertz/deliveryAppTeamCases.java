package bbnow.testcases.hertz;

import admin.pages.HomePage;
import admin.pages.Login;
import admin.pages.TransportManagementSystemPage;
import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.DeliveryBinMethods;
import api.warehousecomposition.planogram_FC.PickingMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import com.google.zxing.NotFoundException;
import framework.BaseTest;
import framework.Settings;
import io.restassured.response.Response;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.order.internal.OrderCountAPI;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import utility.dapi.DeliveryApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class deliveryAppTeamCases extends BaseTest {

    @DescriptionProvider(author = "tushar", description = "Verify login, store switching, cee checkin and config in delivery app ", slug = "Delivery App")
    @Test(groups = { "abc","bbnow"})
    public void checkAppCases() throws InterruptedException, IOException, NotFoundException {
        AutomationReport report = getInitializedReport(this.getClass(), true);
        WebDriver driver = report.driver;
        launchSite("admin", driver);
        String[] cred = AutomationUtilities.getUniqueAdminUser(serverName,"admin-superuser-mfa");
        String adminUser = cred[0];
        String adminPassword = cred[1];
        report.log("Credentials:"+cred,true);
        Login login = new Login(driver,report);
        HomePage homePage = login.doAdminLogin(adminUser,adminPassword);
        TransportManagementSystemPage transportManagementSystemPage = homePage.navigateToTransportManagement();
        transportManagementSystemPage.forceCeeLogoutUsingCeeId("550001");
        String qrCode = transportManagementSystemPage.scanQRCode("BBNow_Shanthinikatan-B-SA","BIKE");
        report.log("qr"+ qrCode,true);
        //report.driver.close();

        // placing order -->Picking ---> Binning
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        report.log("credn" + creds[0],true);
        String bb_decoded_mid;//todo
        JsonObject jsonObject = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select id from member_member where mobile_no="+creds[0] +";"));
        if (jsonObject.getInteger("numRows") == 0)
            bb_decoded_mid = null;
        else
            bb_decoded_mid =  String.valueOf(jsonObject.getJsonArray("rows").getJsonObject(0).getInteger("id"));

        String xcaller="bb-now";
        String entrycontextid="10";
        String entrycontext="bbnow";
        String xService = "123";       // can be any valid string
        String xCaller = "hertz-svc";  // can be any valid string
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));
        Map<String , String> cookie = AdminCookie.getMemberCookie(adminUser,adminPassword , report);

        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[1].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[1].search_term2");
        String[] searchTerms = {searchTerm1,searchTerm2};
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");
        String orderId = OrderPlacement.placeBBNowOrder("bbnow" , 10 , creds[0], areaName, 2, "ps",searchTerms, true, false, report);
        report.log("order Id:" + orderId ,true);

        OrderCountAPI orderCountAPI =  new OrderCountAPI(entrycontext,entrycontextid,xService,xCaller,cookie,report);
        Response response = orderCountAPI.getOrderDetailsApi(orderId);
        JsonArray arr = new JsonArray(response.body().asString());
        JsonObject jsonObj = arr.getJsonObject(0);
        List<Integer> sku = new ArrayList<>();
        JsonArray itemDetailsJson = jsonObj.getJsonArray("item_details");
        for(int i = 0; i< itemDetailsJson.size() ; i++)
        {
            JsonObject itemJson = itemDetailsJson.getJsonObject(i);
            sku.add(itemJson.getInteger("sku_id"));
        }
        report.log("sku id: "+sku,true);

        msvc.eta.internal.GetOrderEta getOrderEta =new msvc.eta.internal.GetOrderEta(orderId,xcaller,entrycontext,entrycontextid,bb_decoded_mid,report);
        PickingMethods.pickingFlowModified(Integer.parseInt(orderId), fcId, adminUser, cookie, report);
        DeliveryBinMethods.perfomOrderBinMapping(fcId,Integer.parseInt(orderId),"binning",cookie,report);

        // calling dapi
        DeliveryApp deliveryApp =new DeliveryApp(report);
        deliveryApp.checkCeeeId("550001").login(550001,"1",qrCode);

    }
}
