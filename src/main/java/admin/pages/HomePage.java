package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import planogram.admin.wio.FcReceivingSearch;
import planogram.admin.wio.WIOAdminUI;
import planogram.admin.wio.WIOSAdministrationUI;

public class HomePage extends WebSettings {

    private static Logger logger = Logger.getLogger(HomePage.class);

    @FindBy(linkText = "IRCDC Order actions")
    WebElement ircdcOrderAction;

    @FindBy(linkText = "IRCDC Order")
    WebElement ircdcOrder;

    @FindBy(xpath = "//a[contains(text(),'Microservices URLs')]")
    WebElement microservicesURLsLink;

    @FindBy(xpath = "//a[contains(text(),'Home')]")
    WebElement homepagelink;
    @FindBy(xpath = "//*[contains(text(),'Welcome,')] | //*[contains(text(),'WELCOME,')] | //*[contains(text(),'welcome,')] | //a[contains(text(),'Home')]")
    WebElement welcome;

    private WebDriver driver;
    private AutomationReport report;
    private WebDriverWait wait;

    public HomePage(WebDriver driver, AutomationReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }




    /**
     * this function click on IRCDC order action link
     *
     * @return
     * @throws InterruptedException
     */

    public IrcdcOrderAction clickIrcdcOrderAction(WebDriver driver, AutomationReport report) throws InterruptedException {
        driver.navigate().refresh();
        wait.until(ExpectedConditions.elementToBeClickable(ircdcOrderAction));
        Thread.sleep(2000);
        ircdcOrderAction.click();
        report.log( "clicked ircdc order action", true);

        return new IrcdcOrderAction(driver,report );
    }

    public IrcdcOrder clickIrcdcOrder(WebDriver driver, AutomationReport report) throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(ircdcOrder));
        Thread.sleep(2000);
        ircdcOrder.click();
        report.log( "clicked ircdc order action", true);

        return new IrcdcOrder(driver,report );
    }
    public Microservices clickOnMicroservicesURLs() {
        wait.until(ExpectedConditions.visibilityOf(microservicesURLsLink));
        clickElement(microservicesURLsLink, driver);
        report.log( "Clicked On Microservices URL link", true);
        return new Microservices(driver, report);
    }

    public HomePage navigatetohomepage(){
        HomePage homePage=new HomePage(driver,report);
        homePage.homepagelink.click();
        wait.until(ExpectedConditions.visibilityOf(welcome));
        return new HomePage(driver,report);
    }

    public WIOSAdministrationUI navigateToPlanogramAdmin(WebDriver driver, AutomationReport report) {
        HomePage homePage=new HomePage(driver,report);
        Login login=new Login(driver,report);
        //Navigating to planogram ui admin page
        report.log( "Navigating to planogram ui admin page", true);
        Microservices microservices = homePage.clickOnMicroservicesURLs();
        PlanogramMicroservices planogramMicroservices = microservices.clickOnPlanogram();
        PlanogramAdminUI planogramAdminUI = planogramMicroservices.clickOnPlanogramLink();
        if (driver.getTitle().contains("bigbasket Admin")) {
            login.doAdminLogin(adminSuperuser,adminSuperuserPassword);
        }

        WIOAdminUI wioAdminUI=planogramAdminUI.clickonWIOAdminLink();
        WIOSAdministrationUI wiosAdministrationUI=wioAdminUI.clickonWIOSadminlink();

        return  new WIOSAdministrationUI(driver,report);
    }



}
