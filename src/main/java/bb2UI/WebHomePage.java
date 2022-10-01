package bb2UI;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import framework.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class WebHomePage extends WebSettings {

    private WebDriver driver;
    private IReport report;
    private WebDriverWait wait;


    @FindBy(xpath = "//*[@class='dropdown-menu latest-at-bb' and not(@style)]//i[@class='caret pull-right']")
    WebElement cityDropdown;

    @FindBy(xpath = "//*[@qa='areaDD']")
    WebElement locationDropdDown;

    @FindBy(xpath = "//*[@qa='cityDD']/input[1]")
    WebElement enterCity;

    @FindBy(xpath = "//div[@class='form-group area-autocomplete area-select ng-scope']//input[@qa='areaInput']")
    WebElement enterArea;

    @FindBy(xpath = "//ul[contains(@id,'typeahead')]")
    WebElement areaList;

    @FindBy(xpath = "//button[text()='Continue']")
    WebElement continueAddress;

    @FindBy(xpath = "//a[@class='bb-logo change-logo hidden-xs hidden-sm']")
    WebElement homePageBbLogo;

    @FindBy(xpath = "//button[text()='CONTINUE']")
    WebElement continueSelectedAddress;


    @FindBy(xpath = "//button[text()='Got it']")
    WebElement gotITBttn;

    @FindBy(xpath = "//a[@title='Close chat']")
    WebElement closeChat;

    @FindBy(xpath = "//button[text()='Yes']")
    WebElement confirmPopup;

    @FindBy(xpath = "//*[name()='g' and contains(@mask,'url(#Avata')]//*[name()='path' and contains(@fill,'#fff')]")
    WebElement userAccount;

    @FindBy(xpath = "//div[contains(@class,'MemberDropdown')]//ul//li//a//span[text()='My Orders']")
    WebElement myOrders;

    @FindBy(xpath = "//div[contains(@class,'MemberDropdown')]//ul//li//a//span[text()='My Smart Basket']")
    WebElement mySmartBasket;

    @FindBy(xpath = "//div[@class='grid place-content-start grid-flow-col gap-x-6']//a//*[name()='svg']//*[name()='path'][4]")
    WebElement BBlogo;

    @FindBy(xpath = "//div[@class='Header___StyledQuickSearch2-sc-19kl9m3-0 bytLxG']//input[@placeholder='Search for Products...']")
    WebElement searchBar;

    @FindBy(xpath ="//div[@class='pl-4 pt-4']")
    WebElement ordersPage;

    public WebHomePage(WebDriver driver, IReport report) {

        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 120);
    }

    public void changeAddressWithoutLogin(String city, String area) throws InterruptedException {


        wait.until(ExpectedConditions.elementToBeClickable(locationDropdDown));
        locationDropdDown.click();

        wait.until(ExpectedConditions.visibilityOf(cityDropdown));
        cityDropdown.click();

        wait.until(ExpectedConditions.visibilityOf(enterCity));
        enterCity.sendKeys(city);
        enterCity.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.visibilityOf(enterArea));
        enterArea.sendKeys(area);
        wait.until(ExpectedConditions.visibilityOf(areaList));
        enterArea.sendKeys(Keys.DOWN);
        enterArea.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.elementToBeClickable(continueAddress));
        continueAddress.click();

        wait.until(ExpectedConditions.visibilityOf(homePageBbLogo));
        report.log("Address successfully changed", true);

    }

    public void changeAddressPostLogin(String default_city) throws InterruptedException {

        Thread.sleep(3000);
        wait.until(ExpectedConditions.elementToBeClickable(locationDropdDown));
        locationDropdDown.click();

        String xpath = "//span[text()='" + default_city + "']";
        driver.findElement(By.xpath(xpath)).click();

        wait.until(ExpectedConditions.elementToBeClickable(continueSelectedAddress));
        continueSelectedAddress.click();

        wait.until(ExpectedConditions.elementToBeClickable(gotITBttn));
        gotITBttn.click();

        report.log("Address successfully changed to " + default_city, true);

    }

    public void closeChat() {

        try {
            Thread.sleep(6000);
            wait.until(ExpectedConditions.visibilityOf(closeChat));
            closeChat.click();
            wait.until(ExpectedConditions.elementToBeClickable(confirmPopup));
            confirmPopup.click();
        } catch (Exception e) {
        }

    }

    public void goToMyOrders() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(userAccount));
        userAccount.click();
        Thread.sleep(2000);
        wait.until(ExpectedConditions.elementToBeClickable(myOrders));
        myOrders.click();
        Thread.sleep(2000);
        report.log("Navigated to My Active Orders", true);
        wait.until(ExpectedConditions.visibilityOf(ordersPage));
        report.log("Orders Page is properly loaded", true);
    }

    public void goToMySmartBasket() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(userAccount));
        userAccount.click();
        Thread.sleep(2000);
        wait.until(ExpectedConditions.elementToBeClickable(mySmartBasket));
        mySmartBasket.click();
        Thread.sleep(2000);
        report.log("Navigated to Smart Basket", true);
    }

    public void verifyHomePageUI() {

        wait.until(ExpectedConditions.visibilityOf(BBlogo));
        report.log("BB logo is present on Home Page", true);
        wait.until(ExpectedConditions.visibilityOf(searchBar));
        report.log("Search bar is present on Home Page", true);
        List<WebElement> Banners = driver.findElements(By.xpath("//div[contains(@class,'Carousel___StyledDiv')][1]"));
        report.log("Banners are coming on Home Page", true);

    }
}
