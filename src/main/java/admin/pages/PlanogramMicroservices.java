package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PlanogramMicroservices extends WebSettings {
    private Logger logger = Logger.getLogger(PlanogramMicroservices.class);

    @FindBy(xpath = "//a[contains(text(),'Rack Variant upload')]")
    WebElement rackVariantUpload;

    @FindBy(xpath = "//a[contains(text(),'Planogram')]")
    WebElement planogram;

    private WebDriver driver;
    private AutomationReport report;
    private WebDriverWait wait;

    public PlanogramMicroservices(WebDriver driver, AutomationReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }



    public PlanogramAdminUI clickOnPlanogramLink() {
        wait.until(ExpectedConditions.visibilityOf(planogram));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        //String xpath = "a[href='https://" + serverName.split("@")[1] + "/planogram-ui/admin/']";
        String microservicePointingServer = planogram.getAttribute("href");
        String xpath = "a[href='"+microservicePointingServer+"']";
        js.executeScript("document.querySelector(\"" + xpath + "\").setAttribute('target','');");
        clickElement(planogram, driver);
        report.log( "Clicked On Planogram link", true);
        return new PlanogramAdminUI(driver, report);
    }

}