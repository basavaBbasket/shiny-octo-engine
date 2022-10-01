package bbnow.pmcases;

import admin.pages.*;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.replenishment.internal.GetPoDetails;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import planogram.admin.wio.FcReceiving;
import planogram.admin.wio.FcReceivingSearch;
import planogram.admin.wio.WIOSAdministrationUI;
import utility.ReplenishmentUtils;
import utility.WarehouseInboundOutboundUtils;
import utility.database.DynamicLocationDBQueries;

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
    @Test(groups = {"bbnow" , "regression","dl2","earlyrelease","oneclickgrn"})
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

        int skuid=Integer.valueOf(System.getProperty("ircdcskuid",Config.bbnowConfig.getString("ircdcskuid")));
        int quantity= 2;
        String action ="Create IRCDC Orders";
        String remoteDc = System.getProperty("bbnowsourcedc",Config.bbnowConfig.getString("interdc_source_dc1"));
        String sourcedc=System.getProperty("bbnowdc",Config.bbnowConfig.getString("ircdc_source_dc1_bb2"));
        String hostDc = System.getProperty("bbnowhostds",Config.bbnowConfig.getString("ircdc_dest_dc1"));


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
        String poid=fcReceiving.selectpoforgrn(orderid);

        fcReceiving.completegrn(1);
        ReplenishmentUtils replenishmentUtils=new ReplenishmentUtils();
        int iteration = 0;
        int status=replenishmentUtils.getCurrentPurchaseOrderStatus(poid,report);
        while(iteration<11){
            if(status==4)
                break;
            Thread.sleep(1000*30);
            driver.navigate().refresh();
            status=replenishmentUtils.getCurrentPurchaseOrderStatus(poid,report);
            iteration++;
        }
        Assert.assertEquals(replenishmentUtils.getCurrentPurchaseOrderStatus(poid,report),4,"purchase order status is not changed after performing grn for the po"+poid);
        GetPoDetails getPoDetails=new GetPoDetails(poid,"bb-internal","102","hertz",report);
        Response response=getPoDetails.getPoDetails(true);
        Assert.assertEquals(getPoDetails.getpostatus(response),4,"po status is not changed to complete post grn");
        WarehouseInboundOutboundUtils warehouseInboundOutboundUtils=new WarehouseInboundOutboundUtils();
        int fcreceivingid=warehouseInboundOutboundUtils.getFcreceivingID(poid,report);
        warehouseInboundOutboundUtils.getFcreceivingItemID(fcreceivingid,report);



    }
}
