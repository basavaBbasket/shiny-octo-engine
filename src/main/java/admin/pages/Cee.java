package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utility.upload.UploadFile;

import java.io.IOException;
import java.util.ArrayList;

public class Cee extends WebSettings {

    @FindBy(xpath = "//*[contains(text(),'Bulk Upload')]")
    WebElement bulkUpload;

    @FindBy(xpath = "//input[@id='file_input']")
    WebElement choseInputFile;

    @FindBy(xpath = "//input[@id='email_input']")
    WebElement inputEmail;

    @FindBy(xpath = "//button[@type='submit']")
    WebElement submitBtn;


    private final WebDriver driver;
    private final IReport report;
    private final WebDriverWait wait;

    public Cee(WebDriver driver, IReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }

    public void clickOnBulkUploadBtn() {
        report.log("Clicking on bulk upload btn", true);
        wait.until(ExpectedConditions.visibilityOf(bulkUpload));
        bulkUpload.click();
        report.log("Clicked on bulk upload btn", true);
    }

    public ArrayList<Object[]> ceeBulkUpload(int numberOfNewCees, int cityId, int saId, int dmId) {
        ArrayList<Object[]> resultSet = new ArrayList<>();
        for (int i = 0; i < numberOfNewCees; i++) {
            long newMobileNum = Long.parseLong(AutomationUtilities.getUniqueMobileNumber(serverName));
            Object[] individualCeeData = {null, "auto_user_" + newMobileNum, "auto_first_" + newMobileNum, "auto_last_" + newMobileNum, newMobileNum, cityId,
                    saId + "-" + dmId, null, newMobileNum, "Regular", "full", "On Roll", null, null, "CEA", null, "yes", "normal", "w1", "Salaried CEEs",
                    "BB", "BigBasket", null, null, null, null, null, null, null, "yes"};
            resultSet.add(individualCeeData);
        }
        return resultSet;
    }


    public String ceeBulkUploadFilepath(ArrayList<Object[]> ceesUploadData) throws IOException {
        UploadFile uploadFile = new UploadFile(report);
        return uploadFile.create_upload_file(ceesUploadData, "dapi/", "cee_upload_sample_bb2.xlsx", "Sheet1");

    }

    public void choseFileToDoBulkUpload(String filePath) {
        report.log("Choosing the bulk upload file from path " + filePath, true);
        wait.until(ExpectedConditions.visibilityOf(choseInputFile));
        choseInputFile.sendKeys(filePath);
        report.log("File has been chosen for the bulk upload from path ", true);
    }

    public void enterEmailIdToReceiveTheReport(String emailId) {
        report.log("Enter email Id " + emailId + " to receive the report", true);
        wait.until(ExpectedConditions.visibilityOf(inputEmail));
        inputEmail.sendKeys(emailId);
        report.log("Email Id " + emailId + " entered to receive the report", true);
    }

    public void clickOnSubmit() {
        report.log("Clicking on submit btn", true);
        wait.until(ExpectedConditions.visibilityOf(submitBtn));
        submitBtn.click();
        report.log("Clicked on submit Btn", true);
    }
}
