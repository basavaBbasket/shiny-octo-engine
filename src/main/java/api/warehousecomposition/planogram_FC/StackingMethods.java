package api.warehousecomposition.planogram_FC;

import api.warehousecomposition.planogram_FC.internal.Helper;
import api.warehousecomposition.planogram_FC.internal.IQApp;
import com.bigbasket.automation.reports.IReport;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

public class StackingMethods extends Helper {

    /**
     * This method completes the Grn Stacking flow
     * CreateGrnStackingJob-->GrnStackingAck-->GrnStackingComplete
     * @param fcId fcId
     * @param skuId skuId to complete stacking
     * @param userName admin username
     * @param grnCreateStackingJobBody stacking request body
     * @param report report instance
     * @return
     */
    public static int grnStackingFlow(int fcId, int skuId, String userName,String pwd, HashMap<String, Object> grnCreateStackingJobBody, IReport report) {
        IQApp app = new IQApp(report);
        Response response = app.iqAppLogin(userName,pwd)
                            .mfaAuthenticate(userName)
                            .stockInfoAPI(fcId, skuId);

        int unStackedGrnQoh = fetchUnStackedGrNQoh(response, report);
        app.grnStackingUnstackedAPI(fcId, skuId);
        if (Integer.parseInt(grnCreateStackingJobBody.get("total_quantity").toString()) > unStackedGrnQoh) {
            Assert.fail("Total requested qty for stacking is greater than available unStacked Qoh" +
                    "\nTotal Quantity requested: " + grnCreateStackingJobBody.get("total_quantity").toString() +
                    "\n UnstackedGrnQoh: " + unStackedGrnQoh);
        }

        response = app.grnStackingUnstackedAPI(fcId, skuId);
        int batchId = fetchBatchId(response, report);

        String userId = String.valueOf(fetchUserId(userName, report));
        grnCreateStackingJobBody.put("user_id", userId);
        grnCreateStackingJobBody.put("username", userName);
        grnCreateStackingJobBody.put("batch_id", batchId);
        response = app.grnStackingCreateJobAPI(fcId, grnCreateStackingJobBody);

        int jobId = fetchJobId(response, report);
        String stackingAckBody = prepareGrnStackingAckApiBody(batchId, response, report);
        app.grnStackingJobAckAPI(fcId, jobId, stackingAckBody);
        app.grnStackingCompleteAPI(fcId, jobId);
        return jobId;
    }

    /**
     * This method completes the Grn Stacking flow
     * CreateGrnStackingJob-->GrnStackingAck-->GrnStackingComplete
     * @param fcId fcId
     * @param skuId skuId to complete stacking
     * @param userName admin username
     * @param grnCreateStackingJobBody stacking request body
     * @param cookie AdminCookie to authenticate dl apis
     * @param report report instance
     * @return
     */
    public static int grnStackingFlow(int fcId, int skuId, String userName, HashMap<String, Object> grnCreateStackingJobBody, Map<String, String> cookie, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(cookie);

        Response response = app.stockInfoAPI(fcId, skuId);
        int unStackedGrnQoh = fetchUnStackedGrNQoh(response, report);
        app.grnStackingUnstackedAPI(fcId, skuId);
        if (Integer.parseInt(grnCreateStackingJobBody.get("total_quantity").toString()) > unStackedGrnQoh) {
            Assert.fail("Total requested qty for stacking is greater than available unStacked Qoh" +
                    "\nTotal Quantity requested: " + grnCreateStackingJobBody.get("total_quantity").toString() +
                    "\n UnstackedGrnQoh: " + unStackedGrnQoh);
        }

        response = app.grnStackingUnstackedAPI(fcId, skuId);
        int batchId = fetchBatchId(response, report);

        String userId = String.valueOf(fetchUserId(userName, report));
        grnCreateStackingJobBody.put("user_id", userId);
        grnCreateStackingJobBody.put("username", userName);
        grnCreateStackingJobBody.put("batch_id", batchId);
        response = app.grnStackingCreateJobAPI(fcId, grnCreateStackingJobBody);

        int jobId = fetchJobId(response, report);
        String stackingAckBody = prepareGrnStackingAckApiBody(batchId, response, report);
        app.grnStackingJobAckAPI(fcId, jobId, stackingAckBody);
        app.grnStackingCompleteAPI(fcId, jobId);
        return jobId;
    }


}
