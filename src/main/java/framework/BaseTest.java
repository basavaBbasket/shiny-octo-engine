package framework;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.AutomationReportListener;
import com.bigbasket.automation.utilities.Libraries;
import com.bigbasket.automation.utilities.webdriver.WebDriverUtilities;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import static com.bigbasket.automation.WebSettings.browser;

public class BaseTest extends Libraries {

    Logger logger = Logger.getLogger(BaseTest.class);
    AutomationReportListener obj = new AutomationReportListener();

    public AutomationReport getInitializedReport(Class<?> testClass, boolean setupDriver) {
        AutomationReport report = obj.getInitializedReport(testClass);
        if (setupDriver) {
            WebDriver driver = WebDriverUtilities.getSimpleWebDriver(browser);
            WebDriverUtilities.maximizeDriver(driver);
            report.setWebDriver(driver);
        }
        return report;
    }

    public AutomationReport getInitializedReport(Class<?> testClass, String postFix,boolean setupDriver, Class<?>... class1 ) {
        AutomationReport report = obj.getInitializedReport(testClass, postFix, class1);
        if (setupDriver) {
            WebDriver driver = WebDriverUtilities.getSimpleWebDriver(browser);
            WebDriverUtilities.maximizeDriver(driver);
            report.setWebDriver(driver);
        }
        return report;
    }
}