package bbnow.testcases.picking;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.PickingMethods;
import com.bigbasket.automation.Config;
import com.bigbasket.automation.mapi.mapi_4_1_0.MemberCookie;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import utility.OrderUtils;

import java.util.HashMap;
import java.util.Map;

public class PlaceBBnowOrderAndCompletePickingFlow extends BaseTest {
    @DescriptionProvider(slug = "place order-->Complete Picking Flow", description = "1. Place BBnow order \n" +
                        "2. Complete the picking flow." +
            "\n Places order-> poll to check order appeared in picking platform -> JobAssignment APi, creates the picking job -> Order-Bag linking -> Picking Ack -> Picking Complete ", author = "tushar")
    @Test(groups = {"bbnow" , "regression", "dlphase2", "pickingFlow"})
    public void placeBBnowOrderAndCompletePickingFlow() {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        String entry_context= Config.bbnowConfig.getString("entry_context");
        String entry_context_id= Config.bbnowConfig.getString("entry_context_id");

        String clientUserSheetName = Config.bbnowConfig.getString("bbnow_stores[1].member_sheet_name");
        String areaName = Config.bbnowConfig.getString("bbnow_stores[1].area");
        int fcId = Integer.parseInt(Config.bbnowConfig.getString("bbnow_stores[1].fc_id"));
        String searchTerm = Config.bbnowConfig.getString("bbnow_stores[1].category_slug1");
        String searchType = Config.bbnowConfig.getString("bbnow_stores[1].search_type2");

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);

        Map<String, String> adminCookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);
        Map<String,String> memberCookie = MemberCookie.getMemberCookieForCustomAddress(cred[0],areaName,report);

        int skuID = EligibleSkuMethods.fetchSkuVisibleAndAvailableInPrimaryBin(memberCookie,adminCookie, fcId,entry_context,Integer.parseInt(entry_context_id),areaName,searchTerm,searchType,report);;

        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID), 1);

        //to handle if there is no open order in the picking_request table
        int orderId1 = Integer.parseInt(OrderPlacement.placeBBNowOrder("bbnow" , 10 , cred[0], areaName, skuMap, true, false, report));

        int orderId2 = PickingMethods.pickingFlowForOldestOpenOrder(fcId, adminUserName, adminCookie, report);
        String orderStatus = OrderUtils.getCurrentOrderStatus(orderId2, report);
        Assert.assertTrue(orderStatus.equalsIgnoreCase("packed"), "Order status is not updated to packed " +
                "\n Current Order Status is: " + orderStatus);
        report.log("Order status updated to packed ", true);
    }
}
