package api.warehousecomposition.planogram_FC;

import api.warehousecomposition.planogram_FC.internal.Helper;
import api.warehousecomposition.planogram_FC.internal.IQApp;
import com.bigbasket.automation.reports.IReport;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryMethods extends Helper {

    /**
     * This util checks whether thier is primary bin mapping for the given sku
     *
     * @param adminUserName adminusername otp enabled
     * @param adminPassword adminpassword
     * @param skuId         skuID
     * @param fcId          fcId
     * @param report        report instance
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

    /**
     * This util checks whether thier is primary bin mapping for the given sku
     *
     * @param adminCookie admin login cookie
     * @param skuId       skuID
     * @param fcId        fcId
     * @param report      report instance
     * @return return boolean value
     */
    public static boolean isPrimaryBinMappedForSku(Map<String, String> adminCookie, int skuId, int fcId, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);
        Response response = app.stockInfoAPI(fcId, skuId);
        if (response.path("primary_qoh.bins_qoh") == null) {
            report.log("Primary bin is not mapped in sku-bin mapping for the sku " + skuId, true);
            return false;
        } else {
            report.log("Primary bin is mapped in sku-bin mapping for the sku " + skuId, true);
            return true;
        }
    }

    /**
     * This method does inward/transferin of stock
     *
     * @param adminUserName
     * @param adminPassword
     * @param fcId
     * @param looseQtySKUList
     * @param containerSKUList
     * @param report
     */
    public static void performGrnTransferIn(String adminUserName, String adminPassword, int fcId, List<HashMap<String, Object>> looseQtySKUList,
                                            List<HashMap<String, Object>> containerSKUList, IReport report) {
        IQApp app = new IQApp(report);
        app.iqAppLogin(adminUserName, adminPassword)
                .mfaAuthenticate(adminUserName);
        String transferInBody = createTransferInApiBody(looseQtySKUList, containerSKUList);

        app.transferInGrnApi(fcId, transferInBody);
    }

    /**
     * This method does inward/transferin of stock
     *
     * @param adminCookie
     * @param fcId
     * @param looseQtySKUList
     * @param containerSKUList
     * @param report
     */
    public static void performGrnTransferIn(Map<String, String> adminCookie, int fcId, List<HashMap<String, Object>> looseQtySKUList,
                                            List<HashMap<String, Object>> containerSKUList, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);
        String transferInBody = createTransferInApiBody(looseQtySKUList, containerSKUList);

        app.transferInGrnApi(fcId, transferInBody);
    }

    public static HashMap<String, Object> settinglooseSKUInfo(int skuId, int quantity, String mfgDate, String expDate, boolean isBundlepack,
                                                              double cp, double mrp, double eretailmrp, double gst, double cess) {
        HashMap<String, Object> skuInfo = new HashMap<>();
        skuInfo.put("sku_id", skuId);
        skuInfo.put("quantity", quantity);
        skuInfo.put("mfg_date", mfgDate);
        skuInfo.put("exp_date", expDate);
        skuInfo.put("is_bundlepack", isBundlepack);
        skuInfo.put("cp", cp);
        skuInfo.put("mrp", mrp);
        skuInfo.put("eretail_mrp", eretailmrp);
        skuInfo.put("gst", gst);
        skuInfo.put("cess", cess);
        return skuInfo;
    }

    public static HashMap<String, Object> settingcontainerSKUInfo(int containerId, String containerTag, String containerType, boolean unknownDimension, int skuId,
                                                                  int quantity, String mfgDate, String expDate, boolean isBundlepack,
                                                                  double cp, double mrp, double eretailmrp, double gst, double cess) {
        HashMap<String, Object> skuInfo = new HashMap<>();
        skuInfo.put("container_id", containerId);
        skuInfo.put("container_tag", containerTag);
        skuInfo.put("container_type", containerType);
        skuInfo.put("unknown_dimension", unknownDimension);
        skuInfo.put("sku_id", skuId);
        skuInfo.put("quantity", quantity);
        skuInfo.put("mfg_date", mfgDate);
        skuInfo.put("exp_date", expDate);
        skuInfo.put("is_bundlepack", isBundlepack);
        skuInfo.put("cp", cp);
        skuInfo.put("mrp", mrp);
        skuInfo.put("eretail_mrp", eretailmrp);
        skuInfo.put("gst", gst);
        skuInfo.put("cess", cess);
        return skuInfo;
    }


    /**
     * This method fetchs the stock Info details for the given product
     *
     * @param adminUserName
     * @param adminPassword
     * @param skuId
     * @param fcId
     * @param report
     * @return
     */
    public static Response stockInfoDetails(String adminUserName, String adminPassword, int skuId, int fcId, IReport report) {
        IQApp app = new IQApp(report);
        Response response = app.iqAppLogin(adminUserName, adminPassword)
                .mfaAuthenticate(adminUserName)
                .stockInfoAPI(fcId, skuId);
        return response;
    }

    /**
     * This method fetchs the stock Info details for the given product
     *
     * @param adminCookie login cookie details
     * @param skuId
     * @param fcId
     * @param report
     * @return
     */
    public static Response stockInfoDetails(Map<String, String> adminCookie, int skuId, int fcId, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);
        Response response = app.stockInfoAPI(fcId, skuId);
        return response;
    }
}
