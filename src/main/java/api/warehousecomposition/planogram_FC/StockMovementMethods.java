package api.warehousecomposition.planogram_FC;

import api.warehousecomposition.planogram_FC.internal.Helper;
import api.warehousecomposition.planogram_FC.internal.IQApp;
import com.bigbasket.automation.reports.IReport;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class StockMovementMethods extends Helper {

    /**
     * This method moves qty from primary bin to secondary bin
     * @param fcId
     * @param skuId
     * @param sourceBinLocation
     * @param adminCookie
     * @param reqQty
     * @param userName
     * @param report
     */
    public static void moveStockFromPrimaryToSecondaryLocation(int fcId, int skuId, String sourceBinLocation,
                                                               Map<String, String> adminCookie,
                                                               int reqQty, String userName, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);
        Response locationDetailResponse = app.locationDetailUsingBinId(fcId, skuId, sourceBinLocation);
        String userId = String.valueOf(fetchUserId(userName, report));
        HashMap<String, Object> stockMovementCreateJobForBinBody
                = prepareStockMovementCreateJobApiBody(userId, userName, skuId, reqQty, locationDetailResponse, 2, report);
        Response stockMovementCreateJobForBinResponse = app.stockMovementCreateJobForBin(fcId, stockMovementCreateJobForBinBody);
        String stockMovementStackAckBody = prepareStockMovementStackAckBody(stockMovementCreateJobForBinResponse, reqQty, reqQty,2);
        int jobId = Integer.parseInt(stockMovementCreateJobForBinResponse.path("job_id").toString());
        app.stockMovementStackAck(fcId, jobId, stockMovementStackAckBody);
        app.stockMovementComplete(fcId, jobId);
    }

    /**
     * This method moves qty from  secondary bin to primary bin
     * @param fcId
     * @param skuId
     * @param sourceBinLocation
     * @param adminCookie
     * @param reqQty
     * @param userName
     * @param report
     */
    public static void moveStockFromSecondaryToPrimaryLocation(int fcId, int skuId, String sourceBinLocation,
                                                               Map<String, String> adminCookie,
                                                               int reqQty, String userName, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);
        Response locationDetailResponse = app.locationDetailUsingBinId(fcId, skuId, sourceBinLocation);
        String userId = String.valueOf(fetchUserId(userName, report));
        HashMap<String, Object> stockMovementCreateJobForBinBody
                = prepareStockMovementCreateJobApiBody(userId, userName, skuId, reqQty, locationDetailResponse, 1, report);
        Response stockMovementCreateJobForBinResponse = app.stockMovementCreateJobForBin(fcId, stockMovementCreateJobForBinBody);
        String stockMovementStackAckBody = prepareStockMovementStackAckBody(stockMovementCreateJobForBinResponse, reqQty, reqQty,2);
        int jobId = Integer.parseInt(stockMovementCreateJobForBinResponse.path("job_id").toString());
        app.stockMovementStackAck(fcId, jobId, stockMovementStackAckBody);
        app.stockMovementComplete(fcId, jobId);
    }

    /**
     * This method moves qty from  primary bin to bulk location
     * @param fcId
     * @param skuId
     * @param sourceBinLocation
     * @param adminCookie
     * @param reqQty
     * @param userName
     * @param report
     */
    public static void moveStockFromPrimaryToBulkLocation(int fcId, int skuId, String sourceBinLocation,
                                                               Map<String, String> adminCookie,
                                                               int reqQty, String userName, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);
        Response locationDetailResponse = app.locationDetailUsingBinId(fcId, skuId, sourceBinLocation);
        String userId = String.valueOf(fetchUserId(userName, report));
        HashMap<String, Object> stockMovementCreateJobForBinBody
                = prepareStockMovementCreateJobApiBody(userId, userName, skuId, reqQty, locationDetailResponse, 3, report);
        Response stockMovementCreateJobForBinResponse = app.stockMovementCreateJobForBin(fcId, stockMovementCreateJobForBinBody);
        String stockMovementStackAckBody = prepareStockMovementStackAckBody(stockMovementCreateJobForBinResponse, reqQty, reqQty,3);
        int jobId = Integer.parseInt(stockMovementCreateJobForBinResponse.path("job_id").toString());
        app.stockMovementStackAck(fcId, jobId, stockMovementStackAckBody);
        app.stockMovementComplete(fcId, jobId);
    }

    /**
     * This method moves qty from  bulk to primary bin location
     * @param fcId
     * @param skuId
     * @param adminCookie
     * @param reqQty
     * @param userName
     * @param report
     */
    public static void moveStockFromBulkToPrimaryLocation(int fcId, int skuId,
                                                          Map<String, String> adminCookie,
                                                          int reqQty, String userName, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);
        Response locationDetailResponse = app.locationDetailUsingLocationId(fcId, skuId, 3);
        String userId = String.valueOf(fetchUserId(userName, report));
        HashMap<String, Object> stockMovementCreateJobForBinBody
                = prepareStockMovementCreateJobApiBody(userId, userName, skuId, reqQty, locationDetailResponse, 1, report);
        Response stockMovementCreateJobForBinResponse = app.stockMovementCreateJobForBin(fcId, stockMovementCreateJobForBinBody);
        String stockMovementStackAckBody = prepareStockMovementStackAckBody(stockMovementCreateJobForBinResponse, reqQty, reqQty,3);
        int jobId = Integer.parseInt(stockMovementCreateJobForBinResponse.path("job_id").toString());
        app.stockMovementStackAck(fcId, jobId, stockMovementStackAckBody);
        app.stockMovementComplete(fcId, jobId);
    }
}
