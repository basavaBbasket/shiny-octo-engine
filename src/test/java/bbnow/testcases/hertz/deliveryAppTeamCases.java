package bbnow.testcases.hertz;

import admin.pages.HomePage;
import admin.pages.Login;
import admin.pages.TransportManagementSystemPage;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import utility.dapi.DeliveryApp;

import java.io.IOException;

public class deliveryAppTeamCases extends BaseTest {

    @DescriptionProvider(author = "tushar", description = "Verify login, store switching, cee checkin and config in delivery app ", slug = "Delivery App")
    @Test(groups = { "bbnow"})

    public void checkAppCases() throws InterruptedException, IOException {
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
        transportManagementSystemPage.forceCeeLogout();


        DeliveryApp deliveryApp =new DeliveryApp(report);
        deliveryApp.checkCeeeId("550001").login(550001,"1");

    }
}
