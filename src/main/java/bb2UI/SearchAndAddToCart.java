package bb2UI;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.mapi.mapi_4_1_0.internal.BigBasketApp;
import com.bigbasket.automation.reports.IReport;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class SearchAndAddToCart extends WebSettings {

    private WebDriver driver;
    private IReport report;
    private WebDriverWait wait;

    int count=1;

    @FindBy(xpath = "//*[@id='siteLayout']/header[2]//div[1]//input")
    WebElement searchBar;

    @FindBy(xpath = "//*[name()='g' and contains(@mask,'url(#cross')]//*[name()='path' and contains(@fill,'#606060')]")
    WebElement clearSearch;

    @FindBy(xpath = "//div[contains(@class,'QuickSearch___StyledMenuItems')]")
    WebElement autoSearchResult;

    @FindBy(xpath = "//div[contains(@class,'QuickSearch___StyledMenuItems')]//li[1]//button")
    WebElement addFromAutoSearch;

    @FindBy(xpath = "//button//*[name()='svg' and contains(@class,'CartCTA___StyledPlusIcon')]")
    WebElement addBttn;

    @FindBy(xpath = "//button//*[name()='svg' and contains(@class,'CartCTA___StyledMinusIcon')]")
    WebElement minusBttn;
    @FindBy(xpath = "//p[@class='mx-4 flex-1']")
    WebElement successPopUp;

    @FindBy(xpath = "//li[contains(@class,'PaginateItems')][1]//img")
    WebElement firstSkuInPsPage;

    @FindBy(xpath = "//button[text()='ADD']")
    WebElement addToBasket;

    @FindBy(xpath = "//div[contains(@class,'Toast___StyledDiv-sc')]")
    WebElement decrementPopUp;

    @FindBy(xpath = "//header[2]//button//*[contains(@class,'CategoryMenu___StyledArrowDown')]")
    WebElement shopByCategoryDD;

    @FindBy(xpath = "//header[2]//div[contains(@id,'headlessui-menu-items')]//a[contains(@class,'CategoryTree___StyledLink')][normalize-space()='Fruits & Vegetables']")
    WebElement selectItemFromL1;

    @FindBy(xpath = "//header[2]//div[contains(@class,'CategoryMenu___StyledMenuItems')]//ul")
    WebElement PLMenu;

    @FindBy(xpath = "//li[contains(@class,'PaginateItems')][1]//button[text()='Add']")
    WebElement addItemFromL1;

    @FindBy(xpath = "//*[@id='cross_svg__a']")
    WebElement closePopUp;

    @FindBy(xpath = "//div[contains(@class,'CartCTA___StyledDiv2')]")
    WebElement qtyText;

    @FindBy(xpath = "//li[contains(@class,'PaginateItems')][1]//div[contains(@class,'Pricing')]//span")
    WebElement getSKUPrice;

    @FindBy(xpath = "//div[contains(@class,'SKUDeck')][1]//button[text()='Add']")
    WebElement addFirstSKUFromSmartBasket;

    @FindBy(xpath = "//div[@class='w-10']//button[contains(@class,'Button-sc-1dr2sn8-0 CtaButtons')]//*[name()='svg']")
    WebElement saveForLater;


    public SearchAndAddToCart(WebDriver driver, IReport report) {

        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 200);}

    //* This method searches product from search bar and add items from autosearch result*//
    public void searchProductAndAdd(String item) throws InterruptedException{

        wait.until(ExpectedConditions.visibilityOf(searchBar));
        searchBar.clear();
        searchBar.sendKeys(item);
        report.log("Searching " + item + " from autosearch", true);
        Thread.sleep(3000);
        wait.until(ExpectedConditions.visibilityOf(autoSearchResult));
        report.log("Auto search results are coming", true);
        wait.until(ExpectedConditions.visibilityOf(addFromAutoSearch));
        addFromAutoSearch.click();
        wait.until(ExpectedConditions.visibilityOf(successPopUp));
        report.log("Item succesfully added to cart",true);
        wait.until(ExpectedConditions.visibilityOf(clearSearch));
        clearSearch.click();

    }
//* This method add items in basket from plus button in basket page*//
    public void incrementItemInCart(){
        wait.until(ExpectedConditions.visibilityOf(addBttn));
        addBttn.click();
        wait.until(ExpectedConditions.visibilityOf(successPopUp));
        report.log("Item incremented in cart",true);
    }

    public void incrementItemFromPD(){
        wait.until(ExpectedConditions.visibilityOf(addBttn));
        addBttn.click();
        wait.until(ExpectedConditions.visibilityOf(successPopUp));
        report.log("Item incremented from PD Page",true);
    }

    public void decrementItemFromPD() throws InterruptedException{
        Thread.sleep(3000);
        wait.until(ExpectedConditions.visibilityOf(minusBttn));
        minusBttn.click();
        wait.until(ExpectedConditions.visibilityOf(decrementPopUp));
        report.log("Item decremented from PD Page",true);
    }

    //* This method remove items in basket from minus button in basket page*//
    public void removeItemFromCart() throws InterruptedException{
        Thread.sleep(3000);
        wait.until(ExpectedConditions.visibilityOf(minusBttn));
        minusBttn.click();
        wait.until(ExpectedConditions.visibilityOf(decrementPopUp));
        report.log("Item decremented from cart",true);
    }

    public String searchProductGoToPdPageAndAdd(String item) throws InterruptedException {

        wait.until(ExpectedConditions.visibilityOf(searchBar));
        searchBar.clear();
        searchBar.sendKeys(item);
        Thread.sleep(5000);
        searchBar.sendKeys(Keys.ENTER);
        searchBar.sendKeys(Keys.ENTER);
        Thread.sleep(5000);
        wait.until(ExpectedConditions.visibilityOf(firstSkuInPsPage));
        report.log("Sku's are visible in PS page", true);
        Thread.sleep(5000);
        wait.until(ExpectedConditions.visibilityOf(getSKUPrice));
        String price= getSKUPrice.getText();
        wait.until(ExpectedConditions.elementToBeClickable(firstSkuInPsPage));
        firstSkuInPsPage.click();
        report.log("First available SKU is clicked",true);
        Thread.sleep(5000);
        String parent=driver.getWindowHandle();
        for (String child_window : driver.getWindowHandles()) {
            if(!parent.contentEquals(child_window)) {
                driver.switchTo().window(child_window);
                report.log("Switched to pd page "+driver.switchTo().window(child_window).getTitle(), true);
                break;
            }
        }
        Thread.sleep(3000);
        wait.until(ExpectedConditions.visibilityOf(addToBasket));
        addToBasket.click();
        report.log("Item succesfully added to cart",true);
        return price;
    }

    public String searchProductGoToPdPageAndSaveItForLater(String item) throws InterruptedException {

        wait.until(ExpectedConditions.visibilityOf(searchBar));
        searchBar.clear();
        searchBar.sendKeys(item);
        Thread.sleep(5000);
        searchBar.sendKeys(Keys.ENTER);
        searchBar.sendKeys(Keys.ENTER);
        Thread.sleep(5000);
        wait.until(ExpectedConditions.visibilityOf(firstSkuInPsPage));
        report.log("Sku's are visible in PS page", true);
        Thread.sleep(5000);
        wait.until(ExpectedConditions.visibilityOf(getSKUPrice));
        String price= getSKUPrice.getText();
        wait.until(ExpectedConditions.elementToBeClickable(firstSkuInPsPage));
        firstSkuInPsPage.click();
        report.log("First available SKU is clicked",true);
        Thread.sleep(5000);
        String parent=driver.getWindowHandle();
        for (String child_window : driver.getWindowHandles()) {
            if(!parent.contentEquals(child_window)) {
                driver.switchTo().window(child_window);
                report.log("Switched to pd page "+driver.switchTo().window(child_window).getTitle(), true);
                break;
            }
        }
        wait.until(ExpectedConditions.visibilityOf(saveForLater));
        saveForLater.click();
        Thread.sleep(2000);
        report.log("Item saved succesfully",true);
        return price;
    }
    //* This method add item in cart from PC/PL*//
    public void searchFromPCAndAddToCart(){


        wait.until(ExpectedConditions.elementToBeClickable(shopByCategoryDD));
        shopByCategoryDD.click();
        wait.until(ExpectedConditions.visibilityOf(PLMenu));
        report.log("L1,L2,L3 menus are visible from Shop By Category",true);
        wait.until(ExpectedConditions.elementToBeClickable(selectItemFromL1));
        selectItemFromL1.click();
        report.log("selected Fruits & Vegetables options from L1 category",true);
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        report.log("scrolling down",true);
        jse.executeScript("window.scrollBy(0,100)");
        wait.until(ExpectedConditions.elementToBeClickable(addItemFromL1));
        addItemFromL1.click();
        wait.until(ExpectedConditions.visibilityOf(successPopUp));
        successPopUp.click();
        report.log("item added in cart",true);

    }

    public void addMaxUnitsOfItem(String item) throws InterruptedException {


        wait.until(ExpectedConditions.visibilityOf(addBttn));
        addBttn.click();
        wait.until(ExpectedConditions.visibilityOf(closePopUp));
        Thread.sleep(3000);
        wait.until(ExpectedConditions.visibilityOf(qtyText));
        int qty= Integer.parseInt(qtyText.getText());
        if(count<qty)
        {   count++;
            addMaxUnitsOfItem(item);

        }
        else{
            report.log("Maximum "+qty+ " units can be added for "+item,true);
        }

    }

    public void addSKUFromSmartBasket() throws InterruptedException{

        wait.until(ExpectedConditions.visibilityOf(addFirstSKUFromSmartBasket));
        addFirstSKUFromSmartBasket.click();
        wait.until(ExpectedConditions.visibilityOf(successPopUp));
        report.log("Item added from Smart Basket",true);
        Thread.sleep(3000);
        JavascriptExecutor jse = (JavascriptExecutor)driver;
        report.log("scrolling up",true);
        jse.executeScript("window.scrollBy(0,-300)");
        Thread.sleep(3000);
    }
}
