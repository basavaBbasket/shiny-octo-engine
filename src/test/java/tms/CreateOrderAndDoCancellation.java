package tms;

import TMS.*;
import com.bigbasket.automation.reports.AutomationReport;
import com.bigbasket.automation.reports.DescriptionProvider;
import framework.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class CreateOrderAndDoCancellation extends BaseTest implements Endpoints {


    @DescriptionProvider(slug = "TMS order creation", description = "1. Get available slots for given location context and group of skus\n" +
            "            2. Reseve the slot by passing slot info  from get-slots\n" +
            "            3. Hit the confirm order Api and genrate order id\n" +
            "            4. check details through order details API \n" +
            "            5. Update order status to packed and do cancellation \n" ,author = "tushar")

    @Test(groups = {"tms_test","TMS","tms-order-creation"})
    public void createOrder() throws IOException {

        AutomationReport report = getInitializedReport(this.getClass(), true);

        String orderType = "Normal";
        String entryContextId = Config.tmsConfig.getString("entry_context_id");
        String entryContext = Config.tmsConfig.getString("entry_context");
        String originContext = Config.tmsConfig.getString("originContext");
        String xCaller = "123";//todo
        String tenantId = "2";//todo
        String projectId = "bb-tms";//todo
        String external_order_id = "21350312";//todo
        Integer reason_id = 1;

        ArrayList<ArrayList<String>> fetchAvailableSlots = getSlotsDates(report,orderType,entryContext,entryContextId,originContext,xCaller,tenantId,projectId);
        report.log("Available Slots: "+ fetchAvailableSlots.get(0),true);
        Integer reservationId = getReservationId(report,orderType,entryContext,entryContextId,originContext,xCaller,tenantId,projectId , fetchAvailableSlots);
        Integer orderId  = fetchOrderId(report,orderType,entryContext,entryContextId,originContext,xCaller,tenantId,projectId ,reservationId);
        report.log("Order Id: "+ orderId,true);
        OrderDetailsInternalApi orderDetailsInternalApi = new OrderDetailsInternalApi(report,entryContext,entryContextId,originContext,xCaller,tenantId,projectId );
        Response orderDetailsResponse = orderDetailsInternalApi.getOrderDetails(orderId);
        report.log(orderDetailsResponse.prettyPrint(),true);
        updateOrderStatus(report,entryContext,entryContextId,originContext,xCaller,tenantId,projectId,orderId,"packed" , external_order_id);
        doCancellation(report,entryContext,entryContextId,originContext,xCaller,tenantId,projectId,orderId, reason_id);


    }
    private Integer fetchOrderId(AutomationReport report, String orderType, String entryContext, String entryContextId, String originContext, String xCaller, String tenantId, String projectId,Integer reservationId ) throws IOException {
        ConfirmOrderApi confirmOrderApi = new ConfirmOrderApi(report,xCaller,entryContext,entryContextId,originContext,tenantId,projectId);

        Response confirmOrderResponse =  confirmOrderApi.confirmOrder(reservationId,orderType);
        JsonPath jsonPath = confirmOrderResponse.jsonPath();
        return jsonPath.get("orders[0].id");
    }

    private Integer getReservationId(AutomationReport report, String orderType, String entryContext, String entryContextId, String originContext, String xCaller, String tenantId, String projectId, ArrayList<ArrayList<String>> slotDatesList) throws IOException {
        ReserveSlotsApi reserveSlotsApi = new ReserveSlotsApi(report,xCaller,entryContext,entryContextId,originContext,tenantId,projectId);
        Response reserveSlotResponse =  reserveSlotsApi.doReserveSlots(orderType , slotDatesList);

        JsonPath jsonPath = reserveSlotResponse.jsonPath();
        return jsonPath.get("reservations[0].reservation_id");
    }

    private ArrayList<ArrayList<String>> getSlotsDates(AutomationReport report, String orderType, String entryContext, String entryContextId, String originContext, String xCaller, String tenantId, String projectId) throws IOException {
        GetSlotsApi getSlotsApi = new GetSlotsApi(report,xCaller,entryContext,entryContextId,originContext,tenantId,projectId);
        Response response = getSlotsApi.getAvailableSlots(orderType);
        ArrayList<String> slotDates = new ArrayList<>();
        ArrayList<String> slot_definition_id = new ArrayList<>();
        ArrayList<String> template_slot_id = new ArrayList<>();
        JsonPath jsonPath = response.jsonPath();

        for(int i = 0 ; i< Integer.parseInt(Config.tmsConfig.getString("max_slot_count")) ; i++) {
            String strPath = "orders[0].slots["+i+"].slot_start_date";
            String slot_def_path = "orders[0].slots["+i+"].slots[0].slot_definition_id";
            String template_def_path= "orders[0].slots["+i+"].slots[0].template_slot_id";
            slotDates.add(jsonPath.getString(strPath));
            slot_definition_id.add(jsonPath.getString(slot_def_path));
            template_slot_id.add(jsonPath.getString(template_def_path));
        }

        ArrayList<ArrayList<String>> slots_details = new ArrayList<>();
        slots_details.add(slotDates);
        slots_details.add(slot_definition_id);
        slots_details.add(template_slot_id);

        return slots_details;
    }


    private void doCancellation(AutomationReport report, String entryContext, String entryContextId, String originContext, String xCaller, String tenantId, String projectId,Integer orderId,Integer reason_id) {
        CancelOrderInternalApi cancelOrderInternalApi = new CancelOrderInternalApi(report,xCaller,entryContext,entryContextId,originContext,tenantId,projectId);
        cancelOrderInternalApi.cancelOrderInternal(orderId,reason_id);
    }


    private void updateOrderStatus(AutomationReport report, String entryContext, String entryContextId, String originContext, String xCaller, String tenantId, String projectId,Integer orderId, String status , String extenal_order_id) {
        UpdateOrderStatus updateOrderStatus = new UpdateOrderStatus(report,xCaller,entryContext,entryContextId,originContext,tenantId,projectId);

        updateOrderStatus.updateOrderStatus(orderId,status, extenal_order_id);
    }






}
