package bbnow.testcases.order.CancelOrder;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.DeliveryBinMethods;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.PickingMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import msvc.order.external.CancelOrder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CancelOrderBeforeBinning extends BaseTest {

    @DescriptionProvider(slug = "Cancel Before Binning", description = "1. Place BBnow order \n" +
            "            2. Check whether the order entry is created in picking_request and picking_item_request table \n" +
            "            3. Complete the picking flow. \n" +
            "            4. Cancel the order before order bin mapping" , author = "Shruti")
    @Test(groups = {"bbnow", "dlphase2", "pickingFlow","deliverybin"})
    public void cancelBinnedOrder() throws InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String entry_context= Config.bbnowConfig.getString("entry_context");
        String entry_context_id=Config.bbnowConfig.getString("entry_context_id");
        String clientUserSheetName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_client_user_sheet_name");
        String areaName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_area_name");
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));
        String searchTerm = Config.bbnowConfig.getString("bbnow_stores[1].category_slug1");
        String searchType = Config.bbnowConfig.getString("bbnow_stores[1].search_type2");

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        String member_id= AutomationUtilities.getMemberIDForGivenID(cred[0],report);

        Map<String, String> adminCookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);
        Map<String,String> memberCookie = MemberCookie.getMemberCookieForCustomAddress(cred[0],areaName,report);


        int skuID =  EligibleSkuMethods.fetchSkuVisibleAndAvailableInPrimaryBin(memberCookie,adminCookie, fcId,entry_context,Integer.parseInt(entry_context_id),areaName,searchTerm,searchType,report);
        int skuID2 =  EligibleSkuMethods.fetchSkuVisibleAndAvailableInPrimaryBin(memberCookie,adminCookie, fcId,entry_context,Integer.parseInt(entry_context_id),areaName,searchTerm,searchType,report);

        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID), 1);
        skuMap.put(String.valueOf(skuID2), 1);

        int orderId = Integer.parseInt(OrderPlacement.placeBBNowOrder("bbnow" , 10,cred[0], areaName, skuMap, true, false, report));

        PickingMethods.pickingFlowModified(orderId, fcId, adminUserName, adminCookie, report);

        Thread.sleep(3000);
        CancelOrder cancelOrder=new CancelOrder(entry_context,entry_context_id,"monolith",report);
        cancelOrder.cancelOrder(String.valueOf(orderId),member_id);
        Thread.sleep(5000);
        String query = "select id,fc_id,request_type,primary_reason,status,created_by,updated_by from fcreceiving where order_id="+orderId+";";
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getWarehouseInboundOutboundDBName(),query));
        int FcReceiving = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
        int fc_id = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("fc_id");
        Assert.assertEquals(fc_id,fcId,"Fc_Id is not same");
        String request_type = jsonObject.getJSONArray("rows").getJSONObject(0).getString("request_type");
        Assert.assertEquals(request_type,"rtv-ti","request type for cancelled order isn't correct"+request_type);
        String primary_reason = jsonObject.getJSONArray("rows").getJSONObject(0).getString("primary_reason");
        Assert.assertEquals(primary_reason,"ORDER_CANCEL_AFTER_PICKING","primary reason for order cancellation isn't correct"+primary_reason);
        String status = jsonObject.getJSONArray("rows").getJSONObject(0).getString("status");
        Assert.assertEquals(status,"receiving_complete","order status isn't correct"+status);
        String created_by = jsonObject.getJSONArray("rows").getJSONObject(0).getString("created_by");
        Assert.assertEquals(created_by,"system","picker name isn't correct"+created_by);
        String updated_by = jsonObject.getJSONArray("rows").getJSONObject(0).getString("updated_by");
        Assert.assertEquals(updated_by,"system","picker name isn't correct"+updated_by);
        report.log("Values are correctly populated in FcReceiving table",true);


        String query2 = "select source,created_by,updated_by from transferin where source_id="+FcReceiving+";";
        jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getWarehouseInboundOutboundDBName(),query2));
        created_by = jsonObject.getJSONArray("rows").getJSONObject(0).getString("created_by");
        Assert.assertEquals(created_by,"system","picker name isn't correct"+created_by);
        updated_by = jsonObject.getJSONArray("rows").getJSONObject(0).getString("updated_by");
        Assert.assertEquals(updated_by,"system","picker name isn't correct"+updated_by);
        String source = jsonObject.getJSONArray("rows").getJSONObject(0).getString("source");
        Assert.assertEquals(source,"FcReceiving","source isn't correct"+updated_by);
        report.log("Values are correctly populated in TransferrIn table",true);

        String query3 = "select sku,created_by,updated_by from fcreceivingitem where fcreceiving_id="+FcReceiving+";";
        jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getWarehouseInboundOutboundDBName(),query3));
        JSONArray rows = jsonObject.getJSONArray("rows");
        for (Object obj: rows) {
            JSONObject iterableObj = (JSONObject) obj;
            created_by = iterableObj.getString("created_by");
            Assert.assertEquals(created_by,"system","picker name isn't correct"+created_by);
            updated_by = iterableObj.getString("updated_by");
            Assert.assertEquals(updated_by,"system","picker name isn't correct"+updated_by);
            int sku_id = iterableObj.getInt("sku");
            report.log("Values are correctly populated in FcReceivingItem table for skuId: "+sku_id,true);
        }}
}