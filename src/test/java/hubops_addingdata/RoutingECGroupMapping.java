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

public class RoutingECGroupMapping extends BaseTest {
    WebDriver driver;
    @DataProvider
    public Object[] hubOpsData () throws IOException {
        return ReadExcelData.getTestData("src/main/resources/Croma/Croma.xlsx", "EC Group Mapping");
    }

    @DescriptionProvider(slug = "HubOps DataSetup", description = "\"1. Do web login of CromaPage \" +\n" +
            "            \"            2. Get the Module and Submodule values from Cromapage\" +\n" +
            "            \"            3. login into TMS page of Admin \" +\n" +
            "            \"            4. Click on Routing module and EC Group Mapping and add the details\"", author = "Nijaguna")
    @Test(dataProvider = "hubOpsData", groups = {"ECGrpMapping","Croma","CromaDataSetup"})
    public void test(Map<String,String> testData) throws InterruptedException, IOException {
        String moduleName = "Routing";
        String subModuleName = "EC Group Mapping";
        AutomationReport report = getInitializedhubopsReport(this.getClass(), true, Map.class);
        driver = report.driver;
        report.log("logging into Croma page", true);
        report.log("Launching the croma", true);
        ModHeaderUtils modHeaderUtils = new ModHeaderUtils(driver, report);
        modHeaderUtils.modHeaderProfiles(driver,"croma");
        driver.get(System.getProperty("cromaURL","https://croma-qa.bb-matrix.in/hertz/index"));
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/main/resources/config.properties"));
        CromaHomePage cromaHomePage = new CromaHomePage(driver);
        cromaHomePage.cromaLogin(prop.getProperty("cromaUsername"), prop.getProperty("cromaPassword"));
        report.log("Navigating to EC Group Mapping page", true);
        TransportManagementSystemPage transportManagementSystemPage=new TransportManagementSystemPage(driver);
        transportManagementSystemPage.navigateToSubModukle(moduleName,subModuleName);
        HubopsPage hubopsPage=new HubopsPage(driver);
        hubopsPage.ecGroupMapping(testData);
        report.log("EC Group Mapping Data is Added ", true);

    }
}
