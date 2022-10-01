package api.warehousecomposition.planogram_FC;

import api.warehousecomposition.planogram_FC.internal.Helper;
import api.warehousecomposition.planogram_FC.internal.IQApp;
import com.bigbasket.automation.reports.IReport;
import io.restassured.response.Response;

public class InventoryMethods extends Helper {

    /**
     * This util checks whether thier is primary bin mapping for the given sku
     * @param adminUserName adminusername otp enabled
     * @param adminPassword adminpassword
     * @param skuId skuID
     * @param fcId fcId
     * @param report report instance
     * @return return boolean value
     */
    public static boolean isPrimaryBinMappedForSku(String adminUserName, String adminPassword, int skuId, int fcId, IReport report) {
        IQApp app = new IQApp(report);
        Response response = app.iqAppLogin(adminUserName, adminPassword)
                .mfaAuthenticate(adminUserName)
                .stockInfoAPI(fcId, skuId);
        if (response.path("primary_qoh.bins_qoh") == null) {
            report.log("Primary bin is not mapped in sku-bin mapping for the sku " + skuId, true);
            return false;
        } else {
            report.log("Primary bin is mapped in sku-bin mapping for the sku " + skuId, true);
            return true;
        }
    }
}
