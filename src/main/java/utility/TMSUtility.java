package utility;

import admin.pages.HomePage;
import admin.pages.Login;
import admin.pages.TransportManagementSystemPage;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import com.bigbasket.automation.utilities.Libraries;
import com.bigbasket.automation.utilities.webdriver.WebDriverUtilities;
import framework.BaseTest;
import org.openqa.selenium.WebDriver;

import java.util.Arrays;

public class TMSUtility extends BaseTest {
    public static String getCommonStoreQRCode(String serviceabilityArea, String deliveryMode, IReport report) {
        headless=true;
        WebDriver driver = WebDriverUtilities.getSimpleWebDriver(browser);
        WebDriverUtilities.maximizeDriver(driver);
        String qrCode = null;
        try {
            Libraries libraries = new Libraries();
            libraries.launchSite("admin", driver);
            String[] cred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
            String adminUser = cred[0];
            String adminPassword = cred[1];
            report.log("Credentials:" + Arrays.toString(cred), true);
            Login login = new Login(driver, report);
            HomePage homePage = login.doAdminLogin(adminUser, adminPassword);
            TransportManagementSystemPage transportManagementSystemPage = homePage.navigateToTransportManagement();
            qrCode = transportManagementSystemPage.scanQRCode(serviceabilityArea, deliveryMode);
            System.out.println("Common Store QR Code Value for \n" +
                    "Serviceability: " + serviceabilityArea + " \n" +
                    "Delivery mode: " + deliveryMode + "\n" +
                    "is: " + qrCode);
            report.log("Common Store QR Code Value for \n" +
                    "Serviceability: " + serviceabilityArea + " \n" +
                    "Delivery mode: " + deliveryMode + "\n" +
                    "is: " + qrCode, true);
            driver.quit();
            return qrCode;
        } catch (Exception e) {
            driver.quit();
        }
        return qrCode;
    }

    public static void ceeForceLogoutUsingCeeId(String ceeId, IReport report) {
        headless=true;
        WebDriver driver = WebDriverUtilities.getSimpleWebDriver(browser);
        WebDriverUtilities.maximizeDriver(driver);
        try {
            Libraries libraries = new Libraries();
            libraries.launchSite("admin", driver);
            String[] cred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
            String adminUser = cred[0];
            String adminPassword = cred[1];
            report.log("Credentials:" + Arrays.toString(cred), true);
            Login login = new Login(driver, report);
            HomePage homePage = login.doAdminLogin(adminUser, adminPassword);
            TransportManagementSystemPage transportManagementSystemPage = homePage.navigateToTransportManagement();
            transportManagementSystemPage.forceCeeLogoutUsingCeeId(ceeId);
            driver.quit();
        } catch (Exception e) {
            driver.quit();
        }
    }
}
