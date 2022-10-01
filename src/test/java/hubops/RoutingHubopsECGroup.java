package hubops;

import HubopsPages.CromaHomePage;
import HubopsPages.ModHeaderUtils;
import HubopsPages.TransportManagementSystemPage;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import foundation.catalog.admin.Utils.CatalogAdminUtils;
import framework.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class RoutingHubopsECGroup extends BaseTest {

    @DescriptionProvider(slug = "HubOps DataSetup", description = "\"1. Do web login of CromaPage \" +\n" +
            "            \"            2. Get the Module and Submodule values from Cromapage\" +\n" +
            "            \"            3. login into TMS page of Admin \" +\n" +
            "            \"            4. Click on Routing module and Hubops EC Group and add the details\"", author = "Nijaguna")
    @Test(groups = {"HubOps","CromaDataSetup"}, enabled = false)
    public void deliveryNotes() throws IOException, InterruptedException {
        String moduleName = "Routing";
        String subModuleName = "Hubops EC Group";
        AutomationReport report = getInitializedhubopsReport(this.getClass(), true);
        WebDriver driver = report.driver;
        ModHeaderUtils modHeaderUtils = new ModHeaderUtils(driver, report);
        modHeaderUtils.modHeaderProfiles(driver,"croma");
        report.log("logging into Croma page", true);
        report.log("Launching the croma", true);
        driver.get(System.getProperty("cromaURL","https://croma-qa.bb-matrix.in/hertz/index"));
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/main/resources/config.properties"));
        CromaHomePage cromaHomePage = new CromaHomePage(driver);
        cromaHomePage.cromaLogin(prop.getProperty("cromaUsername"), prop.getProperty("cromaPassword"));
        report.log("Navigating to Hubops EC Group page", true);
        TransportManagementSystemPage transportManagementSystem = new TransportManagementSystemPage(driver);
        List<List<String>> details = transportManagementSystem.navigateToTransportManagementSystem(moduleName, subModuleName);
        report.log("Getting the Details from Hubops EC Group page", true);
        CatalogAdminUtils.addExcelData("/src/main/resources/HubOps.xlsx",subModuleName,details);

    }
}
