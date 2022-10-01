package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.json.JSONObject;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;


public class Member extends WebSettings {


    @FindBy(xpath = "//input[@name='member-id']")
    WebElement enterMemberid;

    @FindBy(xpath = "//input[@value='Show Members']")
    WebElement showmemberbtn;

    @FindBy(xpath = "//*[@value='Save']")
    WebElement saveBtn;

    @FindBy(xpath = "//*[contains(text(),'was changed successfully')]")
    WebElement successMsg;

    private WebDriver driver;
    private AutomationReport report;
    private WebDriverWait wait;
    Actions action = new Actions(driver);

    public Member(WebDriver driver, AutomationReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 100);

    }


    public Member searchMemberByMemberid(String memberid) {
        enterMemberid.clear();
        enterMemberid.sendKeys(memberid);
        report.log( "entered email as: "+memberid, true);
        showmemberbtn.click();
        report.log( "submitted the Memberid for search", true);
        return this;
    }

    /**
     * this function navigates to the admin home page .
     * @return
     */

    public HomePage navigateToHome() {
        driver.get(trimmedServerName + "/admin");
        return new HomePage(driver,report);
    }


}
