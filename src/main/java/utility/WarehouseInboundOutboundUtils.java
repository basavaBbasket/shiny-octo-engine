package utility;

import com.bigbasket.automation.WebSettings;
import com.bigbasket.automation.reports.IReport;
import com.bigbasket.automation.utilities.AutomationUtilities;
import org.json.JSONObject;
import org.testng.Assert;

public class WarehouseInboundOutboundUtils extends WebSettings {



    public  int getFcreceivingID(String poid, IReport report) {
        String query = "select id from fcreceiving where request_id=\""+poid+"\";";
        report.log("Fetching the fcreceiving id \n Query Executed: " + query,true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getWarehouseInboundOutboundDBName(),query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No id returned ", true);
            Assert.fail("No id returned  for Query: " + query);
        }
        int fcreceivingid = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
        report.log("Current fc receiving id of the purchase order :" + poid + " is : " + fcreceivingid,true);
        return fcreceivingid;
    }

    public  void getFcreceivingItemID(int fcreceivingid, IReport report) {
        String query = "select id from fcreceivingitem where fcreceiving_id="+fcreceivingid+";";
        report.log("Fetching the fcreceivingitem \n Query Executed: " + query,true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getWarehouseInboundOutboundDBName(),query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No id returned ", true);
            Assert.fail("No id returned  for Query: " + query);
        }
        int fcreceivingitemid = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("id");
        report.log("Current id of the fcreceiving_id :" + fcreceivingid + " is : " + fcreceivingitemid,true);

    }

    public  int getCountOfFcreturntable(IReport report){
        String query = "select count(id) as cid  from fc_return;";
        report.log("Fetching the count from fc return table \n Query Executed: " + query,true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getWarehouseInboundOutboundDBName(),query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No count is returned ", true);
            Assert.fail("No count is  returned  for Query: " + query);
        }
        int fcreturncount = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("cid");
        report.log("Current entries in the fcreturn table is  :" + fcreturncount ,true);
      return fcreturncount;
    }


    public  int getCountOfFcreturnItemtable(IReport report){
        String query = "select count(id) as cid  from fc_return_item;";
        report.log("Fetching the count from fc_return_item table \n Query Executed: " + query,true);
        JSONObject jsonObject = new JSONObject(AutomationUtilities.executeMicroserviceDatabaseQuery(getWarehouseInboundOutboundDBName(),query));
        if (jsonObject.getInt("numRows") == 0) {
            report.log("No count is returned ", true);
            Assert.fail("No count is  returned  for Query: " + query);
        }
        int fcreturncount = jsonObject.getJSONArray("rows").getJSONObject(0).getInt("cid");
        report.log("Current entries in the fc_return_item table is  :" + fcreturncount ,true);
        return fcreturncount;
    }
}
