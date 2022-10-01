package dynamicLocation_rel1_FC;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.InventoryMethods;
import api.warehousecomposition.planogram_FC.StackingMethods;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import io.restassured.response.Response;
import msvc.DynamicLocationGeneralMethods;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import utility.database.DynamicLocationDBQueries;

import java.util.*;

public class PreReqInventoryUpdate extends BaseTest {
    private static int looseQty = 200;
    private static String mfgDate = "2020-06-12";
    private static String expDate = "2020-05-27";
    private static boolean isBundlepack = true;
    private static double cp = 91.50;
    private static double mrp = 93.50;
    private static double eretailmrp = 122.50;
    private static double gst = 4.1;
    private static double cess = 4.4;
    private static String rtvId = "1";
    static String primaryBinLoc = null;

    @DescriptionProvider(slug = "Pre Req inventory update", description = "Pre requisite inventory update script ", author = "vinay")
    @Test(groups = {"preReqInventoryUpdate"})
    public void preReqInventoryUpdate() {
        AutomationReport report = getInitializedReport(this.getClass(), false);

        Map<Integer, ArrayList<Integer>> fcIdSkuIdMap = new HashMap<Integer, ArrayList<Integer>>();

        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));
        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> cookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        //Set 1 fcId and skuID list
        fcIdSkuIdMap.put(fcId, new ArrayList<Integer>());
        int[] skuArray = fetchListOfSkusInPrimaryBin(fcId, MAXIMUM_NO_SKUS_ALLOWED_TO_CHECK_VISIBILITY);

        for (int i = 0; i < skuArray.length; i++) {
            fcIdSkuIdMap.get(fcId).add(skuArray[i]);
        }

        Set<Integer> fcIds = fcIdSkuIdMap.keySet();

        for (int key : fcIds) {
            int indfcId = key;
            List<Integer> skuIDsList = fcIdSkuIdMap.get(indfcId); //Get sku List for each fcId
            report.log("Updating inventory for skus: " + skuIDsList + " for fcId:  " + indfcId, true);
            int skuIDList[] = new int[skuIDsList.size()];
            for (int i = 0; i < skuIDsList.size(); i++) {
                skuIDList[i] = skuIDsList.get(i);
            }
            int planogramID = DynamicLocationGeneralMethods.getPlanogramIDForGivenFcID(indfcId);

            for (int i = 0; i < skuIDList.length; i++) {
                int skuId = skuIDList[i];

                try {
                    updateMaxSkuAllowedForAllMappedBinOfTheSkuTo100(indfcId, skuId, report);
                    report.log("Updating inventory for SKU : " + skuId, true);

                    Response response = InventoryMethods.stockInfoDetails(cookie, skuId, indfcId, report);
                    JSONObject jsonObject = new JSONObject(response.asString());
                    int totalQoh = jsonObject.getInt("total_qoh");
                    report.log("Total Qoh is : " + totalQoh, true);
                    int totalQohInPrimary = jsonObject.getJSONObject("primary_qoh").getInt("total_qoh");
                    report.log("Total Qoh in Primary : " + totalQohInPrimary, true);
                    int totalQohInSecondary = jsonObject.getJSONObject("secondary_qoh").getInt("total_qoh");
                    report.log("Total Qoh in Secondary : " + totalQohInSecondary, true);
                    int totalQohInMiscellaneous = jsonObject.getJSONObject("miscellaneous_qoh").getInt("total_qoh");
                    report.log("Total Qoh in Miscellaneous : " + totalQohInMiscellaneous, true);
                    int unstackedGrnO0h = jsonObject.getJSONObject("unstacked_qoh").getInt("grn_qoh");
                    report.log("Unstacked Grn Qoh is  : " + unstackedGrnO0h, true);
                    int unstackedPrnO0h = jsonObject.getJSONObject("unstacked_qoh").getInt("prn_ti_qoh");
                    report.log("Unstacked Prn Qoh is  : " + unstackedPrnO0h, true);
                    int unstackedRtvTiO0h = jsonObject.getJSONObject("unstacked_qoh").getInt("rtv_ti_qoh");
                    report.log("Unstacked RtvTi Qoh is  : " + unstackedRtvTiO0h, true);
                    int unstackedManualO0h = jsonObject.getJSONObject("unstacked_qoh").getInt("manual_ti_qoh");
                    report.log("Unstacked Manual Qoh is  : " + unstackedManualO0h, true);
                    if (totalQohInPrimary != 0) {
                        primaryBinLoc = jsonObject.getJSONObject("primary_qoh").getJSONArray("bins_qoh").getJSONObject(0).getString("bin_location");
                    }

                    Response unStackedApiResponse = StackingMethods.grnStackingunStackedDetails(cookie, skuId, indfcId, report);
                    jsonObject = new JSONObject(unStackedApiResponse.asString());


                    int totalUnstackedQty = 0;
                    if (Integer.parseInt(unStackedApiResponse.path("batches.size()").toString()) != 0) {
                        totalUnstackedQty = Integer.parseInt(unStackedApiResponse.path("batches[0].total_quantity").toString());
                    }
                    report.log("Total unstacked Quantity is : " + totalUnstackedQty, true);
                    report.log("Unstacked GRN Qoh is : " + unstackedGrnO0h, true);
                    report.log("Checking unstacked quanity is less than 200 to perfom transferin", true);

                    unstackedGrnO0h = totalUnstackedQty;

                    int batchID;


                    if (unstackedGrnO0h < 260) {
                        //Inward stocks
                        performTransferIn(cookie, skuId, planogramID, indfcId, looseQty, report);
                    }
                    batchID = DynamicLocationGeneralMethods.getBatchIdForGivenSkuIDPlanogramID(skuId, planogramID, report);

                    int stackQtyInSecOrPriorMisc = 15;


                    if (totalQohInPrimary < 10) {
                        HashMap<String, Object> stackingBody = new HashMap<>();
                        stackingBody.put("total_quantity", stackQtyInSecOrPriorMisc);
                        stackingBody.put("num_of_containers", 0);
                        stackingBody.put("loose_quantity", stackQtyInSecOrPriorMisc);

                        StackingMethods.performGrnStackingForGivenLocation(indfcId, skuId, adminUserName, stackingBody,"primary", cookie, report);
                    }

                    if (totalQohInSecondary < 25) {
                        HashMap<String, Object> stackingBody = new HashMap<>();
                        stackingBody.put("total_quantity", stackQtyInSecOrPriorMisc);
                        stackingBody.put("num_of_containers", 0);
                        stackingBody.put("loose_quantity", stackQtyInSecOrPriorMisc);

                        StackingMethods.performGrnStackingForGivenLocation(indfcId, skuId, adminUserName, stackingBody,"secondary", cookie, report);
                    }


                } catch (Exception e) {
                    report.log("ERROR for the skuID " + skuId + " and warehouseID: " + indfcId, true);
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * fetchs the skus which are in primary bin
     *
     * @param fcId
     * @param numberOfproductsReq
     * @return
     */
    public static int[] fetchListOfSkusInPrimaryBin(int fcId, int numberOfproductsReq) {
        String query = "select sku_id,quantity,bin_id,bin_location,batch_id from fc_sku_stock " +
                "where planogram_id=(select id from fc_planogram where fc_id = " + fcId + ") and " +
                "location_id=1 and quantity>5 order by  id limit " + numberOfproductsReq + ";";
        System.out.println("Fetching the sku list from fc_sku_stock table\n " + query);
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No skus returned table " + query);
        }
        int numProdReturned = jsonObject.getJSONArray("rows").length();
        if (numProdReturned < numberOfproductsReq) {
            System.out.println("Couldn't get the number of products requested, Number of product fetched is : " + numProdReturned);
        }
        int skuList[] = new int[numberOfproductsReq];
        for (int i = 0; i < numberOfproductsReq; i++) {
            skuList[i] = jsonObject.getJSONArray("rows").getJSONObject(i).getInt("sku_id");
        }
        System.out.println("Returned SKU list : " + Arrays.asList(skuList));
        return skuList;
    }

    private void updateMaxSkuAllowedForAllMappedBinOfTheSkuTo100(int fcId, int skuID, AutomationReport report) {
        report.log("Updating the MaxSkuAllowed to 100 for the bins mapped to skuID: " + skuID, true);
        int binIDsMapped[] = DynamicLocationGeneralMethods.getBinIDfromskuBinMappingForGivenSkuIDAndfcID(skuID, fcId, report);
        for (int i = 0; i < binIDsMapped.length; i++) {
            report.log("Updating the Max SKU Allowed to 100 for binID: " + binIDsMapped[i], true);
            DynamicLocationGeneralMethods.updateMaxSKUAllowed(100, binIDsMapped[i], report);
        }
        report.log("Updated MAXSkuAllowed for all the bins mapped to sku : " + skuID, true);
    }

    private void performTransferIn(Map<String, String> adminCookie, int skuID, int planogramID, int fcId, int quantity, AutomationReport report) {
        report.log("Calling TransferIn API for skuID: " + skuID + " planogram: " + planogramID, true);

        List<HashMap<String, Object>> looseQtySKUList = new LinkedList<>();
        looseQtySKUList.add(InventoryMethods.settinglooseSKUInfo(skuID, quantity, mfgDate, expDate,
                isBundlepack, cp, mrp, eretailmrp, gst, cess));

        List<HashMap<String, Object>> containerSKUList = new LinkedList<>();
        InventoryMethods.performGrnTransferIn(adminCookie, fcId, looseQtySKUList, containerSKUList, report);
    }
}
