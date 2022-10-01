package planogram.admin.wio;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.IReport;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;

public class FcReceivingSearch extends WebSettings {


    @FindBy(xpath = "//*[@id=\"filter\"]")
    WebElement filter;
    @FindBy(xpath = "//*[@id=\"dest_fc\"]")
    WebElement dest_fc;



    @FindBy(xpath = "//*[@id=\"source_fc\"]")
    WebElement source_fc;

    @FindBy(xpath = "//*[@id=\"order_type\"]")
    WebElement order_type;

    @FindBy(xpath = "//*[@id=\"submit\"]")
    WebElement getpos;

    @FindBy(xpath = "//*[text()=\"planogram\"]")
    WebElement microserviceplanogram;

    @FindBy(xpath = "//*[text()=\"Planogram\"]")
    WebElement fcreceivingplanogram;

    @FindBy(id = "id_username")
    WebElement loginId;


    private WebDriver driver;
    private IReport report;
    private WebDriverWait wait;

    public FcReceivingSearch(WebDriver driver, IReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }



    public void navigatetofcreceivingpage() throws InterruptedException {
        driver.get(trimmedServerName+"/planogram-ui/admin/wios/fcreceiving/search/");
        Thread.sleep(4000);
        wait.until(ExpectedConditions.visibilityOf(loginId));
    }


    public FcReceiving getActivepos(String Sourcefc, String DestinationFc) throws InterruptedException {

        Thread.sleep(3000);
        wait.until(ExpectedConditions.visibilityOf(dest_fc));
        report.log("selecting the destnation fc"+DestinationFc,true);
        Select destfc = new Select(dest_fc);
        destfc.selectByVisibleText(DestinationFc);
        Thread.sleep(5000);
        report.log("Selecting the SOurce fc for the given"+Sourcefc,true);
        Select sourcefc = new Select(source_fc);
        sourcefc.selectByVisibleText(Sourcefc);
        report.log("Clicking the get pos buttons",true);
        getpos.click();
        return new FcReceiving(driver,report);

    }

    public void validateOrdertype(ArrayList<String> ordertypelist){
        wait.until(ExpectedConditions.visibilityOf(order_type));
        Select ordertype= new Select(order_type);
        for(String ot:ordertypelist){
            try {
                ordertype.selectByVisibleText(ot);
                report.log("order type "+ot+"is visible",true);
            }
            catch (Exception e){
                report.log("order type "+ot+"is not visible",true);
            }

        }
    }


}
