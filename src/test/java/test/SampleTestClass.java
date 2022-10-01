package test;

import api.warehousecomposition.planogram_FC.InventoryMethods;
import com.bigbasket.automation.mapi.mapi_4_1_0.OrderPlacement;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import java.util.HashMap;

public class SampleTestClass extends BaseTest {
    Logger logger = Logger.getLogger(TestClass.class);
    private int looseQty = 1;

    @DescriptionProvider(slug = "dl test", description = "testing", author = "vinay")
    @Test(groups = "dltest")
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
        String adminUserName="harish.a";
        String adminPassword="Hari@123";
       /* String adminUserName="anindra.m";
        String adminPassword="password";*/
        int fcId=236;
        int skuID = 10000137;
        /*Map<String, String> cookie =AdminCookie.getMemberCookie(adminUserName,adminPassword,report);
        System.out.printf("##Cookie: "+cookie.toString());
        StackingMethods.grnStackingFlow(fcId,skuID,adminUserName,map,cookie,report);
*/
        int orderId=1000701974;
       // PickingMethods.pickingFlowModified(orderId,fcId,adminUserName,adminPassword,report);

        InventoryMethods.isPrimaryBinMappedForSku(adminUserName,adminPassword,skuID,fcId,report);

        String cred[] = AutomationUtilities.getUniqueLoginCredential(serverName,"bbnow-seegehalli");
        HashMap<String,Integer> skuMap = new HashMap<>();
        skuMap.put("10000137",1);
        OrderPlacement.placeBBNowOrder(cred[0],"seegehalli",skuMap,true,false,report);
    }
}
