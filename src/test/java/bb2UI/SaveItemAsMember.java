package bb2UI;

import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.io.IOException;

public class SaveItemAsMember extends BaseTest {

    @DescriptionProvider(slug = "Add Save For Later Item in basket", description = "1. Do web login via email number  \n" +
            "            2. Search sku then go to PD page" +
            "            3. Save item for later" +
            "            4. Go to cart and add saved item to cart"+
            "            5. Do checkout and verify payment option",author = "Shruti")

    @Test(groups = {"2.0checkout","prod"})

    public void saveItemAsMember() throws IOException, InterruptedException {

        AutomationReport report = getInitializedReport(this.getClass(), true);

        WebDriver driver = report.driver;
        launchSite("web", driver);

        WebLogin webLogin = new WebLogin(driver,report);
        String default_address=bb2UiProperties.getProperty("default_address");
        String product= bb2UiProperties.getProperty("product1");
        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName,bb2UiProperties.getProperty("member_sheet_name"));
        report.log("login creds "+creds,true);

        BigBasketApp app = new BigBasketApp(report);
        app.getAppDataDynamic().login(creds[0]).emptyCart();

        WebHomePage webHomePage = webLogin.customerLoginViaMobileNum(creds[0]);
        webHomePage.closeChat();
        webHomePage.changeAddressPostLogin(default_address);

        SearchAndAddToCart search = new SearchAndAddToCart(driver,report);
        search.searchProductGoToPdPageAndSaveItForLater(product);


        Checkout checkout=new Checkout(driver,report);
        checkout.goToCart();
        checkout.addSavedItemIncart();
        checkout.doCheckout();
        checkout.continuePayment();
 }

}
