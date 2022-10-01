package api.warehousecomposition.planogram_FC;

import api.warehousecomposition.planogram_FC.internal.Helper;
import api.warehousecomposition.planogram_FC.internal.IQApp;
import com.bigbasket.automation.reports.IReport;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import utility.database.PickingPlatformQueryExecuteWrapper;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class PickingMethods extends Helper {

    /**
     * This method completes the picking flow for the all the open orders with earliest created on time in pick_request table
     * before completing the picking flow for the given order
     * For the given picker if there is any existing assigned job, this will release the same to complete the picking flow
     * jobAssignmentAPI --> order-bag LinkingAPI --> pickingAckAPI --> pickCompleteAPI
     *
     * @param orderId     orderId for picking
     * @param fcId        fcId
     * @param userName    adminusername otp enabled
     * @param adminCookie admin login cookie
     * @param report      report instance
     */
    public static void pickingFlowForAllEarliestOpenOrders(int orderId, int fcId, String userName, Map<String, String> adminCookie, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);
        String orderStatus = checkWhetherTheGivenOrderIsPresentInPickingRequestTable(orderId, report);
        if (orderStatus.equalsIgnoreCase("open")) {

            int curOrderIdAssignedForPicking;
            int userId = fetchUserId(userName, report);
            do {
                orderStatus = checkWhetherTheGivenOrderIsPresentInPickingRequestTable(orderId, report);
                if (orderStatus.equalsIgnoreCase("inprocess")) {
                    Assert.fail("Order/job is assigned to some other picker");
                }

                HashMap<String, Object> jobAssigmentBody = new HashMap<>();
                jobAssigmentBody.put("status", "assigned");
                jobAssigmentBody.put("type", "picking");
                Response response = app.jobAssignment(fcId, jobAssigmentBody);
                if (processPickingJobResponse(userId, response, report)) {
                    report.log("Calling job assignment api, post releasing the user from previous job", true);
                    response = app.jobAssignment(fcId, jobAssigmentBody);
                }

                curOrderIdAssignedForPicking = fetchorderIdFromJobAssignmentApiResponse(response, report);
                int jobId = fetchJobIdFromJobAssignmentApiResponse(response, report);
                report.log("Current orderId assigned for picking is: " + curOrderIdAssignedForPicking, true);

                String bagLabel = String.valueOf(Instant.now().toEpochMilli());
                report.log("Container bag label: " + bagLabel, true);
                HashMap<String, Object> orderBagLinkBody = new HashMap<>();
                orderBagLinkBody.put("order_id", curOrderIdAssignedForPicking);
                orderBagLinkBody.put("container_label", bagLabel);
                app.orderBagLinking(fcId, orderBagLinkBody, false);

                String pickAckBody = preparePickingAckApiBody(bagLabel, response, report);
                app.pickingJobAckAPI(fcId, jobId, pickAckBody);

                HashMap<String, Object> pickCompleteBody = new HashMap<>();
                pickCompleteBody.put("job_id", jobId);
                pickCompleteBody.put("order_id", curOrderIdAssignedForPicking);
                app.pickingPlatformCompleteAPI(fcId, pickCompleteBody);
                report.log("Picking completed for the order: " + curOrderIdAssignedForPicking, true);
            } while (orderId != curOrderIdAssignedForPicking);
            report.log("Picking completed for the given order: " + orderId, true);

        } else if (!orderStatus.equalsIgnoreCase("packed")) {
            Assert.fail("Order/job is assigned to some other picker");
        }
    }

    /**
     * This method updates the Order picking_request created on time to earliest in open orders, so that the given order gets
     * assigned for picking first
     * For the given picker if there is any existing assigned job, this will release the same to complete the picking flow
     * jobAssignmentAPI --> order-bag LinkingAPI --> pickingAckAPI --> pickCompleteAPI
     *
     * @param orderId     orderId for picking
     * @param fcId        fcId
     * @param userName    adminusername otp enabled
     * @param adminCookie admin login cookie
     * @param report      report instance
     */
    public static void pickingFlowModified(int orderId, int fcId, String userName, Map<String, String> adminCookie, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);

        String orderStatus = checkWhetherTheGivenOrderIsPresentInPickingRequestTable(orderId, report);
        checkWhetherTheGivenOrderIsPresentInPickingItemRequestTable(orderId, report);
        if (orderStatus.equalsIgnoreCase("open")) {

            HashMap<String, Object> performPickingOperationBody = new HashMap<>();
            performPickingOperationBody.put("order_id", orderId);
            app.updatePickRequestCreatedOnTimeToEarliest(performPickingOperationBody, report);

            int curOrderIdAssignedForPicking;
            int userId = fetchUserId(userName, report);
            do {
                orderStatus = checkWhetherTheGivenOrderIsPresentInPickingRequestTable(orderId, report);
                if (orderStatus.equalsIgnoreCase("inprocess")) {
                    Assert.fail("Order/job is assigned to some other picker");
                }

                HashMap<String, Object> jobAssigmentBody = new HashMap<>();
                jobAssigmentBody.put("status", "assigned");
                jobAssigmentBody.put("type", "picking");
                Response response = app.jobAssignment(fcId, jobAssigmentBody);
                if (processPickingJobResponse(userId, response, report)) {
                    report.log("Calling job assignment api, post releasing the user from previous job", true);
                    response = app.jobAssignment(fcId, jobAssigmentBody);
                }

                curOrderIdAssignedForPicking = fetchorderIdFromJobAssignmentApiResponse(response, report);
                int jobId = fetchJobIdFromJobAssignmentApiResponse(response, report);
                report.log("Current orderId assigned for picking is: " + curOrderIdAssignedForPicking, true);

                String bagLabel = String.valueOf(Instant.now().toEpochMilli());
                report.log("Container bag label: " + bagLabel, true);
                HashMap<String, Object> orderBagLinkBody = new HashMap<>();
                orderBagLinkBody.put("order_id", curOrderIdAssignedForPicking);
                orderBagLinkBody.put("container_label", bagLabel);
                app.orderBagLinking(fcId, orderBagLinkBody, false);

                String pickAckBody = preparePickingAckApiBody(bagLabel, response, report);
                app.pickingJobAckAPI(fcId, jobId, pickAckBody);

                HashMap<String, Object> pickCompleteBody = new HashMap<>();
                pickCompleteBody.put("job_id", jobId);
                pickCompleteBody.put("order_id", curOrderIdAssignedForPicking);
                app.pickingPlatformCompleteAPI(fcId, pickCompleteBody);
                report.log("Picking completed for the order: " + curOrderIdAssignedForPicking, true);
            } while (orderId != curOrderIdAssignedForPicking);
            report.log("Picking completed for the given order: " + orderId, true);

        } else if (!orderStatus.equalsIgnoreCase("packed")) {
            Assert.fail("Order/job is assigned to some other picker");
        }
    }


    /**
     * This method updates the Order picking_request created on time to earliest in open orders, so that the given order gets
     *  assigned for picking first
     *  this will pick less quantity than actual which is given by the user
     * For the given picker if there is any existing assigned job, this will release the same to complete the picking flow
     * jobAssignmentAPI --> order-bag LinkingAPI --> pickingAckAPI --> pickCompleteAPI
     * @param orderId
     * @param fcId
     * @param userName
     * @param adminCookie
     * @param quantitytobereduced quantity to be reduced from the actual quantity
     * @param report
     */
    public static void pickingFlowLessThanRecommended(int orderId, int fcId, String userName, Map<String, String> adminCookie,int quantitytobereduced,int underpickreasonid, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);

        String orderStatus = checkWhetherTheGivenOrderIsPresentInPickingRequestTable(orderId, report);
        checkWhetherTheGivenOrderIsPresentInPickingItemRequestTable(orderId, report);
        if (orderStatus.equalsIgnoreCase("open")) {

            HashMap<String, Object> performPickingOperationBody = new HashMap<>();
            performPickingOperationBody.put("order_id", orderId);
            app.updatePickRequestCreatedOnTimeToEarliest(performPickingOperationBody, report);

            int curOrderIdAssignedForPicking;
            int userId = fetchUserId(userName, report);
            do {
                orderStatus = checkWhetherTheGivenOrderIsPresentInPickingRequestTable(orderId, report);
                if (orderStatus.equalsIgnoreCase("inprocess")) {
                    Assert.fail("Order/job is assigned to some other picker");
                }

                HashMap<String, Object> jobAssigmentBody = new HashMap<>();
                jobAssigmentBody.put("status", "assigned");
                jobAssigmentBody.put("type", "picking");
                Response response = app.jobAssignment(fcId, jobAssigmentBody);
                if (processPickingJobResponse(userId, response, report)) {
                    report.log("Calling job assignment api, post releasing the user from previous job", true);
                    response = app.jobAssignment(fcId, jobAssigmentBody);
                }

                curOrderIdAssignedForPicking = fetchorderIdFromJobAssignmentApiResponse(response, report);
                int jobId = fetchJobIdFromJobAssignmentApiResponse(response, report);
                report.log("Current orderId assigned for picking is: " + curOrderIdAssignedForPicking, true);

                String bagLabel = String.valueOf(Instant.now().toEpochMilli());
                report.log("Container bag label: " + bagLabel, true);
                HashMap<String, Object> orderBagLinkBody = new HashMap<>();
                orderBagLinkBody.put("order_id", curOrderIdAssignedForPicking);
                orderBagLinkBody.put("container_label", bagLabel);
                app.orderBagLinking(fcId, orderBagLinkBody, false);

                String pickAckBody = preparePickingAckApiBody(bagLabel, response,quantitytobereduced,underpickreasonid, report);
                app.pickingJobAckAPI(fcId, jobId, pickAckBody);

                HashMap<String, Object> pickCompleteBody = new HashMap<>();
                pickCompleteBody.put("job_id", jobId);
                pickCompleteBody.put("order_id", curOrderIdAssignedForPicking);
                app.pickingPlatformCompleteAPI(fcId, pickCompleteBody);
                report.log("Picking completed for the order: " + curOrderIdAssignedForPicking, true);
            } while (orderId != curOrderIdAssignedForPicking);
            report.log("Picking completed for the given order: " + orderId, true);

        } else if (!orderStatus.equalsIgnoreCase("packed")) {
            Assert.fail("Order/job is assigned to some other picker");
        }
    }



    /**
     * This method completes the picking flow for the oldest open order in pick_request table
     * before completing the picking flow for the given order
     * For the given picker if there is any existing assigned job, this will release the same to complete the picking flow
     * jobAssignmentAPI --> order-bag LinkingAPI --> pickingAckAPI --> pickCompleteAPI
     *
     * @param fcId        fcId
     * @param userName    adminusername otp enabled
     * @param adminCookie admin login cookie
     * @param report      report instance
     */
    public static int pickingFlowForOldestOpenOrder(int fcId, String userName, Map<String, String> adminCookie, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);

        verifyAnyOpenOrderPresentInPickingRequestTable(report);

        int curOrderIdAssignedForPicking;
        int userId = fetchUserId(userName, report);

        HashMap<String, Object> jobAssigmentBody = new HashMap<>();
        jobAssigmentBody.put("status", "assigned");
        jobAssigmentBody.put("type", "picking");
        Response response = app.jobAssignment(fcId, jobAssigmentBody);
        if (processPickingJobResponse(userId, response, report)) {
            report.log("Calling job assignment api, post releasing the user from previous job", true);
            response = app.jobAssignment(fcId, jobAssigmentBody);
        }

        curOrderIdAssignedForPicking = fetchorderIdFromJobAssignmentApiResponse(response, report);
        int jobId = fetchJobIdFromJobAssignmentApiResponse(response, report);
        report.log("Current orderId assigned for picking is: " + curOrderIdAssignedForPicking, true);

        String bagLabel = String.valueOf(Instant.now().toEpochMilli());
        report.log("Container bag label: " + bagLabel, true);
        HashMap<String, Object> orderBagLinkBody = new HashMap<>();
        orderBagLinkBody.put("order_id", curOrderIdAssignedForPicking);
        orderBagLinkBody.put("container_label", bagLabel);
        app.orderBagLinking(fcId, orderBagLinkBody, false);

        String pickAckBody = preparePickingAckApiBody(bagLabel, response, report);
        app.pickingJobAckAPI(fcId, jobId, pickAckBody);

        HashMap<String, Object> pickCompleteBody = new HashMap<>();
        pickCompleteBody.put("job_id", jobId);
        pickCompleteBody.put("order_id", curOrderIdAssignedForPicking);
        app.pickingPlatformCompleteAPI(fcId, pickCompleteBody);
        report.log("Picking completed for the order: " + curOrderIdAssignedForPicking, true);
        return curOrderIdAssignedForPicking;
    }

    /**
     * This method invokes the job assignment api and returns the orderId assigned for picking
     *
     * @param fcId        fcId
     * @param userName    adminusername otp enabled
     * @param adminCookie admin login cookie
     * @param report      report instance
     */
    public static int invokeJobAssignmentApi(int fcId, String userName, Map<String, String> adminCookie, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);

        verifyAnyOpenOrderPresentInPickingRequestTable(report);

        int curOrderIdAssignedForPicking;
        int userId = fetchUserId(userName, report);

        HashMap<String, Object> jobAssigmentBody = new HashMap<>();
        jobAssigmentBody.put("status", "assigned");
        jobAssigmentBody.put("type", "picking");
        Response response = app.jobAssignment(fcId, jobAssigmentBody);
        if (processPickingJobResponse(userId, response, report)) {
            report.log("Calling job assignment api, post releasing the user from previous job", true);
            response = app.jobAssignment(fcId, jobAssigmentBody);
        }

        curOrderIdAssignedForPicking = fetchorderIdFromJobAssignmentApiResponse(response, report);
        return curOrderIdAssignedForPicking;
    }

    /**
     * This method invokes the job assignment api and returns the jobId assigned for picking
     *
     * @param fcId        fcId
     * @param userName    adminusername otp enabled
     * @param adminCookie admin login cookie
     * @param report      report instance
     */
    public static Response callJobAssignmentApi(int fcId, String userName, Map<String, String> adminCookie, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);

        verifyAnyOpenOrderPresentInPickingRequestTable(report);

        int userId = fetchUserId(userName, report);

        HashMap<String, Object> jobAssigmentBody = new HashMap<>();
        jobAssigmentBody.put("status", "assigned");
        jobAssigmentBody.put("type", "picking");
        Response response = app.jobAssignment(fcId, jobAssigmentBody);
        if (processPickingJobResponse(userId, response, report)) {
            report.log("Calling job assignment api, post releasing the user from previous job", true);
            response = app.jobAssignment(fcId, jobAssigmentBody);
        }

        return response;
    }

    /**
     * Fetchs the order details from picking item request table
     *
     * @param orderId
     * @param report
     * @return
     */
    public static JSONObject fetchOrderDetailsFromPickingItemRequest(int orderId, IReport report) {
        String query = "select order_id,sku_id,ordered_quantity,reservation_fc_id from picking_item_request where picking_request_id =" +
                " (select id from picking_request where order_id = " + orderId + ");\n";
        report.log("Fetching order details from picking_item_request\n " + query, true);
        System.out.println("Fetching order details from picking_item_request" + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No enrty present in picking item request table " + query);
        }
        return jsonObject;
    }


    /**
     * Fetchs the order details from picking request table
     *
     * @param orderId
     * @param report
     * @return
     */
    public static JSONObject fetchOrderDetailsFromPickingRequest(int orderId, IReport report) {
        String query = "select order_status,picking_status from picking_request where order_id = " + orderId + ";";
        report.log("Fetching order details from picking_request\n " + query, true);
        System.out.println("Fetching order details from picking_request" + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No enrty present in picking request table " + query);
        }
        return jsonObject;
    }


    /**
     * Fetchs the pick ack details from pick ack table
     *
     * @param orderId
     * @param report
     * @return
     */
    public static JSONObject fetchDetailsFromPickAckTable(int orderId, IReport report) {
        String query = "select * from pick_ack where order_id = " + orderId + ";";
        report.log("Fetching details from pick_ack\n " + query, true);
        System.out.println("Fetching details from pick_ack" + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No enrty present in pick ack table " + query);
        }
        return jsonObject;
    }

    /**
     * Fetchs the container label in-use
     *
     * @param report
     * @return
     */
    public static JSONObject fetchContainerLabelInUse(IReport report) {
        String query = "SELECT container_label,cont.fc_id,cont.id " +
                "FROM container cont " +
                "INNER JOIN order_container o_cont ON cont.id = o_cont.container_id  " +
                "WHERE cont.status = \"In-use\" order by cont.id limit 1;";
        report.log("Fetching container label in-use\n " + query, true);
        System.out.println("Fetching container label in-use" + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("None of the containers with in-use status" + query);
        }
        return jsonObject;
    }

    /**
     * This method does the order-bag linking and returns the response
     *
     * @param fcId           fcId
     * @param orderId        orderId
     * @param containerLabel bag label
     * @param adminCookie    admin login cookie
     * @param report         report instance
     */
    public static Response orderBagLinking(int fcId, int orderId, String containerLabel, Map<String, String> adminCookie, boolean skipValidation, IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);

        HashMap<String, Object> orderBagLinkBody = new HashMap<>();
        orderBagLinkBody.put("order_id", orderId);
        orderBagLinkBody.put("container_label", containerLabel);
        Response response = app.orderBagLinking(fcId, orderBagLinkBody, skipValidation);
        return response;
    }

    /**
     * Checks whether the container label is in-use
     * @param containerLabel
     * @param report
     * @return
     */
    public static boolean checkWhetherContainerLabelIsInUse(String containerLabel, IReport report) {
        String query = "select container_label,fc_id,status from container where container_label=\"" + containerLabel + "\";";
        report.log("Fetching status of the container label\n " + query, true);
        System.out.println("Fetching status of the container label" + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("Container label " + containerLabel + " is not present in container" + query);
        }
        return jsonObject.getJSONArray("rows").getJSONObject(0).getString("status").equalsIgnoreCase("In-use");
    }

    /**
     * Checks whether entry exists in order-container table
     * @param orderId
     * @param containerLabel
     * @param report
     * @return
     */
    public static boolean checkWhetherEntryExistsInOrderContainerTable(int orderId,String containerLabel, IReport report) {
        String query = "select fc_id,order_id,container_id from order_container where order_id = "+orderId+" and container_id = " +
                "(select id from container where container_label=\""+containerLabel+"\");";
        report.log("Fetching entries from order-container table\n " + query, true);
        System.out.println("Fetching entries from order-container table" + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("Order "+orderId+" Container label " + containerLabel + " entry is not present in order-container table",true);
            return false;
        }
        return true;
    }


    /**
     * This method makes call to order-container-info api and returns the response
     *
     * @param orderId        orderId
     * @param adminCookie    admin login cookie
     * @param report         report instance
     */
    public static Response orderContainerInfo(int orderId,Map<String, String> adminCookie,  IReport report) {
        IQApp app = new IQApp(report);
        app.cookie.updateCookie(adminCookie);

        Response response = app.orderContainerInfo(orderId);
        return response;
    }

    /**
     * fetchs the container label linked with the given order
     * @param orderId
     * @param report
     * @return
     */
    public static JSONObject fetchContainerLabelLinkedForGivenOrder(int orderId, IReport report) {
        String query = "select container_label from container where id in " +
                "(select container_id from order_container where order_id = "+orderId+");";
        report.log("Fetching the container label linked with the order Id\n " + query, true);
        System.out.println("Fetching the container label linked with the order Id\n " + query);
        JSONObject jsonObject = new JSONObject(PickingPlatformQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            Assert.fail("No entry in order container table \n" + query);
        }
        return jsonObject;
    }
}
