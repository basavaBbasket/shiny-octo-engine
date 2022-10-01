package bbnow.testcases.deliveybin;

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
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;


public class OrderBinMapping extends BaseTest {
    @DescriptionProvider(slug = "place order-->Picking Flow Modified-->Order Bin Mapping", description = "1. Place BBnow order \n" +
            "            2. Check whether the order entry is created in picking_request and picking_item_request table \n" +
            "            3. Complete the picking flow. \n" +
            "            4.Perfrom order bin mapping " +
            "            5. Validate the mapping like reason id and is occupied fields  and check the entry in the FC_SKU_STOCK table ", author = "Pranay")
    @Test(groups = {"bbnow", "dlphase2", "pickingFlow","deliverybin"})
    public void orderBinMapping()  {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        String[] creds = AutomationUtilities.getUniqueLoginCredential(serverName, Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name"));
        report.log("Starting  order placement.", true);
        String entrycontext= Config.bbnowConfig.getString("entry_context");
        String entrycontextid=Config.bbnowConfig.getString("entry_context_id");
        String clientUserSheetName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_client_user_sheet_name");
        String areaName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_area_name");
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));
        String searchTerm = Config.bbnowConfig.getString("bbnow_stores[1].category_slug1");
        String searchType = Config.bbnowConfig.getString("bbnow_stores[1].search_type2");

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> adminCookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        Map<String,String> memberCookie = MemberCookie.getMemberCookieForCustomAddress(cred[0],areaName,report);



        int skuID = EligibleSkuMethods.fetchSkuVisibleAndAvailableInPrimaryBin(memberCookie,adminCookie,
                fcId,entrycontext,Integer.parseInt(entrycontextid),areaName,searchTerm,searchType,report);

        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID), 1);


        int orderId = Integer.parseInt(OrderPlacement.placeBBNowOrder("bbnow" , 10,cred[0], areaName, skuMap, true, false, report));
        PickingMethods.pickingFlowModified(orderId, fcId, adminUserName, adminCookie, report);
        DeliveryBinMethods.perfomOrderBinMapping(fcId,orderId,"binning",adminCookie,report);
        System.out.println("binning completed");

        Assert.assertTrue(DeliveryBinMethods.isOrderBinMapped(fcId,orderId,report));
        Map<String,Object> orderbinmapping = DeliveryBinMethods.getOrderBinMappingDetails(fcId,orderId,report);
        String[] createddate=String.valueOf(orderbinmapping.get("created_on")).split("T");
       // Assert.assertTrue(DeliveryBinMethods.entryInFc_Sku_Stock(orderbinmapping.get("bin_id"),createddate[0],report));




    }
}
