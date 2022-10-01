package bbnow.pmcases;

import admin.pages.HomePage;
import admin.pages.Login;
import admin.pages.Order;
import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminOrder extends BaseTest {

    @DescriptionProvider(slug = "Order Admin Page", description = "1. Place a BBnow Order \n" +
            "            2. Go to Order Page in Admin\n" +
            "            3. Verfiy channel and Source \n" +
            "            4. Check Allowed Options for BBnow in Order Page. \n" ,author = "tushar")

    @Test(groups = {"Order-Admin-Page","bbnow"})
    public void orderAdminCase() throws InterruptedException, IOException {

        AutomationReport report = getInitializedReport(this.getClass(), true);


        // Creating order and fetching order ID

        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        report.log("Starting  order placement.", true);
        String entrycontext= Config.bbnowConfig.getString("entry_context");
        String entrycontextid=Config.bbnowConfig.getString("entry_context_id");
        String clientUserSheetName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_client_user_sheet_name");
        String areaName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_area_name");
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));
        String searchTerm = Config.bbnowConfig.getString("bbnow_stores[1].category_slug1");
        String searchType = Config.bbnowConfig.getString("bbnow_stores[1].search_type2");

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> adminCookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        Map<String,String> memberCookie = MemberCookie.getMemberCookieForCustomAddress(cred[0],areaName,report);

        int skuID =  EligibleSkuMethods.fetchSkuVisibleAndAvailableInPrimaryBin(memberCookie,adminCookie,
                fcId,entrycontext,Integer.parseInt(entrycontextid),areaName,searchTerm,searchType,report);

        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID), 2);
        String orderId = OrderPlacement.placeBBNowOrder("bbnow" , 10,cred[0], areaName, skuMap, true, false, report);

        // Order Admin
        WebDriver driver = report.driver;
        launchSite("admin", driver);
        report.log("Credentials:"+cred,true);
        Login login = new Login(driver,report);
        HomePage homePage = login.doAdminLogin(adminUserName,adminPassword);
        Order order = homePage.navigateToOrder(orderId);

        ArrayList<Boolean> checkElementIsPresent = order.verifySourceShowsEntryContextAsBB();
        for(boolean i: checkElementIsPresent) {
            Assert.assertEquals(i, true);
        }
        report.log("checking Allowed options for bbnow {channel , source}",true);

        ArrayList<Boolean> allowedOptionsList = order.checkAllowedOptionsInBBNow();
        for(boolean i: allowedOptionsList) {
            Assert.assertEquals(i, true);
        }
        report.log("Verified allowed options in bbnow order admin page",true);

        // Commenting R&E function as it is not part of BBnow phase1

        /**

        ArrayList<Boolean> disabledCheckBox = order.checkPickUpAndReplacementOptionsAreDisabled();
        for(boolean i: disabledCheckBox) {
            Assert.assertEquals(i, false);
        }

        report.log("Verified Pickup and replcament options",true);

         **/

    }
}
