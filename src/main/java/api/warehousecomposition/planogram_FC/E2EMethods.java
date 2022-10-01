package api.warehousecomposition.planogram_FC;

import api.warehousecomposition.planogram_FC.internal.Helper;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.IReport;

import java.util.HashMap;
import java.util.Map;

public class E2EMethods extends Helper {

    public static int performBBnowOrderPlacementToBinning(String entryContext, int entryContextId, String logidId, String areaName,
                                                          HashMap<String, Integer> skuQtyMap, boolean useWalletBalance,
                                                          boolean getFullResponse, int fcId, String adminUserName, Map<String, String> adminCookie, IReport report) {
        int orderId = Integer.parseInt(OrderPlacement.placeBBNowOrder(entryContext, entryContextId, logidId, areaName, skuQtyMap,
                useWalletBalance, getFullResponse, report));
        PickingMethods.pickingFlowModified(orderId, fcId, adminUserName, adminCookie, report);
        DeliveryBinMethods.perfomOrderBinMapping(fcId, orderId, "binning", adminCookie, report);
        System.out.println("binning completed");

       /* Assert.assertTrue(DeliveryBinMethods.isOrderBinMapped(fcId, orderId, report));
        Map<String, Object> orderbinmapping = DeliveryBinMethods.getOrderBinMappingDetails(fcId, orderId, report);
        String[] createddate = String.valueOf(orderbinmapping.get("created_on")).split("T");
        Assert.assertTrue(DeliveryBinMethods.entryInFc_Sku_Stock(orderbinmapping.get("bin_id"), createddate[0], report));
        report.log("Binning performed for the orderId: " + orderId, true);*/
        return orderId;
    }
}
