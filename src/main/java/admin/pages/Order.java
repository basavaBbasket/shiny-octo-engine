package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Order extends WebSettings {

    @FindBy(xpath = "//a[contains(text(),'Modify')]")
    WebElement modifyLink;

    @FindBy(xpath = "//div[@class='form-row field-status']/div/div")
    WebElement orderStatus;

    private WebDriver driver;
    private AutomationReport report;
    private WebDriverWait wait;

    public Order(WebDriver driver,AutomationReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }

    public ModifyOrder clickOnModifyOrderLink() {
        wait.until(ExpectedConditions.visibilityOf(modifyLink));
        modifyLink.click();
        report.log( "Clicked on Modify order link", true);
        report.info("Clicked on Modify order link");
        return new ModifyOrder(driver,report);
    }

    public String getOrderStatus(){
        wait.until(ExpectedConditions.visibilityOf(orderStatus));
        String status = orderStatus.getText();
        report.info("Current order Status: "+status);
        return status;
    }

}
