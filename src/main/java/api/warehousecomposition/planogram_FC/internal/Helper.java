package api.warehousecomposition.planogram_FC.internal;

import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import framework.BaseTest;
import io.restassured.response.Response;
import msvc.DynamicLocationGeneralMethods;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import utility.database.DynamicLocationDBQueries;
import utility.database.PickingPlatformQueryExecuteWrapper;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Helper extends BaseTest {


    /**
     * fetches the userId for the given userName
     *
     * @param adminUserName admin user name
     * @param report        report instance
     * @return admin user id
     */
    public static int fetchUserId(String adminUserName, IReport report) {
        String query = "select id from auth_user where username=\"" + adminUserName + "\";";
        report.log("Fetching the admin userId with query \n " + query, true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeDatabaseQuery(serverName, query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No entry returned with the query: " + query);
        }
        int userId = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
        report.log("Admin userId for username " + adminUserName + " is : " + userId, true);
        return userId;
    }

    /**
     * Parses the response to fetch unstackedGrnQoh value
     *
     * @param response stockInfo api response
     * @param report   report instance
     * @return unStackedGrnQoh value
     */
    public static int fetchUnStackedGrNQoh(Response response, IReport report) {
        report.log("Parsing StockInfo api response to fetch UnStacked Grn Qoh ", true);
        int unStackedGrnQoh = Integer.parseInt(response.path("unstacked_qoh.grn_qoh").toString());
        report.log("Unstacked Grn Qoh value is : " + unStackedGrnQoh, true);
        return unStackedGrnQoh;
    }


    /**
     * Parses the response to fetch batch id value
     *
     * @param response un stacked batch api response
     * @param report   report instance
     * @return batchId value
     */
    public static int fetchBatchId(Response response, IReport report) {
        report.log("Parsing grn unstacked batch api response to fetch batch id ", true);
        int batchId = Integer.parseInt(response.path("batches[0].batch_id").toString());
        report.log("Batch ID value is : " + batchId, true);
        return batchId;
    }

    /**
     * Parses the response to fetch job id value
     *
     * @param response grn stacking create job api response
     * @param report   report instance
     * @return jobId value
     */
    public static int fetchJobId(Response response, IReport report) {
        report.log("Parsing grn stacking create job api response to fetch job id", true);
        int jobId = Integer.parseInt(response.path("job_id").toString());
        report.log("job ID value is : " + jobId, true);
        return jobId;
    }

    public static String prepareGrnStackingAckApiBody(int batchId, Response response, IReport report) {
        report.log("Parsing grn stacking create job api response", true);
        int numOfBinsRecommended = response.path("path.size()");
        System.out.println("Number of bins recommended: " + numOfBinsRecommended);
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder binsArrayInfo = Json.createArrayBuilder();
        JsonObjectBuilder individualBinObj = Json.createObjectBuilder();
        for (int i = 0; i < numOfBinsRecommended; i++) {
            int taskId = Integer.parseInt(response.path("path[" + i + "].task_id").toString());
            int skuId = Integer.parseInt(response.path("path[" + i + "].sku_id").toString());
            int binId = Integer.parseInt(response.path("path[" + i + "].bin_id").toString());
            int locationId = Integer.parseInt(response.path("path[" + i + "].location_id").toString());
            int stackedQty = Integer.parseInt(response.path("path[" + i + "].total_quantity").toString());
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .format(new DateTime(new Date()).toDate());
            individualBinObj = createStackingAckApiBody(taskId, skuId, batchId, binId, locationId, stackedQty, 0,
                    0, "", timeStamp);
            binsArrayInfo.add(individualBinObj);
        }
        jsonObjectBuilder.add("bins", binsArrayInfo);
        JsonObject jsonObject = jsonObjectBuilder.build();
        return jsonObject.toString();
    }

    public static String prepareGrnStackingAckApiBodyForGivenLocation(int batchId, Response response, String locationType, IReport report) {
        report.log("Parsing grn stacking create job api response \n" +
                "Stacking will be done only for location type: " + locationType, true);
        int numOfBinsRecommended = response.path("path.size()");
        System.out.println("Number of bins recommended: " + numOfBinsRecommended);
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder binsArrayInfo = Json.createArrayBuilder();
        JsonObjectBuilder individualBinObj = Json.createObjectBuilder();
        for (int i = 0; i < numOfBinsRecommended; i++) {
            String currLocationType = response.path("path[" + i + "].location_type").toString();
            //Stacking ACK body will be prepared for given location type only
            if (currLocationType.equalsIgnoreCase(locationType)) {
                int taskId = Integer.parseInt(response.path("path[" + i + "].task_id").toString());
                int skuId = Integer.parseInt(response.path("path[" + i + "].sku_id").toString());
                int binId = Integer.parseInt(response.path("path[" + i + "].bin_id").toString());
                int locationId = Integer.parseInt(response.path("path[" + i + "].location_id").toString());
                int stackedQty = Integer.parseInt(response.path("path[" + i + "].total_quantity").toString());
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                        .format(new DateTime(new Date()).toDate());
                individualBinObj = createStackingAckApiBody(taskId, skuId, batchId, binId, locationId, stackedQty, 0,
                        0, "", timeStamp);
                binsArrayInfo.add(individualBinObj);
            }
        }
        jsonObjectBuilder.add("bins", binsArrayInfo);
        JsonObject jsonObject = jsonObjectBuilder.build();
        return jsonObject.toString();
    }

    private static JsonObjectBuilder createStackingAckApiBody(int taskID, int skuID, int batchID, int binID,
                                                              int locationID, int stackedQty, int pendingQty,
                                                              int underStackReasonID, String underStackReason,
                                                              String stackingTimeStamp) {
        JsonObjectBuilder indiviualBinObj = Json.createObjectBuilder();
        indiviualBinObj.add("task_id", taskID);
        indiviualBinObj.add("sku_id", skuID);
        indiviualBinObj.add("batch_id", batchID);
        indiviualBinObj.add("bin_id", binID);
        indiviualBinObj.add("location_id", locationID);
        indiviualBinObj.add("stacked_quantity", stackedQty);
        indiviualBinObj.add("pending_quantity", pendingQty);
        indiviualBinObj.add("stacking_timestamp", stackingTimeStamp.replaceAll("\\.[0-9]+", "Z"));
        indiviualBinObj.add("understack_reason_id", underStackReasonID);
        indiviualBinObj.add("understack_reason", underStackReason);
        return indiviualBinObj;
    }

    public static String prepareGrnStackingAckApiBodyForGivenLocationFromAlternateBinApiResponse(int batchId, int totalQtyToStack, Response response, String locationType, IReport report) {
        report.log("Parsing alternate bin api response \n" +
                "Stacking will be done only for location type: " + locationType, true);
        int numOfBinsRecommended = response.path("path.size()");
        System.out.println("Number of bins recommended: " + numOfBinsRecommended);
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder binsArrayInfo = Json.createArrayBuilder();
        JsonObjectBuilder individualBinObj = Json.createObjectBuilder();
        for (int i = 0; i < numOfBinsRecommended; i++) {
            String currLocationType = response.path("path[" + i + "].location_type").toString();
            //Stacking ACK body will be prepared for given location type only
            if (currLocationType.equalsIgnoreCase(locationType)) {
                int taskId = Integer.parseInt(response.path("path[" + i + "].task_id").toString());
                int skuId = Integer.parseInt(response.path("path[" + i + "].sku_id").toString());
                int binId = Integer.parseInt(response.path("path[" + i + "].bin_id").toString());
                int locationId = Integer.parseInt(response.path("path[" + i + "].location_id").toString());
                int stackedQty = totalQtyToStack;
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                        .format(new DateTime(new Date()).toDate());
                individualBinObj = createStackingAckApiBody(taskId, skuId, batchId, binId, locationId, stackedQty, 0,
                        0, "", timeStamp);
                binsArrayInfo.add(individualBinObj);
            }
        }
        jsonObjectBuilder.add("bins", binsArrayInfo);
        JsonObject jsonObject = jsonObjectBuilder.build();
        return jsonObject.toString();
    }

    public static String preparePickingAckApiBody(String bagLabel, Response response, IReport report) {
        report.log("Parsing job assignment(picking create job) api response", true);
        int numOfSkusLocation = response.path("picking[0].sku_location_info.size()");
        System.out.println("Number of sku location: " + numOfSkusLocation);
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder skuInfoArray = Json.createArrayBuilder();
        JsonObjectBuilder individualSkuObj;
        for (int i = 0; i < numOfSkusLocation; i++) {
            int availableQty = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].available_qty").toString());
            int batchId = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].batch_id").toString());
            int binId = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].bin_id").toString());
            String binLocation = DynamicLocationGeneralMethods.getBinLocationForGivenBinID(binId, report);
            boolean isProcessed = Boolean.valueOf(response.path("picking[0].sku_location_info[" + i + "].is_processed").toString());
            int locationId = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].location_id").toString());
            int orderId = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].order_id").toString());
            int qtyPicked = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].quantity_recommended").toString());
            int qtyRecommended = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].quantity_recommended").toString());
            Double qtyWeight = Double.parseDouble(response.path("picking[0].sku_location_info[" + i + "].quantity_weight").toString());
            int sequenceId = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].sequence_id").toString());
            int skuId = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].sku_id").toString());
            int taskId = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].task_id").toString());
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .format(new DateTime(new Date()).toDate());
            individualSkuObj = createPickingAckApiBody(availableQty, skuId, batchId, binId, binLocation, isProcessed, orderId, locationId,
                    qtyRecommended, qtyWeight, sequenceId, taskId, bagLabel, 0, qtyPicked, timeStamp);
            skuInfoArray.add(individualSkuObj);
        }
        jsonObjectBuilder.add("sku_info", skuInfoArray);
        JsonObject jsonObject = jsonObjectBuilder.build();
        return jsonObject.toString();
    }

    /**
     * This method prepares the picking ack api body with less qunatity than recommended
     *
     * @param bagLabel
     * @param response
     * @param quantitytobereduced
     * @param report
     * @return
     */
    public static String preparePickingAckApiBody(String bagLabel, Response response, int quantitytobereduced, int underpickreasonid, IReport report) {
        report.log("Parsing job assignment(picking create job) api response", true);
        int numOfSkusLocation = response.path("picking[0].sku_location_info.size()");
        System.out.println("Number of sku location: " + numOfSkusLocation);
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder skuInfoArray = Json.createArrayBuilder();
        JsonObjectBuilder individualSkuObj;
        for (int i = 0; i < numOfSkusLocation; i++) {
            int availableQty = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].available_qty").toString());
            int batchId = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].batch_id").toString());
            int binId = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].bin_id").toString());
            String binLocation = DynamicLocationGeneralMethods.getBinLocationForGivenBinID(binId, report);
            boolean isProcessed = Boolean.valueOf(response.path("picking[0].sku_location_info[" + i + "].is_processed").toString());
            int locationId = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].location_id").toString());
            int orderId = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].order_id").toString());
            int qtyPicked = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].quantity_recommended").toString()) - quantitytobereduced;
            int qtyRecommended = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].quantity_recommended").toString());
            Double qtyWeight = Double.parseDouble(response.path("picking[0].sku_location_info[" + i + "].quantity_weight").toString());
            int sequenceId = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].sequence_id").toString());
            int skuId = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].sku_id").toString());
            int taskId = Integer.parseInt(response.path("picking[0].sku_location_info[" + i + "].task_id").toString());
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                    .format(new DateTime(new Date()).toDate());
            individualSkuObj = createPickingAckApiBody(availableQty, skuId, batchId, binId, binLocation, isProcessed, orderId, locationId,
                    qtyRecommended, qtyWeight, sequenceId, taskId, bagLabel, underpickreasonid, qtyPicked, timeStamp);
            skuInfoArray.add(individualSkuObj);
        }
        jsonObjectBuilder.add("sku_info", skuInfoArray);
        JsonObject jsonObject = jsonObjectBuilder.build();
        return jsonObject.toString();
    }


    /**
     * --data-raw '{
     * "event_type": "binning",
     * "order_id": 1000699934,
     * "bin_loc": "B-18-A-3",
     * "container_info": [{"id":"BB6798763"}]
     * }'
     */

    public static String orderBinMappingApiBody(String eventType, int orderId, String binLoc, String[] containerids) {
        JSONObject body = new JSONObject();
        JSONObject ids = new JSONObject();
        for (int i = 0; i < containerids.length; i++) {
            ids.put("id", containerids[i]);
        }
        JSONArray containerarray = new JSONArray();
        containerarray.put(ids);
        body.put("event_type", eventType).put("order_id", orderId).put("bin_loc", binLoc).put("container_info", containerarray);
        return body.toString();
    }


    private static JsonObjectBuilder createPickingAckApiBody(int availableQty, int skuId, int batchId, int binId,
                                                             String binLocation, boolean isProcessed, int orderId,
                                                             int locationId, int qtyRecommended, Double qtyWeight,
                                                             int sequenceId, int taskId, String label,
                                                             int underStackReasonId, int qtyPicked,
                                                             String pickingTimeStamp) {
        JsonObjectBuilder individualSkuObj = Json.createObjectBuilder();
        individualSkuObj.add("available_qty", availableQty);
        individualSkuObj.add("batch_id", batchId);
        individualSkuObj.add("bin_id", binId);
        individualSkuObj.add("bin_location", binLocation);
        individualSkuObj.add("is_processed", isProcessed);
        individualSkuObj.add("location_id", locationId);
        individualSkuObj.add("order_id", orderId);
        individualSkuObj.add("quantity_picked", qtyPicked);
        individualSkuObj.add("quantity_recommended", qtyRecommended);
        individualSkuObj.add("quantity_weight", qtyWeight);
        individualSkuObj.add("sequence_id", sequenceId);
        individualSkuObj.add("sku_id", skuId);
        individualSkuObj.add("task_id", taskId);
        individualSkuObj.add("picking_timestamp", pickingTimeStamp.replaceAll("\\.[0-9]+", "Z"));
        individualSkuObj.add("underpick_reason_id", underStackReasonId);

        JsonArrayBuilder crateArray = Json.createArrayBuilder();
        JsonObjectBuilder crateObj = Json.createObjectBuilder();
        crateObj.add("type_id", 0);
        crateObj.add("label", label);
        crateObj.add("quantity", qtyPicked);
        crateObj.add("weight", qtyWeight);
        crateObj.add("status", "open");
        crateObj.add("pick_timestamp", pickingTimeStamp);
        crateArray.add(crateObj);

        individualSkuObj.add("crates", crateArray);
        return individualSkuObj;
    }

    /**
     * Parses the response to fetch job id value
     *
     * @param response job assignment api response
     * @param report   report instance
     * @return jobId value
     */
    public static int fetchJobIdFromJobAssignmentApiResponse(Response response, IReport report) {
        report.log("Parsing job assignment create job api response to fetch job id", true);
        int jobId = response.jsonPath().getInt("picking[0].job_id");
        report.log("job ID value is : " + jobId, true);
        return jobId;

    }

    /**
     * Parses the response to fetch order id value
     *
     * @param response job assignment api response
     * @param report   report instance
     * @return orderId value
     */
    public static int fetchorderIdFromJobAssignmentApiResponse(Response response, IReport report) {
        report.log("Parsing job assignment create job api response to fetch job id", true);
        int orderId = response.jsonPath().getInt("picking[0].sku_location_info[0].order_id");
        report.log("order Id value is : " + orderId, true);
        return orderId;

    }

    /**
     * Checks whether the given orderid is present in picking_request table
     * if order entry is not present, does the polling with every 30 sec interval with 5 retries
     *
     * @param orderId
     * @param report
     * @return
     */
    public static String checkWhetherTheGivenOrderIsPresentInPickingRequestTable(int orderId, IReport report) {
        String query = "select order_status,picking_status from picking_request where order_id = " + orderId + ";";
        report.log("Checking whether the orderId is present in picking_request table\n " + query, true);
        System.out.println("Checking whether the orderId is present in picking_request table\n " + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            int retryCount = 5;
            int minWaitInSec = 30;
            for (int i = 0; i < retryCount; i++) {
                try {
                    Thread.sleep(1000 * minWaitInSec);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Retrying  : " + (i + 1) + " out of " + retryCount);
                jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
                if (jsonObject.getJSONArray("rows").length() != 0) {
                    break;
                }
            }
            if (jsonObject.getInt("numRows") == 0) {
                Assert.fail("No entry returned with  \n Query: " + query +
                        "\n Order: " + orderId + " entry is not created in picking request table");
            }
        }
        String orderStatus = jsonObject.getJSONArray("rows").getJSONObject(0).getString("order_status");
        report.log("Order ID: " + orderId + " is present in picking_request table" +
                "\n Order Status: " + orderStatus +
                "\n Picking Status: " + jsonObject.getJSONArray("rows").getJSONObject(0).getString("picking_status"), true);
        return orderStatus;
    }

    /**
     * Checks whether the given order entry is present in picking_item_request table
     * if order entry is not present, does the polling with every 30 sec interval with 5 retries
     *
     * @param orderId
     * @param report
     * @return
     */
    public static void checkWhetherTheGivenOrderIsPresentInPickingItemRequestTable(int orderId, IReport report) {
        String query = "select order_id,sku_id,ordered_quantity,reservation_fc_id from picking_item_request where picking_request_id = " +
                "(select id from picking_request where order_id = " + orderId + ");";
        report.log("Checking whether the orderId is present in picking_item_request table\n " + query, true);
        System.out.println("Checking whether the orderId is present in picking_item_request table\n " + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            int retryCount = 5;
            int minWaitInSec = 30;
            for (int i = 0; i < retryCount; i++) {
                try {
                    Thread.sleep(1000 * minWaitInSec);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Retrying  : " + (i + 1) + " out of " + retryCount);
                jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
                if (jsonObject.getJSONArray("rows").length() != 0) {
                    break;
                }
            }
            if (jsonObject.getInt("numRows") == 0) {
                Assert.fail("No entry returned with  \n Query: " + query +
                        "\n Order: " + orderId + " entry is not created in picking item request table");
            }
        }
    }

    /**
     * This method processes job assignment api response and if the user as previously assigned job releases the same
     * If there are no orders in picking_request table to assign, picking flow will be marked as failed
     *
     * @param userId
     * @param jobAssignmentResponse
     * @param report
     * @return boolean value is returned to decide whether the job assignment api needs to be called or not
     */
    public static boolean processPickingJobResponse(int userId, Response jobAssignmentResponse, IReport report) {
        if (!(jobAssignmentResponse.path("picking[0].job_id") == null)) {
            report.log("Picking Job is created for the user, Completing the picking flow", true);
            System.out.println("Picking Job is created for the user, Completing the picking flow");
            return false;
        } else {
            report.log("No picking Job is created for the user, Checking whether the user as already assigned jobs", true);
            System.out.println("No picking Job is created for the user, Checking whether the user as already assigned jobs");
            if (checkWhetherUserIsBusyWithOtherJob(userId, report)) {
                report.log("User has already assigned picking job, releaseing the same", true);
                System.out.println("User has already assigned picking job, releaseing the same");
                releaseTheUserFromCurrentPickingJob(userId, report);
                return true;
            } else {
                Assert.fail("No orders in picking_request table to assign for picking flow. " +
                        "some other picker might have picked the order");
            }
        }
        return false;
    }

    /**
     * Checks whether the given userId is currently assigned with any jobs, if yes return true, otherwise false
     *
     * @param userId
     * @param report
     * @return
     */
    private static boolean checkWhetherUserIsBusyWithOtherJob(int userId, IReport report) {
        String query = "select order_status,picking_status,order_id from picking_request where order_status = 'inprocess' and updated_by_id = " + userId + ";";
        report.log("Checking whether user is busy with other job\n " + query, true);
        System.out.println("Checking whether user is busy with other job\n " + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("User is not assigned with other job", true);
            return false;
        }
        report.log("User is assigned with other job " +
                "\ncurrent job/OrderId " + jsonObject.getJSONArray("rows").getJSONObject(0).getInt("order_id"), true);
        return true;
    }

    /**
     * Releases the user from the current picking job
     *
     * @param userId
     * @param report
     * @return
     */
    private static void releaseTheUserFromCurrentPickingJob(int userId, IReport report) {
        String query = "update picking_request set picking_status = 'cancelled', order_status='cancelled' " +
                "where order_status = 'inprocess' and updated_by_id = " + userId + ";";
        report.log("Release the user from current job\n " + query, true);
        System.out.println("Release the user from current job\n " + query);
        PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query);
    }

    /**
     * fetchs the when to pick time of oldest open order from picking request table
     *
     * @param report
     * @return
     */
    public static String fetchTheWhenToPickTimeOfOldestOpenOrderFromPickingRequestTable(IReport report) {
        String query = "select id,order_id,when_to_pick from picking_request where " +
                "order_status=\"open\" and ec_id=10 order by\n" +
                "when_to_pick limit 1;";
        report.log("Fetching when to pick time from picking_request table\n " + query, true);
        System.out.println("Fetching when to pick time from picking_request table\n " + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No entry returned with the query: " + query);
        }
        String whenToPickTime = jsonObject.getJSONArray("rows").getJSONObject(0).getString("when_to_pick");
        report.log("When to pick time of oldest order " + whenToPickTime, true);
        return whenToPickTime;
    }

    /**
     * fetchs the when to pick time of given order from picking request table
     *
     * @param orderId
     * @param report
     * @return
     */
    public static String fetchTheWhenToPickTimeOfGivenOrderFromPickingRequestTable(int orderId, IReport report) {
        String query = "select id,order_id,ec_id,order_status,picking_status,when_to_pick from picking_request where order_id=" + orderId + ";";
        report.log("Fetching when to pick time from picking_request table\n " + query, true);
        System.out.println("Fetching when to pick time from picking_request table\n " + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No entry returned with the query: " + query);
        }
        String whenToPickTime = jsonObject.getJSONArray("rows").getJSONObject(0).getString("when_to_pick");
        report.log("When to pick time of order " + orderId + " is " + whenToPickTime, true);
        return whenToPickTime;
    }


    /**
     * Checks if any order with picking_status open in the picking_request table
     *
     * @param report
     * @return
     */
    public static void verifyAnyOpenOrderPresentInPickingRequestTable(IReport report) {
        String query = "select id,order_id,ec_id,order_status,picking_status,when_to_pick from picking_request where picking_status=\"open\" order by id limit 1;";
        report.log("Checking any open order present in picking request table\n " + query, true);
        System.out.println("Checking any open order present in picking request table\n " + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No open order is present in picking request table " + query);
        }
    }

    /**
     * compares two datetime values
     * format "2021-12-13T12:50:54.000"
     *
     * @param dateTime1
     * @param dateTime2
     * @return
     */
    public static boolean isDateTime1LessThanDateTime2(String dateTime1, String dateTime2) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formatedWhenToPickTimeOfCurAssignOrd = dateTime1.replace("T", " ").replaceAll("[.].*", "");
        long whenToPickTimeForAssignOrd = LocalDateTime.parse(formatedWhenToPickTimeOfCurAssignOrd, dateTimeFormatter).toEpochSecond(ZoneOffset.UTC);
        String formatedWhenToPickTimeOfOldestOrd = dateTime2.replace("T", " ").replaceAll("[.].*", "");
        long whenToPickTimeForOldestOrd = LocalDateTime.parse(formatedWhenToPickTimeOfOldestOrd, dateTimeFormatter).toEpochSecond(ZoneOffset.UTC);
        if (whenToPickTimeForAssignOrd < whenToPickTimeForOldestOrd) {
            return true;
        }
        return false;
    }

    /**
     * fetchs the order status for given orderId
     *
     * @param orderId
     * @param report
     * @return
     */
    public static String fetchOrderStatus(int orderId, IReport report) {
        String query = "select order_status from picking_request where order_id = " + orderId + ";";
        report.log("Fetching the order status from picking_request table\n " + query, true);
        System.out.println("Fetching the order status from picking_request table\n " + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned picking request table " + query);
        }
        String orderStatus = jsonObject.getJSONArray("rows").getJSONObject(0).getString("order_status");
        report.log("Order ID: " + orderId + " is present in picking_request table" +
                "\n Order Status: " + orderStatus, true);
        return orderStatus;
    }

    /**
     * fetchs the picking status for given orderId
     *
     * @param orderId
     * @param report
     * @return
     */
    public static String fetchPickingStatus(int orderId, IReport report) {
        String query = "select picking_status from picking_request where order_id = " + orderId + ";";
        report.log("Fetching the picking status from picking_request table\n " + query, true);
        System.out.println("Fetching the picking status from picking_request table\n " + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No data returned picking request table " + query);
        }
        String pickingStatus = jsonObject.getJSONArray("rows").getJSONObject(0).getString("picking_status");
        report.log("Order ID: " + orderId + " is present in picking_request table" +
                "\n Picking Status: " + pickingStatus, true);
        return pickingStatus;
    }

    public static String createTransferInApiBody(List<HashMap<String, Object>> looseQtySKUList,
                                                 List<HashMap<String, Object>> containerSKUList) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("transfer_id", getUniqueTransferId());
        jsonObjectBuilder.add("po_id", 999);
        jsonObjectBuilder.add("created_by", "AUTOMATION");
        jsonObjectBuilder.add("created_on", "2020-06-20T05:01:09.639Z");

        //Adding json object inside loose quantity array
        JsonArrayBuilder looseQtyInfoArray = Json.createArrayBuilder();
        for (int i = 0; i < looseQtySKUList.size(); i++) {
            JsonObjectBuilder looseQtyObj1 = Json.createObjectBuilder();
            looseQtyObj1.add("sku_id", ((int) looseQtySKUList.get(i).get("sku_id")));
            looseQtyObj1.add("quantity", ((int) looseQtySKUList.get(i).get("quantity")));
            looseQtyObj1.add("mfg_date", looseQtySKUList.get(i).get("mfg_date").toString());
            looseQtyObj1.add("exp_date", looseQtySKUList.get(i).get("exp_date").toString());
            looseQtyObj1.add("is_bundlepack", ((boolean) looseQtySKUList.get(i).get("is_bundlepack")));
            looseQtyObj1.add("cp", ((double) looseQtySKUList.get(i).get("cp")));
            looseQtyObj1.add("mrp", ((double) looseQtySKUList.get(i).get("mrp")));
            looseQtyObj1.add("eretail_mrp", ((double) looseQtySKUList.get(i).get("eretail_mrp")));
            looseQtyObj1.add("gst", ((double) looseQtySKUList.get(i).get("gst")));
            looseQtyObj1.add("cess", ((double) looseQtySKUList.get(i).get("cess")));
            looseQtyInfoArray.add(looseQtyObj1);
        }
        jsonObjectBuilder.add("loose_quantity_info", looseQtyInfoArray);
        JsonArrayBuilder containerArrayInfo = Json.createArrayBuilder();

        //Adding json object inside container sku array
        for (int i = 0; i < containerSKUList.size(); i++) {
            JsonObjectBuilder containerQtyObj1 = Json.createObjectBuilder();
            containerQtyObj1.add("container_id", ((int) containerSKUList.get(i).get("container_id")));
            containerQtyObj1.add("container_tag", containerSKUList.get(i).get("container_tag").toString());
            containerQtyObj1.add("container_type", containerSKUList.get(i).get("container_type").toString());
            containerQtyObj1.add("unknown_dimension", ((boolean) containerSKUList.get(i).get("unknown_dimension")));

            JsonArrayBuilder containerQtyObj1SkuArrayInfo1 = Json.createArrayBuilder();
            JsonObjectBuilder containerQtyObj1SkuArrayInfo1Obj1 = Json.createObjectBuilder();
            containerQtyObj1SkuArrayInfo1Obj1.add("sku_id", ((int) containerSKUList.get(i).get("sku_id")));
            containerQtyObj1SkuArrayInfo1Obj1.add("quantity", ((int) containerSKUList.get(i).get("quantity")));
            containerQtyObj1SkuArrayInfo1Obj1.add("mfg_date", containerSKUList.get(i).get("mfg_date").toString());
            containerQtyObj1SkuArrayInfo1Obj1.add("exp_date", containerSKUList.get(i).get("exp_date").toString());
            containerQtyObj1SkuArrayInfo1Obj1.add("is_bundlepack", ((boolean) containerSKUList.get(i).get("is_bundlepack")));
            containerQtyObj1SkuArrayInfo1Obj1.add("cp", ((double) containerSKUList.get(i).get("cp")));
            containerQtyObj1SkuArrayInfo1Obj1.add("mrp", ((double) containerSKUList.get(i).get("mrp")));
            containerQtyObj1SkuArrayInfo1Obj1.add("eretail_mrp", ((double) containerSKUList.get(i).get("eretail_mrp")));
            containerQtyObj1SkuArrayInfo1Obj1.add("gst", ((double) containerSKUList.get(i).get("gst")));
            containerQtyObj1SkuArrayInfo1Obj1.add("cess", ((double) containerSKUList.get(i).get("cess")));
            containerQtyObj1SkuArrayInfo1.add(containerQtyObj1SkuArrayInfo1Obj1);

            containerQtyObj1.add("sku_info", containerQtyObj1SkuArrayInfo1);
            containerArrayInfo.add(containerQtyObj1);
        }
        jsonObjectBuilder.add("container_info", containerArrayInfo);
        JsonObject jsonObject = jsonObjectBuilder.build();

        System.out.println("Body: " + jsonObject);
        return jsonObject.toString();
    }

    private static long getUniqueTransferId() {
        String query = "select transferin_id from transferin order by transferin_id DESC limit 1;";
        JSONObject jsonObject = new JSONObject(DynamicLocationDBQueries.executeMicroserviceDataBaseQuery(query));
        long currentTansferInID = 0;
        if (jsonObject.getInt("numRows") != 0) {
            currentTansferInID = jsonObject.getJSONArray("rows").getJSONObject(0).getLong("transferin_id");
        }
        return currentTansferInID + 1;
    }

    public static String prepareGrnStackingAlternateBinApiBody(int skuId, int pendingQty, IReport report) {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        JsonArrayBuilder skuListArray = Json.createArrayBuilder();
        JsonObjectBuilder individualSkuObj = Json.createObjectBuilder();
        individualSkuObj.add("sku_id", skuId);
        individualSkuObj.add("pending_quantity", pendingQty);
        skuListArray.add(individualSkuObj);
        jsonObjectBuilder.add("skus", skuListArray);
        JsonObject jsonObject = jsonObjectBuilder.build();
        return jsonObject.toString();
    }


    public static HashMap<String, Object> prepareStockMovementCreateJobApiBody(String userId, String userName, int skuId,
                                                                               int reqQty, Response locationDetailResponse,
                                                                               int destinationLocationId, IReport report) {
        report.log("Parsing location detail api response ", true);


        HashMap<String, Object> stockMovementCreateJobForBinBody = new HashMap<>();
        stockMovementCreateJobForBinBody.put("user_id", userId);
        stockMovementCreateJobForBinBody.put("username", userName);
        stockMovementCreateJobForBinBody.put("sku_id", skuId);
        stockMovementCreateJobForBinBody.put("bin_id", Integer.parseInt(locationDetailResponse.path("bin_id").toString()));
        stockMovementCreateJobForBinBody.put("location_id", Integer.parseInt(locationDetailResponse.path("location_id").toString()));
        stockMovementCreateJobForBinBody.put("requested_quantity", reqQty);
        stockMovementCreateJobForBinBody.put("recommended_quantity", Double.valueOf(locationDetailResponse.path("recommended_quantity").toString()));
        stockMovementCreateJobForBinBody.put("picked_quantity", reqQty);
        stockMovementCreateJobForBinBody.put("underpick_reason_id", 0);
        stockMovementCreateJobForBinBody.put("underpick_reason", "");
        stockMovementCreateJobForBinBody.put("destination_location_id", destinationLocationId);

        return stockMovementCreateJobForBinBody;
    }

    public static String prepareStockMovementStackAckBody(Response stockMovementCreateJobForBinResponse, int stackedQty, int pendingQty, int destinationLocationId) {
        JsonObjectBuilder jsonObject = Json.createObjectBuilder();
        JsonArrayBuilder binsArray = Json.createArrayBuilder();
        JsonObjectBuilder binObject = Json.createObjectBuilder();

        if (Integer.parseInt(stockMovementCreateJobForBinResponse.path("bins.size()").toString()) != 0) {
            binObject.add("task_id", Integer.parseInt(stockMovementCreateJobForBinResponse.path("bins[0].task_id").toString()));
            binObject.add("bin_id", Integer.parseInt(stockMovementCreateJobForBinResponse.path("bins[0].bin_id").toString()));
            binObject.add("location_id", Integer.parseInt(stockMovementCreateJobForBinResponse.path("bins[0].location_id").toString()));
        } else {
            binObject.add("bin_id", 0);
            binObject.add("location_id", destinationLocationId);

        }
        binObject.add("batch_id", Integer.parseInt(stockMovementCreateJobForBinResponse.path("batch_details.batch_id").toString()));
        binObject.add("stacked_quantity", stackedQty);
        binObject.add("pending_quantity", pendingQty);
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .format(new DateTime(new Date()).toDate());
        binObject.add("stacking_timestamp", timeStamp.replaceAll("\\.[0-9]+", "Z"));
        binsArray.add(binObject);
        jsonObject.add("bins", binsArray);
        return jsonObject.build().toString();
    }
}
