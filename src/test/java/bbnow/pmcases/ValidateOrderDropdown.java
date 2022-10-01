package bbnow.pmcases;

import admin.pages.*;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import org.openqa.selenium.WebDriver;

import org.testng.annotations.Test;

import planogram.admin.wio.FcReceiving;
import planogram.admin.wio.FcReceivingSearch;
import planogram.admin.wio.WIOSAdministrationUI;

import java.util.ArrayList;


public class ValidateOrderDropdown extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This test case validates if all the order types are listing under order dropdown in get po filter screen in planogram one click grn flow ")
    @Test(groups = {"bbnow" , "regression","dl2","earlyrelease"})
    public void validateOrderDropdown() {
        AutomationReport report = getInitializedReport(this.getClass(),true);
        WebDriver driver=report.driver;

        launchSite("admin", driver);

        ArrayList<String> orderlistdropdown=new ArrayList<>();
        orderlistdropdown.add("Manual");
        orderlistdropdown.add("LongTail");
        orderlistdropdown.add("BBDaily");
        orderlistdropdown.add("DLT");
        orderlistdropdown.add("DLT-bulk");
        orderlistdropdown.add("DLT-case");
        orderlistdropdown.add("DLT-loose");

        Login login = new Login(driver,report);
        HomePage home = login.doAdminLogin(adminSuperuser,adminSuperuserPassword);
        WIOSAdministrationUI wiosAdministrationUI=home.navigateToPlanogramAdmin(driver,report);
        FcReceivingSearch fcReceivingSearch=wiosAdministrationUI.clickOnOneCLickGRNLink();
        fcReceivingSearch.validateOrdertype(orderlistdropdown);



    }
}
