package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ModifyOrder extends WebSettings {

    @FindBy(xpath = "//*[@id='select_action']")
    WebElement actionDD;

    @FindBy(xpath = "//input[@value='Go']")
    WebElement goButton;

    @FindBy(xpath = "//td[contains(text(),'New status')]/following-sibling::td[1]/select")
    WebElement newOrderStatusDD;

    @FindBy(xpath = "//input[@value='Save']")
    WebElement saveBtn;

    @FindBy(xpath = "//h1[contains(text(),\"Modify Order #\")]")
    WebElement orderid;







    private WebDriver driver;
    private IReport report;
    private WebDriverWait wait;

    public ModifyOrder(WebDriver driver, IReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }


    /**
     * this function selects and change order status and then click on save button
     * to save it.
     *
     * @param status
     * @return
     */

    public ModifyOrder changeStatus(String status) throws InterruptedException {
        Thread.sleep(3000);
        report.log( "Changing Order Status", true);
        wait.until(ExpectedConditions.visibilityOf(actionDD));
        Select selAction = new Select(actionDD);
        selAction.selectByVisibleText("Change Status");
        report.log( "Selected Change status from DD", true);
        goButton.click();
        report.log( "Clicked on GO button", true);
        wait.until(ExpectedConditions.visibilityOf(newOrderStatusDD));

        Select selNewStatus = new Select(newOrderStatusDD);
        selNewStatus.selectByVisibleText(status);
        report.log( "Selected " + status + " from the DD", true);
        saveBtn.click();
        report.log( "Clicked on Save Button", true);
        System.out.println("Order status changed");
        return this;
    }

    public String getorderid(){
        String od=orderid.getText();
        report.log("orderid is"+od,true);
        String[] orderlis=od.split("Modify Order #");

        String order_id=orderlis[1].trim();
        report.log(order_id,true);
        return order_id;
    }
}
