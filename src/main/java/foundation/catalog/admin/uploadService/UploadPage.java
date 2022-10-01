package foundation.catalog.admin.uploadService;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Upload Page has various uploads that can be done for different catalog functionality.
 */
public class UploadPage extends WebSettings {

    /**
     * xpaths
     */
    @FindBy(xpath = "//a/span/span[contains(text(),'Upload')]")
    private WebElement UploadButton;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div/div/header/div/button[2]")
    private WebElement RefreshButton;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div/div/main/div[2]/div/div[2]/div/form/div[2]/button")
    private WebElement FinalUploadButton;

    @FindBy(xpath = "//*[@id=\"upload_type_id\"]")
    private WebElement UploadTypeButton;

    @FindBy(xpath = "//*[@id=\"sub_type\"]")
    private WebElement UploadSubTypeButton;

    @FindBy(xpath = "//*[@id=\"files\"]")
    private WebElement InputFileButton;

    @FindBy(xpath = "//span[contains(text(),'Success')]")
    private WebElement SucessfulUpload;

    @FindBy(xpath = "//span[contains(text(),'Invalid')]")
    private WebElement InvalidUpload;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div/div/main/div[2]/div/div[2]/div/div/div[3]/div/label/span")
    private WebElement Uploadstatus;


    private WebDriver driver;
    private AutomationReport report;
    private WebDriverWait wait;

    /**
     * Stting up report and driver for Upload page
     *
     * @param driver TO setup driver for Upload page
     * @param report To enable custom report
     */
    public UploadPage(WebDriver driver, AutomationReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }


    /*public void navigateToUpload() {
        String baseURL = getCurrentURL(driver);
        driver.get(baseURL + "upload_request");
        wait.until(ExpectedConditions.visibilityOf(RefreshButton));
        clickUploadButton();
    }

    *//**
     * Clicking on actual Upload button
     *
     *//*
    public void clickUploadButton() {
        wait.until(ExpectedConditions.visibilityOf(UploadButton));
        report.log(isScreenshotRequired, "clicking on upload button", true);
        UploadButton.click();
        wait.until(ExpectedConditions.visibilityOf(FinalUploadButton));
        report.log(isScreenshotRequired, "Upload Page Opened", true);
    }


    *//**
     * Select the type of Upload you want to do.
     *
     * @param upload_type Name of the Upload Type
     * @throws InterruptedException
     *//*
    public void selectUploadType(String upload_type) throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(UploadTypeButton));
        report.log(isScreenshotRequired, "clicking on upload type button", true);
        UploadTypeButton.click();
        Thread.sleep(4000);
        String xpath = "//*[contains(text(), '" + upload_type + "')]";
        driver.findElement(By.xpath(xpath)).click();
        Thread.sleep(4000);
    }

    *//**
     * If the upload contains a sub type like create/Update. This method is to select that
     *
     * @throws InterruptedException
     *//*
    public void selectUploadSubTypeCreateButton() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(UploadSubTypeButton));
        report.log(isScreenshotRequired, "clicking on upload type button", true);
        UploadSubTypeButton.click();
        Thread.sleep(4000);
        String xpath = "//*[@id=\"menu-sub_type\"]/div[3]/ul/li[1]";
        driver.findElement(By.xpath(xpath)).click();
        Thread.sleep(4000);
    }

    *//**
     * To input the file which you wish to upload
     *
     * @param file_name name of the file to be Uploaded
     * @throws InterruptedException
     *//*
    public void UploadFile(String file_name) throws InterruptedException {
        //wait.until(ExpectedConditions.visibilityOf(InputFileButton));
        InputFileButton.sendKeys(file_name);
        report.log(isScreenshotRequired, "File added for Upload", true);
        Thread.sleep(10000);
        FinalUploadButton.click();
        report.log(isScreenshotRequired, file_name + " File Uploaded", true);
        Thread.sleep(10000);
    }

    *//**
     * Upload file using the file type and file name without selecting upload sub type
     *
     * @param type_name The type of the Upload to be selected
     * @param filename  Name of the file to be uploaded
     * @throws Exception
     *//*
    public void UploadByTypeandFile(String type_name, String filename) throws Exception {
        selectUploadType(type_name);
        UploadFile(filename);
        String xpath = "//*[@id=\"root\"]/div/div/div/main/div[2]/div/div[2]/div/div/div[1]/div/div/span";
        String UploadId = driver.findElement(By.xpath(xpath)).getText();
        int tried = 0;
        String uploadstatus = Uploadstatus.getText();
        System.out.println(uploadstatus + "  " + UploadId);
        report.log(isScreenshotRequired, "Checking Upload status", true);
        while (!uploadstatus.contains("Success")) {
            RefreshButton.click();
            Thread.sleep(5000);
            if (uploadstatus.contains("Invalid") || tried++ == 5) {
                report.log(isScreenshotRequired, "Upload Failed", false);
                throw new Exception("Upload Failed. Please check the Upload Id" + UploadId);
            }
            uploadstatus = Uploadstatus.getText();
        }
        report.log(isScreenshotRequired, "Upload Successful", true);
        report.log("Upload is successful. please check upload id:" + UploadId);
    }

    *//**
     * Upload file using the file type and file name with selecting upload sub type as CREATE
     *
     * @param type_name The type of the Upload to be selected
     * @param filename  Name of the file to be uploaded
     * @throws Exception
     *//*
    public void UploadByTypeSubTypeandFile(String type_name, String filename) throws Exception {
        selectUploadType(type_name);
        selectUploadSubTypeCreateButton();
        UploadFile(filename);
        String xpath = "//*[@id=\"root\"]/div/div/div/main/div[2]/div/div[2]/div/div/div[1]/div/div/span";
        String UploadId = driver.findElement(By.xpath(xpath)).getText();
        int tried = 0;
        String uploadstatus = Uploadstatus.getText();
        System.out.println(uploadstatus + "  " + UploadId);
        report.log(isScreenshotRequired, "Checking Upload status", true);
        while (!uploadstatus.contains("Success")) {
            RefreshButton.click();
            Thread.sleep(5000);
            if (uploadstatus.contains("Invalid") || tried++ == 5) {
                report.log(isScreenshotRequired, "Upload Failed", false);
                throw new Exception("Upload Failed. Please check the Upload Id" + UploadId);
            }
            uploadstatus = Uploadstatus.getText();
        }
        report.log(isScreenshotRequired, "Upload Successful", true);
        report.log("Upload is successful. please check upload id:" + UploadId);
    }

    *//**
     * Create data for FC sku upload
     *
     * @param fc_id  The FC id for which Upload has to be made
     * @param sku_id The sku_id for which Upload has to be done
     * @return
     *//*
    public ArrayList<String[]> catalogFCUploadData(int fc_id, ArrayList<Integer> sku_id) {
        String[] template = {"100", "1940", "110", "80", "110", "false", "1", "24", "true", "true"};
        ArrayList<String[]> resultSet = new ArrayList<String[]>();
        for (int i = 0; i < sku_id.size(); i++) {
            String[] temp = new String[]{String.valueOf(fc_id), String.valueOf(sku_id.get(i)), "110", "80", "110", "false", "1", "24", "true", "true"};
            resultSet.add(temp);
            System.out.println("array Size" + resultSet.size());
            System.out.println(resultSet.toString());
            System.out.println(Arrays.toString(resultSet.get(i)));
        }
        return resultSet;
    }*/
}
