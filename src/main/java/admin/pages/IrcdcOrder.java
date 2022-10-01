package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;

public class IrcdcOrder extends WebSettings {
    @FindBy(xpath="//*[contains(text(),'IRCDCOrder Search ')]")
    WebElement header;

    @FindBy(xpath="//*[@name='sku_id']")
    WebElement sku_id;

    @FindBy(xpath="//*[@name='from_date']")
    WebElement from_date;

    @FindBy(xpath="//*[@name='Search']")
    WebElement submit_button;


    //on search result page
    @FindBy(xpath="//h1[contains(text(),'Select inter dc order to change')]")
    WebElement searchListHeader;

    @FindBy(xpath="//*[@id='result_list']/tbody/tr")
    List<WebElement> searchResult;
    @FindBy(xpath = "//*[@id='result_list']/tbody/tr[1]/td/a")
    WebElement orderid;

    @FindBy(xpath = "//*[@id='result_list']")
    WebElement orderlist;



    private WebDriver driver;
    private AutomationReport report;
    private WebDriverWait wait;

    public IrcdcOrder(WebDriver driver, AutomationReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }

    public void getIRCDCordersbydate(String date){
        String url =String.format("/admin/order/interdcorder/?created_on__gte=%s+00%3A00%3A00",date);
        driver.get(trimmedServerName+url);

    }


    /**
     * navigates to the IRCDC order listing page, throws assert Error if unable to view the page
     * @throws InterruptedException
     */
    public void gotoIRCDCOrderListPage() throws InterruptedException
    {


        report.log("Navigating to the IRCDC order list page",true);
        report.info("Navigating to the IRCDC order list page");

        driver.navigate().to(trimmedServerName+"/admin/order/interdc-order/search/");

        driver.navigate().refresh();

        try
        {
            wait.until(ExpectedConditions.visibilityOf(header));
            report.log("On IRCDC order list page",true);
            report.info("On IRCDC order list page");
        }
        catch(TimeoutException e)
        {
            report.log("Not on IRCDC order list page",false);
            report.info("On IRCDC order list page");

            //throwing AssertError
            Assert.assertTrue(false,"Expected to be on IRCDC order page, but was unable to view the page");
        }

    }


    /**
     * enter details on which IRCDC search will be done
     * @param skuId
     * @param fromDate
     */
    public void searchForIRCDCOrder(String skuId, String fromDate)
    {

        wait.until(ExpectedConditions.visibilityOf(from_date));
        from_date.sendKeys(fromDate);
        report.info("Entered from_date details in IRCDC search parameter");
        report.log("Entered from_date details in IRCDC search parameter",true);

        wait.until(ExpectedConditions.visibilityOf(sku_id));
        sku_id.sendKeys(skuId);
        report.info("Entered sku_id details in IRCDC search parameter");
        report.log("Entered sku_id details in IRCDC search parameter",true);

        wait.until(ExpectedConditions.visibilityOf(submit_button));
        submit_button.click();
        report.info("Clicked on search button in IRCDC search parameter");
        report.log("Clicked on search button in IRCDC search parameter",true);

        try
        {
            wait.until(ExpectedConditions.visibilityOf(searchListHeader));
            report.info("On Search result page");
            report.log("On Search result page",true);
        }
        catch(TimeoutException e)
        {
            Assert.assertTrue(false,"Expected to redirect to search Result page on clicking the search button");
        }

    }

    public String getIRCDCorderid(){
        wait.until(ExpectedConditions.visibilityOf(orderlist));
        String order_id=orderid.getText();
        return  order_id;
    }

    public Order openorderdetails(){
        wait.until(ExpectedConditions.visibilityOf(orderlist));
        orderid.click();
        return  new Order(driver,report);
    }



}
