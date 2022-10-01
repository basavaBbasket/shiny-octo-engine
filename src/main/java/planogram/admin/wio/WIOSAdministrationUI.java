package planogram.admin.wio;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.IReport;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class WIOSAdministrationUI extends WebSettings {

    @FindBy(xpath = "//a[contains(text(), '1-click GRN')]")
    WebElement oneclickgrnlink;

    @FindBy(xpath = "//a[contains(text(), 'Fc Return Upload')]")
    WebElement fcreturnupload;

    private WebDriver driver;
    private IReport report;
    private WebDriverWait wait;

    public WIOSAdministrationUI(WebDriver driver, IReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }



    public FcReceivingSearch clickOnOneCLickGRNLink(){
        wait.until(ExpectedConditions.visibilityOf(oneclickgrnlink));
        clickElement(oneclickgrnlink,driver);
        report.log( "Clicked On oneclickgrnlink link", true);
        return  new FcReceivingSearch(driver,report);
    }

   public FcReturnUploadUi clickOnFcReturnUploadLink() {
       wait.until(ExpectedConditions.visibilityOf(fcreturnupload));
       clickElement(fcreturnupload,driver);
       report.log( "Clicked On fcreturnupload link", true);
       return new FcReturnUploadUi(driver,report);
   }
}
