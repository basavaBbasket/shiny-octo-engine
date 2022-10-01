package foundation.catalog.admin.fcstatus;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.AutomationReport;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Arrays;

public class CatalogFCStatus extends WebSettings {

    /**
     * xpaths
     */
    @FindBy(xpath = "//*[@id=\"root\"]/div/div/div/header/div/button[2]")
    private WebElement RefreshButton;

    @FindBy(xpath = "//*[@id=\"fc_id\"]")
    private WebElement FC_filter;

    @FindBy(xpath = "//*[@id=\"sku_id\"]")
    private WebElement sku_filter;

    @FindBy(xpath = "//*[@id=\"root\"]/div/div/div/main/div[2]/div/div[2]/div/table/tbody/tr/td[4]/span")
    private WebElement sku_status;



    private WebDriver driver;
    private AutomationReport report;
    private WebDriverWait wait;

    /**
     * constructor for setting up reports and driver
     * @param driver
     * @param report
     */
    public CatalogFCStatus(WebDriver driver, AutomationReport report) {
        this.driver = driver;
        this.report = report;
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 60);
    }


    /**
     * check the Status of SKU to be active or in-active
     * @param FC_name the FC filter
     * @param sku_id The SKU filter
     * @return String with the actual status of the SKU
     * @throws InterruptedException
     */
    public String FilterDataByFCandSKU(String FC_name,int sku_id) throws InterruptedException {
        FC_filter.click();
        FC_filter.sendKeys(Keys.ARROW_DOWN);
        FC_filter.sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        FC_filter.sendKeys(FC_name);
        FC_filter.sendKeys(Keys.ARROW_DOWN);
        FC_filter.sendKeys(Keys.ARROW_DOWN);
        FC_filter.sendKeys(Keys.ENTER);
        Thread.sleep(3000);
        sku_filter.sendKeys(Keys.chord(Keys.CONTROL, "a"),String.valueOf(sku_id));
        Thread.sleep(3000);
        String skuStatus = sku_status.getText();
        return skuStatus;
    }


    /**
     * To create Data for SKU status Upload, Major Fields which are being chnaged are FC_id and sku_id
     * @param fc_id The fc_id for which the SKU status has to be changed.
     * @param sku_id The SKU for which status has to be changed
     * @return An array list of string arrays whose each item is a row entry for file.
     */
    public ArrayList<String[]> catalogStatusUploadData(int fc_id, ArrayList<Integer> sku_id){
        ArrayList<String[]> resultSet = new ArrayList<String[]>();
        for(int i=0;i<sku_id.size();i++){
            String []temp = new String[]
                    {String.valueOf(fc_id),String.valueOf(sku_id.get(i)),"1","bulk-deactivate"};
            resultSet.add(temp);
            System.out.println("array Size"+ resultSet.size());
            System.out.println(resultSet.toString());
            System.out.println(Arrays.toString(resultSet.get(i)));
        }
        return resultSet;
    }

    /**
     * Create data for catalog status upload with custom status id.
     * @param fc_id The Fc for which Upload has to be done
     * @param sku_id List of Skus whose status has to be changed
     * @param status The status id to which the status of the SKU has to be changed
     * @return An array list of string arrays whose each item is a row entry for file.
     */
    public ArrayList<String[]> catalogStatusUploadData(int fc_id, ArrayList<Integer> sku_id,int status){
        ArrayList<String[]> resultSet = new ArrayList<String[]>();
        for(int i=0;i<sku_id.size();i++){
            String []temp = new String[]
                    {String.valueOf(fc_id),String.valueOf(sku_id.get(i)),"" +status+
                            "","bulk-deactivate"};
            resultSet.add(temp);
            System.out.println("array Size"+ resultSet.size());
            System.out.println(resultSet.toString());
            System.out.println(Arrays.toString(resultSet.get(i)));
        }
        return resultSet;
    }
}
