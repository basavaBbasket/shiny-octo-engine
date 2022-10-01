package bbnow.pmcases;

import admin.pages.HomePage;
import admin.pages.Login;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import planogram.admin.wio.FcReceivingSearch;
import planogram.admin.wio.FcReturnUploadUi;
import planogram.admin.wio.WIOSAdministrationUI;
import utility.WarehouseInboundOutboundUtils;
import utility.database.DynamicLocationDBQueries;

import java.io.IOException;
import java.util.ArrayList;

public class InterFcPrn extends BaseTest {

    @DescriptionProvider(author = "Pranay", description = "This test case validates ", slug = "validate InterFcPrn response")
    @Test(groups = {"bbnow" , "regression","dl2","earlyrelease"})
    public void InterFcPrn() throws InterruptedException, IOException {
        AutomationReport report = getInitializedReport(this.getClass(), true);
        WebDriver driver = report.driver;

        launchSite("admin", driver);

        int quantity = 2 ;
        String fcname=Config.bbnowConfig.getString("ircdc_dest_dc1");
        int Skuid=Integer.valueOf(Config.bbnowConfig.getString("interfc_prnskuid"));
        Login login = new Login(driver,report);
        HomePage home = login.doAdminLogin(adminSuperuser,adminSuperuserPassword);

        WIOSAdministrationUI wiosAdministrationUI=home.navigateToPlanogramAdmin(driver,report);
        FcReturnUploadUi fcReturnUploadUi=wiosAdministrationUI.clickOnFcReturnUploadLink();

        double initialquantity= fcReturnUploadUi.getFcReturnSkuidquantity(Skuid);
        ArrayList<String> fcreturnuploaddata= new ArrayList<>();
        fcreturnuploaddata.add(String.valueOf(Skuid));
        fcreturnuploaddata.add(String.valueOf(quantity));
        WarehouseInboundOutboundUtils warehouseInboundOutboundUtils=new WarehouseInboundOutboundUtils();
        int initialfcreturncount=warehouseInboundOutboundUtils.getCountOfFcreturntable(report);
        int initialfcreturnitemcount=warehouseInboundOutboundUtils.getCountOfFcreturnItemtable(report);
        String filelocation=fcReturnUploadUi.fcReturnPRNFileLocation(fcreturnuploaddata,"fcreturn_upload_sample");
        fcReturnUploadUi.interFcPRNUpload(fcname,filelocation);
        double finalquantity=fcReturnUploadUi.getFcReturnSkuidquantity(Skuid);

        Assert.assertEquals(finalquantity,(initialquantity-quantity));
        int finalfcreturncount=warehouseInboundOutboundUtils.getCountOfFcreturntable(report);
        int finalfcreturnitemcount=warehouseInboundOutboundUtils.getCountOfFcreturnItemtable(report);
        Assert.assertEquals(finalfcreturncount,initialfcreturncount+1,"No New entry is create dafter the interfc prn upload in fc return table");
        Assert.assertEquals(finalfcreturnitemcount,initialfcreturnitemcount+1,"No New entry is create dafter the interfc prn upload in fc return item table");
    }
}
