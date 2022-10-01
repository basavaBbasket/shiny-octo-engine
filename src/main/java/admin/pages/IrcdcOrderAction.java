package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.upload.UploadFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class IrcdcOrderAction extends WebSettings {

    @FindBy(id = "select-interdc-order")
    WebElement selectAction;

    @FindBy(id = "host_dc_id")
    WebElement hostDC;

    @FindBy(id = "remote_dc_id")
    WebElement remoteDC;

    @FindBy(xpath = "//*[@id='inter-dc-order-action-createorder']/table/tbody/tr[3]/td[2]/input")
    WebElement browse;

    @FindBy(xpath = "//*[@id='inter-dc-order-action-createorder']//input[@value='Create Inter-Dc orders']")
    WebElement createInterDcOrder;

    //@FindBy(xpath = "//*[@id='container']/ul/li[contains(text(),'Successfully created ircdc order for')]")
    @FindBy(xpath = "//*[@id='container']/ul/li[contains(text(),'File is uploaded successfully, Please wait for the mail')]")
    WebElement successMessage;

    @FindBy(xpath = "//*[@id = 'remote_id']")
    WebElement source;

    @FindBy(xpath = "//*[@id = 'host_id']")
    WebElement destination;

    @FindBy(xpath = "//*[@value='Setup check']")
    WebElement setup_check;

    @FindBy(xpath = "//li[@class=\"success\"]")
    WebElement successmsg;





    private WebDriver driver;
    private AutomationReport report;
    private WebDriverWait wait;

    public IrcdcOrderAction(WebDriver driver, AutomationReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }


    /**
     * Checks whether InterDC Order for the given DCs can be placed or not
     * @param sourceDC -> This is Dc where order needs to be Picked
     * @param destinationDC -> This is Dc where order needs to be delivered
     * @return
     */

    public boolean IrcdcSetupCheck(String sourceDC, String destinationDC)
    {
        driver.navigate().refresh();

        wait.until(ExpectedConditions.visibilityOf(selectAction));

        report.log( "selecting action for checking of setup for inter DC Order as: IRCDC Setup check", true);
        Select selAction = new Select(selectAction);
        selAction.selectByVisibleText("IRCDC Setup check");
        report.log( "selected action for inter DC order: IRCDC Setup check", true);

        wait.until(ExpectedConditions.visibilityOf(source));
        Select selSouce = new Select(source);
        selSouce.selectByVisibleText(sourceDC);
        report.log( "selected Source DC as: "+sourceDC, true);

        wait.until(ExpectedConditions.visibilityOf(destination));
        Select selDestination = new Select(destination);
        selDestination.selectByVisibleText(destinationDC);
        report.log( "selected Destination DC as: "+destinationDC, true);

        wait.until(ExpectedConditions.visibilityOf(setup_check));
        setup_check.click();
        // clickElement(setup_check, driver);


        try {
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(
                    By.xpath("//*[contains(text(),'Setup has been created, now you can create an Inter-DC "
                            + "order for remote-dc: "+sourceDC+" and host-dc: "+destinationDC+"')]"))));
            report.log(getmemberid(),true);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }


    }

    public String getmemberid(){

        String[] successlist=successmsg.getText().split("Member_id: ");
        String[] memberlatlon=successlist[1].split(",");

        String memberid=memberlatlon[0];
        return memberid;
    }

    /**
     * this function creates an Inter DC Order and then click the create inter DC order btn.
     * @param action
     * @param hostDc
     * @param remoteDc
     * @param fileLocation
     * @return
     */

    public IrcdcOrderAction createInterDcOrder(String action, String hostDc, String remoteDc, String fileLocation) {
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOf(selectAction));
        report.log( "selecting action for inter DC Order as: "+action, true);
        Select selAction = new Select(selectAction);
        selAction.selectByVisibleText(action);
        report.log( "selected action for inter DC order: "+action, true);

        Select selHostDc = new Select(hostDC);
        selHostDc.selectByVisibleText(hostDc);
        report.log( "selecting host DC(Destination) as: "+hostDc, true);


        Select selRemoteDc = new Select(remoteDC);
        selRemoteDc.selectByVisibleText(remoteDc);
        report.log( "selecting remote DC(Source) as: "+remoteDc, true);


        browse.sendKeys(fileLocation);
        report.log( "entering file location", true);

        createInterDcOrder.click();
        report.log( "clicked create inter DC Order btn", true);

        //if an alert pops up
        try
        {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        }
        catch (NoAlertPresentException e) {
            //nothing to do
        }

        return this;
    }

    /**
     * this function verifies whether IRCDC order is placed successfully or not .
     * @return
     */

    public boolean isIrcdcOrderPlaced() {
        try {
            if (successMessage.isDisplayed()) {
                report.log( "success msg is displayed",true);
                report.log(successMessage.getText(),true);
                System.out.println(successMessage.getText());

                return true;
            }
            report.log( "success msg is not displayed",true);
            return false;
        } catch (Exception e) {
            report.log("success msg is not displayed",true);
            return false;
        }
    }


    public ArrayList<Object[]> ircdcUploadData( ArrayList<Integer> sku_id, ArrayList<Integer> quantity){
        ArrayList<Object[]> resultSet = new ArrayList<Object[]>();
        for(int i=0;i<sku_id.size();i++){
            String []temp = new String[]
                    {String.valueOf(sku_id.get(i)),String.valueOf(quantity.get(i))};
            resultSet.add(temp);
            System.out.println("array Size"+ resultSet.size());
            System.out.println(resultSet.toString());
            System.out.println(Arrays.toString(resultSet.get(i)));
        }
        return resultSet;
    }


    public String ircdcuploadfilepath(ArrayList<Integer>skulist, ArrayList<Integer>quantity) throws IOException {
        ArrayList<Object[]> Prepareddata= ircdcUploadData(skulist,quantity);
        UploadFile uploadFile=new UploadFile(report);
       return uploadFile.create_upload_file(Prepareddata,"ircdc/","IRCDC.xlsx","Page1");

    }






}
