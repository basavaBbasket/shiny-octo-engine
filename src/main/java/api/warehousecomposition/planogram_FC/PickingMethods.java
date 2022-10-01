package api.warehousecomposition.planogram_FC;

import api.warehousecomposition.planogram_FC.internal.Helper;
import api.warehousecomposition.planogram_FC.internal.IQApp;
import com.bigbasket.automation.reports.IReport;
import io.restassured.response.Response;
import org.testng.Assert;

import java.time.Instant;
import java.util.HashMap;

public class PickingMethods extends Helper {

    /**
     * This method completes the picking flow for the all the open orders with earliest created on time in pick_request table
     * before completing the picking flow for the given order
     * For the given picker if there is any existing assigned job, this will release the same to complete the picking flow
     * jobAssignmentAPI --> order-bag LinkingAPI --> pickingAckAPI --> pickCompleteAPI
     *
     * @param orderId  orderId for picking
     * @param fcId     fcId
     * @param userName adminusername otp enabled
     * @param password adminpassword
     * @param report   report instance
     */
    public static void pickingFlowForAllEarliestOpenOrders(int orderId, int fcId, String userName, String password, IReport report) {
        IQApp app = new IQApp(report)
                .iqAppLogin(userName, password)
                .mfaAuthenticate(userName);
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
                app.orderBagLinking(fcId, orderBagLinkBody);

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
     * @param orderId  orderId for picking
     * @param fcId     fcId
     * @param userName adminusername otp enabled
     * @param password adminpassword
     * @param report   report instance
     */
    public static void pickingFlowModified(int orderId, int fcId, String userName, String password, IReport report) {
        IQApp app = new IQApp(report)
                .iqAppLogin(userName, password)
                .mfaAuthenticate(userName);

        String orderStatus = checkWhetherTheGivenOrderIsPresentInPickingRequestTable(orderId, report);
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
                app.orderBagLinking(fcId, orderBagLinkBody);

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
     * This method completes the picking flow for the oldest open order in pick_request table
     * before completing the picking flow for the given order
     * For the given picker if there is any existing assigned job, this will release the same to complete the picking flow
     * jobAssignmentAPI --> order-bag LinkingAPI --> pickingAckAPI --> pickCompleteAPI
     *
     * @param fcId     fcId
     * @param userName adminusername otp enabled
     * @param password adminpassword
     * @param report   report instance
     */
    public static int pickingFlowForOldestOpenOrder(int fcId, String userName, String password, IReport report) {
        IQApp app = new IQApp(report)
                .iqAppLogin(userName, password)
                .mfaAuthenticate(userName);

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
        app.orderBagLinking(fcId, orderBagLinkBody);

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
     * @param fcId     fcId
     * @param userName adminusername otp enabled
     * @param password adminpassword
     * @param report   report instance
     */
    public static int invokeJobAssignmentApi(int fcId, String userName, String password, IReport report) {
        IQApp app = new IQApp(report)
                .iqAppLogin(userName, password)
                .mfaAuthenticate(userName);

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

}
