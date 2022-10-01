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
import planogram.admin.wio.WIOAdminUI;

public class PlanogramAdminUI extends WebSettings {
    private Logger logger = Logger.getLogger(PlanogramAdminUI.class);

    @FindBy(xpath = "//a[contains(text(),'Global_Planogram')]")
    WebElement globalPlanogramLink;

    @FindBy(xpath = "//a[contains(text(),'Warehouse Planogram')]")
    WebElement warehousePlanogramLink;

    @FindBy(xpath = "//a[contains(text(), 'Cycle_Count')]")
    WebElement cycleCountLink;

    @FindBy(xpath = "//a[contains(text(), 'Wios Admin')]")
    WebElement wioadmin;


    private WebDriver driver;
    private IReport report;
    private WebDriverWait wait;

    public PlanogramAdminUI(WebDriver driver, IReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }

    public WIOAdminUI clickonWIOAdminLink(){
        wait.until(ExpectedConditions.visibilityOf(wioadmin));
        clickElement(wioadmin,driver);
        report.log( "Clicked On warehouse planogram admin link", true);

      return  new WIOAdminUI(driver,report);
    }


}
