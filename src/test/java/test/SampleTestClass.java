package test;

import api.warehousecomposition.planogram_FC.AdminCookie;
import api.warehousecomposition.planogram_FC.EligibleSkuMethods;
import api.warehousecomposition.planogram_FC.StockMovementMethods;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import framework.Settings;
import msvc.DynamicLocationGeneralMethods;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class SampleTestClass extends BaseTest {
    Logger logger = Logger.getLogger(SampleTestClass.class);
    private int looseQty = 1;


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

    @DescriptionProvider(slug = "dl test", description = "testing", author = "vinay")
    @Test(groups = "dlphase2",enabled = true)
    public void testingClass() throws InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        /*GrnStackingJob grnStackingJob = new GrnStackingJob(report);
        String warehouseId = DynamicLocationGeneralMethods.STACKING_WAREHOUSEID;
        int skuID = DynamicLocationGeneralMethods.SKUID3;
        int planogramID = DynamicLocationGeneralMethods.getPlanogramIDForGivenWareHouseID(warehouseId);
        int batchID = DynamicLocationGeneralMethods.getBatchIdForGivenSkuIDPlanogramID(skuID, planogramID, report);

        String createStackingApiResponseStr = grnStackingJob.callGrnStackingJobApi(warehouseId, DynamicLocationGeneralMethods.STACKER_USERID,
                DynamicLocationGeneralMethods.STACKER_USERNAME,
                batchID, looseQty, 0, looseQty
                , false);*/

        HashMap<String, Object> map = new HashMap<>();
        map.put("user_id", "9641");
        map.put("username", "sav");
        map.put("batch_id", 16);
        map.put("total_quantity", 1);
        map.put("num_of_containers", 0);
        map.put("loose_quantity", 1);
        String expectedResponseSchemaPath = "schema//planogram//stacking-create-job-200.json";

       /* String adminUserName="anindra.m";
        String adminPassword="password";*/

        String clientUserSheetName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_client_user_sheet_name");
        String areaName = Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_area_name");
        int fcId = Integer.parseInt(Settings.dlConfig.getProperty(AutomationUtilities.getEnvironmentFromServerName(serverName) + "_fcid"));

        String[] adminCred = AutomationUtilities.getUniqueAdminUser(serverName, "admin-superuser-mfa");
        String adminUserName = adminCred[0];
        String adminPassword = adminCred[1];
        Map<String, String> cookie = AdminCookie.getMemberCookie(adminUserName, adminPassword, report);

        int skuID = EligibleSkuMethods.skuAvailableInPrimaryBin(cookie, fcId, report);

        /*String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName, clientUserSheetName);
        HashMap<String, Integer> skuMap = new HashMap<>();
        skuMap.put(String.valueOf(skuID), 2);
        int orderId = Integer.parseInt(OrderPlacement.placeBBNowOrder("bbnow" , 10 , cred[0], areaName, skuMap, true, false, report));

        PickingMethods.pickingFlowModified(orderId, fcId, adminUserName, cookie, report);

        DeliveryBinMethods.perfomOrderBinMapping(fcId,orderId,"binning",cookie,report);
        System.out.println("binning completed");

        DeliveryBinMethods.perfomOrderBinMapping(fcId,orderId,"delivery",cookie,report);
        System.out.println("emptying completed");*/

        String[] primaryBinLoc = DynamicLocationGeneralMethods.getBinLocationForGivenSkuIDAndfcID(skuID,fcId,1,report);
        String[] secondaryBinLoc = DynamicLocationGeneralMethods.getBinLocationForGivenSkuIDAndfcID(skuID,fcId,2,report);
       /* StockMovementMethods.moveStockFromPrimaryToSecondaryLocation(234,skuID,primaryBinLoc[0]
                ,cookie,1,adminUserName,report);*/

        StockMovementMethods.moveStockFromSecondaryToPrimaryLocation(234,skuID,secondaryBinLoc[0]
                ,cookie,1,adminUserName,report);
    }
}
