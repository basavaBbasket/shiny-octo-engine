package planogram.admin.wio;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.List;

public class FcReceiving extends WebSettings {

    @FindBy(xpath = "//*[contains(text(),\"PO Number\")]")
    WebElement fcreceivingpoele;

    @FindBy(xpath = "//*[@value='Complete GRN']")
    WebElement completegrn;

    @FindBy(xpath = "//*[@class='success']")
    WebElement successmsg;

    @FindBy(xpath = "//*[@class=\"data-population\"]")
    WebElement polisttable;

    @FindBy(xpath = "//*[@class=\"data-population\"]/tbody/tr")
    WebElement polistitems;



    private WebDriver driver;
    private IReport report;
    private WebDriverWait wait;

    public FcReceiving(WebDriver driver, IReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }


    public String selectpoforgrn(String order_id) throws InterruptedException {
        Thread.sleep(3000);
try {
    report.log("selecing the po to do grn for ordr id:"+order_id,true);
    String xpath="//td[text()=\"%s\"]/preceding-sibling::td/input[@name='Checkbox[]']";

    String popath="//td[text()=\"%s\"]/preceding-sibling::td[contains(text(),'SGI')]";
    String poid=driver.findElement(By.xpath(String.format(popath,order_id))).getText();

    driver.findElement(By.xpath(String.format(xpath,order_id))).click();
    return poid;

}
catch (Exception e) {
    report.log("Po is not visible to perform grn for ordr id:" + order_id, false);
    return "po is not found for the given order "+order_id;

}

    }

    public void completegrn(int noofpos) throws InterruptedException {
        Thread.sleep(3000);
        report.log("Clicking on complete grn button ",true);
        clickElement(completegrn,driver);
        Thread.sleep(7000);
        try {
            Alert al = driver.switchTo().alert();

            String expalertmsg=String.format("Want to complete Grn for %s requests ?",noofpos);
            String alertmsg= al.getText();
            al.accept();
            Assert.assertEquals(alertmsg,expalertmsg,"1 CLick GRN is failed for the pos, the current alert message is not what expected");
            report.log(alertmsg,true);
            al.accept();
            wait.until(ExpectedConditions.visibilityOf(successmsg));
            report.log("grn is completed",true);
        }
        catch (Exception e){
            report.log("po is not found to complete grn",false);
        }


    }

     public void validateEmptyPoScreen(){
        wait.until(ExpectedConditions.visibilityOf(polisttable));
         String xpath="//*[@class=\"data-population\"]/tbody/tr";

        List<WebElement> rows = driver.findElements(By.xpath(xpath));
         Assert.assertTrue(1 == rows.size());
     }



}
