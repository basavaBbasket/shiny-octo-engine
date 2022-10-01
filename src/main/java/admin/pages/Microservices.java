package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.IReport;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Microservices extends WebSettings {
    private Logger logger = Logger.getLogger(Microservices.class);

    @FindBy(xpath = "//a[contains(text(),'planogram')]")
    WebElement planogramLink;

    @FindBy(xpath = "//a[contains(text(),'consolidation')]")
    WebElement consolidationLink;

    private WebDriver driver;
    private IReport report;
    private WebDriverWait wait;

    public Microservices(WebDriver driver, IReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }


    public PlanogramMicroservices clickOnPlanogram() {
        wait.until(ExpectedConditions.visibilityOf(planogramLink));
        clickElement(planogramLink, driver);
        report.log("Clicked On planogram link", true);
        return new PlanogramMicroservices(driver, report);
    }

}
