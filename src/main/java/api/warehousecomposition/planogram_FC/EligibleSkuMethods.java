package api.warehousecomposition.planogram_FC;

import api.warehousecomposition.planogram_FC.internal.Helper;
import com.bigbasket.automation.mapi.mapi_4_1_0.ProductSearch;
import com.bigbasket.automation.reports.IReport;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EligibleSkuMethods extends Helper {
    private static AtomicInteger SKU_LIST_IN_PRIMARY_BIN_COUNTER = new AtomicInteger(0);

    /**
     * provides the sku which has inventory in primary bin
     *
     * @param adminCookie admin login cookie
     * @param fcId        fcId
     * @param report      report instance
     * @return skuId
     */
    public static int skuAvailableInPrimaryBin(Map<String, String> adminCookie, int fcId, IReport report) {
        report.log("Current skulist present in primary bin" + Arrays.toString(skuListInPrimaryBin), true);
        int skuID = skuListInPrimaryBin[SKU_LIST_IN_PRIMARY_BIN_COUNTER.getAndIncrement() % NUM_OF_PRODUCTS_TO_SET_IN_GLOBAL_SKULIST_IN_PRIMARY_BIN];
        if (!InventoryMethods.isPrimaryBinMappedForSku(adminCookie, skuID, fcId, report)) {
            Assert.fail("No sku Available in primary bin for skuId: " + skuID);
        }
        report.log("Sku " + skuID + " available in primary bin", true);
        return skuID;
    }

    /**
     * Provides the sku which is visible and has mapped to primary bin
     *
     * @param memberCookie
     * @param adminCookie
     * @param fcId
     * @param entryContext
     * @param entryContextId
     * @param areaName
     * @param searchType     takes the value "ps" or "pc" to perform the search. "pc" is for category search, "ps" is for user search
     * @param searchTerm if searchtype is "ps" then can use random search term otherwise need to specify the correct category slug
     * @param report
     * @return
     */
    public static int fetchSkuVisibleAndAvailableInPrimaryBin(Map<String, String> memberCookie, Map<String, String> adminCookie,
                                                              int fcId, String entryContext, int entryContextId, String areaName,
                                                              String searchTerm, String searchType, IReport report) {

        Assert.assertTrue(searchType.equalsIgnoreCase("ps") || searchType.equalsIgnoreCase("pc"), "" +
                "Search type should be either 'ps' or 'pc'");
        List<String> availableSkus = ProductSearch.getAvailableProductFromSearch(memberCookie,searchType, searchTerm, areaName,
                entryContext, entryContextId, report);
        report.log("List of skus visible : " + availableSkus.toString(), true);
        boolean isSkuAvailable = false;
        int skuId = -999;

        for (int i = 0; i < availableSkus.size(); i++) {
            skuId = Integer.parseInt(availableSkus.get(i));
            if (InventoryMethods.isPrimaryBinMappedForSku(adminCookie, skuId, fcId, report)) {
                report.log("Sku " + skuId + " Visible and available in primary bin", true);
                isSkuAvailable = true;
                break;
            }
        }
        if (!isSkuAvailable) {
            Assert.fail("Out of the below visible sku list, none are available in primary bin " +
                    "\n Visible sku list is : " + availableSkus.toString());
        }
        return skuId;
    }


    /**
     * Provides the sku list which is visible and has mapped to primary bin
     *
     * @param memberCookie
     * @param adminCookie
     * @param fcId
     * @param entryContext
     * @param entryContextId
     * @param areaName
     * @param searchType type should be either "ps" or "pc"
     * @param searchTerm if searchtype is "ps" then can use random search term otherwise need to specify the correct category slug
     * @param report
     * @return
     */
    public static List<Integer> fetchListOfSkuVisibleAndAvailableInPrimaryBin(Map<String, String> memberCookie, Map<String, String> adminCookie,
                                                                              int fcId, String entryContext, int entryContextId, String areaName,
                                                                              String searchType,String searchTerm, IReport report) {
        List<String> availableSkus = ProductSearch.getAvailableProductFromSearch(memberCookie,searchType, searchTerm, areaName,
                entryContext, entryContextId, report);
        report.log("List of skus visible : " + availableSkus.toString(), true);
        System.out.println("List of skus visible : " + availableSkus.toString());
        boolean isSkuAvailable = false;
        int skuId = -999;
        List<Integer> visibleAndAvailableSkus = new ArrayList<>();
        for (int i = 0; i < availableSkus.size(); i++) {
            skuId = Integer.parseInt(availableSkus.get(i));
            if (InventoryMethods.isPrimaryBinMappedForSku(adminCookie, skuId, fcId, report)) {
                report.log("Sku " + skuId + " Visible and available in primary bin", true);
                visibleAndAvailableSkus.add(skuId);
                isSkuAvailable = true;
            }
        }
        if (!isSkuAvailable) {
            Assert.fail("Out of the below visible sku list, none are available in primary bin " +
                    "\n Visible sku list is : " + availableSkus.toString());
        }
        report.log("List of skus visible and available : " + visibleAndAvailableSkus.toString(), true);
        System.out.println("List of skus visible and available : " + visibleAndAvailableSkus.toString());
        return visibleAndAvailableSkus;
    }
}
