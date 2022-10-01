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
     *
     * @param fcId                     fcId
     * @param skuId                    skuId to complete stacking
     * @param userName                 admin username
     * @param grnCreateStackingJobBody stacking request body
     * @param report                   report instance
     * @return
     */
    public static int grnStackingFlow(int fcId, int skuId, String userName, String pwd, HashMap<String, Object> grnCreateStackingJobBody, IReport report) {
        IQApp app = new IQApp(report);
        Response response = app.iqAppLogin(userName, pwd)
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
     *
     * @param fcId                     fcId
     * @param skuId                    skuId to complete stacking
     * @param userName                 admin username
     * @param grnCreateStackingJobBody stacking request body
     * @param cookie                   AdminCookie to authenticate dl apis
     * @param report                   report instance
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


    /**
     * This method fetchs the unstacked details for the given product
     *
     * @param adminCookie login cookie details
     * @param skuId
     * @param fcId
     * @param report
     * @return
     */
    public static Response grnStackingunStackedDetails(Map<String, String> adminCookie, int skuId, int fcId, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);
        Response response = app.grnStackingUnstackedAPI(fcId, skuId);
        ;
        return response;
    }


    /**
     * This method does the stacking for the given location type
     * if for a given locationtype all the quantities cannot be stacked, then the suggested qty will be stacked only for the given location type
     * if any other location type is recommended it will be ignored
     * CreateGrnStackingJob-->GrnStackingAck-->GrnStackingComplete
     *
     * @param fcId                     fcId
     * @param skuId                    skuId to complete stacking
     * @param userName                 admin username
     * @param grnCreateStackingJobBody stacking request body
     * @param locationType             "primary" or "secondary"
     * @param cookie                   AdminCookie to authenticate dl apis
     * @param report                   report instance
     * @return
     */
    public static boolean performGrnStackingForGivenLocation(int fcId, int skuId, String userName, HashMap<String, Object> grnCreateStackingJobBody, String locationType, Map<String, String> cookie, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(cookie);

        if (locationType.equalsIgnoreCase("primary") || locationType.equalsIgnoreCase("secondary")) {

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

            boolean isBinWithGivenLocationTypeRecommended = false;
            int numBinsRecommended = Integer.parseInt(response.path("path.size()").toString());
            for (int i = 0; i < numBinsRecommended; i++) {
                if (response.path("path[" + i + "].location_type").toString().equalsIgnoreCase(locationType)) {
                    isBinWithGivenLocationTypeRecommended = true;
                    break;
                }
            }

            int jobId = fetchJobId(response, report);
            String stackingAckBody = null;
            System.out.println("Checking whether the bin with expected location type recommended by create job api call");
            if (isBinWithGivenLocationTypeRecommended) {
                stackingAckBody = prepareGrnStackingAckApiBodyForGivenLocation(batchId, response, locationType, report);
            } else {
                System.out.println("Bin with expected location type not recommended by create job api call \n" +
                        "Calling alternate bin api");
                String alternateBinApiBody = prepareGrnStackingAlternateBinApiBody(skuId, Integer.parseInt(grnCreateStackingJobBody.get("total_quantity").toString()), report);
                Response alternateBinApiResponse = app.stackingAlternateBinAPI(fcId, jobId, alternateBinApiBody);
                numBinsRecommended = Integer.parseInt(alternateBinApiResponse.path("path.size()").toString());
                for (int i = 0; i < numBinsRecommended; i++) {
                    if (alternateBinApiResponse.path("path[" + i + "].location_type").toString().equalsIgnoreCase(locationType)) {
                        isBinWithGivenLocationTypeRecommended = true;
                        break;
                    }
                }
                System.out.println("Checking whether the bin with expected location type recommended by alternate bin api call");
                if (isBinWithGivenLocationTypeRecommended) {
                    stackingAckBody = prepareGrnStackingAckApiBodyForGivenLocationFromAlternateBinApiResponse
                            (batchId, Integer.parseInt(grnCreateStackingJobBody.get("total_quantity").toString()), alternateBinApiResponse, locationType, report);

                } else {
                    System.out.println("Couldn't get the bin with expected location type from alternate bin api call");
                    return false;
                }
            }
            app.grnStackingJobAckAPI(fcId, jobId, stackingAckBody);
            app.grnStackingCompleteAPI(fcId, jobId);
            return true;
        } else {
            System.out.println("Not a valid location type \n Expected location type is primary, secondary");
            return false;
        }
    }

}
