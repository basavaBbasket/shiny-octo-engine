package hubops_addingdata;

import HubopsPages.CromaHomePage;
import HubopsPages.HubopsPage;
import HubopsPages.ModHeaderUtils;
import HubopsPages.TransportManagementSystemPage;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utility.readexceldata.ReadExcelData;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FieldCeeCategory extends BaseTest {
    WebDriver driver;
    @DataProvider
    public Object[] hubOpsData () throws IOException {
        return ReadExcelData.getTestData("src/main/resources/Croma/Croma.xlsx", "Cee Category");

    }

    @DescriptionProvider(slug = "HubOps DataSetup", description = "\"1. Do web login of CromaPage \" +\n" +
            "            \"            2. Get the Module and Submodule values from Cromapage\" +\n" +
            "            \"            3. login into TMS page of Admin \" +\n" +
            "            \"            4. Click on Field Assets module and Cee Category and add the details\"", author = "Nijaguna")
    @Test(dataProvider = "hubOpsData", groups = {"CeeCategory","Croma","CromaDataSetup"})
    public void test(Map<String,String> testData) throws InterruptedException, IOException {
        String moduleName = "Field Assets ";
        String subModuleName = "Cee Category";
        AutomationReport report = getInitializedhubopsReport(this.getClass(), true, Map.class);
        driver = report.driver;
        report.log("logging into Croma page", true);
        report.log("Launching the croma", true);
        ModHeaderUtils modHeaderUtils = new ModHeaderUtils(driver, report);
        modHeaderUtils.modHeaderProfiles(driver,"croma");
        Properties prop = new Properties();
        driver.get(System.getProperty("cromaURL","https://croma-qa.bb-matrix.in/hertz/index"));
        prop.load(new FileInputStream("src/main/resources/config.properties"));
        CromaHomePage cromaHomePage = new CromaHomePage(driver);
        cromaHomePage.cromaLogin(prop.getProperty("cromaUsername"), prop.getProperty("cromaPassword"));
        report.log("Navigating to Cee Category page", true);
        TransportManagementSystemPage transportManagementSystemPage=new TransportManagementSystemPage(driver);
        transportManagementSystemPage.navigateToSubModukle(moduleName,subModuleName);
        HubopsPage hubopsPage=new HubopsPage(driver);
        hubopsPage.hubopsCeeCategory(testData);
        report.log("Cee Category Data is Added ", true);

    }
}
