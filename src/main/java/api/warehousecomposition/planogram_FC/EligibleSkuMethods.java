package api.warehousecomposition.planogram_FC;

import api.warehousecomposition.planogram_FC.internal.Helper;
import com.bigbasket.automation.reports.IReport;
import org.testng.Assert;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class EligibleSkuMethods extends Helper {
    private static AtomicInteger SKU_LIST_IN_PRIMARY_BIN_COUNTER = new AtomicInteger(0);

    /**
     * provides the sku which has inventory in primary bin
     * @param adminUserName adminusername otp enabled
     * @param adminPassword adminpassword
     * @param fcId fcId
     * @param report report instance
     * @return skuId
     */
    public static int skuAvailableInPrimaryBin(String adminUserName, String adminPassword, int fcId, IReport report) {
        report.log("Current skulist present in primary bin"+ Arrays.toString(skuListInPrimaryBin),true);
        int skuID = skuListInPrimaryBin[SKU_LIST_IN_PRIMARY_BIN_COUNTER.getAndIncrement()% NUM_OF_PRODUCTS_TO_SET_IN_GLOBAL_SKULIST_IN_PRIMARY_BIN];
        if (!InventoryMethods.isPrimaryBinMappedForSku(adminUserName, adminPassword, skuID, fcId, report)) {
            Assert.fail("No sku Available in primary bin for skuId: "+skuID);
        }
        report.log("Sku "+skuID+" available in primary bin",true);
        return skuID;
    }
}
