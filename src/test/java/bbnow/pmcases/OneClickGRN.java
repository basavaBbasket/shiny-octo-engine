package bbnow.pmcases;

import admin.pages.*;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import planogram.admin.wio.FcReceiving;
import planogram.admin.wio.FcReceivingSearch;
import planogram.admin.wio.WIOSAdministrationUI;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class OneClickGRN extends BaseTest {


    @DescriptionProvider(author = "Pranay", description = "This testcase validates the One click grn Flow. * First creates path between ds and dc\n" +
            "     * place and ircdc order\n" +
            "     * mark the ircdc order as packed then po will be  created\n" +
            "     * navigates to one click grn screen \n" +
            "     * select the pos you wish to grn \n" +
            "     * click grn ",slug = "One CLick GRN api")
    @Test(groups = {"bbnow","dl2","earlyrelease","test3"})
    /**
     * first need to create path between ds and dc
     * ircdc order
     * mark the orcdc order as packed then po will created
     * you have open one click grn screen from planogram admin
     * select the pos you wish to grn
     * click grn
     */
    public void OneClickGRN() throws InterruptedException, IOException {
        AutomationReport report = getInitializedReport(this.getClass(),true);
        WebDriver driver=report.driver;

        launchSite("admin", driver);
        int skuid=Integer.valueOf(System.getProperty("ircdcskuid","102830"));
        int quantity= 10;
        String action ="Create IRCDC Orders";
        String remoteDc = System.getProperty("bbnowsourcedc","Bangalore-DC");
        String sourcedc=System.getProperty("bbnowdc","Bangalore-FMCG-DC");
        String hostDc = System.getProperty("bbnowhostds");

        if(trimmedServerName.contains("uat")){
             hostDc = System.getProperty("bbnowhostds","DS-BBNow_Shanthinikatan");
        }
        else {
             hostDc = System.getProperty("bbnowhostds", "DS-BBnow Santhinikean 5K");
        }

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date dateobj = new Date();


        Login login = new Login(driver,report);
        HomePage home = login.doAdminLogin(adminSuperuser,adminSuperuserPassword);

        IrcdcOrderAction ircdcOrderAction = home.clickIrcdcOrderAction(driver, report);

        boolean isIrcdcOrderPossible = ircdcOrderAction.IrcdcSetupCheck(remoteDc, hostDc);
        Assert.assertTrue(isIrcdcOrderPossible, "Setup was not successful for IRCDC order between "
                + "<br>HostDC(delivered HUB) = "+hostDc
                + "<br>RemoteDC(picked HUB) = "+remoteDc);

        String memberid=ircdcOrderAction.getmemberid();

        ArrayList<Integer> skulist=new ArrayList<>();
        skulist.add(skuid);
        ArrayList<Integer> quantitylist=new ArrayList<>();
        quantitylist.add(quantity);
        String filelocation= ircdcOrderAction.ircdcuploadfilepath(skulist,quantitylist);
        ircdcOrderAction = ircdcOrderAction.createInterDcOrder(action, hostDc, remoteDc, filelocation);

        Assert.assertTrue(ircdcOrderAction.isIrcdcOrderPlaced(), "IRCDC order was not placed successfully");

        IrcdcOrder ircdcOrder=new IrcdcOrder(driver,report);
        ircdcOrder.gotoIRCDCOrderListPage();
        ircdcOrder.searchForIRCDCOrder(String.valueOf(skuid),df.format(dateobj));

        Order order= ircdcOrder.openorderdetails();
        ModifyOrder modifyOrder= order.clickOnModifyOrderLink();
        String orderid=modifyOrder.getorderid();
        modifyOrder.changeStatus("Packed");
       home=home.navigatetohomepage();
        WIOSAdministrationUI wiosAdministrationUI=home.navigateToPlanogramAdmin(driver,report);
        FcReceivingSearch fcReceivingSearch=wiosAdministrationUI.clickOnOneCLickGRNLink();
        fcReceivingSearch.getActivepos(sourcedc,hostDc);
        FcReceiving fcReceiving=new FcReceiving(driver,report);
        fcReceiving.selectpoforgrn(orderid);
        fcReceiving.completegrn(1);

    }
}
