package utility;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import org.json.JSONObject;
import org.testng.Assert;


public class ReplenishmentUtils extends WebSettings {

    public  int getCurrentPurchaseOrderStatus(String poid, IReport report) {
        String query = "select status from purchaseorder where po_number=\""+poid+"\";";
        report.log("Fetching the Purchase order status \n Query Executed: " + query,true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getReplenishmentDBName(),query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No status returned ", true);
            Assert.fail("No status returned  for Query: " + query);
        }
        int orderStatus = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("status");
        report.log("Current status of the purchase order :" + poid + " is : " + orderStatus,true);
        return orderStatus;
    }
}
