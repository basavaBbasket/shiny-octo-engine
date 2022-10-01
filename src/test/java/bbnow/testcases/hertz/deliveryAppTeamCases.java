package bbnow.testcases.hertz;

import admin.pages.HomePage;
import admin.pages.Login;
import admin.pages.TransportManagementSystemPage;
import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.DeliveryBinMethods;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.PickingMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
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
import utility.dapi.CeeData;
import utility.dapi.DeliveryApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class deliveryAppTeamCases extends BaseTest {

    @DescriptionProvider(author = "tushar", description = "Verify login, store switching, cee checkin and config in delivery app ", slug = "Delivery App")
    @Test(groups = { "delivery_bbnow","bbnow"})
    public void checkAppCases() throws InterruptedException, IOException, NotFoundException {
        AutomationReport report = getInitializedReport(this.getClass(), true);

        // fetching cee username and pswd
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        int sa_id = Config.bbnowConfig.getInt("bbnow_stores[1].sa_id");
        Map<String, String> ceeData = CeeData.getCeeDetailsMap(env.toUpperCase(),sa_id);



        // do admin login --> force cee logout ---> fetching store QR code
        WebDriver driver = report.driver;
        launchSite("admin", driver);
        String[] cred = AutomationUtilities.getUniqueAdminUser(serverName,"admin-superuser-mfa");
        String adminUser = cred[0];
        String adminPassword = cred[1];
        report.log("Credentials:"+cred,true);
        Login login = new Login(driver,report);
        HomePage homePage = login.doAdminLogin(adminUser,adminPassword);
        TransportManagementSystemPage transportManagementSystemPage = homePage.navigateToTransportManagement();
        //transportManagementSystemPage.forceCeeLogoutUsingCeeId(ceeData.get("username"));
        CeeData.doForceLogOut(env,Integer.parseInt(ceeData.get("id")));

        String qrCode = transportManagementSystemPage.scanQRCode("BBNow_Shanthinikatan-B-SA","BIKE");
        report.log("qr"+ qrCode,true);

        // placing order -->Picking ---> Binning
        String[] userCred = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        String entrycontext= Config.bbnowConfig.getString("entry_context");
        String entrycontextid=Config.bbnowConfig.getString("entry_context_id");
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");
        int fcId = Config.bbnowConfig.getInt("bbnow_stores[1].fc_id");
        String searchTerm = Config.bbnowConfig.getString("bbnow_stores[1].category_slug1");
        String searchType = Config.bbnowConfig.getString("bbnow_stores[1].search_type2");
        Map<String, String> adminCookie = AdminCookie.getMemberCookie(adminUser, adminPassword, report);
        Map<String,String> memberCookie = MemberCookie.getMemberCookieForCustomAddress(userCred[0],areaName,report);
        int skuID =  EligibleSkuMethods.fetchSkuVisibleAndAvailableInPrimaryBin(memberCookie,adminCookie,
                fcId,entrycontext,Integer.parseInt(entrycontextid),areaName,searchTerm,searchType,report);
        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID), 2);
        String orderId = OrderPlacement.placeBBNowOrder("bbnow" , 10,userCred[0], areaName, skuMap, true, false, report);
        report.log("order Id:" + orderId ,true);
        PickingMethods.pickingFlowModified(Integer.parseInt(orderId), fcId, adminUser, adminCookie, report);
        DeliveryBinMethods.perfomOrderBinMapping(fcId,Integer.parseInt(orderId),"binning",adminCookie,report);


        // calling dapi
        DeliveryApp deliveryApp =new DeliveryApp(report);
        deliveryApp.checkCeeeId(ceeData.get("id")).login(Integer.parseInt(ceeData.get("id")),ceeData.get("password"),qrCode);

    }
}
