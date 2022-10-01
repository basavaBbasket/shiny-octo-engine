package bbnow.pmcases;

import admin.pages.HomePage;
import admin.pages.Login;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import planogram.admin.wio.FcReceiving;
import planogram.admin.wio.FcReceivingSearch;
import planogram.admin.wio.WIOSAdministrationUI;


public class ValidateEmptyPo extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This test case validates ", slug = "validate Empty po response")
    @Test(groups = {"bbnow" , "regression","dl2","earlyrelease"})
    public void validateOrderDropdown() throws InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(),true);
        WebDriver driver=report.driver;

        launchSite("admin", driver);

        String sourcedc=Config.bbnowConfig.getString("ircdc_source_dc1_bb2");//todo will change once config is received
        String hostDc = Config.bbnowConfig.getString("ircdc_dest_dc2");//todo will change once config is received


        Login login = new Login(driver,report);
        HomePage home = login.doAdminLogin(adminSuperuser,adminSuperuserPassword);

        WIOSAdministrationUI wiosAdministrationUI=home.navigateToPlanogramAdmin(driver,report);
        FcReceivingSearch fcReceivingSearch=wiosAdministrationUI.clickOnOneCLickGRNLink();
        fcReceivingSearch.getActivepos(sourcedc,hostDc);
        FcReceiving fcReceiving=new FcReceiving(driver,report);
        fcReceiving.validateEmptyPoScreen();


    }
}
