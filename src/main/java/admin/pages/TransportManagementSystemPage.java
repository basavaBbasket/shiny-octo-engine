package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class TransportManagementSystemPage extends WebSettings {


    @FindBy(xpath = "//span[normalize-space()='Field Assets']")
    WebElement FieldsAssets;

    @FindBy(xpath = "//a[normalize-space()=\"Cee's\"]")
    WebElement ceePage;

    @FindBy(xpath = "//input[@type='search']")
    WebElement search;

    @FindBy(xpath = "//i[normalize-space()='Force Cee Logout']")
    WebElement logOutBtn;

    @FindBy(xpath = "//a[@href='/hertz/common-store-qr-code/']")
    WebElement storeQRbtn;

    @FindBy(xpath = "//h4[normalize-space()='Common Store QR Code']")
    WebElement commonStoreQRPage;

    @FindBy(xpath = "//div[@id='sa_select_chosen']")
    WebElement serviceabilityAreaDropdown;

    @FindBy(xpath = "//div[@id='sa_select_chosen']//input[@type='text']")
    WebElement enterSA;

    @FindBy(xpath = "//div[@id='dm_select_chosen']")
    WebElement deliveryModeDropdown;

    @FindBy(xpath = "//div[@id='dm_select_chosen']//input[@type='text']")
    WebElement enterDM;


    @FindBy(xpath = "//button[@type='submit']")
    WebElement submitBtn;

    @FindBy(xpath = "//td[contains(text(),'No matching records found')]")
    WebElement noMatchingRecordTxt;

    private WebDriver driver;
    private IReport report;
    private WebDriverWait wait;

    public TransportManagementSystemPage(WebDriver driver, IReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }

    public void forceCeeLogoutUsingCeeId(String ceeId) {
        wait.until(ExpectedConditions.visibilityOf(FieldsAssets));
        FieldsAssets.click();
        ceePage.click();
        search.sendKeys(ceeId);
        String dynamicXpath = "//a[normalize-space()=" + ceeId + "]";
        driver.findElement(By.xpath(dynamicXpath)).click();
        wait.until(ExpectedConditions.visibilityOf(logOutBtn));
        logOutBtn.click();
    }

    public String scanQRCode(String serviceabilityArea, String deliveryMode) throws IOException, NotFoundException, InterruptedException {
        storeQRbtn.click();
        Thread.sleep(1000);
        //wait.until(ExpectedConditions.visibilityOf(commonStoreQRPage));
        /**
         Select sa_select = new Select(serviceabilityArea);
         sa_select.selectByIndex(2);
         Select dm_select = new Select(deliveryMode);
         dm_select.selectByIndex(0);**/

        serviceabilityAreaDropdown.click();
        enterSA.sendKeys(serviceabilityArea);
        enterSA.sendKeys(Keys.ENTER);
        deliveryModeDropdown.click();
        enterDM.sendKeys(deliveryMode);
        enterDM.sendKeys(Keys.ENTER);
        submitBtn.click();
        String qrCodeURL = driver.findElement(By.xpath("//img[@id='qrImg']")).getAttribute("src");
        URL url = new URL(qrCodeURL);
        BufferedImage bufferedimage = ImageIO.read(url);
        LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedimage);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
        Result result = new MultiFormatReader().decode(binaryBitmap);
        return result.getText();
    }


    public Cee clickOnCeeLink() {
        wait.until(ExpectedConditions.visibilityOf(FieldsAssets));
        FieldsAssets.click();
        ceePage.click();
        return new Cee(driver, report);
    }


}
