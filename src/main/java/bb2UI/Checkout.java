package bb2UI;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.api.bbdaily.RequestAPIs;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.Libraries;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.LogConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static io.restassured.config.RestAssuredConfig.config;

public class Checkout extends WebSettings {

    private WebDriver driver;
    private IReport report;
    private WebDriverWait wait;

    @FindBy(xpath = "//header[2]//div[contains(@class,'grid grid-flow-col')]//a//*[name()='svg']//*[name()='g' and contains(@mask,'url(#baske')]//*[name()='path' and contains(@fill,'#fff')]")
    WebElement basket;

    @FindBy(xpath = "//button[contains(@class,'BasketValue___StyledButton')]")
    WebElement checkoutBttn;

    @FindBy(xpath = "//button[text()='Continue to Payments']")
    WebElement continuePayments;

    @FindBy(xpath = "//button[text()='CHANGE']")
    WebElement changeAddress;

    @FindBy(xpath = "//div[contains(@class,'DeliveryAddressCard')]//div//p[1]")
    WebElement addressList;

    @FindBy(xpath = "//span[text()='Payment Options']")
    WebElement paymentOptions;

    @FindBy(xpath = "//div[contains(@class,'StyledTabGroupTabHeaderList')]//div[2]//div")
    WebElement shipments;

    @FindBy(xpath = "//div[@class=\"pl-4 pt-4\"]")
    WebElement activeOrders;

    @FindBy(xpath = "//div[contains(@class,'BasketTotalPrice')]//span")
    WebElement getSKUPrice;

    @FindBy(xpath = "//*[@id=\"siteLayout\"]//div[2]/section[1]//div[1]/ul/li//div[3]/div[1]/div/button")
    WebElement addSavedItem;



    public Checkout(WebDriver driver, IReport report) {

        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 200);}

    //* This method navigates user to cart*//
    public void goToCart()throws InterruptedException{
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        report.log("scrolling up",true);
        jse.executeScript("window.scrollBy(0,-1000)");
        wait.until(ExpectedConditions.elementToBeClickable(basket));
        basket.click();
        Thread.sleep(3000);
        report.log("Cart Opened", true);

    }

    //* This method performs checkout after adding items in basket*//
   public void doCheckout() throws InterruptedException{
       wait.until(ExpectedConditions.visibilityOf(checkoutBttn));
       checkoutBttn.click();
       report.log("Checkout is clicked", true);
       Thread.sleep(6000);
    }
    public void continuePayment() throws InterruptedException
    {
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        report.log("scrolling down",true);
        jse.executeScript("window.scrollBy(0,50)");
        Thread.sleep(9000);
        wait.until(ExpectedConditions.elementToBeClickable(continuePayments));
        continuePayments.click();
        report.log("Checkout completed",true);
        wait.until(ExpectedConditions.visibilityOf(paymentOptions));
        report.log("Payment Option is available",true);
    }
    //* This method changes delivery address from basket page*//
    public void changeAddressFromBasketpage (String address) throws InterruptedException{

        Thread.sleep(3000);
        String deliverButton="//div//div//p[text()='"+address+"']//parent::div//parent::div//button[text()='DELIVER HERE']";
        wait.until(ExpectedConditions.elementToBeClickable(changeAddress));
        changeAddress.click();
        Thread.sleep(5000);
        try{
            WebElement deliverBttn = driver.findElement(By.xpath(deliverButton));
            wait.until(ExpectedConditions.elementToBeClickable(deliverBttn));
            deliverBttn.click();
            Thread.sleep(3000);
        report.log("Delivery address changed to: "+address,true);}
        catch(Exception e){
            changeAddress.click();
            WebElement deliverBttn = driver.findElement(By.xpath(deliverButton));
            wait.until(ExpectedConditions.elementToBeClickable(deliverBttn));
            deliverBttn.click();
            Thread.sleep(3000);
            report.log("Delivery address changed to: "+address,true);

        }
    }

    public void validateMultipleShipments() throws InterruptedException{
        wait.until(ExpectedConditions.visibilityOf(shipments));
        String shipmentInfo[]= shipments.getText().split("\\:");
        if(shipmentInfo[3].contains("2"))
        report.log("Multiple Shipment slot is reserved",true);
    }

    public void validateActiveOrdersPage() {

        wait.until(ExpectedConditions.visibilityOf(activeOrders));
        report.log("Active Orders are being displayed in MyOrders page",true);
    }

    public String getSKUPriceinCheckoutPage(){

        wait.until(ExpectedConditions.visibilityOf(getSKUPrice));
        String price= getSKUPrice.getText();
        return price;
    }

    public void addSavedItemIncart() throws InterruptedException{
        wait.until(ExpectedConditions.visibilityOf(addSavedItem));
        addSavedItem.click();
        report.log("Saved item successfully added in cart",true);
        Thread.sleep(2000);
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        report.log("scrolling up",true);
        jse.executeScript("window.scrollBy(0,-300)");
        Thread.sleep(2000);
    }
}



