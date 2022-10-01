package bb2UI;

import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.io.IOException;

public class HomePage extends BaseTest {

    @DescriptionProvider(slug = "Verify Home Page UI", description = "1. Do web login via email number  \n" +
            "            2.Verify basic elements of 2.0 Home Page ",author = "Shruti")

    @Test(groups = {"2.0checkout","prod"})

    public void verifyHomePage() throws IOException, InterruptedException {

        AutomationReport report = getInitializedReport(this.getClass(), true);

        WebDriver driver = report.driver;
        launchSite("web", driver);

        WebLogin webLogin = new WebLogin(driver,report);
        String default_address=bb2UiProperties.getProperty("default_address");
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName,bb2UiProperties.getProperty("member_sheet_name"));
        report.log("login creds "+creds,true);

        BigBasketApp app = new BigBasketApp(report);
        app.getAppDataDynamic().login(creds[0]).emptyCart();

        WebHomePage webHomePage = webLogin.customerLoginViaMobileNum(creds[0]);
        webHomePage.closeChat();
        webHomePage.changeAddressPostLogin(default_address);
        webHomePage.verifyHomePageUI();
        webHomePage.goToMyOrders();

    }

}

