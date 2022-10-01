package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TransportManagementSystemPage extends WebSettings {


    @FindBy(xpath = "//span[normalize-space()='Field Assets']")
    WebElement FieldsAssets;

    @FindBy(xpath = "//a[normalize-space()=\"Cee's\"]")
    WebElement ceePage;

    @FindBy(xpath = "//input[@type='search']")
    WebElement search;

    @FindBy(xpath = "//i[normalize-space()='Force Cee Logout']")
    WebElement logOutBtn;





    private WebDriver driver;
    private AutomationReport report;
    private WebDriverWait wait;

    public TransportManagementSystemPage(WebDriver driver, AutomationReport report)
    {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }

    public void forceCeeLogout()
    {
        String ceeiId = "550001";
        wait.until(ExpectedConditions.visibilityOf(FieldsAssets));
        FieldsAssets.click();
        ceePage.click();
        search.sendKeys("550001");
        String dynamicXpath = "//a[normalize-space()="+ceeiId+"]";
        driver.findElement(By.xpath(dynamicXpath)).click();
        wait.until(ExpectedConditions.visibilityOf(logOutBtn));
        logOutBtn.click();

    }




}
