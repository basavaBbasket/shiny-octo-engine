package test;

import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import msvc.DynamicLocationGeneralMethods;
import msvc.planogram.external.GrnStackingJob;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

public class SampleTestClass extends BaseTest {
    Logger logger = Logger.getLogger(TestClass.class);
    private int looseQty = 1;

    @DescriptionProvider(slug = "dl test", description = "testing", author = "vinay")
    @Test(groups = "testing-dl")
    public void testingClass() throws InterruptedException {
        AutomationReport report = getInitializedReport(this.getClass(), false);
        GrnStackingJob grnStackingJob = new GrnStackingJob(report);
        String warehouseId = DynamicLocationGeneralMethods.STACKING_WAREHOUSEID;
        int skuID = DynamicLocationGeneralMethods.SKUID3;
        int planogramID = DynamicLocationGeneralMethods.getPlanogramIDForGivenWareHouseID(warehouseId);
        int batchID = DynamicLocationGeneralMethods.getBatchIdForGivenSkuIDPlanogramID(skuID, planogramID, report);

        String createStackingApiResponseStr = grnStackingJob.callGrnStackingJobApi(warehouseId, DynamicLocationGeneralMethods.STACKER_USERID,
                DynamicLocationGeneralMethods.STACKER_USERNAME,
                batchID, looseQty, 0, looseQty
                , false);



    }
}
