package foundation.catalog.admin.Utils;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Class to navigate across the new catalog admin
 */
public class MenuOptions extends WebSettings
{

    /**
     * xpaths
     */
    @FindBy(xpath = "//*[@id=\"root\"]/div/div/div/header/div/button[2]")
    private WebElement RefreshButton;

    @FindBy(xpath = "(//*[contains(text(), 'Dashboard')])[1]")
    private  WebElement DashboardButton;

    @FindBy(xpath = "//*[@id=\"username\"]")
    private WebElement LoginBox;

    @FindBy(xpath = "//*[@id=\"password\"]")
    private WebElement passwordBox;

    @FindBy(xpath = "//*[contains(text(), 'Sign in')]")
    private WebElement SignInButton;



    private WebDriver driver;
    private AutomationReport report;
    private WebDriverWait wait;

    /**
     * Setting up drivers and custom report for the menu option class
     * @param driver
     * @param report
     */
    public MenuOptions(WebDriver driver, AutomationReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }

    /**
     * Method to navigate across new catalog admin using the menu option name.
     * It uses name of the menu to find Xpath and then clicks on that menu button.
     * Use this only if the menu you want to select has unique name
     * @param menuName name of the menu item to be clicked on
     */
    public void navigateByMenuName(String menuName){
        String xpath = "//*[contains(text(),'"+menuName+"')]";
        WebElement MenuElement = driver.findElement(By.xpath(xpath));
        MenuElement.click();
    }


    /**
     * Navigate to the home page of catalog admin. Its also the Dashboard for available services.
     * @return this class to use current state of the class
     * @throws InterruptedException
     */
    public MenuOptions navigateToDashboard() throws InterruptedException {
        Thread.sleep(2000);
        DashboardButton.click();
        return this;
    }


    /**
     * to get an web driver element for the menu name specified
     * @param menuName The name for which web driver element is required
     * @return Web driver element of the given name
     */
    public WebElement getMenuXpath(String menuName){
        String xpath = "//*[contains(text(),'"+menuName+"')]";
        return driver.findElement(By.xpath(xpath));
    }

    /**
     * Navigate to the catalog status page where we check the current status of an SKU
     * @return MenuOption class object to keep track of current state of page
     * @throws InterruptedException
     */
    public MenuOptions NavigateToCatalogStatus() throws InterruptedException {
        Thread.sleep(1000);
        navigateByMenuName("Catalog FC");
        wait.until(ExpectedConditions.visibilityOf(getMenuXpath("Catalog Status")));
        navigateByMenuName("Catalog Status");
        wait.until(ExpectedConditions.visibilityOf(RefreshButton));
        return this;
    }

    /**
     * Navigate to the Upload page of catalog admin. Here we upload various files for catalog functions
     * @return MenuOption class object to keep track of current state of page
     * @throws InterruptedException
     */
    public MenuOptions NavigateToUploadPage() throws InterruptedException {
        Thread.sleep(1000);
        navigateByMenuName("Catalog FC");
        wait.until(ExpectedConditions.visibilityOf(getMenuXpath("Catalog Status")));
        navigateByMenuName("Catalog Status");
        wait.until(ExpectedConditions.visibilityOf(RefreshButton));
        return this;
    }

    /**
     * To parse the Base url of catalog admin. It used regex to parse the dashboard link of catatog admin
     * @param endpoint The URL from which the base URL has to be parsed
     */
    public void NavigateByURL(String endpoint){
        /*String CurrentURL = getCurrentURL(this.driver);
        Pattern r = Pattern.compile(".+\\/#\\/");
        Matcher m = r.matcher(CurrentURL);
        String BaseURL="";
        while (m.find( ))
        {
            BaseURL = m.group();
        }
        driver.get(BaseURL+endpoint);*/
    }


    public void Login(String username,String password) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(LoginBox));
        //report.log(isScreenshotRequired, "clicking on upload type button", true);
        LoginBox.sendKeys(username);
        passwordBox.sendKeys(password);
        Thread.sleep(4000);
        SignInButton.click();
        wait.until(ExpectedConditions.visibilityOf(RefreshButton));
    }
}
