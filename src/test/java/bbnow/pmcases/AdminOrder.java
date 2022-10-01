package bbnow.pmcases;

import admin.pages.HomePage;
import admin.pages.Login;
import admin.pages.Order;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;

public class AdminOrder extends BaseTest {

    @DescriptionProvider(author = "Tushar", description = "This is Admin Case ", slug = "Admin Order Cases")
    @Test(groups = {"bbnow"})
    public void orderAdminCase() throws InterruptedException, IOException {

        AutomationReport report = getInitializedReport(this.getClass(), true);


        // Creating order and fetching order ID

        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[0].member_sheet_name"));
        report.log("Starting  order placement.", true);
        String searchTerm1 = Config.bbnowConfig.getString("bbnow_stores[0].search_term1");
        String searchTerm2 = Config.bbnowConfig.getString("bbnow_stores[0].search_term2");
        String[] searchTerms = {searchTerm1,searchTerm2};
        String areaName = Config.bbnowConfig.getString("bbnow_stores[0].area");
        String orderId = OrderPlacement.placeBBNowOrder("bbnow" , 10 , creds[0], areaName, 1, searchTerms, true, false, report);
        report.log("Order Id: " + orderId,true);


       // String orderId = "1001050891";

       // Order Admin
        WebDriver driver = report.driver;
        launchSite("admin", driver);
        String[] cred = AutomationUtilities.getUniqueAdminUser(serverName,"admin-superuser-mfa");
        String adminUser = cred[0];
        String adminPassword = cred[1];
        report.log("Credentials:"+cred,true);
        Login login = new Login(driver,report);
        HomePage homePage = login.doAdminLogin(adminUser,adminPassword);
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

        ArrayList<Boolean> disabledCheckBox = order.checkPickUpAndReplacementOptionsAreDisabled();
        for(boolean i: disabledCheckBox) {
            Assert.assertEquals(i, false);
        }

        report.log("Verified Pickup and replcament options",true);





    }
}
