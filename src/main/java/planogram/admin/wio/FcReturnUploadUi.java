package planogram.admin.wio;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FcReturnUploadUi extends WebSettings {

    @FindBy(xpath="//*[@id=\"id_fc\"]")
    WebElement fcdropdown;

    @FindBy(xpath = "//*[@id=\"id_upload_file\"]")
    WebElement uploadfile;



    private WebDriver driver;
    private AutomationReport report;
    private WebDriverWait wait;

    public FcReturnUploadUi(WebDriver driver, AutomationReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }
}
