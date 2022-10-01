package bb2UI;

import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.io.IOException;

public class MemberOrderPlacementFromPdPageTillCheckout extends BaseTest {

    @DescriptionProvider(slug = "Order Placement from PD Page", description = "1. Do web login via mobile number  \n" +
            "            2. Search sku then go to PD page and add it to the cart" +
            "            3. Check Price in PD page" +
            "            4. proceed and do increment/decrement from PD and check sku price at checkout" ,author = "Shruti")

    @Test(groups = {"2.0checkout","prod"})

    public void doOrderPlacementFromPdPage() throws IOException, InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(), true);
        String priceInPSBasket;
        String priceAtCheckout;
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
        priceInPSBasket=search.searchProductGoToPdPageAndAdd(product);
        search.incrementItemFromPD();
        search.decrementItemFromPD();

        Checkout checkout=new Checkout(driver,report);
        checkout.goToCart();
        priceAtCheckout=checkout.getSKUPriceinCheckoutPage();
        if(priceAtCheckout.contains(String.valueOf(priceInPSBasket))){
            report.log("Prices are similar in PS & Checkout Page",true);
        }
        checkout.doCheckout();
        checkout.continuePayment();

    }

}
