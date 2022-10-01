package utility;

import com.bigbasket.automation.reports.IReport;
import msvc.order.external.CancelOrder;
import org.json.JSONObject;
import org.testng.Assert;
import utility.database.OrderQueryExecuteWrapper;

public class OrderUtils {
    /**
     * Fetchs the current status of the given orderID (BB2.0)
     * @param orderId
     * @param report
     * @return
     */
    public static String getCurrentOrderStatus(int orderId, IReport report) {
        String query = "select status from order_detail where id = " + orderId + ";";
        report.log("Fetching the order status \n Query Executed: " + query,true);
        JSONObject jsonObject = new JSONObject(OrderQueryExecuteWrapper.executeMicroserviceDataBaseQuery(query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No status returned ", true);
            Assert.fail("No status returned  for Query: " + query);
        }
        String orderStatus = jsonObject.getJSONArray("rows").getJSONObject(0).getString("status");
        report.log("Current status of the order :" + orderId + " is : " + orderStatus,true);
        return orderStatus;
    }

    /**
     * Method to cancel the bb2 foundation order
     * @param orderId
     * @param entryContext
     * @param entryContextId
     * @param xCaller
     * @param report
     */
    public static void cancelOrder(String orderId,String entryContext,String entryContextId,String xCaller, IReport report,String m_id){
        CancelOrder cancelOrder = new CancelOrder(entryContext,entryContextId,xCaller,report);
        cancelOrder.cancelOrder(orderId,m_id);
    }
}
