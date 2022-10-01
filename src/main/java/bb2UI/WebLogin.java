package bb2UI;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class WebLogin extends WebSettings {

    private WebDriver driver;
    private IReport report;
    private WebDriverWait wait;

    ExpectedCondition<Boolean> expectation;

    @FindBy(xpath = "//a[@ng-show='vm.newLoginFlow']")
    WebElement loginBtn;

    @FindBy(xpath = "//input[@id='otpEmail']")
    WebElement enterMobileNo;

    @FindBy(xpath = "//button[@type='submit'][normalize-space()='Continue']")
    WebElement continueBtn;

    @FindBy(xpath = "//*[@id='otp']")
    WebElement otpBtn;

    @FindBy(xpath = "//a[@class='bb-logo change-logo hidden-xs hidden-sm']")
    WebElement homePageBbLogo;

    @FindBy(xpath = "//button[text()='Verify & Continue']")
    WebElement verfyBtn;

    @FindBy(xpath = "//button[normalize-space()='Login using Email Address']")
    WebElement switchToEmailBtn;

    @FindBy(xpath = "//input[@id='otpEmail']")
    WebElement enterEmail;


    public WebLogin(WebDriver driver, IReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 120);

         expectation = new ExpectedCondition<Boolean>(){
            public Boolean apply(WebDriver driver){ return
                    ((JavascriptExecutor)driver).executeScript("return document.readyState").toString().equals("complete");}};
    }


    //* This method do login from registered mobile number*//
    public WebHomePage customerLoginViaMobileNum(String mobileNo) throws InterruptedException {

        Thread.sleep(3000);
        wait.until(ExpectedConditions.visibilityOf(loginBtn));
        loginBtn.click();

        wait.until(ExpectedConditions.visibilityOf(enterMobileNo));
        enterMobileNo.sendKeys(mobileNo);
        continueBtn.click();

        Thread.sleep(5000);
        String otp = AutomationUtilities.getOtp(serverName, mobileNo);
        wait.until(ExpectedConditions.visibilityOf(otpBtn));
        otpBtn.sendKeys(otp);
        verfyBtn.click();

        wait.until(ExpectedConditions.elementToBeClickable(homePageBbLogo));
        wait.until(expectation);

        return new WebHomePage(driver,report);

    }

    public WebHomePage customerLoginViaEmail(String email) throws InterruptedException {

        wait.until(ExpectedConditions.visibilityOf(loginBtn));
        loginBtn.click();

        wait.until(ExpectedConditions.visibilityOf(switchToEmailBtn));
        switchToEmailBtn.click();
        enterEmail.sendKeys(email);
        continueBtn.click();
        Thread.sleep(3000);
        String otp = AutomationUtilities.getOtp(serverName, email);
        otpBtn.sendKeys(otp);
        Thread.sleep(3000);
        verfyBtn.click();

        wait.until(ExpectedConditions.elementToBeClickable(homePageBbLogo));

        return new WebHomePage(driver,report);

    }



}






