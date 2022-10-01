package planogram.admin.wio;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WIOAdminUI extends WebSettings {

    @FindBy(xpath = "//a[contains(text(), 'Wios')]")
    WebElement wiosadminlink;

    private WebDriver driver;
    private AutomationReport report;
    private WebDriverWait wait;

    public WIOAdminUI(WebDriver driver, AutomationReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }

    public WIOSAdministrationUI clickonWIOSadminlink(){
        wait.until(ExpectedConditions.visibilityOf(wiosadminlink));
        clickElement(wiosadminlink,driver);
        report.log( "Clicked On warehouse inbound outbound admin [wios] link", true);

        return new WIOSAdministrationUI(driver,report);
    }



}
