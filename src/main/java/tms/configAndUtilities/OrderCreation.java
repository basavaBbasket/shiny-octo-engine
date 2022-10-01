package tms.configAndUtilities;

import tms.api.OMS.ConfirmOrderApi;
import tms.api.OMS.GetSlotsApi;
import tms.api.OMS.ReserveSlotsApi;
import com.bigbasket.automation.reports.IReport;
import io.restassured.response.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class OrderCreation {
    public static  ArrayList<ArrayList<String>> fetchAvailableSlots;

    public static Integer placeTmsOrder(IReport report , Map<String ,String> memberData , Map<String,String> headersMap,Map<String,String> skuData,int external_reference_id) throws IOException {

        fetchAvailableSlots = GetSlotsApi.getSlotsDates(report, headersMap,memberData,skuData,external_reference_id);
    report.log("Available Slots: "+ fetchAvailableSlots.get(0),true);
    Integer reservationId = ReserveSlotsApi.getReservationId(report,memberData,headersMap,skuData,fetchAvailableSlots,external_reference_id);
    Integer orderId  = ConfirmOrderApi.fetchOrderId(report,memberData,headersMap,reservationId,external_reference_id);
    report.log("Order Id: "+ orderId,true);
    return orderId;
    }

    public static Response placeTmsOrderWithOutReservation(IReport report , Map<String ,String> memberData , Map<String,String> headersMap, Map<String,String> skuData, int Reservationid,int external_reference_id) throws IOException {
        return   ConfirmOrderApi.fetchOrderIdWithOutreservationid(report,memberData,headersMap,Reservationid,external_reference_id);




    }
    public static Response placeTmsVariableOrder(IReport report , Map<String ,String> memberData , Map<String,String> headersMap,Map<String,String> skuData,int Original_order_id,int external_reference_id) throws IOException {

         fetchAvailableSlots = GetSlotsApi.getSlotsDates(report, headersMap,memberData,skuData,external_reference_id);
        report.log("Available Slots: "+ fetchAvailableSlots.get(0),true);
        Integer reservationId = ReserveSlotsApi.getVariableReservationId(report,memberData,headersMap,skuData,fetchAvailableSlots,Original_order_id,external_reference_id);
        ConfirmOrderApi confirmOrderApi = new ConfirmOrderApi(report,headersMap.get("xCaller"),headersMap.get("entryContext"),headersMap.get("entryContextId"),headersMap.get("originContext"),headersMap.get("tenantId"),headersMap.get("projectId"));
        Response confirmOrderResponse =  confirmOrderApi.confirmOrder(reservationId,headersMap.get("orderType"),external_reference_id);

       // Integer orderId  = ConfirmOrderApi.fetchOrderId(report,memberData,headersMap,reservationId);
//        report.log("Order Id: "+ orderId,true);
        return confirmOrderResponse;
    }




}
