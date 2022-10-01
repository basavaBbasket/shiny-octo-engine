package planogram.admin.wio;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utility.database.DynamicLocationDBQueries;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FcReturnUploadUi extends WebSettings {

    @FindBy(xpath = "//*[@id=\"id_fc\"]")
    WebElement fcdropdown;

    @FindBy(xpath = "//*[@id=\"id_upload_file\"]")
    WebElement uploadinput;

    @FindBy(xpath ="//*[@value=\"Upload\"]")
    WebElement uploadbtn;

    @FindBy(xpath = "//*[@class=\"success\"]")
    WebElement successmsg;

    @FindBy(xpath = "//a[contains(text(),'Upload Manager')]")
    WebElement Uploadmanager;

    @FindBy(xpath = "//*[text()=\"Select upload manager to change\"]")
    WebElement uploadmanagertext;

    @FindBy(xpath = "//*[@id=\"result_list\"]/tbody/tr[1]/td[5]")
    WebElement uploadstatus;



    private WebDriver driver;
    private IReport report;
    private WebDriverWait wait;

    public FcReturnUploadUi(WebDriver driver, IReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }


    public  void interFcPRNUpload(String fcname, String filelocation) throws InterruptedException {

        wait.until(ExpectedConditions.visibilityOf(fcdropdown));
        report.log( "selecting fc:"+fcname, true);
        Select fc= new Select(fcdropdown);
        fc.selectByVisibleText(fcname);
        report.log("uploading the interfc PRN upload file",true);
        uploadinput.sendKeys(filelocation);
        report.log( "clciking on upload btn", true);
        uploadbtn.click();
        wait.until(ExpectedConditions.visibilityOf(successmsg));
        Uploadmanager.click();
        wait.until(ExpectedConditions.visibilityOf(uploadmanagertext));
        driver.navigate().refresh();
        Thread.sleep(4000);
        int iteration = 0;
        String status=uploadstatus.getText();
        while(iteration<4){
            if(status.equalsIgnoreCase("Completed"))
                break;
            Thread.sleep(10000*iteration);
            driver.navigate().refresh();
            status=uploadstatus.getText();
            iteration++;
        }
        Assert.assertEquals(status,"Completed","interfc prn upload failed");

    }


    public static  String WriteCSVFile(ArrayList<String> dataToWrite, String templateFileName, ArrayList<String> csvheader)  throws IOException {
        String CSV_File_Path = System.getProperty("user.dir") + "//target//"+templateFileName;



        BufferedWriter writer = Files.newBufferedWriter(Paths.get(CSV_File_Path));
        CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                .withHeader(csvheader.get(0), csvheader.get(1)));

        csvPrinter.printRecord(dataToWrite);
        csvPrinter.flush();
        csvPrinter.close();
        return CSV_File_Path;
    }

    public   String fcReturnPRNFileLocation(ArrayList<String> dataToWrite, String templateFileName) throws IOException {
        ArrayList<String> csvheader=new ArrayList<>();
        csvheader.add("sku_id");
        csvheader.add("quantity");
        FcReturnUploadUi fcReturnUploadUi=new FcReturnUploadUi(driver,report);
        return  WriteCSVFile(dataToWrite,templateFileName,csvheader);
    }

    public ArrayList getFcReturnSkuid(){
        DynamicLocationDBQueries dynamicLocationDBQueries=new DynamicLocationDBQueries();
        String query="select sku_id,quantity from fc_sku_stock where quantity>500 and location_id=4;";
        report.log("Fetching the fcreceivingitem \n Query Executed: " + query,true);
        JSONObject jsonObject = new JSONObject( dynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
         if (jsonObject.getInt("numRows") == 0) {
                report.log("No id returned ", true);
                Assert.fail("No sku id returned  for Query: " + query);
            }
            int skuid = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("sku_id");
            double quantity= jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("quantity");
            report.log("getting the first sku whose stock in fc sku stock is greater than 500 and location_id =4 is " + skuid,true);
            ArrayList skuquantity=new ArrayList();
            skuquantity.add(skuid);
            skuquantity.add(quantity);
            return skuquantity;
        }


    public double getFcReturnSkuidquantity(int skuid){
        DynamicLocationDBQueries dynamicLocationDBQueries=new DynamicLocationDBQueries();
        String query="select quantity from fc_sku_stock where location_id=4 and sku_id="+skuid+";";
        report.log("Fetching the quantity of the skuid \n Query Executed: " + query,true);
        JSONObject jsonObject = new JSONObject( dynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No quantity returned ", true);
            Assert.fail("No quantity returned  for Query: " + query);
        }
        double quantity= jsonObject.getJSONArray("rows").getJSONObject(0).getDouble("quantity");
        report.log("getting the first sku whose stock in fc sku stock is greater than 500 and location_id =4" + skuid,true);
return quantity;
    }





}


