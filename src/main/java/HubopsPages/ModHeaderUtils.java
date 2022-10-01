package HubopsPages;

import com.bigbasket.automation.reports.IReport;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class ModHeaderUtils {
    WebDriver driver;
    private IReport report;
    private WebDriverWait wait;

    @FindBy(name="header-name")
    public WebElement header1TextBox;
    @FindBy(xpath="(//input[@name='header-name'])[2]")
    public WebElement header2TextBox;
    @FindBy(name="header-value")
    public WebElement header1Value;
    @FindBy(xpath="(//input[@name='header-value'])[2]")
    public WebElement header2Value;
    @FindBy(id="add-button")
    public WebElement addbutton;
    @FindBy(id="add-request-header")
    public WebElement addRequestHeader;


    public ModHeaderUtils(WebDriver driver, IReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }
    public void modHeaderProfiles(WebDriver driver,String appName) throws InterruptedException {
        String xProject = System.getProperty("X-Project","bb-tms");
        String xTenantId = System.getProperty("X-Tenant-Id","2");
        driver.get("chrome-extension://idgpnmonknjnojddfkpgkljpfnnfcklj/app.html");
        wait.until(ExpectedConditions.visibilityOf(header1TextBox));
        header1TextBox.sendKeys("X-Project");
        header1Value.sendKeys(xProject);
        addbutton.click();
        addRequestHeader.click();
        header2TextBox.sendKeys("X-Tenant-Id");
        report.log("Launching the croma", true);
        if(appName.equals("croma")) {
            header2Value.sendKeys("2");
        }else{
            header2Value.sendKeys("1");
        }
        Thread.sleep(5000);
    }
}
