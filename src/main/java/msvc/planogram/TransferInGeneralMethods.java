package msvc.planogram;

import org.apache.log4j.Logger;

import java.util.HashMap;

public class TransferInGeneralMethods {
    private static Logger logger = Logger.getLogger(TransferInGeneralMethods.class);
    private long tranferId = -999;

    public long getTransferId() {
        return tranferId;
    }

    public void setTranferId(long tranferId) {
        this.tranferId = tranferId;
    }

    /**
     * Creates the hashmap with loose sku info for using in transferin api call for inward of stock
     *
     * @param skuId
     * @param quantity
     * @param mfgDate
     * @param expDate
     * @param isBundlepack
     * @param cp
     * @param mrp
     * @param eretailmrp
     * @param gst
     * @param cess
     * @return
     */
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

    /**
     * Creates the hashmap with container sku info for using in transferin api call for inward of stock
     *
     * @param containerId
     * @param containerTag
     * @param containerType
     * @param unknownDimension
     * @param skuId
     * @param quantity
     * @param mfgDate
     * @param expDate
     * @param isBundlepack
     * @param cp
     * @param mrp
     * @param eretailmrp
     * @param gst
     * @param cess
     * @return
     */
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

}
