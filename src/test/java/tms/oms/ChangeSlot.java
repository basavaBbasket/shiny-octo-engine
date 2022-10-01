package tms.oms;

import tms.api.OMS.ChangeSlots;
import tms.api.OMS.UpdateOrderStatus;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.vertx.core.json.JsonObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import tms.configAndUtilities.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class ChangeSlot extends BaseTest {

    @DescriptionProvider(slug = "Change Slot when order is in open status", description = "1. Get available slots for given location context and group of skus\n" +
            "            2. Reseve the slot by passing slot info  from get-slots\n" +
            "            3. Hit the confirm order Api and genrate order id\n" +
            "            4. Fetch Slot details  \n" +
            "            5. Do Change Slot to next aviablable date \n" ,author = "tushar")

    @Test(groups = {"test_oms","TMS","OMS","change_slot"})
    public void changeSlotInOpenState() throws IOException {

        AutomationReport report = getInitializedReport(this.getClass(), false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String ,String> headersData = CreateHeader.createHeader();
        Map<String,String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
        Random rand = new Random();
        int external_reference_id=(int)rand.nextInt(1000000000);
        Integer orderId = OrderCreation.placeTmsOrder(report , memberData,headersData,skuData,external_reference_id);
        String checkOrderDetailsDbQuery = "select * from order_detail where id = "+ orderId.toString() +";";
        String orderDbName = env.toUpperCase()+ "-"+"ORDER-CROMA";
        JsonObject orderDetailsJson = new JsonObject(TmsDBQueries.getOrderDetails(orderDbName,checkOrderDetailsDbQuery));
        Assert.assertEquals(true, orderDetailsJson.getInteger("numRows") == 1,"");
        ArrayList<ArrayList<String>> avialableSLotsDetails = OrderCreation.fetchAvailableSlots;
        String sloDate = avialableSLotsDetails.get(0).get(1);
        String slotDefinitionId = avialableSLotsDetails.get(1).get(1);
        String templateSlotId = avialableSLotsDetails.get(2).get(1);
        ChangeSlots.doChangeSlot(report,headersData,orderId,sloDate,slotDefinitionId,templateSlotId);
    }

    @DescriptionProvider(slug = "Change SLot when order is in packed status", description = "1. Get available slots for given location context and group of skus\n" +
            "            2. Reseve the slot by passing slot info  from get-slots\n" +
            "            3. Hit the confirm order Api and genrate order id\n" +
            "            4. Fetch Slot details  \n" +
            "            5. Do Change Slot to next aviablable date \n" ,author = "tushar")

    @Test(groups = {"test_oms","TMS","OMS","change_slot"})
    public  void changeSlotInPackedState() throws IOException {

        AutomationReport report = getInitializedReport(this.getClass(), false);
        String env = AutomationUtilities.getEnvironmentFromServerName(serverName);
        Map<String, String> memberData = MemberData.getMemberDetailsMap(env.toUpperCase());
        Map<String ,String> headersData = CreateHeader.createHeader();
        Map<String,String> skuData = SkuData.getSkuDetailsMap(env.toUpperCase());
        Random rand = new Random();
        int external_reference_id=(int)rand.nextInt(1000000000);
        Integer orderId = OrderCreation.placeTmsOrder(report , memberData,headersData,skuData,external_reference_id);
        String checkOrderDetailsDbQuery = "select * from order_detail where id = "+ orderId.toString() +";";
        String orderDbName = env.toUpperCase()+ "-"+"ORDER-CROMA";
        JsonObject orderDetailsJson = new JsonObject(TmsDBQueries.getOrderDetails(orderDbName,checkOrderDetailsDbQuery));
        Assert.assertEquals(true, orderDetailsJson.getInteger("numRows") == 1,"");
        UpdateOrderStatus.updateOrderStatus(report,memberData,skuData,headersData,orderId,"packed");
        ArrayList<ArrayList<String>> avialableSLotsDetails = OrderCreation.fetchAvailableSlots;
        String sloDate = avialableSLotsDetails.get(0).get(1);
        String slotDefinitionId = avialableSLotsDetails.get(1).get(1);
        String templateSlotId = avialableSLotsDetails.get(2).get(1);
        ChangeSlots.doChangeSlot(report,headersData,orderId,sloDate,slotDefinitionId,templateSlotId);

    }

}
