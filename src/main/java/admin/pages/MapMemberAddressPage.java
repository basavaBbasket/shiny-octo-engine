package admin.pages;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import io.vertx.core.json.JsonObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MapMemberAddressPage extends WebSettings {

    @FindBy(xpath = "//input[@value='order_id']")
    WebElement orderIdBtn;

    @FindBy(xpath = "//input[@value='member_id']")
    WebElement memberIdBtn;

    @FindBy(xpath = "//input[@value='member_email']")
    WebElement emailIdBtn;

    @FindBy(xpath = "//input[@value='member_contact']")
    WebElement mobileNumberBtn;

    @FindBy(xpath = "//*[@id=\"member_input\"]")
    WebElement enterData;

    @FindBy(xpath = "//*[@id=\"get_data_btn\"]")
    WebElement searchbtn;


    private WebDriver driver;
    private IReport report;
    private WebDriverWait wait;

    public MapMemberAddressPage(WebDriver driver, IReport report)
    {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }

    public void checkMemberAddress(ArrayList<String> id, String addressIdSelectedForOrder) throws InterruptedException {

        ArrayList<WebElement> btn = new ArrayList<WebElement>();
        btn.add(orderIdBtn);
        btn.add(memberIdBtn);
        btn.add(emailIdBtn);
        btn.add(mobileNumberBtn);

        ArrayList<String> btnNames = new ArrayList<String>();
        btnNames.add("orderIdBtn");
        btnNames.add("memberIdBtn");
        btnNames.add("emailIdBtn");
        btnNames.add("mobileNumberBtn");


        JsonObject jsonObjAddressId = new JsonObject(AutomationUtilities.executeDatabaseQuery(serverName, "select id from member_memberaddress where member_id="+id.get(1) +";"));
        ArrayList<Integer> listOfAddressId = new ArrayList<>();
        for(int i = 0 ; i< jsonObjAddressId.getJsonArray("rows").size();i++) {
            listOfAddressId.add(jsonObjAddressId.getJsonArray("rows").getJsonObject(i).getInteger("id"));
        }
        report.log(String.valueOf(listOfAddressId),true);

        for(int i = 0 ; i<btn.size(); i++)
        {

            wait.until(ExpectedConditions.visibilityOf(enterData));
            btn.get(i).click();
            enterData.clear();
            enterData.sendKeys(id.get(i));
            searchbtn.click();
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            report.log("checking "+ btnNames.get(i),true);
            ArrayList<Boolean> exists = new ArrayList<Boolean>();
                for(int j = 0 ; j<listOfAddressId.size();j++)
                {
                    if(btnNames.get(i)=="orderIdBtn")
                    {

                        String xPath_intial = "//*[@id=\"" + addressIdSelectedForOrder + "-heading\"]/span[5]/a/b";
                        report.log(xPath_intial, true);

                        if (driver.findElements(By.xpath(xPath_intial)).size() > 0)
                            exists.add(true);
                        else
                            exists.add(false);

                        report.log(String.valueOf(listOfAddressId.get(j)), exists.get(j));
                        break;

                    }
                    else {
                        String address_id = String.valueOf(listOfAddressId.get(j));
                        String xPath_intial = "//*[@id=\"" + address_id + "-heading\"]/span[5]/a/b";
                        report.log(xPath_intial, true);

                        if (driver.findElements(By.xpath(xPath_intial)).size() > 0)
                            exists.add(true);
                        else
                            exists.add(false);


                        //exists.add(true);
                        report.log(String.valueOf(listOfAddressId.get(j)), exists.get(j));
                    }
                }
                boolean result = false;
                for(Boolean ele: exists)
                    result = result || ele;

               Assert.assertEquals(result,true);



        }
    }



}
