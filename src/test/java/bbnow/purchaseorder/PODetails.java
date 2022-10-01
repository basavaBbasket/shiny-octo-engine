package bbnow.purchaseorder;

import admin.pages.*;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import msvc.replenishment.internal.GetPoDetails;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import planogram.admin.wio.FcReceiving;
import planogram.admin.wio.FcReceivingSearch;
import planogram.admin.wio.WIOSAdministrationUI;
import utility.ReplenishmentUtils;
import utility.WarehouseInboundOutboundUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PODetails extends BaseTest {


    @DescriptionProvider(author = "Shruti", description = "This testcase validates the response of the GetPODetails API ",slug = "GetPODetails API")
    @Test(groups = {"bbnow" , "regression","dl2","earlyrelease","oneclickgrn","uat2monolith"})
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

        String query = "select request_id,status,request_type,primary_reason from fcreceiving where order_id="+orderid+";";
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getWarehouseInboundOutboundDBName(),query));
        String poid = jsonObject.getJSONArray("rows").getJSONObject(0).getString("request_id");
        String status = jsonObject.getJSONArray("rows").getJSONObject(0).getString("status");
        String request_type = jsonObject.getJSONArray("rows").getJSONObject(0).getString("request_type");
        String primary_reason = jsonObject.getJSONArray("rows").getJSONObject(0).getString("primary_reason");
        Assert.assertEquals("pending_receiving",status);
        report.log("Status of InterFC Request is "+status+ " ,request_type is "+request_type+" and primary_reason is "+primary_reason+" when order is packed",true);

        GetPoDetails getPoDetails=new GetPoDetails(String.valueOf(poid),"bb-internal","102","hertz",report);
        Response response=getPoDetails.getPoDetails(true);
        jsonObject = new JSONObject(response.asString());
        boolean destination_fc_id = jsonObject.has("destination_fc_id");
        Assert.assertTrue(destination_fc_id);
        boolean po_number = jsonObject.has("po_number");
        Assert.assertTrue(po_number);
        boolean po_type = jsonObject.has("po_type");
        Assert.assertTrue(po_type);
        JsonPath json = new JsonPath(response.asString());
        int sku_id = json.getInt("items[0].sku_id");
        double cp=json.getDouble("items[0].final_cp");
        double mrp =json.getDouble("items[0].mrp");
        report.log("Fc_id,po_number,po_type "+"sku_id: "+sku_id+" cp: "+cp+" mrp: " +mrp+"are present in GetPODetails response",true);

    }
}
