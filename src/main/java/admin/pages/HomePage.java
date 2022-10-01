package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import org.apache.log4j.Logger;
import org.openqa.selenium.Cookie;
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
    @FindBy(xpath = "//*[@id='content']/form/table[1]/tbody/tr[2]/td[2]/input")
    WebElement enterOrder;

    @FindBy(xpath = "//*[@id='content']/form/table[2]/tbody/tr[7]/td[2]/input")
    WebElement showOrders;

    @FindBy(xpath = "//*[@id='data-table']/tbody/tr/td[1]/a")
    WebElement orderIdLink;

    @FindBy(xpath = "//*[@id=\"head_pane\"]/h2")
    WebElement mapMemberAddress;

    @FindBy(xpath = "//*[@id=\"member-address-module\"]/ul/li[1]/a")
    WebElement mma;

    @FindBy(xpath = "//a[normalize-space()='Transport Management System']")
    WebElement transportManagement;



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

    public Order navigateToOrder(String orderId) throws InterruptedException {

        String url = driver.getCurrentUrl();
        String newurl = url + "order/order/";
        driver.get(newurl);
        wait.until(ExpectedConditions.visibilityOf(enterOrder));
        enterOrder.sendKeys(orderId);
        report.log("Entering Order Id:"+ orderId,true);
        showOrders.click();
        wait.until(ExpectedConditions.visibilityOf(orderIdLink));
        orderIdLink.click();
        report.log("Clicked Order info",true);

        String handlewindow = (String) driver.getWindowHandles().toArray()[1];
        driver.switchTo().window(handlewindow);

        return new Order(driver,report);

    }

    public MapMemberAddressPage navigateToMapMemberAddress()
    {
        String url = driver.getCurrentUrl();

        String newUrl = url.replaceFirst(".com/",".com/member-svc/") ;
        newUrl+="address/locate/";
        driver.get(newUrl);
        //mma.click();
        wait.until(ExpectedConditions.visibilityOf(mapMemberAddress));
        Cookie ck = new Cookie("csrftoken", "lrRSGGHOvAFCh8eYKpgsDuO1KFVZiXCkchFvkWSkis6rgH4XO3wUzXp7n4ysVuHz");
        driver.manage().addCookie(ck);
        return  new MapMemberAddressPage(driver,report);
    }

    public TransportManagementSystemPage navigateToTransportManagement()
    {
         transportManagement.click();
         return new TransportManagementSystemPage(driver,report);
    }



}
