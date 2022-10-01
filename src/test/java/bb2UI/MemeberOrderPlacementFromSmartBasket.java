package bb2UI;

import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.io.IOException;

public class MemeberOrderPlacementFromSmartBasket extends BaseTest {

    @DescriptionProvider(slug = "BB2.0 order placement (till checkout)", description = "1. Do web login via mobile number  \n" +
            "            2. Change Address to 2.0 \n" +
            "            3. Go to Smart Basket \n" +
            "            4. Add First Suggested SKU from smart basket\n" +
            "            5. Do Checkout \n" +
            "            6. Verify payment option is there \n",author = "Shruti")

    @Test(groups = {"2.0checkout","prod"})

    public void placeOrderFromAutoSearch() throws IOException, InterruptedException {
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
        webHomePage.goToMySmartBasket();

        SearchAndAddToCart search = new SearchAndAddToCart(driver,report);
        search.addSKUFromSmartBasket();

        Checkout checkout=new Checkout(driver,report);
        checkout.goToCart();
        checkout.doCheckout();
        checkout.continuePayment();
    }

}


