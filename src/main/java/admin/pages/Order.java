package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Order extends WebSettings {

    @FindBy(xpath = "//a[contains(text(),'Modify')]")
    WebElement modifyLink;

    @FindBy(xpath = "//div[@class='form-row field-status']/div/div")
    WebElement orderStatus;

    @FindBy(xpath = "//div[normalize-space()='Entry context ID:']")
    WebElement entrycontextElement;

    @FindBy(xpath = "//div[normalize-space()='Bb android app']")
    WebElement channelElement;

    @FindBy(xpath = "//*[@id='pickup-checkbox-1']")
    WebElement pickUpCheckBox;

    @FindBy(xpath = "//*[@id='replacement-checkbox-1']")
    WebElement replacementCheckBox;

    @FindBy(xpath = "//*[@id='button-return-exchange']")
    WebElement returnAndExchange;

    @FindBy(xpath =  "//*[@id='button-more']/div")
    WebElement moreBtn;

    @FindBy(xpath = "//*[@id=\"option-communicate-to-customer\"]")
    WebElement communicateToCustomerBtn;

    @FindBy(xpath = "//*[@id=\"option-add-member-credit-log\"]")
    WebElement addMemberCreditLogBtn;

    @FindBy(xpath = "//*[@id=\"option-creditdebit-log\"]")
    WebElement crditDebitLogBtn;

    @FindBy(xpath = "//*[@id=\"app\"]/div/div[2]/div[2]/div[4]/div[2]")
    WebElement viewOrderAddressBtn;

    @FindBy(xpath = "//*[@id=\"option-order-status-log\"]")
    WebElement orderStatusLog;

    @FindBy(xpath = "//*[@id=\"option-van-assignment\"]")
    WebElement vanAssignment;

    @FindBy(xpath = "//*[@id=\"option-order-acknowledgement\"]")
    WebElement orderAcknowledgement;

    @FindBy(xpath = "//*[@id=\"go-to-order-details\"]/button/span[1]/p")
    WebElement goToOrderDetailsPage;



    private WebDriver driver;
    private IReport report;
    private WebDriverWait wait;

    public Order(WebDriver driver, IReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }

    public ModifyOrder clickOnModifyOrderLink() throws IOException {
        wait.until(ExpectedConditions.visibilityOf(modifyLink));
        modifyLink.click();
        report.log( "Clicked on Modify order link", true);
        report.info("Clicked on Modify order link");
        return new ModifyOrder(driver,report);
    }

    public String getOrderStatus() throws IOException {
        wait.until(ExpectedConditions.visibilityOf(orderStatus));
        String status = orderStatus.getText();
        report.info("Current order Status: "+status);
        return status;
    }

    public ArrayList<Boolean> verifySourceShowsEntryContextAsBB() {

        ArrayList<Boolean> checkElementIsPresent = new ArrayList<>();
        wait.until(ExpectedConditions.visibilityOf(entrycontextElement));
        report.log("EntryContext same as BB",true);
        checkElementIsPresent.add(entrycontextElement.isDisplayed());
        wait.until(ExpectedConditions.visibilityOf(channelElement));
        report.log("Channel same as source",true);
        checkElementIsPresent.add(channelElement.isDisplayed());

        return checkElementIsPresent;
    }

    public ArrayList<Boolean> checkPickUpAndReplacementOptionsAreDisabled()
    {
        ArrayList<Boolean> disabledCheckBox = new ArrayList<>();
        returnAndExchange.click();
        report.log("click R&E",true);
        wait.until(ExpectedConditions.visibilityOf(pickUpCheckBox));
         disabledCheckBox.add(pickUpCheckBox.isEnabled());
         report.log("pickup is disabled",true);
         wait.until(ExpectedConditions.visibilityOf(replacementCheckBox));
         disabledCheckBox.add(replacementCheckBox.isEnabled());
        report.log("replacement is disabled",true);

        return disabledCheckBox;
    }

    /**
     * this function will verify following options are enabled in bbnow
     * communicate to customer
     * View Order Address
     * Add member credit log
     * View credit/debit log
     * @return
     */

    public ArrayList<Boolean> checkAllowedOptionsInBBNow()
    {
        ArrayList<WebElement> btn = new ArrayList<>();
        btn.add(communicateToCustomerBtn);
        btn.add(addMemberCreditLogBtn);
        btn.add(crditDebitLogBtn);
        btn.add(viewOrderAddressBtn);
        btn.add(orderStatusLog);
        btn.add(vanAssignment);
       // btn.add(orderAcknowledgement);

        ArrayList<String> pageText  = new ArrayList<>();
        pageText.add("Communicate to customer");
        pageText.add("Add member credit log");
        pageText.add("Member credit logs");
        pageText.add("Change member address");
        pageText.add("Order Status Logs");
        pageText.add("Order Van Assignment");
       // pageText.add("")


        ArrayList<Boolean> allowedOptionsList = new ArrayList<>();
        String parentWindow  =  driver.getWindowHandle();


        for(int i=0 ; i< btn.size() ; i++)
        {
            if(btn.get(i)!=viewOrderAddressBtn)
               moreBtn.click();
           btn.get(i).click();
           if(driver.getWindowHandles().size() >2) {
               driver.switchTo().window((String) driver.getWindowHandles().toArray()[2]);
               driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
               boolean exits = driver.getPageSource().contains(pageText.get(i));
               report.log("BB-Now:" + pageText.get(i), exits);
               allowedOptionsList.add(exits);
               driver.close();
               driver.switchTo().window(parentWindow);
           }
           else
           {
               boolean exits = driver.getPageSource().contains(pageText.get(i));
               report.log("BB-Now:" + pageText.get(i), exits);
               allowedOptionsList.add(exits);
               wait.until(ExpectedConditions.visibilityOf(goToOrderDetailsPage));
               goToOrderDetailsPage.click();
           }
        }


        return allowedOptionsList;

    }

}
